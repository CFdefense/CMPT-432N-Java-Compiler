/*
    Generate Machine Code
    Takes an AST and Generates 6502 Machine Code

*/

package project4;

import java.util.ArrayList;

public class GenerateMachineCode {
    
    //! Start of Machine Code Generator Construction

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
    private int myErrorCount; // Error Count? Do I Need?
    private int currTemp; // Current Temp Variable Number
    private int currJump; // Current Jump Variable Number
    private int[] gramDigit; // to store acceptable digits
    private String truePointer; // Hex Pointer to 'true'
    private String falsePointer; // Hex Pointer to 'false'
    private String t2Pointer; // Hex Pointer to last byte for use
    private String t1Pointer; // Hex Pointer for second to last byte for Use
    private boolean myHeapOverflow; // Keep track of Heap Overflow 

    // Null Constructor
    public GenerateMachineCode() {
        this.myMemory = new String[myMaxSize]; // Initialize Byte Array With Size 256
        this.myStaticTable = new ArrayList<>(); // Initialize ArrayList
        this.myJumpTable = new ArrayList<>(); // Initialize ArrayList
        this.myCodePointer = 0; // Code Pointer Starts at Beginning
        this.myStackPointer = 0; // To Start After Code Pointer
        this.myHeapPointer = myMaxSize - 13; // Heap Pointer Starts at End
        this.myAST = null; // To be Loaded in
        this.myProgramCounter = 0; // To be Updated
        this.myErrorCount = 0; // Initialize to 0
        this.currTemp = 0; // Initialize to 0 - start T0XX
        this.gramDigit = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}; // Acceptable Digits
        this.currJump = 0; // Current Jump Object
        this.myHeapOverflow = false;

