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
    private int[] gramDigit; // to store acceptable digits

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
        this.gramDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
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
        String currType = currNode.getType(); // Curr Node Type
        ArrayList<Node> children = currNode.getChildren(); // Children of Node
        String firstChild = children.get(0).getType(); // First Child
        System.out.println("Curr Node: " + currType);


        // Generate Machine Code Per Situation
        switch(currType) {
            case "VarDecl":

            // Determine Type and Generate Accordingly
            switch(firstChild) {
                case "int":
                case "boolean":
                int varScope = children.get(0).getScope(); // First Child Scope
                String varName = children.get(1).getType(); // Second Child
                LDAConst(varName, varScope); // LDA Accordingly

                break;
                case "string":
                // Simply Add to Static Table Because Data is Solely for Heap

                // Add to Static Table
                this.myStaticTable.add(new TempObject(("T" + this.currTemp + "FF"), varName, this.currTemp, varScope));

                // Increment Current Temporary Value Counter
                this.currTemp++;

                break;
            }
            break;

            case "Assignment":
            // Instance Variables
            int varScope = children.get(0).getScope(); // First Child Scope
            String varName = children.get(1).getType(); // Second Child
            String firstTempAddress = getTempAddress(firstChild, varScope); // Temp Address of First Child
            String firstType = getStaticType(firstChild, varScope); // Type of First Child

            // Create Bytes From FoundTemp Location
            byte firstTempByte = (byte) Integer.parseInt(firstTempAddress.substring(0, 2), 16);
            byte secondTempByte = (byte) Integer.parseInt(firstTempAddress.substring(2, 4), 16);
            
            switch(firstType) {
                case "int":
                case "boolean":
                    // Begin Adding All Other Children
                    for(int i = 1; i < children.size(); i++) {
                        Node currChild = children.get(i); // Get Curr Child starting at second
                        boolean foundDig = false; // Boolean for determining type
                            
                        // Determine if we're looking at an id or number
                        for (int digit : gramDigit) {
                            try {
                                int value = Integer.valueOf(currChild.getType());
                                if (value == digit) {
                                    // If we are an int
                                    foundDig = true;
                                }
                            } catch (NumberFormatException e) {
                                // Not an integer, continue checking
                            }
                        }

                        // Depending on found dig or not do the following
                        if(foundDig) {
                            // Digit Case
                            this.myMemory[this.myCodePointer++] = (byte)0xA9; // LDA Const
                            this.myMemory[this.myCodePointer++] = (byte)(0x0 + Integer.valueOf(currChild.getType())); // Value to Load
                            
                            // STA Temporary Location
                            this.myMemory[this.myCodePointer++] = firstTempByte;
                            this.myMemory[this.myCodePointer++] = secondTempByte;

                        } else {
                            // Its an ID
                            
                        }
                    }

                    break;
                case "string":
            }
            
            // if String write to heap -> then store static pointer

            break;
            case "While Statement":
            break;

            case "If Statement":
            // if(a==b) load X rgister with contents of a, compare x register to b, branch on not equal
            // Add to jump table
            break;
            case "Print":
            // Load register Y with contents of print, load x with 1, SYS Call
            break;
        }   

        // Recursively call on children
        for(Node child : children) {
            generateCode(child);
        }


    }

    // Method to search Static Table For Variable Temp Address
    public String getTempAddress(String varName, int varScope) {
        String tempAddress = ""; // Resulting Temp Address

        // Traverse Static Table
        for(TempObject symbol : this.myStaticTable) {
            // Check if Symbol has Var Name and Scope
            if(symbol.getVarName().equalsIgnoreCase(varName) && symbol.getScope() == varScope) {
                tempAddress = symbol.getTempAddress(); // update its temp address
            }
        }

        return tempAddress;
    }

    // Method to search Static Table For Variable Type
    public String getStaticType(String varName, int varScope) {
        String varType = ""; // Resulting Temp Address

        // Traverse Static Table
        for(TempObject symbol : this.myStaticTable) {
            // Check if Symbol has Var Name and Scope
            if(symbol.getVarName().equalsIgnoreCase(varName) && symbol.getScope() == varScope) {
                varType = symbol.getVarName(); // update its temp address
            }
        }

        return varType;
    }

    public void LDAConst(String newVarName, int newScope) {
        // Load Accumulator -> Default 00 -> means 00 and false
        this.myMemory[this.myCodePointer++] = (byte)0xA9; // LDA Const
        this.myMemory[this.myCodePointer++] = (byte)0x00; // Default of 00

        // STA Temporary Location
        this.myMemory[this.myCodePointer++] = (byte)0x8D; // STA
        this.myMemory[this.myCodePointer++] = (byte)('T' + currTemp); // Temp Location Number
        this.myMemory[this.myCodePointer++] = (byte)0xFF; // Temporary Byte 

        // Add to Static Table
        this.myStaticTable.add(new TempObject(("T" + this.currTemp + "FF"), newVarName, this.currTemp, newScope));

        // Increment Current Temporary Value Counter
        this.currTemp++;
    }

    public void LDAMemory() {
        this.myMemory[this.myCodePointer++] = (byte)0xAD; // LDA Memory

        // Find Temporary Memory of the ID
        String childTempAddress = getTempAddress(currChild.getType(), currChild.getScope());

        // Create Bytes From FoundTemp Location
        byte firstByte = (byte) Integer.parseInt(childTempAddress.substring(0, 2), 16);
        byte secondByte = (byte) Integer.parseInt(childTempAddress.substring(2, 4), 16);

        // LDA Memory Address
                            this.myMemory[this.myCodePointer++] = firstByte;
                            this.myMemory[this.myCodePointer++] = secondByte;

                            //STA Temporary Location
                            this.myMemory[this.myCodePointer++] = (byte)0x8D; // STA

                            // STA Memory Location
                            this.myMemory[this.myCodePointer++] = firstTempByte;
                            this.myMemory[this.myCodePointer++] = secondTempByte;
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
