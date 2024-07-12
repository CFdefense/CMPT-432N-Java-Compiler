/*
    Generate Machine Code
    Takes an AST and Generates 6502 Machine Code

*/

package project4;

import java.util.ArrayList;

public class GenerateMachineCode {
    
    // Private Instance Variables
    private byte[] myMemory; // Byte Array to hold the memory
    private ArrayList<TempObject> myStaticTable;
    private ArrayList<JumpObject> myJumpTable;
    private int myCodePointer; // Pointer to Current Position in The Code
    private int myStackPointer; // Pointer to Current Position in The Stack
    private int myHeapPointer; // Pointer to Current Position in The Heap
    private final int myMaxSize = 256; // Max Limited Byte Size of Memory
    private AST myAST; // AST to be Translated Into Machine Code
    private int myProgramCounter; // Current Program Were on
    private int myErrorCount; // Error Count? Do we Need?
    private int currTemp; // Current Temp Variable Number

    // Null Constructor
    public GenerateMachineCode() {
        this.myMemory = new byte[myMaxSize]; // Initialize Byte Array With Size 256
        this.myStaticTable = new ArrayList<>(); // Initialize ArrayList
        this.myJumpTable = new ArrayList<>(); // Initialize ArrayList
        this.myCodePointer = 0; // Code Pointer Starts at Beginning
        this.myStackPointer = 0; // To Start After Code Pointer
        this.myHeapPointer = myMaxSize; // Heap Pointer Starts at End
        this.myAST = null; // To be Loaded in
        this.myProgramCounter = 0; // To be Updated
        this.myErrorCount = 0; // Initialize to 0
        this.currTemp = 0; // Initialize to 0 - start T0XX
    }   

    // Method For Controlling all Steps of Code Generation
    public void generateMyMachineCode() {
        
        // Generate Code By Recursively Analyzing The AST
        generateCode(this.myAST.getRoot());

        // Generate Stack

        // BackPatch -> Jumps then Static

        // Fill Zeros
    }

    // Recursive Method For Generating Machine Code
    public void generateCode(Node currNode) {
        // Instance Variables
        String currType = currNode.getType();
        ArrayList<Node> children = currNode.getChildren();
        System.out.println("Curr Node: " + currType);

        // Generate Machine Code Per Situation
        switch(currType) {
            case "VarDecl":
            // Load Accumulator with 0, Store temp location in static table
            
            case "Assignment":
            // Load accumulator with value, lookup value for temp address
            // Can be a digit or id -> digit is simply the value id is the temp address
            // if String write to heap -> then store static pointer

            case "While Statement":

            case "If Statement":
            // if(a==b) load X rgister with contents of a, compare x register to b, branch on not equal
            // Add to jump table

            case "Print":
            // Load register Y with contents of print, load x with 1, SYS Call

        }

        // Recursively call on children
        for(Node child : children) {
            generateCode(child);
        }


    }

    // Setter Methods
    public void setAST(AST newAST) {
        this.myAST = newAST;
    }

    public void setProgramCount(int newProgramCounter) {
        this.myProgramCounter = newProgramCounter;
    }

    // Method For Clearing The Instance
    public void clear() {
        this.myMemory = new byte[myMaxSize];
        this.myStaticTable.clear();
        this.myJumpTable.clear();
        this.myCodePointer = 0;
        this.myStackPointer = 0;
        this.myHeapPointer = myMaxSize;
        this.myAST = null;
        this.myProgramCounter = 0;
        this.myErrorCount = 0;
    }

}
