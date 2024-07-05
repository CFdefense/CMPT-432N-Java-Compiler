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
    private int myScope; // Could be a SymbolNode object ie parent?

    // Null Constructor
    public Symbol() {
        this.myName = null;
        this.myType = null;
        this.isInit = false;
        this.isUsed = false;
        this.myScope = 0;
    }

    // Semi Constructor -> maybe init other vars TBD
    public Symbol(String newType) {
        this.myName = null;
        this.myType = newType;
        this.isInit = false;
        this.isUsed = false;
        this.myScope = 0;
    }

    // getter functions
    public String getType() {
        return this.myType;
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
