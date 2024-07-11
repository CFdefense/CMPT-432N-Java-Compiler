/*
    Temp Jump Object to Assist with If/While Jumps
    Crucial for BackPatching
    Will Hold Temp Location, Distance

*/

package project4;

public class JumpObject {
    // Private Instance Variables
    private String myTempAddress;
    private int myJumpDistance;

    // Constructor
    public JumpObject(String newTempAddress) {
        myTempAddress = newTempAddress;
        myJumpDistance = -1; // temp  distance value 
    }

    // Getter Methods
    public String getTempAddress() {
        return this.myTempAddress;
    }

    public int getJumpDistance() {
        return this.myJumpDistance;
    }

    // Setter Methods
    public void setTempAddress(String newTempAddress) {
        this.myTempAddress = newTempAddress;
    }

    public void setJumpDistance(int newJumpDistance) {
        this.myJumpDistance = newJumpDistance;
    }


}
