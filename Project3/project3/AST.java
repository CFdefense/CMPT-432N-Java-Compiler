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
        boolean digit = false;

        // check for types -> these will create a node and continue down
        switch(nodeType) {
            case "Program":
                // Create new node
                System.out.println("Creating Program");
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
                // Dont Create but move deeper
                System.out.println("Found " + nodeType + " were going deeper");

                // No update for AST nodes because no new ones created

                // update current CST node
                this.myCurrCST = currCSTNode;

                // Recursively call on children of currCST node
                for(Node child : this.myCurrCST.getChildren()) {
                    createAST(child);
                }
                
                // Move up?
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
            case "Int Expression":
                // create the node to be added
                Node newNode;
                if(nodeType.equalsIgnoreCase("int") || nodeType.equalsIgnoreCase("string") || nodeType.equalsIgnoreCase("boolean")) {
                    newNode = new Node(nodeType,"Leaf", myCurrAST); 
                } else {
                    newNode = new Node(nodeType,"Branch", myCurrAST);
                }
                System.out.println("Created " + nodeType);
                
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
                    // Either "a" or "abc"
                    // Str expression -> Char List -> char ->"" char ->"" char->""
                    String addChar = ""; // String to accumulated all chars

                    // Dont Create but move deeper
                    System.out.println("Found " + nodeType + " were adding chars");

                    // No update for AST nodes because no new ones created

                    // update current CST node -> go lower
                    this.myCurrCST = currCSTNode;

                    // Recursively call on children of currCST node
                    for(Node currChar : this.myCurrCST.getChildren()) { 
                        for(Node charContents : currChar.getChildren()) {
                            addChar += charContents.getType();
                        }
                    }

                    // Create the node
                    Node newChar = new Node(addChar, "leaf", this.myCurrAST);

                    // Add to parent node
                    this.myCurrAST.addChild(newChar);
                    
                    // Move up?
                    goUpCST();
                    break;
            default:
                // Checking here due to weird glitch in switch
                if(nodeType.equalsIgnoreCase("Boolean Expression")) {
                    // Dont Create but move deeper
                    System.out.println("Found " + nodeType + " were going deeper");

                    // No update for AST nodes because no new ones created

                    // update current CST node
                    this.myCurrCST = currCSTNode;

                    // Special case because we have EXPR OP EXPR -> need to find and add EXPR FIRST
                    booleanCase();
                    // Recursively call on chilren of currCST node
                    for(Node child : this.myCurrCST.getChildren()) {
                        createAST(child);
                    }
                    // Move up?
                    goUpCST();
                    goUpAST();
                } else {
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
                            digit = true;
                        }
                    }

                    // if we have identified the type to be of a char or digit
                    if(foundDigChar) {
                        // create new leaf node
                        Node newFound = new Node(nodeType, "leaf", myCurrAST);
                        System.out.println("Created " + nodeType);
                        // add leaf node to AST
                        this.myCurrAST.addChild(newFound);

                        // Move up?
                        goUpCST();
                        if(digit) {
                            //goUpAST(); // add extra go up if we go deep for digit
                        }
                    }
                }
                break;
        }
    }

    // Method to correctly store the unique boolean case
    public void booleanCase() {
        // We have Boolean Expr ::== Expr BoolOp Expr
        // Need to find and add BoolOp type to AST FIRST

        // When this is called current node is 
        // Find, Identify and Add BoolOp
        for(Node child : this.myCurrCST.getChildren()) {
            // Find the Child which is 'Bool Op'
            if(child.getType().equalsIgnoreCase("Boolean Operator")) {
                // its child will be the operator
                for(Node operator : child.getChildren()) {
                    if(operator.getType().equalsIgnoreCase("==")) {
                        // Add Boolop ==
                        Node newFound = new Node("isEq", "Branch", myCurrAST);
                        System.out.println("Created isEq");
                        // add leaf node to AST
                        this.myCurrAST.addChild(newFound);

                        // update current AST
                        this.myCurrAST = newFound;
                    } else if(operator.getType().equalsIgnoreCase("!=")) {
                        // Add Boolop !=
                        Node newFound = new Node("isNotEq", "Branch", myCurrAST);
                        System.out.println("Created isEq");
                        // add leaf node to AST
                        this.myCurrAST.addChild(newFound);

                        // update current AST
                        this.myCurrAST = newFound;
                    }
                }
            }
        }
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

    //! End AST Construction and Manipulation

    //! Begin AST Methods

    //! End AST Methods
}
