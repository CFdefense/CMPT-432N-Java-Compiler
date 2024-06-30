/*
    Node class for CST to hold values of each node in the tree
    Parent - Contents - Children
*/

package project2;

import java.util.ArrayList;

public class Node {

    // Private Instance Variables
    private String myType; // to indicate token type
    private String myTreeType; // to indicate type of tree structure
    private Node myParent; // parent node
    private ArrayList<Node> myChildren; // list of child nodes
    
    // Null Constructor
    public Node() {
        myType = "";
        myTreeType = "";
        myParent = null;
        myChildren = new ArrayList<Node>();
    }

    // Semi Constructor
    public Node(String newType, String newTreeType) {
        myType = newType;
        myTreeType = newTreeType;
        myParent = null;
        myChildren = new ArrayList<Node>();
    }

    // Setter Methods
    public void setType(String newType) {
        myType = newType;
    }

    public void setParent(Node newParent) {
        myParent = newParent;
    }

    // Getter Methods
    public Node getParent() {
        return myParent;
    }

    // Method for adding children
    public void addChild(Node newChild) {
        myChildren.add(newChild);
    }
}