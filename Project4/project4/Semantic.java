/*
    Semantic Analysis Class -> Analyze AST -> Responsible for 
    -> Traverse AST in DFT Method
    -> Build Symbol Table (Tree of Hash Tables)
    -> Check Scope
    -> Check Type
*/
package project4;

import java.util.ArrayList;

public class Semantic {
    
    // Private Instance Variables
    private AST myAST; // AST to run Semantic Anaylsis on
    private SymbolNode currentNode; // Current Symbol Node(Scope) Were On
    private SymbolNode previousNode; // Previous Node to go back to
    private int errorCount; // Count for # of Errors
    private int warningCount; // Count for # of Warnings
    private SymbolTable mySymbolTable; // Instance of Symbol Table
    private int[] gramDigit; // Acceptable digit values
    private int myProgramNumber; // Current program 
    private GenerateMachineCode myCodeGenerator;

    // Null Constructor
    public Semantic() {
        this.myAST = null;
        this.errorCount = 0;
        this.warningCount = 0;
        this.currentNode = null;
        this.previousNode = null;
        this.mySymbolTable = new SymbolTable();
        this.gramDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        this.myProgramNumber = 0;
        this.myCodeGenerator = new GenerateMachineCode();
    }

    // Method For Overall Semantic Analysis Control
    public void startSemantic(AST newAST, int newProgramNumber) {
        // Load in AST
        this.myAST = newAST;

        // Update Program Number
        setProgramNumber(newProgramNumber);

        // Begin Semantic Anaylsis
        System.out.println("\nSTARTING SEMANTIC ANALYSIS ON PROGRAM # " + this.myProgramNumber +  "...\n");
        SemanticAnalysis(this.myAST.getRoot(), 0);

        // Update Warning Count With Unused Vars
        checkForUsed();

        // Display Results and Determine If We Continue
        System.out.println("Program " + this.myProgramNumber + " Semantic Anaylsis produced " + this.errorCount + " error(s) and " + this.warningCount + " warning(s) \n");

        // Determine How We Proceed
        if(this.errorCount > 0) {
            System.out.println("Program " + this.myProgramNumber + " Symbol Table not displayed due to detected Semantic Analysis Error(s)");
        } else {
            // Display Table
            displaySymbolTable();

            // Load AST Into Code Generator
            this.myCodeGenerator.setAST(myAST);

            // Run Code Generator
            this.myCodeGenerator.generateMyMachineCode();
        }
    }

