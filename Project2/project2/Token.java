/*
    Token Object to be generated from Lexical Anaylsis
    Tokens hold a type, lexeme and the line in which they were read from.
*/

package project2;

public class Token {
    
    // Private Instance Variables
    private String myType;
    private String myLexeme;
    private int myLine;

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