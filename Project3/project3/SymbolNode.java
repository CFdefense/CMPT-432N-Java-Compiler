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

    // Getter Method
    public SymbolNode getParent() {
        return this.myParent;
    }

    // Method to add children
    public void addChild(SymbolNode newChild) {
        this.myChildren.add(newChild);
    }

    // Method to add create and add symbol
    public void addSymbol(String newType, int newLineNumber) {
        mySymbols.put("", new Symbol(newType, newLineNumber));
    }
}
