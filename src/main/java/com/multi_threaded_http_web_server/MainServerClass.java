package com.multi_threaded_http_web_server;

/**
 * Main class for the Multi Threaded HTTP Web Server.
 * @author Hodaifa98
 * @version 1.0
 * @since 10/10/2020
 */
public class MainServerClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("STARTING...");
        HTTPServer server = new HTTPServer();
        server.startServer();
    }
    
}
