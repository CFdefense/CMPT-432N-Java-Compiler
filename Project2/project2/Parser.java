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
    private char gramSpace; // to store acceptable space char
    private int[] gramDigit; // to store acceptable digits
    private String[] gramBoolOp; // to store acceptable bool op
    private String[] gramBoolVal; // to store acceptable bool val
    private char intOp; // to store acceptable int op
    private CST myTree; // instance of Concrete Syntax Tree

    // Null Constructor
    public Parser() {
        // Initialize Private Variables
        myTokens = new ArrayList<Token>();
        gramType = new String[]{"int", "string", "boolean"};
        gramChar = new char[26];
        for (char c = 'a'; c <= 'z'; c++) {
            gramChar[c - 'a'] = c; // using ASCII
        }
        gramSpace = ' ';
        gramDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        gramBoolOp = new String[]{"==", "!="};
        gramBoolVal = new String[]{"false", "true"};
        intOp = '+';

        myTree = new CST();
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
        myTokens.clear(); // reset token arrayList
        myTree.clear();
        System.out.println("PARSER CLEARED...");
    }

    // Method to match and add our tokens
    public void match() {

        // Check if our currentToken matches the expected token of the grammer

        // Also check if we match with $ to end the program and check for errors and begin semantic anaylsis

        // if the current token does not match expected throw an error
    }

    public void parseProgram() {
        System.out.println("->Parsing Program<-");
        myTree.addNode("root", "program");
    }

    public void parseBlock() {
        System.out.println("->Parsing Block<-");
    }

    public void parseStatement() {
        System.out.println("->Parsing Statement<-");
    }

    public void parseStatementList() {
        System.out.println("->Parsing Statement List<-");
    }

    public void parsePrint() {
        System.out.println("->Parsing Print<-");
    }

    public void parseAssignment() {
        System.out.println("->Parsing Assignment<-");
    }

    public void parseVarDecl() {
        System.out.println("->Parsing VarDecl<-");
    }

    public void parseWhile() {
        System.out.println("->Parsing While<-");
    }

    public void parseIf() {
        System.out.println("->Parsing If<-");
    }

    public void parseExpr() {
        System.out.println("->Parsing Expr<-");
    }

    public void parseIntExpr() {
        System.out.println("->Parsing IntExpr<-");
    }

    public void parseStringExpr() {
        System.out.println("->Parsing StringExpr<-");
    }

    public void parseBooleanExpr() {
        System.out.println("->Parsing BooleanExpr<-");
    }

    public void parseCharList() {
        System.out.println("->Parsing CharList<-");
    }

    public void parseId() {
        System.out.println("->Parsing Id<-");
    }

    public void parseType() {
        System.out.println("->Parsing Type<-");
    }

    public void parseChar() {
        System.out.println("->Parsing Char<-");
    }

    public void parseSpace() {
        System.out.println("->Parsing Space<-");
    }

    public void parseDigit() {
        System.out.println("->Parsing Digit<-");
    }

    public void parseBoolOp() {
        System.out.println("->Parsing BoolOp<-");
    }

    public void parseBoolVal() {
        System.out.println("->Parsing BoolVal<-");
    }

    public void parseIntOp() {
        System.out.println("->Parsing IntOp<-");
    }
}
