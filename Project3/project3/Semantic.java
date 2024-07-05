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

    // Null Constructor
    public Semantic() {
        this.myAST = null;
        this.errorCount = 0;
        this.warningCount = 0;
        this.currentNode = null;
        this.mySymbolTable = new SymbolTable();
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
                // Assignment is ID = EXPR where expr can be 1 or 1 + 2

                // Get the children of assign
                ArrayList<Node> currAssign = currNode.getChildren();

                // First Child is the ID, check it has been declared
                Symbol findings = this.mySymbolTable.search(currAssign.get(0).getType());
                if(findings != null) {
                    // if findings arent null we continue to type check
                    // get current type
                    String currType = findings.getType();

                    // Switch type with expected values
                    switch(currType) {
                        case "int":
                            // Int expression case
                            
                        break;
                        case "string":
                            // String case either a or "something"
                            boolean result = false;

                            // Look to see if second index is a valid declared id
                            Symbol findingsString = this.mySymbolTable.search(currAssign.get(1).getType());

                            // Short circuit evaluation to check if index is a valid id and of type string
                            if(findingsString != null && findingsString.getType().equalsIgnoreCase("string")) {
                                result = true;
                            }
                            
                            // Otherwise check if there are quotes at the front and the back
                            if(currAssign.get(1).getType().charAt(0) == '\"' || currAssign.get(1).getType().charAt(currAssign.get(1).getType().length() - 1) == '\"') {
                                result = true;
                            }

                            // Output results
                            if(result != true) {
                                System.out.println("ERROR Type-Mismatch - Expected String Variation but found " + findingsString.getType());
                                this.errorCount++;
                            }

                        break;
                    }
                    
                } else {
                    // ID could not be found
                    System.out.println("ERROR ID [ " + currAssign.get(0).getType() + " ] UNDECLARED WITHIN SCOPE");
                    this.errorCount++;
                }
                break;
            case "isEq":
                // Check for declaration and type check
            case "isNotEq":
                // Check for declaration and type check
            case "Print":
                // Check for declaration
            case "IF Statement":
                // Traverse down
            case "Int Expression":
                // Traverse down    
        }

        // Otherwise traverse deeper looking for VarDecl,Assign,Print,If,isEq,isNotEq,etc


    }

    // Method to clear Semantic
    public void clear() {
        this.myAST.clear();
        this.errorCount = 0;
        this.warningCount = 0;
        this.currentNode = null;
        this.mySymbolTable.clear();
    }
    
}
