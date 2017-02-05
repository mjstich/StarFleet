import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Field {

    private List<Mine> mines;

    private Field(List<Mine> mines) {
        this.mines = mines;
    }

    public static Field fromFile(String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            
            final AtomicInteger width = new AtomicInteger(-1);
            final AtomicInteger lineNumber = new AtomicInteger(0);
            final List<Mine> mines = new ArrayList<Mine>();

            stream.forEach(line -> {

                if (width.get() == -1) {
                    width.set(line.length());
                }
                if (line.length() != width.get()) {
                    throw new RuntimeException("Invalid field line " + line);
                }

                for (int i = 0; i < line.length(); i++) {
                    Mine mine = null;
                    int ascii = line.codePointAt(i);

                    if (ascii >= 65 && ascii <= 90) {
                        mine = new Mine(i, lineNumber.get(), 26 + (ascii - 64));
                    }
                    else if (ascii >= 97 && ascii <= 122) {
                        mine = new Mine(i, lineNumber.get(), (ascii - 96));
                    }

                    if (mine != null) {
                        mines.add(mine);
                    }
                }
                lineNumber.incrementAndGet();
            });

            if (lineNumber.get() % 2 == 0 || width.get() % 2 == 0) {
                throw new RuntimeException("Invalid field file.  Height and width should not be even values.");
            }

            int xCenter = width.get() / 2;
            int yCenter = lineNumber.get() / 2;
            mines.forEach(mine -> {
                mine.setXOffset(mine.getXOffset() - xCenter);
                mine.setYOffset(mine.getYOffset() - yCenter);
            });
            return new Field(mines);
        }
    }

    public void move(Direction direction) {
        if (direction != null) {
            mines.forEach(m -> {
                m.setXOffset(m.getXOffset() - direction.xOffset());
                m.setYOffset(m.getYOffset() - direction.yOffset());
            });
        }
    }

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

    public int getMineCount() {
        return mines.size();
    }

    public boolean anyActiveMines() {
        return mines.stream().anyMatch(m -> m.canTrigger());
    }

    public boolean anyRemainingMines() {
        return mines.stream().anyMatch(m -> m.isActive());
    }

    public void drop() {
        mines.forEach(m -> m.drop());
    }

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