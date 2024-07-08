/*
    Symbol Table Class -> To be a tree of hash tables
    -> Create Class Methods and Tree Structure

*/
package project3;

import java.util.Hashtable;
import java.util.Map;

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

    // Method to update the current symbol node
    public void setCurrent(SymbolNode newCurrent) {
        this.myCurrent = newCurrent;
    }

    public void displaySymbolTable() {
        // Instance Variables
        SymbolNode displayCurrent = this.myRoot; // start at the root

        // We will print until we cannot go up and print any more
        while(displayCurrent != null) {
            // Output Scope
            System.out.println("--- Scope " + displayCurrent.getScope() + " ---");

            // Get current SymbolNode's HashTable
            Hashtable<String, Symbol> currentHash = displayCurrent.getSymbols();

            // Iterate over Hashtable instances
            for(Map.Entry<String, Symbol> current : currentHash.entrySet()) {
                String currVar = current.getKey();
                Symbol currSymbol = current.getValue();

                // Print all Info
                System.out.println("- " + currVar + " | " + );
            }
        }
    }
}
