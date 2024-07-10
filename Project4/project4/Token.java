/*
    Token Object to be generated from Lexical Anaylsis
    Tokens hold a type, lexeme and the line in which they were read from.
*/

package project4;

public class Token {
    
    // Private Instance Variables
    private String myType; // type of token
    private String myLexeme; // lexeme of token
    private int myLine; // the line the token is on

    //! Begin Token Construction and Manipulation

    // Full Constructor
    public Token(String newType, String newLexeme, int newLine) {
        this.myType = newType;
        this.myLexeme =newLexeme;
        this.myLine = newLine;
    }

    //! End Token Construction and Manipulation

    //! Begin Token Methods

    // Getter Methods
    public String getType() {
        return this.myType;
    }

    public String getLexeme() {
        return this.myLexeme;
    }

    public int getLine() {
        return this.myLine;
    }

    //! End Token Methods

}