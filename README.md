# Assignment 2

A simple HTTP server application

To run:
1. Git clone this repository
2. Compile using Maven in IntelliJ (run command `mvn clean install`)
3. In a terminal, navigate to `Comp445Assignment2/target`
4. Run the command `java -jar httpfs [-p] (port_number) [-d] (new_root_directory)`
5. Server is now running
   - Using your favourite HTTP client application:
      - Send a `GET` request for `http://localhost:8080` to retrieve the contents of the working directory
      - Send a `GET` request for `http://localhost:8080/file_name` to retrieve the contents of a file
      - Send a `POST` request for `http://localhost:8080/file_name` with file contents to write to a file with name `file_name` or to create a file with name `file_name` if it doesn't already exist in the working directory
