/*
    CST - Concrete Syntax Tree
    Tree representation of the Grammar (Rules of how the program should be written)
    Will Properly place Nodes created by the Parser in a CST tree format

*/

package project2;

public class CST {

    // Private Instance Variables
    private Node myRoot;
    private Node myCurrent;

    //! Begin CST Construction and Manipulation

    // Null Constructor
    public CST() {
        this.myRoot = null;
        this.myCurrent = null;
    }

    // clear method for resetting tree
    public void clear() {
        this.myRoot = null;
        this.myCurrent = null;
    }

    //! End CST Construction and Manipulation

    //! Begin CST Methods

    // Getter method
    public Node getRoot() {
        return this.myRoot;
    }

    // Method to add node to the correct place
    public void addNode(String treeType, String newType) {
        // Create a new node with the type
        Node newNode = new Node(newType, treeType);

        // Check if the tree is empty
        if(this.myRoot == null) {
            this.myRoot = newNode;
            newNode.setParent(null);
        } else {
            // Else we set the parent to be our current
            newNode.setParent(this.myCurrent);
            newNode.getParent().addChild(newNode);
        }

        // If were not a leaf ie root/branch - we set the current to our new node
        if(treeType.equalsIgnoreCase("leaf") == false) {
            this.myCurrent = newNode;
        }

    }

    // Mystery Function to go back up the tree
    public void goUp() {
        if(this.myCurrent.getParent() != null) {
            this.myCurrent = this.myCurrent.getParent();
        } else {
            // Throw error if reached root before we should have
            System.out.println("ERROR - COULD NOT MOVE UP TREE");
        }
    }

    // Recursive method to print the CST to the console
    public void displayCST(Node currNode, int currDepth) {
        // Print a '-' for each depth per example
        for(int i = 0; i < currDepth; i++) {
            System.out.print("-");
        }

        // if leaf node print accordingly
        if(currNode.getChildren().size() == 0) {
            System.out.println("[" + currNode.getType() + "]");
        } else {
            // print type
            System.out.println("<" + currNode.getType() + ">");
            
            // Recursively call the method on the children of the curr node
            for(Node child : currNode.getChildren()) {
            displayCST(child, currDepth + 1);
        }
        }
    }

    //! End CST Methods
}
