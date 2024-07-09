/*
    Parser to take in stream of tokens and then validates them and their syntax


*/

package project3;

import java.util.ArrayList;

public class Parser {
    
    // Private Instance Variables
    private ArrayList<Token> myTokens; // to store token stream
    private String[] gramType; // to store acceptable types
    private String[] gramChar; // to store acceptable chars
    private int[] gramDigit; // to store acceptable digits
    private String[] gramBoolOp; // to store acceptable bool op
    private String[] gramBoolVal; // to store acceptable bool val
    private CST myTree; // instance of Concrete Syntax Tree
    private int instructionCount; // count what instruction we are on
    private Token currentToken; // the current token we are on
    private int errorCount; // the number of errors found
    private boolean foundEnd; // flag to mark if EOP is found to trigger parseResults()
    private AST myAST; // AST Instance to create AST from CST
    private Semantic mySemantic; // Semantic Instance to run Semantic Analysis
    private int myProgramNumber;

    //! Begin Parser Construction and Manipulation

    // Null Constructor
    public Parser() {
        // Initialize Private Variables
        this.myTokens = new ArrayList<Token>();
        this.gramType = new String[]{"int", "string", "boolean"};
        this.gramChar = new String[26];
        for (char c = 'a'; c <= 'z'; c++) {
            this.gramChar[c - 'a'] = String.valueOf(c); // using ASCII
        }
        this.gramDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        this.gramBoolOp = new String[]{"==", "!="};
        this.gramBoolVal = new String[]{"false", "true"};

        this.myTree = new CST();
        this.instructionCount = 0;
        this.currentToken = null;
        this.foundEnd = false;
        this.myAST = new AST();
        this.mySemantic = new Semantic();
        this.myProgramNumber = 0;
    }

    // Method to read in tokens to parser
    public void tokenStream(ArrayList<Token> lexerTokens) {
        // add each token from method call to parser tokens
        for(Token currToken : lexerTokens) {
            this.myTokens.add(currToken);
        }
    }

    // Method to reset parser in between program uses
    public void reset() {
        this.myTokens.clear(); // reset token arrayList
        this.myTree.clear();
        this.instructionCount = 0;
        this.errorCount = 0;
        System.out.println("PARSER CLEARED... \n");
        this.foundEnd = false;
        if(this.myAST.getRoot() != null) {
            this.myAST.clear();
        }
        this.mySemantic.clear();
        this.myProgramNumber = 0;
    }

    // Method to get next token from stream -> increment instructionCount
    public void nextToken() {
        // Check if there are more tokens to get
        if(this.instructionCount < myTokens.size()) {
            this.currentToken = this.myTokens.get(this.instructionCount++);
        }
    }

    // Update program number
    public void setProgramNumber(int newProgramNumber) {
        this.myProgramNumber = newProgramNumber;
    }

    // Method to determine the results of the parse
    public void parseResults() {
        // foundEnd -> if EOP is found
        // errorcount 
        if(this.foundEnd == true && this.errorCount == 0) {
            System.out.println("PARSE SUCCESSFULLY COMPLETED WITH " + errorCount + " Error(s) \n");
            
            System.out.println("DISPLAYING CONCRETE SYNTAX TREE FOR PROGRAM # " + this.myProgramNumber + "...");
            myTree.displayCST(myTree.getRoot(), 0);

            // load and create AST
            myAST.loadAST(myTree);

            // update AST Program Number
            myAST.setProgramNumber(this.myProgramNumber);

            // Display AST
            System.out.println("DISPLAYING ABSTRACT SYNTAX TREE FOR PROGRAM # " + this.myProgramNumber + "...");
            myAST.displayAST(myAST.getRoot(), 0);

            // load and start semantic analysis
            this.mySemantic.startSemantic(myAST, this.myProgramNumber);

        } else if(this.foundEnd != true) {
            System.out.println("PARSE FAILED FAILED WITH " + errorCount + " Error(s) EOP NOT FOUND \n");
            System.out.println("CST WILL NOT BE PRINTED");
            // Say how next part will not start
        } else if(this.errorCount > 0) {
            System.out.println("PARSE FAILED WITH " + this.errorCount + " Error(s) \n");
            System.out.println("CST WILL NOT BE PRINTED");
            // Say how next part will not start
        }
    }

