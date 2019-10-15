import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
        // else return the requested file
        //          return 200 OK if successful
        //          return 401 Unauthorized if unauthorized to be there
        //          return 405 Not Found if file is not found
        PrintWriter responseWriter = null;
        BufferedOutputStream outputBuffer = null;

        try {
            responseWriter = new PrintWriter(socket.getOutputStream());
            responseWriter.println("HTTP/1.0 403 Forbidden");
            responseWriter.println("Date: " + getDate());
            responseWriter.println();
            responseWriter.flush();
            responseWriter.close();
        } catch(IOException e) {

        }
    }

    private void handlePost(String fileName) {
        // create the file with fileName if the file does not exist
        // over write the file if it does exist in directory
        //          return 201 Created if successful
        //          return 403 Forbidden if file is not in directory
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.CANADA);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(calendar.getTime());
    }
}
