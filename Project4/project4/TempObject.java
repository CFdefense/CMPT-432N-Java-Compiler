/*
    Temp Object to Assist with Stack 
    Crucial for BackPatching
    Will Hold Temp Location, Var Name, Address

*/

package project4;

public class TempObject {
    // Private Instance Variables
    private String myTempAddress; // Temporary Address of The Variable
    private String myVarName; // Variable Name 
    private int myOffset; // Offset For Placement
    private int myScope; // scope of variable

    // Constructor
    public TempObject(String newTempAddress, String newVarName, int newOffset, int newScope) {
        this.myTempAddress = newTempAddress;
        this.myVarName = newVarName;
        this.myOffset = newOffset;
        this.myScope = newScope;
    }

    // Getter Methods
    public String getTempAddress() {
        return this.myTempAddress;
    }

    public String getVarName() {
        return this.myVarName;
    }

    public int getOffset() {
        return this.myOffset;
    }

    public int getScope() {
        return this.myScope;
    }

    // Setter Methods
    public void setTempAddress(String newTempAddress) {
        this.myTempAddress = newTempAddress;
    }

    public void setVarName(String newVarName) {
        this.myVarName = newVarName;
    }

    public void setOffset(int newOffset) {
        this.myOffset = newOffset;
    }

    public void setScope(int newScope) {
        this.myScope = newScope;
    }

}