    //! End Parser Construction and Manipulation

    //! Begin Methods for Matching Lexemes

    // Method to match the current lexeme with its expected value
    public void match(String expectedValue) {

        // Check if EOP -> check for errors and begin semantic anaylsis
        if(this.currentToken.getLexeme().equals("$")) {
            // Create Node and update next
            this.foundEnd = true; // indicate we found the end -> parseResults()
            this.myTree.addNode("leaf", currentToken.getLexeme(), currentToken.getLine());
            nextToken();
        } else if(this.currentToken.getLexeme().equalsIgnoreCase(expectedValue)) {
            // If Current Lexeme matches we make Node and update next
            this.myTree.addNode("leaf", this.currentToken.getLexeme(), currentToken.getLine());
            nextToken();
        } else {
            // if the current token does not match expected throw an error
            System.out.println("ERROR DETECTED -> Token Lexeme [ " + this.currentToken.getLexeme() + " ] DOES NOT MATCH EXPECTED VALUE [ " + expectedValue + " ] at line " + this.currentToken.getLine());
            this.errorCount++;
        }
    }

    // Method to determine if current lexeme matches any of the expected values of its type in our grammer
    public void matchFinal(String expectedType) {
        // determine which to match
        boolean switchResult = false;
        String expected = "";
        switch(expectedType) {
            case "CHAR":
            case "ID":
                for(int i = 0; i < gramChar.length; i++) {
                    if(gramChar[i].equalsIgnoreCase(this.currentToken.getLexeme())) {
                        switchResult = true;
                        
                    }
                    expected = expected + gramChar[i] + " ";
                }
                break;
            case "TYPE":
                for(int i = 0; i < gramType.length; i++) {
                    if(gramType[i].equalsIgnoreCase(this.currentToken.getLexeme())) {
                        switchResult = true;
                    }
                    expected = expected + gramType[i] + " ";
                }
                break;
            case "DIGIT":
                for(int i = 0; i < gramDigit.length; i++) {
                    if(Integer.toString(gramDigit[i]).equalsIgnoreCase(this.currentToken.getLexeme())) {
                        switchResult = true;
                    }
                    expected = expected + gramDigit[i] + " ";
                }
                break;
            case "BOOLVAL":
                for(int i = 0; i < gramBoolVal.length; i++) {
                    if(gramBoolVal[i].equalsIgnoreCase(this.currentToken.getLexeme())) {
                        switchResult = true;
                    }
                    expected = expected + gramBoolVal[i] + " ";
                }
                break;
            case "BOOLOP":
                for(int i = 0; i < gramBoolOp.length; i++) {
                    if(gramBoolOp[i].equalsIgnoreCase(this.currentToken.getLexeme())) {
                        switchResult = true;
                    }
                    expected = expected + gramBoolOp[i] + " ";
                }
            break;
        }

        // if we successfully match we will create the leaf
        if(switchResult) {
            this.myTree.addNode("leaf", this.currentToken.getLexeme(), this.currentToken.getLine());
        } else {
            // throw error for unmatched expected lexeme
            System.out.println("ERROR NO MATCH FOUND!!! Input -> " + this.currentToken.getLexeme() + " Does not match any expected: " + expected);
            this.errorCount++;
        }

        // get next token
        this.nextToken();

    }

    //! End Methods for Matching Lexemes

    // ! Begin Methods for Parsing Grammer

    // Program ::== Block $ 
    public void parseProgram() {
        System.out.println("->Parsing Program # " + this.myProgramNumber + "<-");
        
        // get first token
        this.nextToken();
        
        // create the root node and its type is program
        myTree.addNode("root", "Program");
        parseBlock(); 
        // After Recursion we will match this final Token
        match("$");

        parseResults();
    }

