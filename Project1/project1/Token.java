/*
    Token Object to be generated from Lexical Anaylsis
    Tokens hold a type, lexeme and the line in which they were read from.
*/
C:\Users\CFdef\Documents\GitHub\Compiler\Project3\testfiles\test6.txt
package project1;

public class Token {
    
    // Private Instance Variables
    private String myType;
    private String myLexeme;
    private int myLine;

    // Full Constructor
    public Token(String newType, String newLexeme, int newLine) {
        this.myType = newType;
        this.myLexeme =newLexeme;
        this.myLine = newLine;
    }

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

}