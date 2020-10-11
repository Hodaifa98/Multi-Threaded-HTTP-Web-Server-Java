package com.multi_threaded_http_web_server;


//Imports.
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The HTTPResponse class that handles, parses, and write the HTTP response appropriate to the output steam of
 * the client depending on its HTTP request.
 * @author Hodaifa98.
 */
public class HTTPResponse {
    
    /**
     * A string denoting the standard Carriage Return (ASCII 13, \r ) Line Feed (ASCII 10, \n ). 
     * Used to end terminate HTTP status line, as well as HTTP headers in the standard HTML protocol.
     * Per the official specification: <b>rfc2616-sec4</b>.
     */
    public static final String CRLF = "\r\n";
    
    /**
     * A string denoting a double CRLF used to terminate the HTTP headers. An empty line separate the headers
     * from any HTTP content body.
     */
    public static final String CONTENT_CRLF = "\r\n\r\n";
    
    //HTTP method and version.
    private final HTTP_Method http_method;
    private String http_version = "";
    
    //Request resource and MEME type.
    private String requested_resourse = "";
    private String meme_type = "";
    
    //A boolean to denote if this is a Bad request.
    private boolean is_bad_request = false;
    
    //HTTP Status code.
    private int http_status_code = 0;
    
    //Response line and headers.
    private String http_response_line = "";
    private String response_headers = "";
    
    //HTTPResponse content length.
    private long content_length = -1;
    
    //HTTPRequest GET Parameters
    private HashMap<String, String> request_GET_query_string_values = null;
    
    //HTTPRequest content values
    private HashMap<String, String> request_content_values = null;
    
    
    //GETTERS.
    
    /**
     * Get the HTTP version of the request.
     * @return A string denoting the HTTP version.
     */
    public String getHttp_version() {
        return this.http_version;
    }

    /**
     * Get the HTTP method of the request.
     * @return An HTTP method enum object.
     */
    public HTTP_Method getHttp_method() {
        return this.http_method;
    }

    /**
     * Get the requested resource (with the full path) by the client from the HTTP server.
     * @return A string denoting the request resource.
     */
    public String getRequested_resourse() {
        return this.requested_resourse;
    }

    /**
     * Get the requested resource's MEME type.
     * @return A string denoting the MEME type.
     */
    public String getMeme_type() {
        return this.meme_type;
    }

    /**
     * Check if the HTTP request is badly formatted, and not conforming to the standard HTTP request line:
     * HTTP_METHOD /RESOURCE HTTP_VERSION.
     * Per the specification: <b>rfc2616-sec5</b>.
     * @return A boolean denoting if the request is badly formatted.
     */
    public boolean isIs_bad_request() {
        return this.is_bad_request;
    }

    /**
     * Get the HTTPResponse status code denoting the result of parsing the request.
     * @return An integer denoting the HTTP status code.
     */
    public int getHttp_status_code() {
        return this.http_status_code;
    }

    /**
     * Get the HTTPResponse line fetched from the HTTPRequest.
     * Used to parse and compose the appropriate HTTP response.
     * @return A string denoting the HTTP response line.
     */
    public String getHttp_response_line() {
        return this.http_response_line;
    }

    /**
     * Get the HTTP response headers in a string, separated by CRLF.
     * @return A string denoting the response headers.
     */
    public String getResponse_headers() {
        return this.response_headers;
    }
    
    /**
     * Get the content length of the HTTP response's content.
     * @return A long denoting the content length of the response.
     */
    public long getContent_length() {
        return this.content_length;
    }

    /**
     * Get a HashMap of extracted GET query string parameters and values,
     * or null if none were provided in the HTTP request.
     * @return A HashMap of GET query string parameters and values.
     */
    public HashMap<String, String> getRequest_GET_query_string_values() {
        return this.request_GET_query_string_values;
    }

    /**
     * Get a HashMap of extracted content parameters and values,
     * or null if none were provided in the HTTP request.
     * @return A HashMap of content parameters and values.
     */
    public HashMap<String, String> getRequest_content_values() {
        return this.request_content_values;
    }
    
    
    /**
     * The main HTTPResponse constructor.
     * Takes an HTTPRequest instance and use its properties (such as method, MEME type, requested resource...)
     * to construct the current HTTPResponse instance.
     * @param request An HTTPRequest instance used to create the current HTTPResponse object.
     */
    public HTTPResponse(HTTPRequest request) {
        this.http_method = request.getHttp_method();
        this.meme_type = request.getRequest_meme_type();
        this.requested_resourse = request.getRequested_resource();
        //If the request resource is the index page, then it will be "/".
        if(this.requested_resourse.equals("/"))
            this.requested_resourse += HTTPServerInformation.INDEX_PAGE;
        this.http_version = request.getHttp_version();
        //Check if the current HTTP request is correctly formatted.
        this.is_bad_request = this.checkIfBadRequest(request.getRequest_line());
        //
        this.request_GET_query_string_values = request.extractGETParameters();
        this.request_content_values = request.extractPOSTContent();
    }
    