    // Block ::== { StatementList } 
    public void parseBlock() {
        System.out.println("->Parsing Block<-");
        // Create branch node
        this.myTree.addNode("branch", "Block");
        match("{");
        parseStatementList();
        match("}");
        this.myTree.goUp();
    }
    // StatementList ::== Statement StatementList ε
    public void parseStatementList() {
        System.out.println("->Parsing Statement List<-");

        // Create the node
        myTree.addNode("branch", "Statement List");

        // Identify which case of statementlist we have
        switch(this.currentToken.getType()) {
            case "T_PRINT":
            case "T_ID":
            case "T_INT":
            case "T_BOOLEAN":
            case "T_STRING":
            case "T_WHILE":
            case "T_IF":
            case "T_OPENING_BRACE":
                parseStatement(); // parse the statement
                parseStatementList(); // continue to parse the statement list
                break;
            default:
                // empty statement list
                break;
        }
        this.myTree.goUp();
    }

    // Statement ::== PrintStatement AssignmentStatement VarDecl WhileStatement IfStatement Block 
    public void parseStatement() {
        System.out.println("->Parsing Statement<-");

        // Create the node
        myTree.addNode("branch", "Statement");

        // Determine and execute the correct operation
        switch(this.currentToken.getType()) {
            // No need to match or increment as each function will do that 
            case "T_PRINT":
                parsePrint();
                break;
            case "T_ID":
                parseAssignment();
                break;
            case "T_INT":
            case "T_BOOLEAN":
            case "T_STRING":
                parseVarDecl();
                break;
            case "T_WHILE":
                parseWhile();
                break;
            case "T_IF":
                parseIf();
                break;
            case "T_OPENING_BRACE":
                parseBlock();
                break;
            default:
                break;
        }
        this.myTree.goUp();
    }

    // PrintStatement ::== print ( Expr ) 
    public void parsePrint() {
        System.out.println("->Parsing Print<-");

        //Create the node
        this.myTree.addNode("branch", "Print");

        // match and consume the ( expected token
        match("print");
        match("(");

        parseExpr(); // parse next token which should be the expr

        // match and consume the final ) expected token
        match(")");

        this.myTree.goUp();
    }

    // AssignmentStatement ::== Id = Expr 
    public void parseAssignment() {
        System.out.println("->Parsing Assignment<-");

        //create the node
        this.myTree.addNode("branch", "Assignment");

        // Parse expected 'ID'
        parseId();
        
        match("="); // match expected =

        // parse expected future 'Expr'
        parseExpr();

        this.myTree.goUp();
    }
    
    // AssignmentStatement ::== Id = Expr
    public void parseVarDecl() {
        System.out.println("->Parsing VarDecl<-");
        
        // Create the node
        this.myTree.addNode("branch", "VarDecl");
        this.parseType();
        this.parseId();

        this.myTree.goUp();
    }

    // WhileStatement ::== while BooleanExpr Block
    public void parseWhile() {
        System.out.println("->Parsing While<-");

        // Create the node
        this.myTree.addNode("branch", "While Statement");

        // match and use the expected while
        match("while");
        parseBooleanExpr();
        parseBlock();

        this.myTree.goUp();
    }

    // IfStatement ::== if BooleanExpr Block 
    public void parseIf() {
        System.out.println("->Parsing If<-");
        
        // Create the node
        this.myTree.addNode("branch", "IF Statement");

        // match expected if
        match("if");

        parseBooleanExpr();

        parseBlock();

        this.myTree.goUp();

    }

    // Expr ::== IntExpr String Expr BooleanExpr Id 
    public void parseExpr() {
        System.out.println("->Parsing Expr<-");

        // Create the node
        this.myTree.addNode("branch", "Expression");

        // determine expression type and parse accordingly
        switch(this.currentToken.getType()) {
            case "T_DIGIT":
                parseIntExpr();
                break;
            case "T_QUOTES":
                parseStringExpr();
                break;
            case "T_TRUE":
            case "T_FALSE":
                parseBooleanExpr();
                break;
            case "T_ID":
                parseId();
                break;
        }
        this.myTree.goUp();
    }

    // IntExpr ::== digit intop Expr || digit 
    public void parseIntExpr() {
        System.out.println("->Parsing IntExpr<-");
        
        // Create the node
        this.myTree.addNode("branch", "Int Expression");

        // Parse digit cause we always parse first
        parseDigit();

        // if next is intop we continue
        if(this.currentToken.getType().equalsIgnoreCase("T_ADDITION_OP")) {
            parseIntOp();
            parseExpr();
        }

        this.myTree.goUp();
    }

