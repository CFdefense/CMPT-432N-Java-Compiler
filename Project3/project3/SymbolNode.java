/*
    Class for SymbolNode Object to be Nodes of the SymbolTable Tree
    ->Child Nodes
    ->Parent Node
    ->Scope
    ->HashTable of Symbols -> per request of slides
*/

package project3;

import java.util.ArrayList;
import java.util.Hashtable;

public class SymbolNode {

    // Private instance Variables
    private SymbolNode myParent; // SymbolNode of Parent SymbolNode
    private ArrayList<SymbolNode> myChildren; // Array of Child SymbolNodes
    private int myScope; // Scope Number
    private Hashtable<String, Symbol> mySymbols; // Hashtable of pair "VarName", Symbol Corresponding to Var
    
    // Null Constructor
    public SymbolNode(int newScope) {
        this.myParent = null;
        this.myChildren = new ArrayList<>();
        this.myScope = newScope;
        this.mySymbols = new Hashtable<>();
    }

    // Semi Constructor
    public SymbolNode(SymbolNode newParent, int newScope) {
        this.myParent = newParent;
        this.myChildren = new ArrayList<>();
        this.myScope = newScope;
        this.mySymbols = new Hashtable<>();
    }

    // Setter Method
    public void setParent(SymbolNode newParent) {
        this.myParent = newParent;
    }

    // Getter Methods
    public SymbolNode getParent() {
        return this.myParent;
    }

    public int getScope() {
        return this.myScope;
    }

    public ArrayList<SymbolNode> getChildren() {
        return this.myChildren;
    }

    public Hashtable<String, Symbol> getSymbols() {
        return this.mySymbols;
    }

    // Method to add children
    public void addChild(SymbolNode newChild) {
        this.myChildren.add(newChild);
    }

    // Method to add create and add symbol
    public void addSymbol(String newType, String newKey, int newLine) {
        mySymbols.put(newKey, new Symbol(newType, newLine, this.myScope));
    }

    // Method to see if ID has been declared
    public Symbol searchID(String ID) {
        Symbol result = null;

        // Lookup key value -> returns symbol or null if DNE
        Symbol findings = mySymbols.get(ID);

        // See if it exists and update results accordingly
        if(findings != null) {
            result = findings;
        }

        return result;
    }
}
