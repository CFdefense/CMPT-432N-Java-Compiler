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

    // Mystery Function to go back up the tree
    public void goUp() {
        if(this.myCurrent.getParent() != null) {
            this.myCurrent = this.myCurrent.getParent();
        } else {
            System.out.println("ERROR - COULD NOT MOVE UP TREE");
        }
    }


}
