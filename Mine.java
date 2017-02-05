public class Mine {
    private int xOffset;
    private int yOffset;
    private int z;
    boolean active;

    public Mine(int xOffset, int yOffset, int z) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.z = z;
        this.active = true;
    }

    public void drop() {
        z--;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public void setXOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setYOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean canTrigger() {
        return active && ((z >= 27 && z <= 52) || (z >= 1 && z <= 26)); 
    }

    public String toString() {
        if (z >= 27 && z <= 52) {
            return Character.toString((char)(z - 26 + 64));
        }
        else if (z >= 1 && z <= 26) {
           return Character.toString((char)(z + 96));
        }
        return "*";
    }
}