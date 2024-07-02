/*
    Semantic Analysis Class -> Analyze AST -> Responsible for 
    -> Traverse AST in DFT Method
    -> Build Symbol Table (Tree of Hash Tables)
    -> Check Scope
    -> Check Type
*/
package project3;

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
            case "Block":
                // IF new Block, create and add to our Symbol Table
                SymbolNode newSymbolNode = new SymbolNode(currScope);
                this.mySymbolTable.addNode(newSymbolNode);

                // Traverse Down the Block
                for(Node child : currNode.getChildren()) {
                    SemanticAnalysis(child, currScope + 1);
                }
                break;
            case "VarDecl":
            case "Assignment":
            case "Print":
            case "IF Statement":
            case "Int Expression":
            case "isEq":
            case "isNotEq":
                //Maybe more to test childs

                
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