    // StringExpr ::== " CharList " 
    public void parseStringExpr() {
        System.out.println("->Parsing StringExpr<-");

        // Create the node
        this.myTree.addNode("branch", "String Expression");

        // match to the expected format
        match("\"");
        parseCharList();
        match("\"");

        this.myTree.goUp();
    }

    // BooleanExpr ::== ( Expr boolop Expr ) || boolval || char
    public void parseBooleanExpr() {
        System.out.println("->Parsing BooleanExpr<-");

        // Create the node
        this.myTree.addNode("branch", "Boolean Expression");

        // if next token is opening parenthesis we know its first format
        if(this.currentToken.getType().equalsIgnoreCase("T_OPENING_PARENTHESIS")) {
            match("(");
            parseExpr();
            parseBoolOp();
            parseExpr();
            match(")");
        } else if(this.currentToken.getType().equalsIgnoreCase("T_CHAR")) {
            // char format
            parseChar();
        } else {
            // bool val format
            parseBoolVal();
        }
        this.myTree.goUp();
    }

    //CharList ::== char CharList || space CharList || ε
    public void parseCharList() {
        System.out.println("->Parsing CharList<-");

        boolean hasChar = false;
    
        while (this.currentToken.getType().equalsIgnoreCase("T_CHAR") ||
               this.currentToken.getType().equalsIgnoreCase("T_SPACE")) {
            // Create the node only if we find a char or space
            if (!hasChar) {
                this.myTree.addNode("branch", "Char List");
                hasChar = true;
            }
    
            // if next is char then first format
            if (this.currentToken.getType().equalsIgnoreCase("T_CHAR")) {
                parseChar();
            } else if (this.currentToken.getType().equalsIgnoreCase("T_SPACE")) {
                // if next is space then second format
                parseSpace();
            }
        }
    
        if (hasChar) {
            this.myTree.goUp();
        }
    }

    // Id ::== char 
    public void parseId() {
        System.out.println("->Parsing Id<-");

        // Create the node
        this.myTree.addNode("branch", "ID");

        matchFinal("ID"); // match with grammer

        this.myTree.goUp();
    }

    // type ::== int | string | boolean 
    public void parseType() {
        System.out.println("->Parsing Type<-");

        // Create the node
        this.myTree.addNode("branch", "Type");

        matchFinal("TYPE"); // match with grammer

        this.myTree.goUp();
    }

    // char ::== a | b | c ... z 
    public void parseChar() {
        System.out.println("->Parsing Char<-");
        
        // Create the node
        this.myTree.addNode("branch", "Char");

        matchFinal("CHAR"); // match with grammer

        this.myTree.goUp();
        
    }

    // space ::== the space character 
    public void parseSpace() {
        System.out.println("->Parsing Space<-");

        // Create the node
        this.myTree.addNode("branch", "Space");

        // match and consume expected space
        match(" ");

        this.myTree.goUp();
    }

    // digit ::== 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 
    public void parseDigit() {
        System.out.println("->Parsing Digit<-");

        // Create the node
        this.myTree.addNode("branch", "Digit");

        matchFinal("DIGIT"); // match with grammer

        this.myTree.goUp();
    }

    // boolop ::== == | !=
    public void parseBoolOp() {
        System.out.println("->Parsing BoolOp<-");

        // Create the node
        this.myTree.addNode("branch", "Boolean Operator");

        matchFinal("BOOLOP"); // match with grammer

        this.myTree.goUp();
    }

    // boolval ::== false | true 
    public void parseBoolVal() {
        System.out.println("->Parsing BoolVal<-");

        // Create the node
        this.myTree.addNode("branch", "Boolean Value");

        matchFinal("BOOLVAL"); // match with grammer

        this.myTree.goUp();
    }

    // intop ::== + 
    public void parseIntOp() {
        System.out.println("->Parsing IntOp<-");

        //Create the node
        this.myTree.addNode("branch", "Int Operator");

        match("+"); // match for +

        this.myTree.goUp();
    }

    //! End Methods for Parsing Grammer
}
