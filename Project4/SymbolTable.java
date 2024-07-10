/*
    Symbol Table Class -> To be a tree of hash tables
    -> Create Class Methods and Tree Structure

*/
package Project4;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    // Private Instance Varuables
    private SymbolNode myRoot; // Root Node for Tree
    private SymbolNode myCurrent; // Current Node for Tree
    private int myProgramNumber;

    // Null Constructor
    public SymbolTable() {
        this.myRoot = null;
        this.myCurrent = null;
        this.myProgramNumber = 0;
    }

    // clear method for resetting tree
    public void clear() {
        this.myRoot = null;
        this.myCurrent = null;
    }

    // Getter method
    public SymbolNode getRoot() {
        return this.myRoot;
    }

    // Unused but gets rid of VScode errors
    public int getProgramNumber() {
        return this.myProgramNumber;
    }

    // Setter method
    public void setProgramNumber(int newProgramNumber) {
        this.myProgramNumber = newProgramNumber;
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
    public void createSymbol(String newType, String newKey, int newLine) {

        // Call current scopes add method
        this.myCurrent.addSymbol(newType, newKey, newLine);
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

    // Recursive Method to display tree
    public void displayTree(SymbolNode displayCurrent) {

        // If at root display table header
        if(displayCurrent == this.myRoot) {
            System.out.println("--------------------------------------");
            System.out.println("    Name     Type     Scope    Line   ");
            System.out.println("--------------------------------------");
        }

        // Base Case
        if(displayCurrent == null) {
            return;
        } else {
            // Recursive Case

            // Get current SymbolNode's HashTable
            Hashtable<String, Symbol> currentHash = displayCurrent.getSymbols();
            
            // Collect entries into a list
            List<Map.Entry<String, Symbol>> symbolList = new ArrayList<>(currentHash.entrySet());

            // Lambda function to Sort the list based on Symbol.getLine()
            symbolList.sort((entry1, entry2) -> Integer.compare(entry1.getValue().getLine(), entry2.getValue().getLine()));

            // Iterate over Hashtable instances
            for(Map.Entry<String, Symbol> current : symbolList) {
                String currVar = current.getKey();
                Symbol currSymbol = current.getValue();
                int currScope = currSymbol.getScope();
                int currLine = currSymbol.getLine();

                // Print all Symbol Info
                System.out.println("     " + currVar + "       " + currSymbol.getType() + "\t" + currScope + "\t" + currLine);
            
            }

            // Recursive Call on Child Nodes
            for(SymbolNode child : displayCurrent.getChildren()) {
                displayTree(child);
            }
            
        }
    }

    // Method to Determine Unused Variables Following Semantic Analysis
    public int checkUsed(SymbolNode currentSymNode, int warningCount) {
        // Base Case
        if(currentSymNode == null) {
            return warningCount;
        } else {
            // Recursive Case

            // Get current SymbolNode's HashTable
            Hashtable<String, Symbol> currentHash = currentSymNode.getSymbols();

            // Iterate over Hashtable instances
            for (Map.Entry<String, Symbol> current : currentHash.entrySet()) {
            String currVar = current.getKey();
            Symbol currSymbol = current.getValue();
            int currScope = currSymbol.getScope();
            int currLine = currSymbol.getLine();

            // Check if the variable is not used and not init
            if (!currSymbol.getUsed() && !currSymbol.getInit()) {
                warningCount++;
                System.out.println("WARNING: VARIABLE " + currVar + " UNINITIALIZED AND UNUSED | Declared on line " + currLine + ", in scope " + currScope);
            } else if(!currSymbol.getUsed()) {
                warningCount++;
                System.out.println("WARNING: VARIABLE " + currVar + " UNUSED | Declared on line " + currLine + ", in scope " + currScope);
            }
            }

            // Recursively Call on Children
            for(SymbolNode child : currentSymNode.getChildren()) {
                warningCount = checkUsed(child, warningCount);
            }
        }

        return warningCount;
    }
}
