import CLIParsing.CLIParameter;
import com.beust.jcommander.JCommander;

public class Httpfs {

    private static String port = "8080";
    private static String directory;
    private boolean debuggingMessages = false;

    public static void main(String[] args) {

        CLIParameter parameters = new CLIParameter();
        JCommander parser = JCommander.newBuilder().addObject(parameters).build();
        parser.parse(args);

        if(parameters.getHelp()) { // help
            // print out help message
        } else {
            //
        }

    }
}