    /**
     * Takes an HTTP request line in the format of:
     * HTTP_METHOD /RESOURCE HTTP_METHOD.
     * And parse it, making sure it's valid per the HTTP specification. If not, return true.
     * @param line The HTTP request status line.
     * @return A boolean denoting if the request line passed in the parameters is valid or not.
     */
    private boolean checkIfBadRequest(String line){
        try {
            //Split the request line by spaces.
            String []tokens = line.split("\\s");
            //If the first token (HTTP method) doesn't match any HTTP method that exist, then return true.
            if(!Arrays.stream(HTTPServerInformation.ALL_HTTP_METHODS).anyMatch(tokens[0]::equals))
                return true;
            //Check if the HTTP version used by the request exist.
            if(!Arrays.stream(HTTPServerInformation.ALL_HTTP_VERSIONS).anyMatch(tokens[2]::equals))
                return true;
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return true;
        }
        //No need to check the request resource, since it's valid by default.
        return false;
    }
    
    /**
     * Write an HTTP response to the stream associated with the DataOutputStream instance provided in the parameters.
     * @param dos A DataOutputStream object used to write an HTTP response to the stream.
     * @throws IOException 
     */
    public void writeHTTPResponse(DataOutputStream dos) throws IOException{
        this.http_status_code = this.getHTTPStatusCodes();
        try {
            //Compose the HTTPResponseLine.
            this.http_response_line = this.composeHTTPResponseLine(this.http_status_code);
            //Writing the response line to the client.
            dos.write(http_response_line.getBytes("UTF-8"));
            
            //Writing the response headers.
            this.response_headers += CRLF + "Date: " +  this.getCurrentDatePerHTTP();
            this.response_headers += CRLF + "Connection: Closed";
            this.response_headers += CRLF + "Server: " + HTTPServerInformation.SERVER_NAME;
            
            //If the HTTP method is OPTIONS, then do not write the HTTP headers.
            if(this.http_method == HTTP_Method.OPTIONS && this.getOPTIONSResponseToStream() != null)
                //Add the HTTP request OPTIONS's response to the response headers.
                this.response_headers = this.getOPTIONSResponseToStream() + this.response_headers;
            else{                
                //If the HTTP method used is GET/POST/HEAD, then write the content type and length of the HTTP
                //body response to the stream.
                if(this.http_method == HTTP_Method.GET || this.http_method == HTTP_Method.POST || this.http_method == HTTP_Method.HEAD){
                    //If the HTTP status code is a one of the error pages defined by the server
                    //then it's an error.
                    if(HTTPServerInformation.ERROR_PAGES.containsKey(this.http_status_code))
                        //Calculate the content length of the HTTP status page.
                        this.content_length = this.getHTTPStatusPageContentLength(this.http_status_code);
                    if(this.content_length != -1){
                        //If there is any content length, then write it to the stream, along with the MEME type
                        //of the error page.
                        this.response_headers += CRLF + "Content-Length: ";
                        this.response_headers += this.content_length;
                        this.response_headers += CRLF + "Content-Type: " + this.meme_type;
                    }
                }
            }
            //Writing the full response headers to the stream.
            dos.write(response_headers.getBytes("UTF-8"));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            this.http_status_code = 500;
        }
        
        //If the response code is 200 (OK), then write the requested resource to the stream.
        if(this.http_status_code == 200 && this.http_method != HTTP_Method.HEAD)
           this.writeResourceToStream(dos, HTTPServerInformation.PUBLIC_DIRECTORY + this.requested_resourse);
        //Else there is an HTTP status page is included in the defined error pages of the server, write it.
        else if(HTTPServerInformation.ERROR_PAGES.containsKey(this.http_status_code))
            this.sendHTTPStatusPage(dos, this.http_status_code);
        
    }
    
    /**
     * Get the current date at the time of writing the HTTP response.<br>
     * The format is defined per <b>RFC 7231, section 7.1.1.2: Date</b>.
     * @return A string representing the current date.
     */
    private String getCurrentDatePerHTTP(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss O");
        return formatter.format(ZonedDateTime.now(ZoneOffset.UTC));
    }
    
