/*
    Generate Machine Code
    Takes an AST and Generates 6502 Machine Code

*/

package project4;

import java.util.ArrayList;

public class GenerateMachineCode {
    
    // Private Instance Variables
    private String[] myMemory; // Byte Array to hold the memory
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
    private String[] gramChar; // to store acceptable chars

    // Null Constructor
    public GenerateMachineCode() {
        this.myMemory = new String[myMaxSize]; // Initialize Byte Array With Size 256
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
        this.gramChar = new String[26];
        for (char c = 'a'; c <= 'z'; c++) {
            this.gramChar[c - 'a'] = String.valueOf(c); // using ASCII
        }
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
        boolean foundCase = false;
        String currType = currNode.getType(); // Curr Node Type
        ArrayList<Node> children = currNode.getChildren(); // Children of Node
        if (children.isEmpty()) {
            return; // Early exit if there are no children
        }
        String firstChild = children.get(0).getType(); // First Child
        int firstScope = children.get(0).getScope();
        System.out.println("Curr Node: " + currType);


        // Generate Machine Code Per Situation
        switch(currType) {
            case "VarDecl":
                foundCase = true; // Update so we dont continue down the tree
            // Determine Type and Generate Accordingly
            switch(firstChild) {
                case "int":
                case "boolean":
                    // Instance Variables
                    int varScope = children.get(0).getScope(); // First Child Scope
                    String varName = children.get(1).getType(); // Second Child

                    // Generate Machine Code
                    LDAConst("00"); // LDA 0x00 Default
                    STAMemory(varName, varScope, firstChild, true, "0", "0"); // STA Create New Entry

                break;
    
                case "string":
                    // Instance Variables
                    int varSScope = children.get(0).getScope(); // First Child Scope
                    String varSName = children.get(1).getType(); // Second Child

                    // Add to Static Table
                    this.myStaticTable.add(new TempObject(("T" + this.currTemp + "XX"), firstChild, varSName, this.currTemp, varSScope));

                    // Increment Current Temporary Value Counter
                    this.currTemp++;

                break;
            }

            break;

            case "Assignment":
                foundCase = true; // Update so we dont continue down the tree
                // Instance Variables
                int varScope = children.get(0).getScope(); // First Child Scope
                String varName = children.get(0).getType(); // First Child 
                String varContents = children.get(1).getType(); // Second child
                String firstTempAddress = getTempAddress(firstChild, varScope); // Temp Address of First Child
                String firstType = getStaticType(firstChild, varScope); // Type of First Child

                // Create Bytes From FoundTemp Location
                String firstTempByte = firstTempAddress.substring(0, 2);
                String secondTempByte = firstTempAddress.substring(2, 4);
            
                switch(firstType) {
                    case "int":
                    case "boolean":
                        // Check if there are more than two children use - ADC
                        if(children.size() > 2) {
                            handleExprOverload(children, varScope);
                        } else {
                            // If Children count is 2 or less use LDA

                            // For Each Child Determine Type
                            for(int i = 1; i < children.size(); i++) {
                                Node currChild = children.get(i); // Get Curr Child starting at second

                                // Determine if were looking at a digit
                                boolean foundDig = isDigit(currChild.getType());
                                    
                                // Depending on found dig or not do the following
                                if(foundDig) {
                                    // LDA Constant Value
                                    LDAConst("0" +(currChild.getType()));
    
                                    // STA Temporary Location
                                    STAMemory(varName, varScope, firstChild, false, firstTempByte, secondTempByte);
    
                                } else {
                                    // Its an ID
                                    LDAMemory(currChild, firstTempByte, secondTempByte);
                                }
                        }

                        
                        }

                    break;

                    case "string":
                        // if String write to heap -> then store static pointer
                        writeToHeap(varContents); // Writes String to Heap

                        // Store the heap pointer as a 2-digit hex string
                        String heapAddress = String.format("%02X", myHeapPointer);

                        LDAConst(String.valueOf(heapAddress)); // LDA Heap Pointer
                        STAMemory(varName, varScope, firstChild, false, firstTempByte, secondTempByte); // STA Variable for String

                    break;
                }

            break;

            case "While Statement":

            break;

            case "IF Statement":
                // if(a==b) load X rgister with contents of a, compare x register to b, branch on not equal
                // Add to jump table
                // Determine both sides of boolop and then get memory addresses

                // Get Valued Children
                ArrayList<Node> ifChildren = children.get(0).getChildren();

                // For Each Child
                for(Node child : ifChildren) {
                    // Is ID?
                    String tempAddress = getTempAddress(child.getType(), child.getScope());
                    // Otherwise write to heap
                }
                


            break;
            case "Print":
                foundCase = true; // Update so we dont continue down the tree
                
                // If the first is a quote we know were looking at a charlist
                if(firstChild.charAt(0) == '\"') {
                    // Write Contents to Heap
                    writeToHeap(firstChild);

                    // Get Hex Heap Pointer
                    String heapAddress = String.format("%02X", myHeapPointer);

                    // Load register Y with address of Heap pointer
                    LDYMemory(heapAddress, "00");
                } else {
                    // Were looking at an id -> find it
                    String tempAddress = getTempAddress(firstChild, firstScope);

                    String firstByte = tempAddress.substring(0, 2);
                    String secondByte = tempAddress.substring(2, 4);
                    
                    //Load register Y with contents of print
                    LDYMemory(firstByte, secondByte);
                } 
                
                //load x with 1
                    LDXConst("1");
                //SYS Call
                    SYSCall();
            break;
        }   

        // Recursively call on children if we havent found a case
        if (!children.isEmpty() && !foundCase) {
            for (Node child : children) {
                generateCode(child);
            }
        }

    }