        initMemory();
    }  
    
    // Initialize Memory Spots
    public void initMemory() {
        // Initialize true and false
        this.myMemory[253] = "00";
        this.myMemory[252] = "65"; // e
        this.myMemory[251] = "75"; // u
        this.myMemory[250] = "72"; // r
        this.myMemory[249] = "74"; // t
        this.myMemory[248] = "00"; // divider
        this.myMemory[247] = "65"; // e
        this.myMemory[246] = "73"; // s
        this.myMemory[245] = "6C"; // l
        this.myMemory[244] = "61"; // a
        this.myMemory[243] = "66"; // f

        // Initialize true and false pointers
        this.truePointer = String.valueOf(String.format("%02X", 249));
        this.falsePointer = String.valueOf(String.format("%02X", 243));
        
        // Initialize t2 and t1 pointers
        this.t2Pointer = String.valueOf(String.format("%02X", 255));
        this.t1Pointer = String.valueOf(String.format("%02X", 254));
        this.myMemory[254] = "00"; // initialize t2 to 00
        this.myMemory[255] = "00"; // initialize t1 to 00
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
        this.myHeapPointer = myMaxSize - 13;
        this.myAST = null;
        this.myProgramCounter = 0;
        this.myErrorCount = 0;
        this.currJump = 0;
        this.currTemp = 0;
        this.myHeapOverflow = false;
        initMemory();
    } 

    //! End of Machine Code Generator Construction


    //! Start of Machine Code Functions and Methods

    // Method For Controlling all Steps of Code Generation
    public void generateMyMachineCode() {
        // Starting Machine Code Generator
        System.out.println("\nGENERATING MACHINE CODE FOR PROGRAM # " + this.myProgramCounter + "...");

        // Generate Code By Recursively Analyzing The AST
        generateCode(this.myAST.getRoot());

        // BRK At End
        BRKCall();

        // Generate Stack Pointer
        this.myStackPointer = this.myCodePointer;

        // BackPatch -> Jumps then Static
        backPatchJump();

        // if there are some static variables
        backPatchStatic();

        // Fill Zeros
        fillZeros();

        // Display Memory
        displayMachineCode();

        // Calculate and Display TBU
        displayBytesUsed();
    }

    // Recursive Method For Generating Machine Code
    public void generateCode(Node currNode) {
        // Immediately Leave if No Children
        if(currNode.getChildren().size() == 0 ) {
            return;
        }
        // Instance Variables
        boolean foundCase = false; // Flag for Finding Case
        String currType = currNode.getType(); // Current Case Node Type
        ArrayList<Node> children = currNode.getChildren(); // Children of Node
        String firstChildType = children.get(0).getType(); // First Child Type
        int firstChildScope = children.get(0).getScope(); // First Child Scope
        String firstType = getStaticType(firstChildType, firstChildScope); // Type of First Child

        // Generate Machine Code Per Situation
        switch(currType) {
            case "VarDecl":
                foundCase = true; // Update so we dont continue down the tree
                String secondChildType = children.get(1).getType(); // Second Child Type
                // if int or boolean
                if(firstChildType.equalsIgnoreCase("int") || firstChildType.equalsIgnoreCase("boolean")) {
                    // Generate Machine Code
                    LDAConst("00"); // LDA 0x00 Default
                    STANMemory(firstChildType, secondChildType, firstChildScope); // STA Create New Entry
                } else {
                    // string -> Add to Static Table & Increment Current Temporary Value Counter
                    this.myStaticTable.add(new TempObject(("T" + this.currTemp + "XX"), firstChildType, secondChildType, this.currTemp++, firstChildScope));
                }    
                break;
            case "Assignment":
                // If the First Childs Type is Int or Boolean
                if(firstType.equalsIgnoreCase("int") || firstType.equalsIgnoreCase("boolean")) {
                    boolIntAssignment(children, firstChildType, firstChildScope);
                } else {
                // If the First Childs Type is String
                    String secondAChildType = children.get(1).getType();
                    stringAssignment(secondAChildType, firstChildType, firstChildScope);
                }
                break;
            case "While Statement":
                foundCase = true; // Update Flag
                whileStatement(children);
                break;
            case "IF Statement":
                foundCase = true; // Will traverse down here to keep track of jump location
                ifStatement(children);
                break;
            case "Print":
                foundCase = true; // Update so we dont continue down the tree
                // Create Bytes From FoundTemp Location of First Child
                if(firstChildType.charAt(0) != '\"') {
                    if(firstChildType.equalsIgnoreCase("true") || firstChildType.equalsIgnoreCase("false")) {
                        // case for straight bool printing
                        printBoolop(firstChildType);
                    } else if(isInteger(firstChildType) == true) {
                        // case for straight num printing
                        printNum(firstChildType);
                    } else {
                        // use id/heap temp address
                        String tempByte = getTempAddress(firstChildType, firstChildScope).substring(0, 2);
                        String secondByte = getTempAddress(firstChildType, firstChildScope).substring(2, 4);
    
                        printStatement(firstChildType, firstChildScope, tempByte, secondByte);
                    }
                } else {
                    // Quotes Case 
                    printStatement(firstChildType, 0, "00", "00");
                }
                
                break;
        }

        // Recursively call on children if we havent found a case
        goDownTree(children, true, foundCase);
    }

    // Method For Boolean and Int Assignment 
    public void boolIntAssignment(ArrayList<Node> children, String firstChildType, int firstChildScope) {
        // Check if there are more than two children use - ADC

        // Create Bytes From FoundTemp Location of First Child
        String firstTempByte = getTempAddress(firstChildType, firstChildScope).substring(0, 2);
        String secondTempByte = getTempAddress(firstChildType, firstChildScope).substring(2, 4);
        
        if(children.size() > 2) {
            handleExprOverload(children, firstChildScope);
        } else {
            // If Children count is 2 or less use LDA
            for(int i = 1; i < children.size(); i++) { // For Each Child Determine Type
                Node currChild = children.get(i); // Get Curr Child starting at second

                // Check if variable has been declared and memory can be optimized
                /*if(tryOptimization(firstTempByte, secondTempByte) == true) {
                    // move codepointer to override previous declaration
                    this.myCodePointer -= 5;
                }
                */
                // Depending on found dig or not do the following
                if(isDigit(currChild.getType())) {
                    // LDA Const Value
                    LDAConst("0" +(currChild.getType()));
                } else if(currChild.getType().equalsIgnoreCase("true")) {
                    // Bool Op True -> LDA with true pointer
                    LDAConst(this.truePointer);
                } else if(currChild.getType().equalsIgnoreCase("false")) {
                    // Bool Op False -> LDA with false pointer
                    LDAConst(this.falsePointer);
                } else {
                    LDAMemory(currChild, firstTempByte, secondTempByte, false); // Its an ID
                }
                // STA Temporary Location
                STAEMemory(firstTempByte, secondTempByte);
            }
        }
    }

    // Method For String Assignments
    public void stringAssignment(String varContents, String firstChildType, int firstChildScope) {
        // Create Bytes From FoundTemp Location of First Child
        String firstTempByte = getTempAddress(firstChildType, firstChildScope).substring(0, 2);
        String secondTempByte = getTempAddress(firstChildType, firstChildScope).substring(2, 4);
        
        // if String write to heap -> then store static pointer
        boolean result = writeToHeap(varContents); // Writes String to Heap

        // Upd heapOverFlow
        if(result == true && this.myHeapOverflow == false) {
            System.out.println("ERROR - HEAP OVERFLOW"); // print error once
            this.myErrorCount++; // increment error count once
            this.myHeapOverflow = true;
        }
        LDAConst(String.valueOf(String.format("%02X", myHeapPointer))); // LDA Heap Pointer
        STAEMemory(firstTempByte, secondTempByte); // STA Variable for String
    }

    // Method For Generating Machine Code For While Statements
    public void whileStatement(ArrayList<Node> children) {

        // Get Type of Comparison Being Made
        String comparisonType = children.get(0).getType();

        // Log Starting Address To Jump Back To
        int jumpBackTo = this.myCodePointer;

        // Load X Register with First -> Compare X with Second
        loadCompareX(children); // If Conditions

        // Update currJumpOne
        int currJumpOne = this.currJump;

        int startingPosOne = this.myCodePointer;

        // Branch DNE to Next Jump
        BNE("");

        // Update Accumulator
        if(comparisonType.equalsIgnoreCase("isNotEq")) {
            LDAConst("00");
        } else {
            LDAConst("01"); 
        }
        
        // Store Accumulator at FF 00
        STAEMemory(t2Pointer, "00");

        // Set X Register 
        if(comparisonType.equalsIgnoreCase("isNotEq")) {
            LDXConst("1");
        } else {
            LDXConst("0");
        }
        
        // Compare X register and End Pointr
        CPXMemory("00", "00", true);

        // Update Jump One to Jump here
        updateJumpAddress(currJumpOne, this.myCodePointer - startingPosOne);

        // Branch DNE 
        BNE("05");

        // Update Accumulator
        if(comparisonType.equalsIgnoreCase("isNotEq")) {
            LDAConst("01");
        } else {
            LDAConst("00"); 
        }

        // Store at end pointer
        STAEMemory(t2Pointer, "00");

        // Set X Register
        LDXConst("1");

        // Compare X With End Pointer
        CPXMemory("00", "00", true);

        // Log Number of Jump Object for BNE
        int currJumpTwo = currJump;

        int startingPosTwo = this.myCodePointer;

        // Branch DNE To Post Branch 
        BNE(""); 

        // Do Loop contents by continuing down
        goDownTree(children, false, false);
    
        // Set While Condition Jump To After Block
        updateJumpAddress(currJumpTwo, this.myCodePointer);

        // Reset Accumulator
        LDAConst("00");

        // Store Accumulator at End Pointer
        STAEMemory(t2Pointer, "00");

        // LDX With 1
        LDXConst("1");

        // Compare X With End Pointer
        CPXMemory("00", "00", true);

        //Update Previous Jump marker
        updateJumpAddress(currJumpTwo, this.myCodePointer - startingPosTwo);

        // Jump back to the start of the comparison
        this.myMemory[myCodePointer++] = "D0"; // BNE opcode for unconditional jump
        int offset = jumpBackTo - (this.myCodePointer + 1);
        this.myMemory[myCodePointer++] = String.format("%02X", offset & 0xFF); // Wrap to Fit 255
    }

    // Method For Generating Machine Code For If Statements
    public void ifStatement(ArrayList<Node> children) {
        // Load first Child into X Register -> Compare Second Child to X

        // Get Type of Comparison Being Made
        String comparisonType = children.get(0).getType();

        loadCompareX(children);

        // If Were looking at a != Comparison 
        if(comparisonType.equalsIgnoreCase("isNotEq")) {
            // Reset Accumulator
            LDAConst("00");

            // Branch DNE 02?
            BNE("02");

            // LDA Accumulator 01
            LDAConst("01");

            // Reset X Register
            LDXConst("0");

            // Store Accumulator at End Pointer
            STAEMemory(t2Pointer, "00");

            // Compare X register and End Pointr
            CPXMemory("00", "00", true);
        }
        
        // Log current Jump Number
        int currIfJump = currJump;

        // Branch DNE To Post Bracket
        BNE("");
        
        // Log current Code Pointer For Jump
        int logPointer = this.myCodePointer;

        // Recursively call on children
        goDownTree(children, false, false);

        // Backpatch Jump for Logged Jump Number
        updateJumpAddress(currIfJump, this.myCodePointer - logPointer);
    }

    // Method For Generating Machine Code for Printing Straight BoolVals
    public void printBoolop(String boolType) {
        // set bool pointer to be true or false pointer depending on booltype
        String boolPointer = boolType.equalsIgnoreCase("true") ? this.truePointer : this.falsePointer;

        // Load Y with bool pointer WHY DOES LOADING W CONST WORK BUT LOADING W MEM DOESNT?
        LDYConst(boolPointer);

        // Load X with 02 to tell sys call to print string there
        LDXConst("2");

        // Make SYS Call
        SYSCall();
    }

    // Method to Print Straight Nums
    public void printNum(String firstChildType) {
        // load y with const prefixing num w 0
        LDYConst("0" + firstChildType);

        // load x with 01
        LDXConst("1");

        // sys call
        SYSCall();
    }
    // Method For Generating Machine Code For Print Statements
    public void printStatement(String firstChildType, int firstChildScope, String firstTempByte, String secondTempByte) {
        // If the first has a quote we know were looking at a charlist
        if(firstChildType.charAt(0) == '\"') {
            // Write Contents to Heap
            boolean result = writeToHeap(firstChildType);

            // Upd heapOverFlow
            if(result == true && this.myHeapOverflow == false) {
                System.out.println("ERROR - HEAP OVERFLOW"); // print error once
                this.myErrorCount++; // increment error count once
                this.myHeapOverflow = true;
            }

            // Load register X with 2 to signify there is a string to be read in Y
            LDXConst("2");

            // Load register Y with address of Heap pointer
            LDYConst(String.format("%02X", myHeapPointer));

        } else {
            // Were looking at an id -> get bytes

            //Load register Y with contents of print
            LDYMemory(firstTempByte, secondTempByte);

            // Check What Value to Load X With
            if(getStaticType(firstChildType, firstChildScope).equalsIgnoreCase("string") || getStaticType(firstChildType, firstChildScope).equalsIgnoreCase("boolean")) {
                LDXConst("2");
            } else {
                LDXConst("1");
            }
        }
        // Make SYS Call
        SYSCall();
    }

    // Method to Display The Memory
    public void displayMachineCode() {
        //Instance Variables
        int linecounter = 0;

        // Determine if Machine Code Should Be Displaued
        if(this.myErrorCount <= 0) {
            System.out.println("\nMACHINE CODE SUCCESSFULL GENERATED FOR PROGRAM # " + this.myProgramCounter);
            System.out.println("\nDISPLAYING 6502 MICROPROCESSOR MACHINE CODE FOR PROGRAM # " + this.myProgramCounter + "...");
            // Traverse Memory, printing all and a new line every 8 bytes
            for(String curr : this.myMemory) {
                System.out.print(curr + " ");
                if(linecounter == 8) {
                    System.out.println("");
                    linecounter = 0;
                } else {
                    linecounter++;
                }
            }
        } else {
            System.out.println("PROGRAM #" + this.myProgramCounter + " MACHINE CODE GENERATION FAILED DUE TO " + this.myErrorCount + " Error(s)");
            System.out.println("MACHINE CODE WILL NOT BE DISPLAYED...");
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

    // Method to Search Static Table For a Variable's Type
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

    // Method to determine the type of a Node
    public String getNodeType(Node valueNode) {
        // Instance Variables
        String nodeResult = "";
        String nodeInfo = valueNode.getType();

        // Check if ID
        String isID = getTempAddress(nodeInfo, valueNode.getScope());
        if(isID != null) {
            nodeResult = "ID";
        } else if(isDigit(nodeInfo)) {
            // Check if Digit
            nodeResult = "DIGIT";
        } else if(nodeInfo.equalsIgnoreCase("true") || nodeInfo.equalsIgnoreCase("false")) {
            // Check if BoolVal
            nodeResult = "BOOLVAL";
        } else if(nodeInfo.charAt(0) == '\"' && nodeInfo.charAt(nodeInfo.length() - 1) == '\"') {
            // Check if String
            nodeResult = "STRING";
        }

        // Return Resukt
        return nodeResult;
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

    // Output Total Byte Space Used
    public void displayBytesUsed() {
        // TBU = MyCodePointer + StaticSize + heapsize
        int bytesUsed = myCodePointer + this.myStaticTable.size() + (255 - myHeapPointer);

        // Display TBU
        System.out.println("\nTotal Bytes Used - " + bytesUsed + "/256"); 
    }

    // Method For Traversing AST
    public void goDownTree(ArrayList<Node> children, boolean useBoolean, boolean boolContents) {
        // If were using a second condition to determine traversal
        if(useBoolean) {
            if (!children.isEmpty() && !boolContents) {
                for (Node child : children) {
                    generateCode(child);
                }
            }
        } else {
            // If were using one condition to determine traversal
            if (!children.isEmpty()) {
                for (Node child : children) {
                    generateCode(child);
                }
            }
        }
    }

    // Method For Optimizing Declaration and Initialization Subsequent Instructions
    public boolean tryOptimization(String firstTempByte, String secondTempByte) {
        boolean result = true; // default true
        int currPointer = this.myCodePointer - 1;
        // Get Expected for Optimization
        String[] expected = new String[]{"A9", "00", "8D", firstTempByte, secondTempByte};
        for(int i = expected.length - 1; i >= 0; i--) {
            if(!this.myMemory[currPointer--].equalsIgnoreCase(expected[i])) {
                result = false;
            }
        }
        return result;
    }

    // Method For If/While Statements Beginning Comparisons
    public void loadCompareX(ArrayList<Node> children) {
    // Load X Register w First, Compare X with Second, BNE or BEQ, Add JMP
        // Get Valued Children
        ArrayList<Node> ifChildren = children.get(0).getChildren();

        // Get Types of Each Child
        String childOneType = getNodeType(ifChildren.get(0));
        String childTwoType = getNodeType(ifChildren.get(1));

        // Load X Register with Contents of First Child
        switch(childOneType) {
            case "ID":
                // Search Static Table for Temp Address
                String tempAddress = getTempAddress(ifChildren.get(0).getType(), ifChildren.get(0).getScope());

                // Get Bytes
                String firstByte = tempAddress.substring(0, 2);
                String secondByte = tempAddress.substring(2, 4);

                // Load X Register w Memory
                LDXMemory(firstByte, secondByte);

                break;
            case "DIGIT":
                // Load Const Into X Register
                LDXConst(ifChildren.get(0).getType());

                break;
            case "BOOLVAL":
            case "STRING":
                // Write to Heap and Load X Register w Location
                boolean result = writeToHeap(ifChildren.get(0).getType());

                // Upd heapOverFlow
                if(result == true && this.myHeapOverflow == false) {
                    System.out.println("ERROR - HEAP OVERFLOW"); // print error once
                    this.myErrorCount++; // increment error count once
                    this.myHeapOverflow = true;
                }

                // Load X With Contents of First Child
                LDXMemory(String.format("%02X", myHeapPointer), "00"); // Set 00 Flag to Only Use First Byte

                break;
        }

        // Compare Contents of X with Second Child
        switch(childTwoType) {
            case "ID":
                // Search Static Table for Temp Address
                String tempAddress = getTempAddress(ifChildren.get(1).getType(), ifChildren.get(1).getScope());

                // Get Bytes
                String firstByte = tempAddress.substring(0, 2);
                String secondByte = tempAddress.substring(2, 4);

                // Compare X Register w Memory
                CPXMemory(firstByte, secondByte, false);

                break;
            case "DIGIT":
                // Load into A 
                LDAConst("0" + ifChildren.get(1).getType());

                // Store Memory Address
                STAEMemory(this.t1Pointer, "00");

                //Compare X with Constant -> getting previous two bytes as memory address
                CPXMemory(this.myMemory[this.myCodePointer - 1], this.myMemory[this.myCodePointer - 2], false);

                break;
            case "BOOLOP":
            case "STRING":
                // Write to Heap and Compare X With Second Child
                boolean result = writeToHeap(ifChildren.get(1).getType());

                // Upd heapOverFlow
                if(result == true && this.myHeapOverflow == false) {
                    System.out.println("ERROR - HEAP OVERFLOW"); // print error once
                    this.myErrorCount++; // increment error count once
                    this.myHeapOverflow = true;
                }
                // Compare Contents of X With Second Child
                CPXMemory(String.format("%02X", myHeapPointer), "00", false); // Set 00 Flag to Only Use First Byte
                break;
        }
    }
    // Method to check if a string can be an int
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    // Method to handle Expr Overload ie assignments such as a = 1 + 2 + a
    public void handleExprOverload(ArrayList<Node> children, int varScope) {
        // Instance Variables
        Node lastChild = children.get(children.size() - 1);
        String lastChildType = getNodeType(lastChild);
        String lastChildAddress = getTempAddress(lastChild.getType(), varScope);
        Node firstChild = children.get(0);
        String firstChildAddress = getTempAddress(firstChild.getType(), varScope);

        // Determine Last Child and Store in t2
        if(lastChildType.equalsIgnoreCase("ID")) {
            LDAMemory(lastChild, t2Pointer, "00", false);
        } else { // Digit Case
            // Load Accumulator With Value
            LDAConst("0" + lastChild.getType());

            // Store at T2
            STAEMemory(t2Pointer, "00");
        }
        
        // for each child before last child
        for(Node child : children) {
            // Skip Last Child and First Child
            if(child != firstChild && child != lastChild) { 
                if(getNodeType(child).equalsIgnoreCase("ID")) { // ID Case
                    LDAMemory(child, lastChildAddress.substring(0,2), lastChildAddress.substring(2,4), true);
                } else { // Digit Case
                    // Load Accumulator With Value
                    LDAConst("0" + child.getType());
                }
                // Add With Carry T2
                ADCMemory(t2Pointer, "00");

                // Store Accumulator in T2
                STAEMemory(t2Pointer, "00");
            }
        }
        // Store in first child location
        STAEMemory(firstChildAddress.substring(0, 2), firstChildAddress.substring(2, 4));
    }
        

    // Method to Write for BNE for If/While Statements
    public void BNE(String location) {
        if(location.equalsIgnoreCase("")) {
            // Branch on Does Not Equal
            this.myMemory[myCodePointer++] = "D0"; // BNE Op Code
            this.myMemory[myCodePointer++] = "J" + currJump; // Temporary Jump Object

            // Create and Add Temp Jump Object
            this.myJumpTable.add(new JumpObject("J" + currJump++));
        } else {
            this.myMemory[myCodePointer++] = "D0"; // BNE Op Code
            this.myMemory[myCodePointer++] = location; // Jump Destination
        }
        
    }

    // Method to Find and Update Jump Objects
    public void updateJumpAddress(int jumpNumber, int newLocation) {
        // Find Correct Jump Number
        for(JumpObject currObj : this.myJumpTable) {
            if(currObj.getTempAddress().equalsIgnoreCase("J" + jumpNumber)) {
                currObj.setFinalAddress(String.format("%02X", newLocation)); // Update Its New Final Address
            }
        }
    }

    // Method to Write a String to the Heap
    public boolean writeToHeap(String value) {
        boolean heapOverFlow = false; // heap overflow flag

        // Add each Characters Ascii Hex To The Heap
        for(int i = value.length() - 2; i > 0; i--) { // skip first and last cause ""
            if(this.myHeapPointer <= this.myCodePointer) {
                heapOverFlow = true; // update error flag
            } else {
                if(i == value.length() - 2) {// ie first value 
                    this.myMemory[--myHeapPointer] = "00"; // Write 00 next up to seperate Strings
                }
                this.myMemory[--myHeapPointer] = Integer.toHexString(value.charAt(i)).toUpperCase();
            }
            
        }
        return heapOverFlow;
    }

    // Method to Backpatch Code With Final Jump Addresses
    public void backPatchJump() { 
        // For each Jump Object
        for(JumpObject currObj : this.myJumpTable) {
           String jumpName = currObj.getTempAddress(); // Name to Look For

           // Look For All Instances of temp address in Memory and Replace with Final Address
           for(int i = 0; i < this.myCodePointer; i++) {
                // IF we found the temp addres -> update its address
                if(this.myMemory[i].equalsIgnoreCase(jumpName)) {
                    this.myMemory[i] = currObj.getFinalAddress();
                }
            }
        }
    }

    // Method for Back Patching Updated Addresses for Static Table Objects
    public void backPatchStatic() {
        // Check For Stack Overflow -> Enough Space
        if(this.myStackPointer > this.myHeapPointer - this.myStaticTable.size()) {
            System.out.println("ERROR - STACK OVERFLOW");
            this.myErrorCount++; // increment error count
        } else {
            for(TempObject currObj : this.myStaticTable) {
                String tempAddress = currObj.getTempAddress(); // Get Temp Address of Static Object
    
                // Look for matching temp address to replace
                for(int i = 0; i < this.myCodePointer; i++) { // Go Every Two 
                    // Two Byte String to Be Compared
                    String currBytes = this.myMemory[i] + this.myMemory[i+1];
    
                    // If we Found The Temp Address
                    if(currBytes.equalsIgnoreCase(tempAddress)) {
                        // Replace the Temp Address with New Address
                        this.myMemory[i] = String.format("%02X", this.myStackPointer); // Important Byte First
                        this.myMemory[i+1] = "00"; // 00
                    }
                }
                this.myStackPointer++; // get next address
            }
        }
        
    }

    // Method to Fill Empty Bytes with Zeros
    public void fillZeros() {
        // Instance Variable
        int zeroPointer = this.myCodePointer;

        // Go From Code Pointer -> Heap Pointer and Fill Zeros
        while(zeroPointer < this.myHeapPointer) {
            this.myMemory[zeroPointer++] = "00"; // Set Zeros, Increment zeroPointer
        }
    }

    //! End of Machine Code Generator Function and Methods

    
    //! Begin Assembly Methods

    // Method to Load Accumulator with Constant
    public void LDAConst(String value) {
        // Load Accumulator -> Default 00 -> means 00 and false
        this.myMemory[this.myCodePointer++] = "A9"; // LDA Const
        this.myMemory[this.myCodePointer++] = value; // Default of 00
    }

    // Method to Store Accumulator in memory for a new Static var
    public void STANMemory(String newType, String newVarName, int newScope) {
        // STA Temporary Location
        this.myMemory[this.myCodePointer++] = "8D"; // STA

        // If Were Making a New Static Entry
        this.myMemory[this.myCodePointer++] = ('T' + String.valueOf(currTemp)); // Temp Location Number
        this.myMemory[this.myCodePointer++] = "XX"; // Temporary Byte 

        // Add to Static Table
        this.myStaticTable.add(new TempObject(("T" + this.currTemp + "XX"), newType, newVarName, this.currTemp, newScope));

        // Increment Current Temporary Value Counter
        this.currTemp++;
    }

    // Method to Store Bool Flag in temp storage -> for bool comparison fix


    // Method to Store Existing Static Var in Memory
    public void STAEMemory(String firstByte, String secondByte) {
        // STA Temporary Location
        this.myMemory[this.myCodePointer++] = "8D"; // STA

        // Existing Static Entry
        this.myMemory[this.myCodePointer++] = firstByte;
        this.myMemory[this.myCodePointer++] = secondByte;
        
    }
    
    // Method to Add with Carry from Memory
    public void ADCMemory(String firstByte, String secondByte) {
        this.myMemory[this.myCodePointer++] = "6D"; // ADC Op Code
        this.myMemory[this.myCodePointer++] = firstByte;
        this.myMemory[this.myCodePointer++] = secondByte;
    }

    // Method to Load Accumulator with a Memory Address
    public void LDAMemory(Node currChild, String firstTempByte, String secondTempByte, boolean isADC) {
        this.myMemory[this.myCodePointer++] = "AD"; // LDA Memory

        // Find Temporary Memory of the ID
        String childTempAddress = getTempAddress(currChild.getType(), currChild.getScope());

        // Create Bytes From FoundTemp Location
        String firstByte = childTempAddress.substring(0, 2);
        String secondByte = childTempAddress.substring(2, 4);

        // LDA Memory Address
        this.myMemory[this.myCodePointer++] = firstByte;
        this.myMemory[this.myCodePointer++] = secondByte;

        //STA Temporary Location
        if(!isADC) {
            this.myMemory[this.myCodePointer++] = "8D"; // STA 

            // STA Memory Location
            this.myMemory[this.myCodePointer++] = firstTempByte;
            this.myMemory[this.myCodePointer++] = secondTempByte;
        }
    }

    // Method to load the Y register with a const
    public void LDYConst(String firstByte) {
        this.myMemory[this.myCodePointer++] = "A0"; // LDY Op code
        this.myMemory[this.myCodePointer++] = firstByte; // Important Byte
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
            this.myMemory[this.myCodePointer++] = secondByte; // temp change might break 
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

    // Method to Compare X Register to Address in Memory
    public void CPXMemory(String firstByte, String secondByte, boolean isEnd) {
        this.myMemory[this.myCodePointer++] = "EC"; // CPX Op code
        if(isEnd) {
            this.myMemory[this.myCodePointer++] = this.t2Pointer;
            this.myMemory[this.myCodePointer++] = "00";
        } else {
            // Check if were using Temp Memory Address
            if(firstByte.charAt(0) == 'T' && secondByte.charAt(1) == 'X') {
                this.myMemory[this.myCodePointer++] = firstByte;
                this.myMemory[this.myCodePointer++] = secondByte;
            } else {
                // Heap Memory Address
                this.myMemory[this.myCodePointer++] = secondByte;
                this.myMemory[this.myCodePointer++] = "00";
            }
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

}

//! End of Assembly Methods