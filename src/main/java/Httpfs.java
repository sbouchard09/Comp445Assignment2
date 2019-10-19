import CLIParsing.CLIParameter;
import com.beust.jcommander.JCommander;

import java.io.IOException;
import java.net.ServerSocket;

public class Httpfs {

    private static Integer port = 8080;
    private static String directory = "";
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

        // set port
        if(parameters.getPort() != null) {
            port = parameters.getPort();
        }

        try{
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Listening on port " + port);

            while(true) {
                Server server = new Server(serverSocket.accept(), directory);

                // bonus fulfillment: concurrent connections using threads
                Thread thread = new Thread(server);
                thread.start();
            }
        } catch(IOException e) {

        }
    }
}
