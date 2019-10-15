import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private Socket socket;
    private String directory;

    public Server(Socket socket, String directory) {
        this.socket = socket;
        if(directory.equals("")) {

        } else {
            this.directory = directory;
        }
    }

    @Override
    public void run() {

        BufferedReader reader = null;
        PrintWriter output = null;
        BufferedOutputStream outputBuffer = null;

        try {
            String line = "";
            String[] info;
            String requestMethod; // GET | POST
            String file;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            line = reader.readLine();
            info = line.split(" ");

            requestMethod = info[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
            file = info[1];

            if(requestMethod.equals("GET")) {
                handleGet(file);
            } else if(requestMethod.equals("POST")) {
                handlePost(file);
            } else {
                System.out.println("Invalid request method");
                return;
            }
        } catch(IOException e) {

        }
    }

    private void handleGet(String fileName) {
        // if fileName.equals("/") --> return all files in directory
        //          return 200 OK if successful
        //          return 401 Unauthorized if unauthorized to be there
        // else return the requested file
        //          return 200 OK if successful
        //          return 401 Unauthorized if unauthorized to be there
        //          return 405 Not Found if file is not found
    }

    private void handlePost(String fileName) {
        // create the file with fileName if the file does not exist
        // over write the file if it does exist in directory
        //          return 201 Created if successful
        //          return 401 Unauthorized if not authorized to be there
    }
}
