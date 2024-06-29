/*
    Parser to take in stream of tokens and then validates them and their syntax


*/

package project2;

import java.util.ArrayList;

public class Parser {
    // Private Instance Variables
    private ArrayList<Token> myTokens; // to store token stream
    private String[] gramType; // to store acceptable types
    private char[] gramChar; // to store acceptable chars
    private String gramSpace; // to store acceptable space char
    private int[] gramDigit; // to store acceptable digits
    private String[] gramBoolOp; // to store acceptable bool op
    private String[] gramBoolVal; // to store acceptable bool val
    private char intOp; // to store acceptable int op
    private CST myTree; // instance of Concrete Syntax Tree
    private int instructionCount; // count what instruction we are on
    private Token currentToken; // the current token we are on
    private int errorCount; // the number of errors found

    // Null Constructor
    public Parser() {
        // Initialize Private Variables
        myTokens = new ArrayList<Token>();
        gramType = new String[]{"int", "string", "boolean"};
        gramChar = new char[26];
        for (char c = 'a'; c <= 'z'; c++) {
            gramChar[c - 'a'] = c; // using ASCII
        }
        gramSpace = " ";
        gramDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        gramBoolOp = new String[]{"==", "!="};
        gramBoolVal = new String[]{"false", "true"};
        intOp = '+';

        myTree = new CST();
        instructionCount = 0;
        currentToken = null;
    }

    //! Method to read in tokens to parser
    public void tokenStream(ArrayList<Token> lexerTokens) {
        // add each token from method call to parser tokens
        for(Token currToken : lexerTokens) {
            myTokens.add(currToken);
        }
        System.out.println("LEXER TOKENS CACHED IN PARSER...");
    }

    //! Method to reset parser in between program uses
    public void reset() {
        this.myTokens.clear(); // reset token arrayList
        this.myTree.clear();
        this.instructionCount = 0;
        this.errorCount = 0;
        System.out.println("PARSER CLEARED...");
        
    }

    //! Method to get next token from stream -> increment instructionCount
    public void nextToken() {
        this.currentToken = this.myTokens.get(this.instructionCount++);
    }

    // Method to match and add our tokens
    public void match(String expectedValue) {

        // Check if EOP -> check for errors and begin semantic anaylsis
        if(this.currentToken.getLexeme().equals("$")) {
            // Create Node and update next
            this.myTree.addNode("leaf", currentToken.getLexeme());
            if(this.instructionCount < myTokens.size()) {
                this.nextToken();
            }
            // If there are no errors weve reached the end of the parse
            if(this.errorCount == 0) {
                System.out.println("PARSE SUCCESSFULLY COMPLETED WITH " + this.errorCount + " ERROR(S)...");
                // output cst and begin semantic analysis
            } else {
                System.out.println("PARSE FAILED! DUE TO " + this.errorCount + " ERROR(S)...");
                System.out.println("CST Will Not be Displayed...")
            }
        } else if(this.currentToken.getLexeme().equalsIgnoreCase(expectedValue)) {
            // If Current Lexeme matches we make Node and update next
            this.myTree.addNode("leaf", this.currentToken.getLexeme());
            if(this.instructionCount < myTokens.size()) {
                this.nextToken();
            }
        } else {
            // if the current token does not match expected throw an error
            System.out.println("ERROR DETECTED -> Token Lexeme [ " + this.currentToken.getLexeme() + " ] DOES NOT MATCH EXPECTED VALUE [ " + expectedValue + " ]");
            this.errorCount++;
        }
    }
    // method to determine if the token matches our final grammer
    public void matchFinal(String expectedType) {
        // determine which to match
        boolean switchResult = false;
        String expected = "";
        switch(expectedType) {
            case "ID":
                for(int i = 0; i < gramDigit.length; i++) {
                    if(Integer.toString(gramDigit[i]).equalsIgnoreCase(this.currentToken.getLexeme())) {
                        switchResult = true;
                        
                    }
                    expected = expected + gramDigit[i] + " ";
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
            this.myTree.addNode("leaf", this.currentToken.getLexeme());
        } else {
            // throw the error
            System.out.println("ERROR NO MATCH FOUND!!! Input -> " + this.currentToken.getLexeme() + " Does not match any expected: " + expected);
            this.errorCount++;
        }

        // get next token
        this.nextToken();

    }
    // Program ::== Block $ -> 
    public void parseProgram() {
        System.out.println("->Parsing Program<-");
        // create the root node and its type is program
        myTree.addNode("root", "program");
        parseBlock(); 
        // After Recursion we will match this final Token
        match("$");
    }

    // Block ::== { StatementList } 
    public void parseBlock() {
        System.out.println("->Parsing Block<-");
        // Create branch node
        this.myTree.addNode("branch", "block");
        match("{");
        parseStatementList();
        match("}");
        this.myTree.goUp();
    }
    // StatementList ::== Statement StatementList ε
    public void parseStatementList() {
        System.out.println("->Parsing Statement List<-");

        // Create the node
        myTree.addNode("branch", "statement list");

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
        myTree.addNode("branch", "statement");

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
        }
        this.myTree.goUp();
    }

