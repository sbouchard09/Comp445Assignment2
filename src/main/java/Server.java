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
            String input = " ";
            while ((line = reader.readLine()) != null) {
                input += line;
            }
            String body = input.split("\r\n\r\n")[1];
            requestMethod = info[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
            file = info[1];

            if(file.length() > 1) {
                file = file.startsWith("/") ? file.substring(1) : file;
            }

            if(requestMethod.equals("GET")) {
                handleGet(file);
            } else if(requestMethod.equals("POST")) {
                handlePost(file, body);
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

            if(fileName.equals("/")) { // return all files
                File directory = new File("/");
                File[] list = directory.listFiles();
                StringBuilder directoryBuilder = new StringBuilder();
                directoryBuilder.append("{ \n");

                for(int i = 0; i < list.length; i++) {
                    if(list[i].isFile()) {
                        directoryBuilder.append("\tFile: ");
                    } else if (list[i].isDirectory()) {
                        directoryBuilder.append("\tDirectory: ");
                    } else {
                        continue;
                    }
                    directoryBuilder.append(list[i].getName() + "\n");
                }
                directoryBuilder.append("}");

                responseWriter.println("HTTP/1.0 200 OK");
                responseWriter.println("Date: " + getDate());
                responseWriter.println("Server: localhost");
                responseWriter.println("Content-Length: " + directoryBuilder.toString().length());
                responseWriter.println();
                responseWriter.println(directoryBuilder.toString()); // list of files
                responseWriter.flush();
                responseWriter.close();
            } else { // get file
                File file = new File(fileName);

                if(file.isDirectory()) { // 403
                    forbidden(responseWriter);
                } else if(!file.exists()) { // 405
                    responseWriter.println("HTTP/1.0 405 Not Found");
                    responseWriter.println("Date: " + getDate());
                    responseWriter.println(fileName);
                    responseWriter.println();
                    responseWriter.flush();
                    responseWriter.close();
                } else if(file.isFile()) { // 200 OK
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    StringBuilder sb = new StringBuilder();
                    String line = reader.readLine();

                    while(line != null) {
                        sb.append(line);
                        sb.append("\n");
                        line = reader.readLine();
                    }

                    responseWriter.println("HTTP/1.0 200 OK");
                    responseWriter.println("Date: " + getDate());
                    responseWriter.println("Server: localhost");
                    //responseWriter.println("Content-Type: " + getContentType(fileName)); // not implemented
                    responseWriter.println("Content-Length: " + file.length());
                    responseWriter.println();
                    responseWriter.println(sb.toString()); // body/contents of the file
                    responseWriter.flush();
                    responseWriter.close();
                }
            }
        } catch(IOException e) {

        }
    }

    private void handlePost(String fileName, String body) {
        // overload the method with body parameter to handle input to file
        // create the file with fileName if the file does not exist
        // over write the file if it does exist in directory
        //          return 201 Created if successful
        //          return 403 Forbidden if file is not in directory (forbidden method below)
        PrintWriter responseWriter = null;
        PrintWriter fileWriter = null;

        try {

            if(isForbidden(fileName)) {
                responseWriter = new PrintWriter(socket.getOutputStream());

                forbidden(responseWriter);

            } else {
                responseWriter = new PrintWriter(socket.getOutputStream());

                fileWriter = new PrintWriter(new FileOutputStream(fileName, false));

                fileWriter.println(body);
                fileWriter.flush();
                fileWriter.close();

                responseWriter.println("HTTP/1.0 200 OK");
                responseWriter.println("Date: " + getDate());
                responseWriter.println("Server: localhost");
                //responseWriter.println("Content-Type: " + getContentType(fileName)); // not implemented
                responseWriter.println("Content-Length: " + fileName.length());
                responseWriter.println();
                responseWriter.flush();
                responseWriter.close();
            }


        } catch(IOException e) {

        }

    }

    private boolean isForbidden(String fileName){
        File dir = new File(directory);
        File file = new File(fileName);
        return !(dir.getAbsolutePath().toString().contains(file.getAbsolutePath().toString()));
    }

    private void forbidden(PrintWriter responseWriter) throws IOException {
        responseWriter.println("HTTP/1.0 403 Forbidden");
        responseWriter.println("Date: " + getDate());
        responseWriter.println();
        responseWriter.flush();
        responseWriter.close();
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.CANADA);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(calendar.getTime());
    }
}
