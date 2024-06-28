/*
    Node class for CST to hold values of each node in the tree
    Parent - Contents - Children
*/

package project2;

import java.util.ArrayList;

public class Node {

    // Private Instance Variables
    private String myType;
    private Node myParent;
    private ArrayList<Node> myChildren;
    
    // Null Constructor
    public Node() {
        myType = "";
        myParent = null;
        myChildren = new ArrayList<Node>();
    }

    // Semi Constructor
    public Node(String newType) {
        myType = newType;
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