    // Method to search Static Table For Variable Temp Address
    public String getTempAddress(String varName, int varScope) {
        String tempAddress = null; // Resulting Temp Address

        // Traverse Static Table
        for(TempObject symbol : this.myStaticTable) {
            // Check if Symbol has Var Name 
            if(symbol.getVarName().equalsIgnoreCase(varName)) {
                // Now Check if that var has a scope equal to or smaller
                if(symbol.getScope() <= varScope) { 
                    tempAddress = symbol.getTempAddress(); // update its temp address
                    break;
                }
            }
        }

        return tempAddress;
    }

    // Method to search Static Table For Variable Type
    public String getStaticType(String varName, int varScope) {
        String varType = ""; // Resulting Type

        // Traverse Static Table
        for(TempObject symbol : this.myStaticTable) {
            // Check if Symbol has Var Name
            if(symbol.getVarName().equalsIgnoreCase(varName)) {
                // Check if Scope is equal or less than
                if(symbol.getScope() <= varScope) {
                    varType = symbol.getType(); // update its type
                }
            }
        }
        return varType;
    }

    // Method to determine if we have a digit
    public boolean isDigit(String value) {
        // Determine if we're looking at an id or number
        boolean foundDig = false;
        for (int digit : gramDigit) {
            try {
                int newValue = Integer.valueOf(value);
                if (newValue == digit) {
                    // If we are an int
                    foundDig = true;
                    break;
                }
            } catch (NumberFormatException e) {
                // Not an integer, continue checking
            }
        }
        return foundDig;
    }

    // Method to handle Expr Overload ie assignments such as a = 1 + 2 + a
    public void handleExprOverload(ArrayList<Node> children, int varScope) {
        // Instance Variables
        boolean isFirstAddition = true; // Flag First Var For Indicating LDA
        String varName = children.get(0).getType();
        String varTempAddress = getTempAddress(varName, varScope);
        String firstByte = varTempAddress.substring(0, 2);
        String secondByte = varTempAddress.substring(2, 4);

        for (int i = 1; i < children.size(); i++) {
            Node currChild = children.get(i);
            boolean isDigit = isDigit(currChild.getType());

            if (isDigit) {
                if (isFirstAddition) {
                    LDAConst("0" + currChild.getType());
                    isFirstAddition = false;
                } else {
                    ADCConst(currChild.getType()); // UPD
                }
            } else {
                String tempAddress = getTempAddress(currChild.getType(), currChild.getScope());
                if (isFirstAddition) {
                    LDAMemory(currChild, firstByte, secondByte);
                    isFirstAddition = false;
                } else {
                    ADCMemory(currChild, firstByte, secondByte);
                }
            }
        }

        // Store the result back to the variable
        String varAddress = getTempAddress(varName, varScope);

        // Split Var Address into Bytes
        String varFirstByte = varAddress.substring(0, 2);
        String varSecondByte = varAddress.substring(2, 4);

        // Store the Accumulator into Memory
        STAMemory(varName, varScope, varName, false, varFirstByte, varSecondByte);
    }
    

    // Method to Load Accumulator with Constant
    public void LDAConst(String value) {
        // Load Accumulator -> Default 00 -> means 00 and false
        this.myMemory[this.myCodePointer++] = "A9"; // LDA Const
        this.myMemory[this.myCodePointer++] = value; // Default of 00
    }

    // Method to Store the Accumulator in Memory
    public void STAMemory(String newVarName, int newScope, String newType, boolean isNew, String firstByte, String secondByte) {
        // STA Temporary Location
        this.myMemory[this.myCodePointer++] = "8D"; // STA

        if(isNew) {
            // If Were Making a New Static Entry
            this.myMemory[this.myCodePointer++] = ('T' + String.valueOf(currTemp)); // Temp Location Number
            this.myMemory[this.myCodePointer++] = "XX"; // Temporary Byte 
    
            // Add to Static Table
            this.myStaticTable.add(new TempObject(("T" + this.currTemp + "XX"), newType, newVarName, this.currTemp, newScope));
    
            // Increment Current Temporary Value Counter
            this.currTemp++;
        } else {
            // Existing Static Entry
            this.myMemory[this.myCodePointer++] = firstByte;
            this.myMemory[this.myCodePointer++] = secondByte;
        }
        
    }

