// Class ussed to represent a movement of the ship.  
// The direction contains the offset of the mines to the ship
// when the direction is applied to the field.

public enum Direction {
    north(0, -1), south(0, 1), east(1, 0), west(-1, 0);

    private int xOffset;
    private int yOffset;

    // Constructor 
    private Direction(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }
}