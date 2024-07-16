/*
    Temp Jump Object to Assist with If/While Jumps
    Crucial for BackPatching
    Will Hold Temp Location, Distance

*/

package project4;

public class JumpObject {
    // Private Instance Variables
    private String myTempAddress;
    private String myFinalAddress;

    // Constructor
    public JumpObject(String newTempAddress) {
        myTempAddress = newTempAddress;
        myFinalAddress = ""; // temp  distance value 
    }

    // Getter Methods
    public String getTempAddress() {
        return this.myTempAddress;
    }

    public String getFinalAddress() {
        return this.myFinalAddress;
    }

    // Setter Methods
    public void setTempAddress(String newTempAddress) {
        this.myTempAddress = newTempAddress;
    }

    public void setFinalAddress(String newFinalAddress) {
        this.myFinalAddress = newFinalAddress;
    }


}
