/*
    Parser to take in stream of tokens and then validates them and their syntax


 */

import java.util.ArrayList;

public class Parser {
    // Private Instance Variables
    private ArrayList<Token> myTokens;

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
