/*
    Node class for CST to hold values of each node in the tree
    Parent - Contents - Children
*/

package project3;

import java.util.ArrayList;

public class Node {

    // Private Instance Variables
    private String myType; // to indicate token type
    private String myTreeType; // to indicate type of tree structure
    private Node myParent; // parent node
    private ArrayList<Node> myChildren; // list of child nodes

    //! Start Node Construction
    
    // Null Constructor
    public Node() {
        this.myType = "";
        this.myTreeType = "";
        this.myParent = null;
        this.myChildren = new ArrayList<Node>();
    }

    // Semi Constructor
    public Node(String newType, String newTreeType) {
        myType = newType;
        myTreeType = newTreeType;
        myParent = null;
        myChildren = new ArrayList<Node>();
    }

    //! End Node Construction

    //! Begin Node Methods

    // Setter Methods
    public void setType(String newType) {
        this. myType = newType;
    }

    public void setParent(Node newParent) {
        this.myParent = newParent;
    }

    // Getter Methods
    public Node getParent() {
        return this.myParent;
    }

    public String getType() {
        return this.myType;
    }

    public String getTreeType() {
        return this.myTreeType;
    }

    public ArrayList<Node> getChildren() {
        return this.myChildren;
    }

    // Method for adding children
    public void addChild(Node newChild) {
        this.myChildren.add(newChild);
    }

    //! End Node Methods
}