    /**
     * Write a resource (file in the server) to the output stream provided as a parameter.
     * @param dos A DataOutputStream instance used to write the resource to.
     * @param resource A string denoting the resource to write to the stream provided as a parameter.
     */
    private void writeResourceToStream(DataOutputStream dos, String resource){
        try(FileInputStream fis = new FileInputStream(resource)){
            //Writing a double CRLF to separate the HTTP headers from the content.
            dos.write(CONTENT_CRLF.getBytes("UTF-8"));
            final byte[] buffer;
            buffer = new byte[1024];
            int bytes;
            while((bytes = fis.read(buffer)) != -1)
                dos.write(buffer, 0, bytes);
        } catch(Exception ex){
            this.http_status_code = 500;
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Check if there is an error page corresponding to the HTTP status code provided in the parameter.<br>
     * If yes, return its content length, otherwise, return -1.
     * @param code An integer denoting a HTTP status code.
     * @return A long denoting the content length of the HTTP status page.
     */
    private long getHTTPStatusPageContentLength(int code){
        if(HTTPServerInformation.ERROR_PAGES.containsKey(code)){
            if(new File(HTTPServerInformation.ERROR_PAGES.get(code)).exists()){
                String error_page = HTTPServerInformation.ERROR_PAGES.get(code);
                //Set the HTTPResponse MEME type to the type of the error page.
                this.meme_type = MEME_TYPES.getMEMETypeFromResource(error_page);
                return this.getContentLength(HTTPServerInformation.ERROR_PAGES.get(code));
            }
        }
        return -1;
    }
    
    /**
     * Send a status (error) page to the client's output steam based on the HTTP status code sent 
     * in the parameters.<br>
     * @param dos A DataOutputStream object to use to write to a client's steam.
     * @param code An integer denoting a HTTP status code.
     */
    private void sendHTTPStatusPage(DataOutputStream dos, int code){
        if(HTTPServerInformation.ERROR_PAGES.containsKey(code))
            if(new File(HTTPServerInformation.ERROR_PAGES.get(code)).exists())
                this.writeResourceToStream(dos, HTTPServerInformation.ERROR_PAGES.get(code));
    }
    
    /**
     * Get one of the supported HTTP response codes depending on the request.<br>
     * For example: Return 400 if the request is badly formatted. Or 505 if the HTTP version isn't supported.
     * @return An integer denoting the HTTP response code.
     */
    private int getHTTPStatusCodes(){
        int code;
        String public_resource_path = HTTPServerInformation.PUBLIC_DIRECTORY + this.requested_resourse;
        if(this.is_bad_request)
            code = 400;
        else if(this.http_method == HTTP_Method.UNSOPPORTED)
            code = 501;
        else if(!this.http_version.equals(HTTPServerInformation.SUPPORTED_HTTP_VERSION))
            code = 505;
        else if(this.isRequestedResourceForbidden(this.requested_resourse) && new File(public_resource_path).exists())
            code = 403;
        else if(this.meme_type.equals("UNSOPPORTED") && new File(public_resource_path).exists())
            code = 415;
        else if(!new File(public_resource_path).exists())
            code = 404;
        else if(this.http_method == HTTP_Method.OPTIONS)
            code = 204;
        else{
            this.content_length = this.getContentLength(HTTPServerInformation.PUBLIC_DIRECTORY + this.requested_resourse);
            code = 200;
        }
        return code;
    }
    
    /**
     * Compose the HTTP response line based on the HTTP status code provided in the parameters.<br>
     * The format is: HTTP_VERSION HTTP_STATUS_CODE HTTP_STATUS_TEXT.<br>
     * Per the specification: <b>rfc2616-sec6</b>.
     * @param http_code An integer denoting the HTTP status code.
     * @return A string denoting the HTTP response line.
     */
    private String composeHTTPResponseLine(int http_code){
        String response_line = HTTPServerInformation.SUPPORTED_HTTP_VERSION + " ";
        response_line += Integer.toString(http_code) + " ";
        response_line += HTTPServerInformation.SUPPORTED_HTTP_STATUS.get(http_code);
        return response_line;
    }

    /**
     * Get an OPTIONS response line based on the HTTP request. Specifying which HTTP methods a client is allowed
     * to use in a request.<br>
     * The format is: <br>
     * HTTP_VERSION 204 No Content<br>
     * Allow: SUPPORTED HTTP REQUEST METHODS<br>
     * Other response headers.<br>
     * Example:<br>
     * HTTP/1.1 204 No Content<br>
     * Allow: OPTIONS, GET, HEAD, POST<br>
     * Per the specification: <b>RFC 7231, section 4.3.7: OPTIONS</b><br>
     * Note: CORS is not supported.<br>
     * @return A string denoting the OPTIONS response line.
     */
    private String getOPTIONSResponseToStream() {
        try {
            String supported_http_values = CRLF + "Allow:";
            for(HTTP_Method method : HTTP_Method.values())
                if(method != HTTP_Method.UNSOPPORTED)
                    supported_http_values += " " + method + ",";
            //Remove last "," in the supported http_values string.
            supported_http_values = supported_http_values.substring(0, supported_http_values.length()-1);
            return supported_http_values;
        } catch (Exception ex) {
            this.http_status_code = 500;
            System.err.println(ex.getMessage());
        }
        return null;
    }
    
    /**
     * Check if the requested resource is forbidden by the server by iterating through the
     * forbidden directories and checking if the resource path is withing one of those directories.
     * @param resource A string of the resource path.
     * @return A boolean denoting if the resource is forbidden to request or not.
     */
    private boolean isRequestedResourceForbidden(String resource){
        return HTTPServerInformation.getFORBIDDEN_DIRECTORIES().stream().anyMatch(directory -> resource.contains(directory));
    }
    
    /**
     * Get the content length in bytes of a resource provided as a parameter.<br>
     * If the resource doesn't exist, or if there is any error, return -1.
     * @param resource A string denoting a resource.
     * @return A long denoting the length of the requested resource in bytes.
     */
    private long getContentLength(String resource){
        if(new File(resource).exists())
            return new File(resource).length();
        return -1;
    }
    
}