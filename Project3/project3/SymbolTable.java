/*
    Symbol Table Class -> To be a tree of hash tables
    -> Create Class Methods and Tree Structure

*/
package project3;

public class SymbolTable {

    // Private Instance Varuables
    private SymbolNode myRoot; // Root Node for Tree
    private SymbolNode myCurrent; // Current Node for Tree

    // Null Constructor
    public SymbolTable() {
        this.myRoot = null;
        this.myCurrent = null;
    }

    // clear method for resetting tree
    public void clear() {
        this.myRoot = null;
        this.myCurrent = null;
    }

    // Method to add node to the correct place
    public void addNode(SymbolNode newNode) {
        // Check if the tree is empty
        if(this.myRoot == null) {
            this.myRoot = newNode;
            newNode.setParent(null);
        } else {
            // Else we set the parent to be our current
            newNode.setParent(this.myCurrent);
            newNode.getParent().addChild(newNode);
        }
        // Update Current
        this.myCurrent = newNode;
    }

    // Method to create a new symbol
    public void createSymbol(String newType, String newKey) {

        // Call current scopes add method
        this.myCurrent.addSymbol(newType, newKey);
    }

    // Method to identify if a var has been declared
    public Symbol search(String findID) {
        Symbol result = null;

        // We need to check the current scope and any parent scopes for this variable
        SymbolNode currentNode = this.myCurrent;

        // While were not at scope 0
        while (currentNode != null) {
            // Check current scope for the ID
            if (currentNode.searchID(findID) != null) {
                result = currentNode.searchID(findID);
                break;
            }

            // Get Parent Scope
            currentNode = currentNode.getParent();
        }
        return result;
    }
}
