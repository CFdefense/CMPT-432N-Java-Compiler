/*
    Semantic Analysis Class -> Analyze AST -> Responsible for 
    -> Traverse AST in DFT Method
    -> Build Symbol Table (Tree of Hash Tables)
    -> Check Scope
    -> Check Type
*/
package project3;

import java.util.ArrayList;

public class Semantic {
    
    // Private Instance Variables
    private AST myAST; // AST to run Semantic Anaylsis on
    private SymbolNode currentNode; // Current Symbol Node(Scope) Were On
    private int errorCount; // Count for # of Errors
    private int warningCount; // Count for # of Warnings
    private SymbolTable mySymbolTable; // Instance of Symbol Table
    private int[] gramDigit; // Acceptable digit values

    // Null Constructor
    public Semantic() {
        this.myAST = null;
        this.errorCount = 0;
        this.warningCount = 0;
        this.currentNode = null;
        this.mySymbolTable = new SymbolTable();
        this.gramDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    }

    // Method to load in AST and begin Semantic Anaylsis
    public void loadAST(AST newAST) {
        // Load in AST
        this.myAST = newAST;
        System.out.println("AST LOADED INTO SEMANTIC...");

        // Begin Semantic Anaylsis
        System.out.println("STARTING SEMANTIC ANALYSIS...");
        SemanticAnalysis(this.myAST.getRoot(), 0);
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

                // Traverse Down the Block
                for(Node child : currNode.getChildren()) {
                    SemanticAnalysis(child, currScope + 1);
                }
                // Go up?
                break;
            case "VarDecl":
                // Get the children of VarDecl
                ArrayList<Node> currVarDecl = currNode.getChildren();

                // Create symbol as first child is type and second is ID
                this.mySymbolTable.createSymbol(currVarDecl.get(0).getType(), currVarDecl.get(1).getType());

                break;
            case "Assignment":
                // Check for declaration and type check

                // Get the children of assign
                ArrayList<Node> currAssign = currNode.getChildren();

                // First Child is the ID, check it has been declared
                Symbol findings = this.mySymbolTable.search(currAssign.get(0).getType());

                // Boolean for result
                boolean overallResult = true;

                // It has been declared
                if(findings != null) {
                    switch(findings.getType()) {
                        case "int":
                            // Ensure all neighboring children are of the same type
                            for(Node children : currAssign) {
                                String childType = children.getType();
                                boolean localResult = false;
                                boolean isNum = false;
                                // Check if it's a digit
                                try {
                                    int childValue = Integer.valueOf(childType);
                                    for (int digit : gramDigit) {
                                        if (digit == childValue) {
                                            localResult = true;
                                            isNum = true;
                                            break;
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    // Not a digit, skip to the next check
                                }           

                                // Check if its a valid id
                                Symbol findingsInt = this.mySymbolTable.search(childType);

                                if((findingsInt != null && findingsInt.getType().equalsIgnoreCase("int")) || isNum) {
                                    localResult = true;
                                } else {
                                    localResult = false;
                                }

                                // Update overall result if any are false
                                if(!localResult) {
                                    overallResult = false;
                                }
                            }

                            if(!overallResult) {
                                System.out.println("ERROR TYPE MISMATCH FOR Integer [ " + currAssign.get(0).getType() + " ]");
                            }

                        case "string":
                            // Ensure all neighboring children are of the same type
                            for(Node children : currAssign) {
                                String childType = children.getType();
                                boolean localResult = false;
                                // Check if it's a string be seeing if first and last are quotes
                                if(childType.charAt(0) == ('\"') && childType.charAt(childType.length() - 1) == ('\"')) {
                                    localResult = true;
                                } else {
                                    // Otherwise Check if its a valid id
                                    Symbol findingsStr = this.mySymbolTable.search(childType);

                                    if((findingsStr != null && findingsStr.getType().equalsIgnoreCase("string"))) {
                                        localResult = true;
                                    } else {
                                        localResult = false;
                                    }
                                }

                                // Update overall result if any are false
                                if(!localResult) {
                                    overallResult = false;
                                }
                            }

                            if(!overallResult) {
                                System.out.println("ERROR TYPE MISMATCH FOR String [ " + currAssign.get(0).getType() + " ]");
                            }

                        case "boolean":
                            // Ensure all neighboring children are of the same type
                            

                    }
                
                } else {
                    // It has not been declared throw error
                    System.out.println("ERROR UNDECLARED VARIABLE [ " + currAssign.get(0) + " ]");
                    this.errorCount++;
                }

            }       
        }

    // Method to clear Semantic
    public void clear() {
        this.errorCount = 0;
        this.warningCount = 0;
        this.currentNode = null;
        this.mySymbolTable.clear();
    }
    
}
