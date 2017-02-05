
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Script {

    private List<String> commands;

    private Script(List<String> commands) {
        this.commands = commands;
    }

    public static Script fromFile(String filename) throws IOException {
        final List<String> commands = new ArrayList<String>();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(line -> {
                commands.add(line);
            });
            return new Script(commands);
        }
    }

    public void run(Field field) {
        System.out.println("");
        int mineCount = field.getMineCount();
        int shotsFired = 0;
        int moves = 0;
        boolean movesRemaining = false;

        for (int i = 0; i < commands.size(); i++) {
            String line = commands.get(i);
            System.out.println("Step " + (i+1) + "\n");     
                
            if (line.trim().length() == 0) {
                field.drop();
            }
            else {
                System.out.println(field.toString());
                String[] tokens = line.split(" ");
                Direction direction = null;
                FiringPattern firingPattern = null; 

                for (int j = 0; j < tokens.length; j++) {
                    try {
                        firingPattern = FiringPattern.valueOf(tokens[j]);
                    } 
                    catch (IllegalArgumentException e) {
                    }

                    if (firingPattern != null) {
                        field.fire(firingPattern);  
                        shotsFired++;   
                    }                   

                    try {
                        direction = Direction.valueOf(tokens[j]);
                    }
                    catch (IllegalArgumentException e) {
                    }

                    if (direction != null) {
                        field.move(direction);
                        moves++;
                    }                   
                }
                if (firingPattern != null) {
                    System.out.print(firingPattern + " ");
                }
                if (direction != null) {
                    System.out.println(direction);
                }
                System.out.println("");
                field.drop();
                System.out.println(field.toString());
            }

            if (!field.anyActiveMines() && (i+1) < commands.size()) {
                movesRemaining = true;
                break;
            }
        }

        if (field.anyRemainingMines()) {
            System.out.println("fail (0)");
        }
        else {
            int score;
            if (movesRemaining) {
                score = 1;
            }
            else {
                shotsFired = Math.min((shotsFired * 5), (mineCount * 5));
                moves = Math.min((moves * 2), (mineCount * 3));
                score = (mineCount * 10) - shotsFired - moves;
            }
            System.out.printf("pass (%d)\n", score);
        }
    }
}