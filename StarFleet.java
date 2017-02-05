
import java.io.IOException;
import java.nio.file.*;

// Main class used for program entry, creating field and running script.
public class StarFleet {
    public static void main(String[] args) {

        // Arg count check
        if (args.length != 2) {
            System.err.println("Incorrect number of parameters.  Use:  StartFleet <fieldfile> <scriptfile>");
            return;
        }

        String fieldFilename = args[0];
        String scriptFilename = args[1];

        // Check if the field file exists.
        if (!Files.exists(Paths.get(fieldFilename))) {
            System.err.println("Field file: " + fieldFilename + " does not exist.");
            return;
        }

        // Check if the script file exists
        if (!Files.exists(Paths.get(scriptFilename))) {
            System.err.println("Script file: " + scriptFilename + " does not exist.");
            return;
        }

        // Create the field.  
        Field field = null;
        try {
            field = Field.fromFile(fieldFilename);
        } catch (Exception e) {
            System.out.println("Error reading field file " + fieldFilename + " " + e.getMessage());
        }

        // Create the script.
        Script script = null;
        try {
            script = Script.fromFile(scriptFilename);
        } catch (Exception e) {
            System.out.println("Error reading script file " + scriptFilename + " " + e.getMessage());
        }

        // Run the script.
        script.run(field);
    }
}