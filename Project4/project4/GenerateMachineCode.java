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

    // Null Constructor
    public GenerateMachineCode() {
        this.myMemory = new byte[myMaxSize]; // Initialize Byte Array With Size 256
        this.myCodePointer = 0; // Code Pointer Starts at Beginning
        this.myStackPointer = 0; // To Start After Code Pointer
        this.myHeapPointer = myMaxSize; // Heap Pointer Starts at End
        this.myAST = null; // To be Loaded in
        this.myProgramCounter = 0; // To be Updated
        this.myErrorCount = 0; // Initialize to 0
    }   

    // Setter Methods
    public void setAST(AST newAST) {
        this.myAST = newAST;
    }

    public void setProgramCount(int newProgramCounter) {
        this.myProgramCounter = newProgramCounter;
    }

    // Method For Generating Machine Code
    public void generateCode() {

    }

}
