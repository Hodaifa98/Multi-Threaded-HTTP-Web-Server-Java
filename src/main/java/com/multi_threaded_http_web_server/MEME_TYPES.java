package com.multi_threaded_http_web_server;


//Imports.
import java.util.HashMap;

/**
 * This class contains all the supported MEME types 
 * and allows determining the correct type based on a resource's file extension.
 * @author Hodaifa98
 */
public class MEME_TYPES {
    
    /**
     * A HashMap of the supported MEME types of this server.
     * With the keys being extensions, and their values are the corresponding MEME types.
     */
    private static final HashMap<String, String> MIME_TYPES = new HashMap<>();
    static{
        //Images.
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("jpg", "image/jpg");
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("bmp", "image/bmp");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("svg", "image/svg+xml");
        MIME_TYPES.put("ico", "image/image/x-icon");
        //Plain Text and documents.
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("ppt", "application/vnd.ms-powerpoint");
        MIME_TYPES.put("doc", "application/msword");
        MIME_TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        //Markup, style and scripts...
        MIME_TYPES.put("htm", "text/html");
        MIME_TYPES.put("html", "text/html");
        MIME_TYPES.put("xml", "text/xml");
        MIME_TYPES.put("css", "text/css");
        MIME_TYPES.put("js", "text/javascript");
        MIME_TYPES.put("json", "application/json");
        MIME_TYPES.put("csv", "text/csv");
        //Videos.
        MIME_TYPES.put("mp4", "video/mp4");
        MIME_TYPES.put("mpeg", "video/mpeg");
        MIME_TYPES.put("avi", "video/x-msvideo");
        MIME_TYPES.put("3gpp", "video/3gpp");
        MIME_TYPES.put("wmv", "video/x-ms-wmv");
        MIME_TYPES.put("flv", "video/x-flv");
        MIME_TYPES.put("webm", "video/webm");
        //Audios.
        MIME_TYPES.put("mp3", "audio/mp3");
        MIME_TYPES.put("ogg", "audio/ogg");
        MIME_TYPES.put("acc", "audio/acc");
        MIME_TYPES.put("amr", "audio/amr");
        //Archives.
        MIME_TYPES.put("rar", "application/vnd.rar");
        MIME_TYPES.put("zip", "application/zip");
        //SQL.
        MIME_TYPES.put("sql", "application/sql");
    }
    
    /**
     * Parse the name of a resource and determine its corresponding MEME type based on the
     * extracted extension.
     * @param resource A string denoting the full name of a resource including its extension.
     * @return A string representing the MEME type of resource sent as a parameter.
     */
    public static String getMEMETypeFromResource(String resource){
        try {
            String tokens[] = resource.split("\\.");
            //Get the last element, example
            String extension = tokens[tokens.length-1];
            if(resource.equals("/"))
                return MIME_TYPES.get("html");
            return MIME_TYPES.containsKey(extension) ? MIME_TYPES.get(extension) : "UNSOPPORTED";
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return "UNSOPPORTED";
        }
    }
}