package com.multi_threaded_http_web_server;


//Imports.
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * The main HTTP server class that starts the server and allows for a continuous running and listening to incoming
 * connections by clients.
 * @author Hodaifa98
 */
public class HTTPServer {
    
    /**
     * The default port to use for the server in the case of not providing a custom port.
     */
    public static final int DEFAULT_PORT = 8080;
    
    //The HTTPServer's port  number.
    private final int port_number;

    /**
     * 
     * @return The port number associated with the current instance of the HTTPServer.
     */
    public int getPort_number() {
        return port_number;
    }
    
    /**
     * Default constructor for the HTTPServer class when a custom port isn't provided.
     * The default port number is used instead.
     */
    public HTTPServer(){
        this.port_number = DEFAULT_PORT;
    }
    
    /**
     * HTTPServer class's constructor with a port number provided as a parameter.
     * @param port_number A custom port number to use for the current instance of HTTPServer.
     */
    public HTTPServer(int port_number) {
        this.port_number = port_number;
    }
    
    /**
     * Main method to start the HTTP server.
     */
    public void startServer(){
        try {
            //Initialising a counter for clients who make a request to the server.
            int client_number = 1;
            
            //Initialising the server socket to listen for incoming HTTP requests.
            final ServerSocket server = new ServerSocket(this.port_number);
            
            //The server socket will continiously listen to incoming requests.
            System.out.println("Listening on port: " + this.port_number);
            while(true){
                //Accepting a socket connecting.
                Socket s = server.accept();
                //Getting the reading and writing streams for the client's socket.
                BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream writer = new DataOutputStream(s.getOutputStream());
                System.out.println("\nServing client: " + client_number);
                //this.printClientInformation(s);
                //Create a new thread object for this client so that the server can serve multiple requests.
                Thread t = new Thread(new ClientHandler(s, reader, writer));
                //Start the client's socket HTTP exchange with the server in a separate thread.
                t.start();
                //Increment the count of clients who connected to our HTTP server.
                client_number++;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /**
     * Print to the console output a client information based on its associated connecting socket.
     * @param client A socket object associated with a client that connected to the HTTPServer.
     */
    private void printClientInformation(Socket client) throws SocketException{
        System.out.println("Client information: ");
        System.out.println("\tInetAddress: " + client.getInetAddress());
        System.out.println("\tLocalAddress: " + client.getLocalAddress());
        System.out.println("\tLocalPort: " + client.getLocalPort());
        System.out.println("\tLocalSocketAddress: " + client.getLocalSocketAddress());
        System.out.println("\tPort: " + client.getPort());
    }
}
