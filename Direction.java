public enum Direction {
    north(0, -1), south(0, 1), east(1, 0), west(-1, 0);

    private int xOffset;
    private int yOffset;

    // Constructor 
    private Direction(int xOffset, int yOffset) {
    	this.xOffset = xOffset;
    	this.yOffset = yOffset;
    }

    public int xOffset() {
        return xOffset;
    }

    public int yOffset() {
    	return yOffset;
    }
}