    // Method to Add a Constant From Memory 
    public void ADCConst(String value) {
        this.myMemory[this.myCodePointer++] = "69"; // ADC Op Code -> Not in Instruction set but needed?
        this.myMemory[this.myCodePointer++] = value; // Value to add
    }
    
    // Method to Add with Carry from Memory
    public void ADCMemory(Node currChild, String firstByte, String secondByte) {
        this.myMemory[this.myCodePointer++] = "6D"; // ADC Op Code
        this.myMemory[this.myCodePointer++] = firstByte;
        this.myMemory[this.myCodePointer++] = secondByte;
    }

    // Method to Load Accumulator with a Memory Address
    public void LDAMemory(Node currChild, String firstTempByte, String secondTempByte) {
        this.myMemory[this.myCodePointer++] = "AD"; // LDA Memory

        // Find Temporary Memory of the ID
        String childTempAddress = getTempAddress(currChild.getType(), currChild.getScope());

        // Create Bytes From FoundTemp Location
        String firstByte = childTempAddress.substring(0, 2);
        String  secondByte = childTempAddress.substring(2, 4);

        // LDA Memory Address
        this.myMemory[this.myCodePointer++] = firstByte;
        this.myMemory[this.myCodePointer++] = secondByte;

        //STA Temporary Location
        this.myMemory[this.myCodePointer++] = "8D"; // STA 

        // STA Memory Location
        this.myMemory[this.myCodePointer++] = firstTempByte;
        this.myMemory[this.myCodePointer++] = secondTempByte;
    }

    // Method to Load the Y Register With a Memory Address
    public void LDYMemory(String firstByte, String secondByte) {
        this.myMemory[this.myCodePointer++] = "AC"; // LDY Op code

        // Check if were using Temp Memory Address
        if(firstByte.charAt(0) == 'T' && secondByte.charAt(1) == 'X') {
            this.myMemory[this.myCodePointer++] = firstByte;
            this.myMemory[this.myCodePointer++] = secondByte;
        } else {
            // Heap Memory Address
            this.myMemory[this.myCodePointer++] = firstByte;
        }
    }

    // Method to Load the X Register With a Constant
    public void LDXConst(String value) {
        this.myMemory[this.myCodePointer++] = "A2"; // LDX Op code

        this.myMemory[this.myCodePointer++] = "0" + value; // Value to be loaded
    }

    // Method to Load the X Register With a Memory Address
    public void LDXMemory(String firstByte, String secondByte) {
        this.myMemory[this.myCodePointer++] = "AE"; // LDX Op code

        // Check if were using Temp Memory Address
        if(firstByte.charAt(0) == 'T' && secondByte.charAt(1) == 'X') {
            this.myMemory[this.myCodePointer++] = firstByte;
            this.myMemory[this.myCodePointer++] = secondByte;
        } else {
            // Heap Memory Address
            this.myMemory[this.myCodePointer++] = firstByte;
        }
    }

    // Method to Write EC And Its Comparing Address to Memory
    public void CPXMemory(String firstByte, String secondByte) {
        this.myMemory[this.myCodePointer++] = "EC"; // CPX Op code

        // Check if were using Temp Memory Address
        if(firstByte.charAt(0) == 'T' && secondByte.charAt(1) == 'X') {
            this.myMemory[this.myCodePointer++] = firstByte;
            this.myMemory[this.myCodePointer++] = secondByte;
        } else {
            // Heap Memory Address
            this.myMemory[this.myCodePointer++] = firstByte;
        }
    }

    // Method to Write SYS Call to Memory
    public void SYSCall() {
        this.myMemory[this.myCodePointer++] = "FF"; // Write SYS Call to Memory
    }

    // Method to Write Break Call to Memory
    public void BRKCall() {
        this.myMemory[this.myCodePointer++] = "00"; // Write BRK Call to Memory
    }

    // Method to Write a String to the Heap
    public void writeToHeap(String value) {
        this.myMemory[--myHeapPointer] = "00"; // Write 00 next up to seperate Strings

        // Add each Characters Ascii Hex To The Heap
        for(int i = value.length() - 2; i > 0; i--) { // skip first and last cause ""
            this.myMemory[--myHeapPointer] = Integer.toHexString(value.charAt(i)).toUpperCase();
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
        this.myMemory = new String[myMaxSize];
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