    // Semantic Analysis Method to Traverse AST and Preform Semantic Analysis
    // DFS through AST
    public void SemanticAnalysis(Node currNode, int currScope) {
        
        // Get Current type
        String currentType = currNode.getType();

        // Determine What Type
        switch(currentType) {
            case "Program":
                // IF program continue down
                for(Node child : currNode.getChildren()) {
                    SemanticAnalysis(child, currScope);
                }
                break;
            case "Block":
                // IF new Block, create and add to our Symbol Table
                SymbolNode newSymbolNode = new SymbolNode(currScope);
                this.mySymbolTable.addNode(newSymbolNode);

                // Save previous Block to go back down later
                this.previousNode = this.currentNode;

                // Update Current Block
                this.currentNode = newSymbolNode;

                // Traverse Down the Block
                for(Node child : currNode.getChildren()) {
                    SemanticAnalysis(child, currScope + 1);
                }

                // Go back down
                this.currentNode = this.previousNode;
                this.mySymbolTable.setCurrent(this.currentNode);

                
                break;
            case "VarDecl":
                // Get the children of VarDecl
                ArrayList<Node> currVarDecl = currNode.getChildren();

                // Check if symbol has already been declared
                Symbol isDec = this.mySymbolTable.search(currVarDecl.get(1).getType());
                
                if(isDec == null) {
                    // Create symbol as first child is type and second is ID
                    this.mySymbolTable.createSymbol(currVarDecl.get(0).getType(), currVarDecl.get(1).getType(), currVarDecl.get(1).getLine());

                    // Update the scope for the current node
                    currNode.setScope(currScope);
                } else {
                    // Throw error
                    System.out.println("ERROR: VARIABLE REDECLARED - [ " + currVarDecl.get(1).getType() + " ]");
                    this.errorCount++;
                }

                break;
            case "Assignment":
                // Check for declaration and type check

                // Get the children of assign
                ArrayList<Node> currAssign = currNode.getChildren();

                // Update Scopes of Children
                for(Node child : currAssign) {
                    child.setScope(currScope);
                }

                // First Child is the ID, check it has been declared
                Symbol findings = this.mySymbolTable.search(currAssign.get(0).getType());

                //Arraylist to hold types of assignment
                ArrayList<String> assignTypes = new ArrayList<>();

                // Boolean for result
                boolean overallResult = true;

                // Boolean for isDeclared
                boolean isDeclared = true;

                // It has been declared
                if(findings != null) {
                    findings.setUsed(true); // Mark the variable as used -> Dont need to error check
                    switch(findings.getType()) {
                        case "int":
                            // Log all Neighboring Types (and declare check)
                            for(Node children : currAssign) {
                                String currType = checkType(children);
                                if(!currType.equalsIgnoreCase("")) {
                                    assignTypes.add(currType);
                                }
                            }

                            // Check to see if all Neighboring types are ints
                            for(String typeResults : assignTypes) {
                                if(!typeResults.equalsIgnoreCase("int")) {
                                    overallResult = false;
                                }
                            }

                            // If they are not all ints throw error
                            if(!overallResult) {
                                System.out.println("ERROR TYPE MISMATCH FOR Integer [ " + currAssign.get(0).getType() + " ]");
                                this.errorCount++;
                            }

                            break;
                        case "string":
                            // Log all Neighboring Types (and declare check)
                            for(Node children : currAssign) {
                                String currType = checkType(children);
                                if(!currType.equalsIgnoreCase("")) {
                                    assignTypes.add(currType);
                                }
                            }

                            // Check to see if all Neighboring types are strings
                            for(String typeResults : assignTypes) {
                                if(!typeResults.equalsIgnoreCase("string")) {
                                    overallResult = false;
                                }
                            }

                            // If not all Strings throw error
                            if(!overallResult) {
                                System.out.println("ERROR: TYPE MISMATCH FOR String [ " + currAssign.get(0).getType() + " ]");
                                this.errorCount++;
                            }

                            break;
                        case "boolean":
                            // Log all Neighboring Types (and declare check)
                            for(Node children : currAssign) {
                                String currType = checkType(children);
                                if(!currType.equalsIgnoreCase("")) {
                                    assignTypes.add(currType);
                                }
                            }

                            // Check to see if all Neighboring types are Booleans
                            for(String typeResults : assignTypes) {
                                if(!typeResults.equalsIgnoreCase("boolean")) {
                                    overallResult = false;
                                }
                            }

                            // If they are not all ints throw error
                            if(!overallResult) {
                                System.out.println("ERROR: TYPE MISMATCH FOR Boolean [ " + currAssign.get(0).getType() + " ]");
                                this.errorCount++;
                            }

                            break;
                    }

                } else {
                    // It has not been declared throw error
                    System.out.println("ERROR: UNDECLARED VARIABLE [ " + currAssign.get(0).getType() + " ]");
                    this.errorCount++;
                    isDeclared = false;
                }
                
                // If the Symbol is declared and all types match we set it as initialized
                if(isDeclared && overallResult) {
                    findings.setInit(true);
                }

                break;
            case "While Statement":
            case "IF Statement":
                // Go Down to isEq or isNEq
                for(Node child : currNode.getChildren()) {
                    SemanticAnalysis(child, currScope);
                }
                break;
            case "isEq":
                // type check and declare check the children
            case "isNEq":
                // Both isEq and isNEq children are expr so check if all on both sides are same type
                // Get the children of equals
                ArrayList<Node> currEquals = currNode.getChildren();

                // Assign Scopes of Children
                for(Node child : currEquals) {
                    child.setScope(currScope);
                }
                
                ArrayList<String> leftTypes = new ArrayList<>(); // ArrayList to hold all types of left node
                ArrayList<String> rightTypes = new ArrayList<>(); // ArrayList to hold all types of right node

                // Must and can only have two sides of the ==/!= sign
                Node leftSide = currEquals.get(0);
                Node rightSide = currEquals.get(1);

                String leftType = "";
                String rightType = "";

                boolean leftSuccess = true;
                boolean rightSuccess = true;

                // Add all left types to array
                for(Node child : leftSide.getChildren()) {
                    String currType = checkType(child);
                    if(!currType.equalsIgnoreCase("")) {
                        leftTypes.add(currentType);
                    }
                }

                // Add all right types to array
                for(Node child : rightSide.getChildren()) {
                    String currType = checkType(child);
                    if(!currType.equalsIgnoreCase("")) {
                        rightTypes.add(currentType);
                    }
                }

                // Check if all Types in Left Equal
                for(int i = 0; i < leftTypes.size(); i++) {
                    if(i == 0) {
                        leftType = leftTypes.get(i);
                    } else {
                        if(!leftType.equalsIgnoreCase(leftTypes.get(i))) {
                            System.out.println("ERROR: TYPE MISMATCH - Expected [ " + leftType + " ] but found " + leftTypes.get(i));
                            leftSuccess = false;
                            this.errorCount++;
                            break;
                        }
                    }
                }

                // Check if all Types in Right Equal
                for(int i = 0; i < rightTypes.size(); i++) {
                    if(i == 0) {
                        rightType = rightTypes.get(i);
                    } else {
                        if(!rightType.equalsIgnoreCase(rightTypes.get(i))) {
                            System.out.println("ERROR: TYPE MISMATCH - Expected [ " + rightType + " ] but found " + rightTypes.get(i));
                            rightSuccess = false;
                            this.errorCount++;
                            break;
                        }
                    }
                }
                
                // Check if Both Sides are Equal
                if(!leftSuccess || !rightSuccess) {
                    System.out.println("ERROR: TYPE MISMATCH - [ " + leftType + " ] != [ " + rightType + " ]");
                    this.errorCount++;
                }
                break;
            case "Print":
                // type check and declare check the children

                // Update Scope of Children
                for(Node child : currNode.getChildren()) {
                    child.setScope(currScope);
                }

                //Arraylist to hold types of assignment
                ArrayList<String> printTypes = new ArrayList<>();

                // Boolean for result
                boolean printResult = true;

                // ArrayList to hold all Mismatched Types
                ArrayList<String> misMatched = new ArrayList<>();

                // String for resulting string
                String resultstring = "";

                // Log all children types of print (and declare check)
                for(Node children : currNode.getChildren()) {
                    String printType = checkType(children);
                    if(!printType.equalsIgnoreCase("")) {
                        printTypes.add(printType);
                    }
                }

                // Check to see if all are of the same type
                String firstType = printTypes.get(0);
                for(String prints : printTypes) {
                    if(!prints.equalsIgnoreCase(firstType)) {
                        printResult = false;
                        misMatched.add(prints);
                    }
                }

                // Add up mismatched
                for(String mis : misMatched) {
                    resultstring += (mis + " ");
                }

                // Display results
                if(!printResult) {
                    System.out.println("ERROR: TYPE MISMTACH IN PRINT STATEMENT - Expected [ " + firstType + " ] but found [ " + resultstring + " ]");
                    this.errorCount++;
                }
        }       
    }

