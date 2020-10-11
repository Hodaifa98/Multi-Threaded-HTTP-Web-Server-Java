package com.multi_threaded_http_web_server;


//Imports.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The HTTPRequest class that handles, parses, and extract all the information related to the client's HTTP request.
 * @author Hodaifa98.
 */
public final class HTTPRequest {
    //Request line: HTTP_METHOD /RESOURCE HTTP_VERSION.
    private final String request_line;
    private HTTP_Method http_method;
    private String requested_resource;
    private String http_version;

    //HTTP headers.
    private final HashMap<String, String> request_headers = new HashMap<>();;
    
    //Request MEME type.
    private final String request_meme_type;
    
    //Request content.
    private String request_content = "";
    
    //GET query string (if available).
    private String GET_query_string = "";
    
    
    //Getters.
    
    /**
     * Get the HTTP request line in the form of:
     * HTTP_METHOD /RESOURCE HTTP_VERSION.
     * @return The HTTP request line as a string.
     */
    public String getRequest_line() {
        return request_line;
    }
    
    /**
     * Get the HTTP request method used by the client to make a request to server.
     * @return An HTTP Method enum value .
     */
    public HTTP_Method getHttp_method() {
        return this.http_method;
    }
    
    /**
     * Get the requested resource (file) from the server, or "/" if the request is the
     * index page.
     * @return The requested resource from the server.
     */
    public String getRequested_resource() {
        return this.requested_resource;
    }
    
    /**
     * @return The HTTP version used by the client in its request.
     */
    public String getHttp_version() {
        return this.http_version;
    }
    
    /**
     * 
     * @return A HashMap of the request headers and their values.
     */
    public HashMap<String, String> getRequest_headers() {
        return this.request_headers;
    }
    
    /**
     * Get the HTTP request MEME type associated with the requested resource.
     * @return The request MEME type.
     */
    public String getRequest_meme_type(){
        return this.request_meme_type;
    }
    
    /**
     * Get the HTTP request content in the form of headers=values.<br>
     * @return The request content header.
     */
    public String getRequest_content() {
        return this.request_content;
    }
    
