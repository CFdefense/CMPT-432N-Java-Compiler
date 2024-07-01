/*
    AST - Abstract Syntax Tree
    To read in the CST and convert it to an AST


*/

package project3;

public class AST {

    // Private Instance Variables
    private Node myASTRoot; // root node of the tree
    private Node myCurrAST; // current node we are on
    private Node myCurrCST;
    private CST myCST; // the CST we are turning into AST
    private String[] gramChar; // to store acceptable chars
    private int[] gramDigit; // to store acceptable digits

    //! Begin AST Construction and Manipulation

    // Null Constructor
    public AST() {
        this.myASTRoot = null; // to keep track of AST root
        this.myCurrAST = null; // to keep track of current AST node
        this.myCurrCST = null; // to keep track of CST curr node being traversed
        this.myCST = null;
        this.gramChar = new String[26];
        for (char c = 'a'; c <= 'z'; c++) {
            this.gramChar[c - 'a'] = String.valueOf(c); // using ASCII
        }
        this.gramDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    // clear method for resetting tree
    public void clear() {
        this.myASTRoot = null;
        this.myCurrAST = null;
        this.myCurrCST = null;
        this.myCST = null;
    }

    // Method to load in CST and create AST 
    public void loadAST(CST newCST) {
        // Load CST and update curent and root
        this.myCST = newCST;

        // Call creation of AST
        createAST(myCST.getRoot());
    }

    // Method for converting the CST into the AST 'Just the good stuff'
    // AST WILL CONTAIN -> Block(no brace), VarDecl, Type/Id(Specific ie 'int' or 'a' etc)
    // -> Assignment, Print Statement
    // Traverse the CST looking for the above statements
    public void createAST(Node currCSTNode) {

        // Instance Variable for Type of CST Node
        String nodeType = currCSTNode.getType();
        boolean foundDigChar = false;

        // check for types -> these will create a node and continue down
        switch(nodeType) {
            case "Program":
                // Create new node
                Node newASTNode = new Node("Program", "root");

                // Set AST root as this new node
                this.myASTRoot = newASTNode;

                // Update AST current to be this new node
                this.myCurrAST = newASTNode;

                // Update current CST pointer to be curr node of the method
                this.myCurrCST = currCSTNode;
                
                // Recursively Call createAST on children of curr CST Node
                for(Node child : this.myCurrCST.getChildren()) {
                    createAST(child);
                }

                break;
            case "Block":
            case "VarDecl":
            case "Assignment":
            case "Print":
            case "int":
            case "string":
            case "boolean":
            case "IF Statement":
            case "While Statement":
            case "Boolean Expression":
                // create the node to be added
                Node newNode = new Node(nodeType,"Branch");
                
                // add the created node to the AST
                this.myCurrAST.addChild(newNode);

                // update current AST node
                this.myCurrAST = newNode;

                // update current CST node
                this.myCurrCST = currCSTNode;

                // Recursively call on chilren of currCST node
                for(Node child : this.myCurrCST.getChildren()) {
                    createAST(child);
                }

                // Move up?

            default:
                // Check for matching char
                for(int i = 0; i < this.gramChar.length; i++) {
                    if(nodeType.equalsIgnoreCase(gramChar[i])) {
                        foundDigChar = true;
                    }
                }

                // Check for matching digit
                for(int i = 0; i < this.gramDigit.length; i++) {
                    if(nodeType.equalsIgnoreCase(String.valueOf(gramDigit[i]))) {
                        foundDigChar = true;
                    }
                }

                // if we have identified the type to be of a char or digit
                if(foundDigChar) {
                    // create new leaf node
                    Node newFound = new Node(nodeType, "leaf");

                    // add leaf node to AST
                    this.myCurrAST.addChild(newFound);

                    // Move up?
                }

                break;
        }
    }
    //! End AST Construction and Manipulation

    //! Begin AST Methods

    //! End AST Methods
}