    // PrintStatement ::== print ( Expr ) 
    public void parsePrint() {
        System.out.println("->Parsing Print<-");

        //Create the node
        this.myTree.addNode("branch", "print");

        // match and consume the ( expected token
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
        this.myTree.addNode("branch", "assignment");

        // Parse expected 'ID'
        parseId();
        
        match("="); // match expected =

        // parse expected 'Expr'
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
        this.myTree.addNode("branch", "while");

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
        this.myTree.addNode("branch", "if");

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
        this.myTree.addNode("branch", "expression");

        // determine expression type and parse accordingly
        switch(this.currentToken.getType()) {
            case "T_DIGIT":
                parseIntExpr();
                break;
            case "T_QUOTE":
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
        this.myTree.addNode("branch", "int expression");

        // parse digit cause always first digit
        parseDigit();

        // check if now were adding then proceed else nothing
        if(this.currentToken.getType().equalsIgnoreCase("T_ADD")) {
            parseIntOp();
            parseExpr();
        }

        this.myTree.goUp();
    }

    // StringExpr ::== " CharList " 
    public void parseStringExpr() {
        System.out.println("->Parsing StringExpr<-");

        // Create the node
        this.myTree.addNode("branch", "string expression");

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
        this.myTree.addNode("branch", "boolean expression");

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

        // Create the node
        this.myTree.addNode("branch", "char list");

        // if next is char then first format
        if(this.currentToken.getType().equalsIgnoreCase("T_CHAR")) {
            parseChar();
            parseCharList();
        } else if(this.currentToken.getType().equalsIgnoreCase("T_SPACE")) {
            // if next is space then second format
            parseSpace();
            parseCharList();
        }
        // else empty do nothing

        this.myTree.goUp();
    }
    // Id ::== char 
    public void parseId() {
        System.out.println("->Parsing Id<-");

        // Create the node
        this.myTree.addNode("branch", "id");

        matchFinal("ID"); // match with grammer

        this.myTree.goUp();
    }

    // type ::== int | string | boolean 
    public void parseType() {
        System.out.println("->Parsing Type<-");

        // Create the node
        this.myTree.addNode("branch", "type");

        matchFinal("TYPE"); // match with grammer

        this.myTree.goUp();
    }

    // char ::== a | b | c ... z 
    public void parseChar() {
        System.out.println("->Parsing Char<-");
        
        // Create the node
        this.myTree.addNode("branch", "char");

        matchFinal("CHAR"); // match with grammer

        this.myTree.goUp();
        
    }

    // space ::== the space character 
    public void parseSpace() {
        System.out.println("->Parsing Space<-");

        // Create the node
        this.myTree.addNode("leaf", "space");

        // match and consume expected space
        match(" ");

        this.myTree.goUp();
    }

    // digit ::== 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 
    public void parseDigit() {
        System.out.println("->Parsing Digit<-");

        // Create the node
        this.myTree.addNode("branch", "digit");

        matchFinal("DIGIT"); // match with grammer

        this.myTree.goUp();
    }

    // boolop ::== == | !=
    public void parseBoolOp() {
        System.out.println("->Parsing BoolOp<-");

        // Create the node
        this.myTree.addNode("branch", "bool operator");

        matchFinal("BOOLOP"); // match with grammer

        this.myTree.goUp();
    }

    // boolval ::== false | true 
    public void parseBoolVal() {
        System.out.println("->Parsing BoolVal<-");

        // Create the node
        this.myTree.addNode("branch", "boolean value");

        matchFinal("BOOLVAL"); // match with grammer

        this.myTree.goUp();
    }

    // intop ::== + 
    public void parseIntOp() {
        System.out.println("->Parsing IntOp<-");

        //Create the node
        this.myTree.addNode("branch", "int operator");

        match("+"); // match for +

        this.myTree.goUp();
    }
}
