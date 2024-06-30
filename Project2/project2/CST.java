/*
    CST - Concrete Syntax Tree
    Tree representation of the Grammar(Rules of how the program should be written)
    Will store and manipulate a stream of tokens in conjunction with our established grammer rules

*/

package project2;

public class CST {

    // Private Instance Variables
    private Node myRoot;
    private Node myCurrent;

    // Null Constructor
    public CST() {
        myRoot = null;
        myCurrent = null;
    }

    // clear method for resetting tree
    public void clear() {
        myRoot = null;
        myCurrent = null;
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
            newNode.setParent(myCurrent);
            newNode.getParent().addChild(newNode);
        }

        // If were not a leaf ie root/branch - we set the current to our new node
        if(treeType.equalsIgnoreCase("leaf") == false) {
            this.myCurrent = newNode;
        }

    }

    // Getter methods
    public Node getRoot() {
        return myRoot;
    }

    // Mystery Function to go back up the tree
    public void goUp() {
        if(this.myCurrent.getParent() != null) {
            this.myCurrent = this.myCurrent.getParent();
        } else {
            System.out.println("ERROR - COULD NOT MOVE UP TREE");
        }
    }

    // Recursive method to print the CST to the console
    public void displayCST(Node currNode, int currDepth) {
        // Print a - for each depth
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
}
