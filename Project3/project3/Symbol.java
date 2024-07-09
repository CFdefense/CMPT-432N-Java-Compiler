/*
    Class for Symbol Object to be Stored in Symbol Nodes
    -> to hold varName, varType, isInit, isUsed, Scope, Line #
*/

package project3;

public class Symbol {

    // Private Instance Variables
    private String myName; // variable name ie id
    private String myType; // variable type ie string bool etc
    private boolean isInit; // if Symbol has been initialized
    private boolean isUsed; // if Symbol has been used
    private int myScope; // Current symbols Scope
    private int myLine; // Current symbol Line #

    // Null Constructor
    public Symbol() {
        this.myName = null;
        this.myType = null;
        this.isInit = false;
        this.isUsed = false;
        this.myScope = 0;
        this.myLine = 0;
    }

    // Semi Constructor -> maybe init other vars TBD
    public Symbol(String newType, int newLine, int newScope) {
        this.myName = null;
        this.myType = newType;
        this.isInit = false;
        this.isUsed = false;
        this.myScope = newScope;
        this.myLine = newLine;
    }

    // getter functions
    public String getType() {
        return this.myType;
    }

    public String getName() {
        return this.myName;
    }

    public boolean getInit() {
        return this.isInit;
    }

    public boolean getUsed() {
        return this.isUsed;
    }
    
    public int getScope() {
        return this.myScope;
    }

    public int getLine() {
        return this.myLine;
    }

    // Setter functions
    public void setName(String newName) {
        this.myName = newName;
    }

    public void setInit(boolean newInit) {
        this.isInit = newInit;
    }

    public void setUsed(boolean newUsed) {
        this.isUsed = newUsed;
    }

    public void setScope(int newScope) {
        this.myScope = newScope;
    }

}
