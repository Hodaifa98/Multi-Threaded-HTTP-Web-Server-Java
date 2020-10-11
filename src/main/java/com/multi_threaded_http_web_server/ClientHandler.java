package com.multi_threaded_http_web_server;


//Imports.
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The ClientHandler class that is responsible for
 * handling the client's HTTP request and response related.
 * @author Hodaifa98.
 */
public class ClientHandler implements Runnable{
    //Client's Socket and its output and input streams.
    final private Socket s;
    final private BufferedReader br;
    final private DataOutputStream dos;
    
    /**
     * Client's HTTPRequest object containing all information about the request to the Server.
     */
    public HTTPRequest client_http_request;
    /**
     * Client's HTTPResponse object containing all information about the response to the client.
     */
    public HTTPResponse client_http_response;
    
    /**
     * The ClientHandler class's main constructor.
     * @param s The client's socket.
     * @param br The BufferedReader assigned to the client socket's input stream.
     * @param dos The DataOutputStream of the client's socket.
     */
    public ClientHandler(Socket s, BufferedReader br, DataOutputStream dos) {
        this.s = s;
        this.br = br;
        this.dos = dos;
    }
    
    /**
     * Overriding the default run method of the Runnable interface.<br>
     * In here, a method will be invoked to handle the incoming HTTP request from a client.
     */
    @Override
    public void run() {
        //Call the main method to handle the HTTP request made by the client.
        this.handleHTTPRequest();
    }
    
    /**
     * Handles the HTTP Request initiated by the client.
     */
    private void handleHTTPRequest(){
        //Using the try-with to automatically flush and close the streams and sockets when the server is done
        //reading the HTTP request, and writing the appropriate response.
        try(this.br; this.dos; this.s) {
            //Call the readHTTPRequest() method that uses BufferedReader to read the request from the input stream.
            this.client_http_request = this.readHTTPRequest();
            //Initiate the HTTP response for the client's request.
            this.client_http_response = new HTTPResponse(this.client_http_request);
            //Write the HTTP response to the client's OutputStream
            this.client_http_response.writeHTTPResponse(dos);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Use the client's BufferedReader to read the HTTP request line by line, and then
     * create an HTTPRequest instance containing the information about the client's request.
     * @return An HTTPRequest instance representing the client's request to the server.
     * @throws IOException 
     */
    private HTTPRequest readHTTPRequest() throws IOException{
        //Initiate an ArrayList to hold the HTTP request headers.
        ArrayList<String> http_request_headers = new ArrayList<>();
        //Read the first line which contains the request line in the format of:
        //HTTP_METHOD /RESOURCE HTTP_VERSION
        String line = br.readLine();
        //Store the request line.
        String request_line = line;
        //Initiate an integer to hold the content line.
        int content_length = -1;
        String post_content = "";
        try{
            //Loop through the client's InputStream using the BufferedReader.
            //Then read the HTTP request line by line.
            while((line = br.readLine()) != null && (line.length() != 0)){
                //If one of the HTTP request headers contains Content-Length, it means there is some content sent in the request body
                //or as form values. If that is the case, then extract that length (in bytes) to use for reading the content.
                if (line.contains("Content-Length:"))
                    content_length = Integer.parseInt(line.split("\\s")[1]);
                //Add each request header to the headers ArrayList.
                http_request_headers.add(line);
            }
            
            //If the HTTP request contains a Content-Length header
            if(content_length != -1){
                //Declare a char array with its size being the content length.
                char [] content_array = new char[content_length];
                //Read as much bytes from the InputStream and store them in the char array.
                br.read(content_array, 0, content_length);
                //Convert the char array to a string.
                post_content = new String(content_array);
            }
        } catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        
        //Return a new HTTPRequest instanciated with the request line and headers.
        if(post_content.isEmpty())
            return new HTTPRequest(request_line, http_request_headers);
        
        //If there is any post content, then invoke the full HTTPRequest's constructor.
        return new HTTPRequest(request_line, http_request_headers, post_content);
    }
}