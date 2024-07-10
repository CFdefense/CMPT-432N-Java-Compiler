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
    private int myProgramNumber; // Current Program Number

    //! Begin AST Construction and Manipulation

    // Null Constructor
    public AST() {
        this.myProgramNumber = 0;
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

    // Setter Method
    public void setProgramNumber(int newProgramNumber) {
        this.myProgramNumber = newProgramNumber;
    }

    // Getter Method -> Unused but gets rid of VScode errors
    public int getProgramNumber() {
        return this.myProgramNumber;
    }

    // Method to load in CST and create AST 
    public void loadAST(CST newCST) {
        // Load CST and update curent and root
        this.myCST = newCST;

        // Call creation of AST
        createAST(myCST.getRoot());   
    }

    // Method for converting the CST into the AST 'Just the good stuff' 
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
            case "Digit":
            case "ID":
            case "Type":
            case "Statement List":
            case "Statement":
            case "Expression":
            case "String Expression":
            case "Int Expression":
            case "Boolean Value":
                // Dont Create but move deeper

                // update current CST node
                this.myCurrCST = currCSTNode;

                // Recursively call on children of currCST node
                for(Node child : this.myCurrCST.getChildren()) {
                    createAST(child);
                }
                
                // Move up CST
                goUpCST();

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
                // create the node to be added
                Node newNode;
                if(nodeType.equalsIgnoreCase("int") || nodeType.equalsIgnoreCase("string") || nodeType.equalsIgnoreCase("boolean")) {
                    newNode = new Node(nodeType,"Leaf", myCurrAST, currCSTNode.getLine()); 
                } else {
                    newNode = new Node(nodeType,"Branch", myCurrAST);
                }
                
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
                goUpCST();
                goUpAST();

                break;
            case "Char List": 
                // Char list Case
                String addChar = ""; // String to accumulated all chars
                
                // Dont Create but move deeper
                
                // update current CST node -> go lower
                this.myCurrCST = currCSTNode;

                // Recursively call on children of currCST node
                for(Node currChar : this.myCurrCST.getChildren()) { 
                    for(Node charContents : currChar.getChildren()) {
                        addChar += charContents.getType();
                    }
                }
                
                // add Quotes for clarification
                addChar = "\"" + addChar + "\"";

                // Create the node
                Node newChar = new Node(addChar, "leaf", this.myCurrAST, currCSTNode.getLine());

                // Add to parent node
                this.myCurrAST.addChild(newChar);
                    
                // Move up CST
                goUpCST();

                break;
            default:

                // Checking outside of swich due to glitch :/
                if(nodeType.equalsIgnoreCase("Boolean Expression")) {
                    // Dont Create but move deeper for one case
                    boolean boolCase = false;

                    // Determine if boolean value
                    for(Node child : currCSTNode.getChildren()) {
                        if(child.getType().equalsIgnoreCase("Boolean Value")) {
                            boolCase = true;
                        }
                    }

                    // Call Special Case Method
                    if(!boolCase) {
                        // update current CST node
                        this.myCurrCST = currCSTNode;

                        // Call special case
                        booleanCase();
                    } else {
                        // update current CST node
                        this.myCurrCST = currCSTNode;
                    }
                    
                    // Recursively call on children of currCST node
                    for(Node child : this.myCurrCST.getChildren()) {
                        createAST(child);
                    }
                    
                
                    // Move up Both
                    goUpCST();
                    if(!boolCase) {
                        goUpAST();
                    }

                } else {

                    // Check for char
                    for(int i = 0; i < this.gramChar.length; i++) {
                        if(nodeType.equalsIgnoreCase(gramChar[i])) {
                            foundDigChar = true;
                        }
                    }

                    // Check for digit
                    for(int i = 0; i < this.gramDigit.length; i++) {
                        if(nodeType.equalsIgnoreCase(String.valueOf(gramDigit[i]))) {
                            foundDigChar = true;
                        }
                    }

                    // Check if bool value
                    if(nodeType.equalsIgnoreCase("true") || nodeType.equalsIgnoreCase("false")) {
                        // create new leaf node
                        Node newFound = new Node(nodeType, "leaf", myCurrAST, currCSTNode.getLine());

                        // add leaf node to AST
                        this.myCurrAST.addChild(newFound);

                    }

                    // if we have identified the type to be of a char or digit
                    if(foundDigChar) {
                        // create new leaf node
                        Node newFound = new Node(nodeType, "leaf", myCurrAST, currCSTNode.getLine());

                        // add leaf node to AST
                        this.myCurrAST.addChild(newFound);

                        // Move up CST
                        goUpCST();
                    }
                }
                break;
        }
    }

    // Method to correctly store the unique boolean case
    public void booleanCase() {
        // Boolean case is stored expr boolop expr -> need to reach ahead and pull boolop first

        // Find, Identify and Add BoolOp
        for(Node child : this.myCurrCST.getChildren()) {
            // Find the Child which is 'Bool Op'
            if(child.getType().equalsIgnoreCase("Boolean Operator")) {
                // its child will be the operator
                for(Node operator : child.getChildren()) {
                    if(operator.getType().equalsIgnoreCase("==")) {

                        // Create node
                        Node newFound = new Node("isEq", "Branch", myCurrAST);

                        // add leaf node to AST
                        this.myCurrAST.addChild(newFound);

                        // update current AST
                        this.myCurrAST = newFound;

                    } else if(operator.getType().equalsIgnoreCase("!=")) {
                        
                        // Create node
                        Node newFound = new Node("isNotEq", "Branch", myCurrAST);

                        // add leaf node to AST
                        this.myCurrAST.addChild(newFound);

                        // update current AST
                        this.myCurrAST = newFound;
                    }
                }
            }
        }
    }

    //! End AST Construction and Manipulation

    //! Begin AST Methods

    // Getter Method
    public Node getRoot() {
        return this.myASTRoot;
    }
    
    // Method to go up the CST
    public void goUpCST() {
        if(this.myCurrCST.getParent() != null) {
            this.myCurrCST = this.myCurrCST.getParent();
        } else {
            // Throw error if reached root before we should have
            System.out.println("ERROR - COULD NOT MOVE UP CST");
        }
    }

    // Method to go up AST
    public void goUpAST() {
        if(this.myCurrAST.getParent() != null) {
            this.myCurrAST = this.myCurrAST.getParent();
        } else {
            // Throw error if reached root before we should have
            System.out.println("ERROR - COULD NOT MOVE UP AST");
        }
    }

    // Recursive method to print the CST to the console
    public void displayAST(Node currNode, int currDepth) {
        // Print a '-' for each depth per example
        for(int i = 0; i < currDepth; i++) {
            System.out.print("-");
        }

        // if leaf node print accordingly
        if(currNode.getChildren().size() == 0) {
            System.out.println("[" + currNode.getType() + "]");
        } else {
            // print type -> Dont print program but need for a root
            if(!currNode.getType().equalsIgnoreCase("Program")) {
                System.out.println("<" + currNode.getType() + ">");
            }
            
            // Recursively call the method on the children of the curr node
            for(Node child : currNode.getChildren()) {
            displayAST(child, currDepth + 1);
            }
        }
    }
    //! End AST Methods
}
