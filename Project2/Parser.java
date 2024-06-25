/*
    Parser to take in stream of tokens and then validates them and their syntax


 */

import java.util.ArrayList;

public class Parser {
    // Private Instance Variables
    private ArrayList<Token> myTokens;

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




    // Null Constructor
    public Parser() {
        myTokens = new ArrayList<Token>();
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
    
}
