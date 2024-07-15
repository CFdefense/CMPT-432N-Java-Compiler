/*
    Node class for CST to hold values of each node in the tree
    Parent - Contents - Children
*/

package project4;

import java.util.ArrayList;

public class Node {

    // Private Instance Variables
    private String myType; // to indicate token type
    private String myTreeType; // to indicate type of tree structure
    private Node myParent; // parent node
    private ArrayList<Node> myChildren; // list of child nodes
    private int myLine;
    private int myScope;

    //! Start Node Construction
    
    // Null Constructor
    public Node() {
        this.myType = "";
        this.myTreeType = "";
        this.myParent = null;
        this.myChildren = new ArrayList<Node>();
        this.myLine = -1;
        this.myScope = -1;
    }

    // Semi Constructor
    public Node(String newType, String newTreeType) {
        this.myType = newType;
        this.myTreeType = newTreeType;
        this.myParent = null;
        this.myChildren = new ArrayList<Node>();
        this.myLine = -1;
    }

    // Semi Constructor
    public Node(String newType, String newTreeType, int newLine) {
        this.myType = newType;
        this.myTreeType = newTreeType;
        this.myParent = null;
        this.myChildren = new ArrayList<Node>();
        this.myLine = newLine;
    }

    // Semi Constructor
    public Node(String newType, String newTreeType, Node newParent) {
        this.myType = newType;
        this.myTreeType = newTreeType;
        this.myParent = newParent;
        this.myChildren = new ArrayList<Node>();
        this.myLine = -1;
    }

    // Full Constructor
    public Node(String newType, String newTreeType, Node newParent, int newLine) {
        this.myType = newType;
        this.myTreeType = newTreeType;
        this.myParent = newParent;
        this.myChildren = new ArrayList<Node>();
        this.myLine = newLine;
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

    public void setScope(int newScope) {
        this.myScope = newScope;
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

    public int getLine() {
        return this.myLine;
    }

    public int getScope() {
        return this.myScope;
    }

    // Method for adding children
    public void addChild(Node newChild) {
        this.myChildren.add(newChild);
    }

    //! End Node Methods
}