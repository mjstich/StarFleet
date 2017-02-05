import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

// Class used to represent the StartFleet field contains the mines and the ship.
public class Field {

    private List<Mine> mines;

    // Construct the Field from the collection of mines.
    private Field(List<Mine> mines) {
        this.mines = mines;
    }

    // Create the Field from the field file.
    public static Field fromFile(String filename) throws IOException {
        // Read the field file.
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            
            final AtomicInteger width = new AtomicInteger(-1);
            final AtomicInteger lineNumber = new AtomicInteger(0);
            final List<Mine> mines = new ArrayList<Mine>();

            // Process each line of the field file.
            stream.forEach(line -> {

                if (width.get() == -1) {
                    width.set(line.length());
                }
                if (line.length() != width.get()) {
                    throw new RuntimeException("Invalid field line " + line);
                }

                // Examine each character in the line to determine if
                // the character represents a mine.  Mines are 
                // designated as A-Z or a-z 
                for (int i = 0; i < line.length(); i++) {
                    Mine mine = null;
                    int ascii = line.codePointAt(i);
 
                    // Check if the chacacter is from A - Z.
                    if (ascii >= 65 && ascii <= 90) {
                        mine = new Mine(i, lineNumber.get(), 26 + (ascii - 64));
                    }
                    // Check if the character is from a - z.
                    else if (ascii >= 97 && ascii <= 122) {
                        mine = new Mine(i, lineNumber.get(), (ascii - 96));
                    }

                    // Found mine
                    if (mine != null) {
                        mines.add(mine);
                    }
                }
                lineNumber.incrementAndGet();
            });

            if (lineNumber.get() % 2 == 0 || width.get() % 2 == 0) {
                throw new RuntimeException("Invalid field file.  Height and width should not be even values.");
            }

            // This is an initial condition.  We know have all mines and the width and height of 
            // the field.  Go back through each of the mines and calculate their offset relative
            // to the ship position which is the center of the board.
            int xCenter = width.get() / 2;
            int yCenter = lineNumber.get() / 2;
            mines.forEach(mine -> {
                mine.setXOffset(mine.getXOffset() - xCenter);
                mine.setYOffset(mine.getYOffset() - yCenter);
            });
            return new Field(mines);
        }
    }

    // Move the ship.  This exercise here is to adjust the mine's positition
    // relative to the new location of the ship.
    public void move(Direction direction) {
        if (direction != null) {
            mines.forEach(m -> {
                m.setXOffset(m.getXOffset() - direction.getXOffset());
                m.setYOffset(m.getYOffset() - direction.getYOffset());
            });
        }
    }

    // Execcute the firing pattern.  Iterate over the array
    // of offsets contained in the firing pattern to determine
    // if there is a mine at the specified offset.  If there is,
    // then mark the mine inactive.
    public void fire(FiringPattern firingPattern) {
        if (firingPattern != null) {
            for (int i = 0; i < firingPattern.getFiringPattern().length; i++) {
                int xOffset = firingPattern.getFiringPattern()[i][0];
                int yOffset = firingPattern.getFiringPattern()[i][1];

                mines.forEach(m -> {
                    if (m.canTrigger() && 
                        m.getXOffset() == xOffset && m.getYOffset() == yOffset) {
                        m.setActive(false);
                    }
                });
            }
        }
    }

    // Return the number of mines contained in the field.
    public int getMineCount() {
        return mines.size();
    }

    // Return if any mines can still be triggered.
    public boolean anyActiveMines() {
        return mines.stream().anyMatch(m -> m.canTrigger());
    }

    // Return if any mines are still active.
    public boolean anyRemainingMines() {
        return mines.stream().anyMatch(m -> m.isActive());
    }

    // Drop the ship one level.  
    public void drop() {
        mines.forEach(m -> m.drop());
    }

    // Determine the minimum width of the field based on the active mines.
    private int getWidth() {
        int width = 0;
        for (Mine mine : mines) {
            if (mine.isActive()) {
                width = Math.max(width, Math.abs(mine.getXOffset()));
            }
        }
        if (width > 0) {
            width = (width * 2) + 1;
        } else {
            width = 1;
        }
        return width;
    }

    // Determine the minimum height of the field based on the active mines.
    private int getHeight() {
        int height = 0;
        for (Mine mine : mines) {
            if (mine.isActive()) {
                height = Math.max(height, Math.abs(mine.getYOffset()));
            }
        }
        if (height > 0) {
            height = (height * 2) + 1;
        }
        else {
            height = 1;
        }
        return height;
    }

    // Print the field.
    public String toString() {

        int width = getWidth();
        int height = getHeight();

        int xCenter = width / 2;
        int yCenter = height / 2;

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Mine mine = null;

                for (int k = 0; k < mines.size(); k++) {
                    if (mines.get(k).isActive() &&
                        mines.get(k).getXOffset() == (j - xCenter) && 
                        mines.get(k).getYOffset() == (i - yCenter)) {
                        mine = mines.get(k);
                    }
                }
                if (mine != null) {
                    sb.append(mine.toString());
                }
                else {
                    sb.append(".");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}