    /**
     * Get the HTTP request content parameters, extracted in the case of a GET request.
     * @return The request parameters.
     */
    public String getGET_query_string() {
        return this.GET_query_string;
    }
    
    
    /**
     * Main HTTPRequest constructor.
     * @param request_line The HTTP request line.
     * @param http_request The HTTP request headers and body.
     */
    public HTTPRequest(String request_line, ArrayList<String> http_request){
        this.request_line = request_line;
        this.extractHTTPRequestLine(request_line);
        this.extractHTTPRequestHeaders(http_request);
        this.request_meme_type = MEME_TYPES.getMEMETypeFromResource(this.requested_resource);
    }
    /**
     * HTTPRequest constructor in the case of a request that contains any sent content.
     * @param request_line The HTTP request line.
     * @param http_request The HTTP request headers and body.
     * @param content The HTTP request sent data (content).
     */
    public HTTPRequest(String request_line, ArrayList<String> http_request, String content){
        this(request_line, http_request);
        this.request_content = content;
    }
    
    
    /**
     * Parse a request line in the form of:<br>
     * HTTP_METHOD /resource HTTP_VERSION<br>
     * and Extract each individual part of the request line, and 
     * set the current HTTPRequest instance's properties.
     * @param request_line The request line to extract data from.
     */
    private void extractHTTPRequestLine(String request_line) {
        try {
            String lines[] = request_line.split("\\s");
            this.requested_resource = lines[1];
            //If the request line contains GET parameters.
            if(lines[1].contains("?")){
                this.requested_resource = lines[1].split("\\?", 2)[0];
                this.GET_query_string = lines[1].split("\\?", 2)[1];
            }
            this.http_version = lines[2];
            //Determining the HTTP method.
            try{
                this.http_method = HTTP_Method.valueOf(lines[0]);
            }catch(IllegalArgumentException ex){
                //If the HTTP method used to make the request doesn't exist in the
                //supported HTTP methods by the server, an IllegalArgumentException will be thrown
                //and we will set the HTTP method to UNSOPPORTED.
                this.http_method = HTTP_Method.UNSOPPORTED;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Extract the HTTP request headers and add them to this current HTTPRequest instance's request_headers property.
     * @param request_headers An ArrayList of request headers to parse.
     */
    private void extractHTTPRequestHeaders(ArrayList<String> request_headers){
        try {
            //Map through each request header and separate the string by ": ". And then parse the header name and content.
            //Example:
            //Content-Length: 128 would become ["Content-Length", "128"].
            request_headers.stream().map((r) -> r.split(": ", 2)).forEachOrdered((header_and_data) -> {
                //Add the header to the current HTTPRequest instance's request_headers HashMap.
                this.request_headers.put(header_and_data[0], header_and_data[1]);
            });
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Parse and extract key-value pairs of either content or GET query string.<br>
     * Example: a=1&b=2 => {[a, 1], [b, 2]}
     * @param parameters A string denoting either content, or a GET query string.
     * @return A HashMap of parsed data and its values.
     */
    private HashMap<String, String> parseParameters(String parameters){
        //If the parameters string is empty, then return null.
        if(parameters.isBlank())
            return null;
        //Initiate a HashMap to contain the GET/POST parameters and their values.
        HashMap<String, String> parameters_values = new HashMap<>();
        try {
            //Split the parameters by "a".
            //Example: a=1&b=2&c=3 => {["a", "1"], ["b", "2"], ["c", "3"]}
            String content[] = parameters.split("&");
            for(String data : content){
                String key = data.split("=")[0];
                //If there is a value associated with the key, then use it, otherwise add null to signify
                //and empty value for the parameter.
                String value = (data.split("=").length > 1) ? data.split("=")[1] : null;
                parameters_values.put(key, value);
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return parameters_values;
    }
    
    /**
     * Use a parsing method to extract GET parameters and their values from the HTTP request's GET query string. 
     * @return A HashMap of GET parameters and their values.
     */
    public HashMap<String, String> extractGETParameters(){
        return this.parseParameters(this.GET_query_string);
    }
    
    /**
     * Use a parsing method to extract content parameters from the HTTP request's content header.
     * @return A HashMap of posted content parameters and their values.
     */
    public HashMap<String, String> extractPOSTContent(){
        return this.parseParameters(this.request_content);
    }

    /**
     * 
     * @return The string representation of a HTTPRequest instance.
     */
    @Override
    public String toString() {
        String request_string = "";
        request_string += "\nHTTP Request Information: \n";
        request_string += "HTTP Request line: \n";
        request_string += "\tHTTP Method: " + this.http_method + "\n";
        request_string += "\tRequested resource: " + this.requested_resource + "\n";
        request_string += "\tHTTP Version: " + this.http_version + "\n";
        request_string += "HTTP Headers: \n";
        for(Map.Entry<String, String> header : this.request_headers.entrySet())
            request_string += "\t" + header.getKey() + ": " + header.getValue() + "\n";
        request_string += "Request GET parameters:";
        //Check if there are any GET query parameters. If not, return the string "EMPTY".
        if (this.extractGETParameters() == null)
            request_string += "\n\tEMPTY";
        else{
            //Otherwise, return all GET parameters.
            for(Map.Entry<String, String> kv : this.parseParameters(this.GET_query_string).entrySet())
                request_string += "\n\t" + kv.getKey() + ": " + kv.getValue();
        }
        request_string += "\nRequest content:";
        //Check if there is any POST content. If not, return the string "EMPTY".
        if (this.extractPOSTContent() == null)
            request_string += "\n\tEMPTY";
        else{
            //Otherwise, return all POST content key-value pairs.
            for(Map.Entry<String, String> kv : this.parseParameters(this.request_content).entrySet())
                request_string += "\n\t" + kv.getKey() + ": " + kv.getValue();
        }
        //
        return request_string;
    }
}