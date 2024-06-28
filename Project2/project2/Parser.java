/*
    Parser to take in stream of tokens and then validates them and their syntax


*/

package project2;

import java.util.ArrayList;

import project2.Token;

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
    }

    //! Method to read in tokens to parser
    public void tokenStream(ArrayList<Token> lexerTokens) {
        // add each token from method call to parser tokens
        for(Token currToken : lexerTokens) {
            myTokens.add(currToken);
        }
    }

    //! Method to reset parser in between program uses
    public void reset() {
        myTokens.clear(); // reset token arrayList
    }

    public void parseProgram() {
        
    }

    public void parseBlock() {

    }

    public void parseStatement() {

    }

    public void parseStatementList() {

    }

    public void parsePrint() {

    }

    public void parseAssignment() {

    }

    public void parseVarDecl() {

    }

    public void parseWhile() {

    }

    public void parseIf() {
        
    }

    public void parseExpr() {

    }

    public void parseIntExpr() {

    }

    public void parseStringExpr() {

    }

    public void parseBooleanExpr() {

    }

    public void parseCharList() {

    }

    public void parseId() {

    }

    public void parseType() {

    }

    public void parseChar() {

    }

    public void parseSpace() {

    }

    public void parseDigit() {

    }

    public void parseBoolOp() {

    }

    public void parseBoolVal() {

    }

    public void parseIntOp() {

    }
}
