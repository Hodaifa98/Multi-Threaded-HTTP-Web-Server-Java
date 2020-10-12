package com.multi_threaded_http_web_server;


//Imports.
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains all HTTP information about the server.<br>
 * Such as: Supported HTTP methods, forbidden directories...
 * @author Hodaifa98
 */
public class HTTPServerInformation {
    /**
     * All confirmed HTTP methods per the HTTP specification: <b>rfc2616-sec9</b>.
    */
    public static final String ALL_HTTP_METHODS[] = new String[] {"GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS", "TRACE", "CONNECT"};
    
    /**
     * All recognized and used HTTP versions per the HTTP specification: <b>rfc2145</b>
     */
    public static final String ALL_HTTP_VERSIONS[] = new String[] {"HTTP/0.9", "HTTP/1.0", "HTTP/1.1", "HTTP/2.0"};
    
    /**
     * An ArrayList of forbidden directories by the server. Such as configuration directories, or error_pages.
     */
    private static final ArrayList<String> FORBIDDEN_DIRECTORIES = new ArrayList<>();
    static{
        FORBIDDEN_DIRECTORIES.add("/config/");
        FORBIDDEN_DIRECTORIES.add("/error_pages/");
    }
    
    /**
     * The public directory of resources in the server.
     */
    public static final String PUBLIC_DIRECTORY = "www";
    
    /**
     * A HashMap of available error pages in the server.<br>
     * The keys are HTTP status codes, and the values are the path of the error pages.
     */
    public static final HashMap<Integer, String> ERROR_PAGES = new HashMap<>();
    static{
        ERROR_PAGES.put(404, PUBLIC_DIRECTORY + "/error_pages/404.html");
    }
    
    /**
     * A string denoting the supported HTTP version by the server
     */
    public static final String SUPPORTED_HTTP_VERSION = "HTTP/1.1";
    
    /**
     * A string denoting the server name.
     */
    public static final String SERVER_NAME = "UNKNOWN";
    
    /**
     * A HashMap of the supported HTTP status by the server.<br>
     * The keys are the HTTP status codes, and the values represent the HTTP status texts.
     */
    public static final HashMap<Integer, String> SUPPORTED_HTTP_STATUS = new HashMap<>();
    static{
        SUPPORTED_HTTP_STATUS.put(200, "OK");
        SUPPORTED_HTTP_STATUS.put(204, "No Content");
        SUPPORTED_HTTP_STATUS.put(400, "Bad Request");
        SUPPORTED_HTTP_STATUS.put(403, "Forbidden");
        SUPPORTED_HTTP_STATUS.put(404, "Not Found");
        SUPPORTED_HTTP_STATUS.put(415, "Unsupported Media Type");
        SUPPORTED_HTTP_STATUS.put(500, "Internal Server Error");
        SUPPORTED_HTTP_STATUS.put(501, "Not Implemented");
        SUPPORTED_HTTP_STATUS.put(505, "HTTP Version Not Supported");
    }
    
    /**
     * A string denoting the index page.
     */
    public static final String INDEX_PAGE = "index.html";
    
    //Private empty constructor to prevent this class from being instantiated.
    private HTTPServerInformation(){}
    
    /**
     * A getter of the server's forbidden directories such as configuration directories, or error_pages.
     * @return An ArrayList of the forbidden directories by the server.
     */
    public static ArrayList<String> getFORBIDDEN_DIRECTORIES() {
        return FORBIDDEN_DIRECTORIES;
    }
}