    // Method to clear Semantic
    public void clear() {
        this.errorCount = 0;
        this.warningCount = 0;
        this.myProgramNumber = 0;
        this.currentNode = null;
        this.mySymbolTable.clear();
        this.myCodeGenerator.clear();
    }

    public void setProgramNumber(int newProgramNumber) {
        this.myProgramNumber = newProgramNumber;
    }

    // Method to determine the type of a leaf node
    public String checkType(Node leafNode) {
        // Instance Variables
        String nodeType = "";
        String leafType = leafNode.getType();
        boolean isNum = false;
        boolean isBool = false;
        boolean isStr = false;

        // Check if it's a digit
        try {
            int childValue = Integer.valueOf(leafType);
            for (int digit : gramDigit) {
                if (digit == childValue) {
                    isNum = true;
                    nodeType = "int";
                    break;
                }
            }
        } catch (NumberFormatException e) {
            // Not a digit, skip to the next check
        }

        // Check if String
        if(leafNode.getType().charAt(0) == '\"' && leafNode.getType().charAt(leafNode.getType().length() - 1) == '\"') {
            nodeType = "string";
            isStr = true;
        }

        // Check if Bool op
        if(leafNode.getType().equalsIgnoreCase("true") || leafNode.getType().equalsIgnoreCase("false")) {
            nodeType = "boolean";
            isBool = true;
        }

        // Check if its a valid id
        Symbol findings = this.mySymbolTable.search(leafType);
        
        if(findings != null) {
            findings.setUsed(true); // Mark the variable as used no need to error check
            String findingsType = findings.getType();
            switch(findingsType) {
                case "int":
                    nodeType = "int";
                    break;
                case "string":
                    nodeType = "string";
                    break;
                case "boolean":
                    nodeType = "boolean";
                    break;
            }
        } else if(!isNum && !isBool && !isStr) {
            // invalid id
            System.out.println("ERROR: UNDECLARED VARIABLE USED: [ " + leafNode.getType() + " ]");
            this.errorCount++;
        }
        return nodeType;
    }

    // Method to Display The Symbol Table
    public void displaySymbolTable() {
        System.out.println("Displaying Program # " + this.myProgramNumber + " Symbol Table...");
        this.mySymbolTable.displayTree(this.mySymbolTable.getRoot());
    }

    // Method to Determine Unused Variables Following Semantic Analysis
    public void checkForUsed() {
        // Call The Symbol Table's Method
        int warningUpdate = this.mySymbolTable.checkUsed(this.mySymbolTable.getRoot(), 0);

        // Update Semantic Running Warning Count
        this.warningCount += warningUpdate;
    }
    
}
