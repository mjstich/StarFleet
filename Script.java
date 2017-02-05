
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Script {

    private List<String> commands;

    /**
    * Script file contstuctor.
    */
    private Script(List<String> commands) {
        this.commands = commands;
    }

    /*
    * Create the Script object from the script file.
    */
    public static Script fromFile(String filename) throws IOException {
        final List<String> commands = new ArrayList<String>();
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream.forEach(line -> {
                commands.add(line);
            });
            return new Script(commands);
        }
    }

    /*
    * Run the script relative to the field.
    */
    public void run(Field field) {
        System.out.println("");
        int mineCount = field.getMineCount();
        int shotsFired = 0;
        int moves = 0;
        boolean movesRemaining = false;

        // Iterate over the commands in the scipt file.
        for (int i = 0; i < commands.size(); i++) {
            String line = commands.get(i);
            System.out.println("Step " + (i+1) + "\n");     
                
            // Degenerative case - just drop to next level.    
            if (line.trim().length() == 0) {
                field.drop();
            }
            else {
                System.out.println(field.toString());

                // Split the command line into tokens.
                String[] tokens = line.split(" ");
                Direction direction = null;
                FiringPattern firingPattern = null; 

                // Process each of the commands.
                for (int j = 0; j < tokens.length; j++) {
                    try {
                        // Firing Pattern?
                        firingPattern = FiringPattern.valueOf(tokens[j]);
                    } 
                    catch (IllegalArgumentException e) {
                    }

                    // Have firing pattern - apply to field.
                    if (firingPattern != null) {
                        field.fire(firingPattern);  
                        shotsFired++;   
                    }                   

                    try {
                        // Direction?
                        direction = Direction.valueOf(tokens[j]);
                    }
                    catch (IllegalArgumentException e) {
                    }

                    // Have direction - apply to field.
                    if (direction != null) {
                        field.move(direction);
                        moves++;
                    }                   
                }
                // Print out the commands.
                if (firingPattern != null) {
                    System.out.print(firingPattern + " ");
                }
                if (direction != null) {
                    System.out.println(direction);
                }
                System.out.println("");
                // Drop to next level and print updated board.
                field.drop();
                System.out.println(field.toString());
            }

            // Have all the minds been passed or exploded and 
            // there are more commands remaining?  If so, set flag 
            // and break out.
            if (!field.anyActiveMines() && (i+1) < commands.size()) {
                movesRemaining = true;
                break;
            }
        }

        // Any mines still active - if so, this is a failure condition.
        if (field.anyRemainingMines()) {
            System.out.println("fail (0)");
        }
        else {
            // Score the script.
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