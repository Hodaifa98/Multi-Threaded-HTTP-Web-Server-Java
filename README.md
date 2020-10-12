# Multi-Threaded-HTTP-Web-Server-Java
A Multi Threaded HTTP Web Server in Java.

# Introduction
This is a non-blocking, multi-threaded HTTP web server written in Java. It uses the native ServerSocket and Socket classes. 
It allows you to define your public directory, as well as unauthorized directories (like configurations).
In the case of HTTP request to resources with large data, the server uses a buffer which allows for more efficient responses and writing to clients' output stream.

It serves at the moment static files only.


# Documentation:
https://hodaifa98.github.io/Multi-Threaded-HTTP-Web-Server-Java/


# Structure
These are the current main classes of the project:

## /src/main

This is the main project package that contains the classes directory. All packages and classes used in this application are located in here.

| Class name | Description |
| ----------- | ----------- |
| **HTTPServerInformation** | This class is used as a configuration class for the server, and contains all HTTP information about the server. Such as: Supported HTTP methods, forbidden directories... |
| **HTTPServer** | The main HTTP server class that starts the server and allows for a continuous running and listening to incoming connections by clients. |
| **ClientHandler** | Responsible for handling the client's HTTP request and related response. |
| **HTTPRequest** | Handles, parses, and extract all the information related to the client's HTTP request. |
| **HTTPResponse** | Handles, parses, and write the HTTP response appropriate to the output steam of the client depending on its HTTP request. |
| **MEME_TYPES** | This class contains all the supported MEME types and  allows determining the correct type based on a resource's file extension. |
| **HTTPMethod** | An enum of supported HTTP methods of the server. |
| **HTTPServerLogger** (Yet to be implemented) | This class logs all information regarding the server: Errors, HTTP requests and responses... |



## /docs
This is the Javadocs directory. It contains full documentation of the project API in HTML format, extracted from the Java source code.

## /www
The default directory for all resources related to the HTTP server. This includes public resources (web-pages, documents, stylesheets, scripts, media files...), config files, and even error pages (Can be changed to a different directory in HTTPServerInformation).

By default, all files in this folder are accessible by a client making an HTTP request, unless the server owner add certain subdirectories to the server's forbidden directories in the HTTPServerInformation class.

## /www/error_pages
This is the default directory for error_pages for various HTTP responses status. An 404 html page is provided by default in this directory, and it's by default unauthorized to access the resources withing by an HTTP request. The content can only be sent by the server in case of a status code besides 200.

## /www/test_files
This an extra directory contains various test files to use for testing HTTP requests. Such as text/doc/mp3/pdf/... files you can make an HTTP request to.


## /pom.xml
Project Object Model. This is the main XML file used by Maven to contain information about the project and configuration details, moreover, it is used to build the project.

It contains information such as: Project version, description, developers...


# Requirements
- JDK 7 or higher.
- Maven.


# Usage
- Optional: Configure the HTTPServerInformation class. And add custom error pages, forbidden directories, and other information...
- Create a main class (MainServerClass.java is the default main class) that contains an entry static main method.
- Crate an instance of HTTPServer class.
- Invoke the HTTPServer's instance "startServer" method (Can take a custom port as a parameter, or use the default port defined in HTTPServer).
    ```
    HTTPServer server = new HTTPServer();
    server.startServer();
    ```

- Access localhost:port_number for the index page, or specify the name of a request resource (or full path).
    ```
    localhost:8080
    localhost:8080/profile.html
    localhost:8080/test_files/video_test.mp4
    ```


# Technical details
## Supported HTTP version
- HTTP/1.1

## Supported HTTP methods
- GET
- POST
- HEAD
- OPTIONS

## Supported HTTP response status
| Code | Description |
| ----------- | ----------- |
| 200 | OK |
| 204 | No Content |
| 400 | Bad Request |
| 403 | Forbidden |
| 404 | Not Found |
| 415 | Unsupported Media Type |
| 500 | Internal Server Error |
| 501 | Not Implemented |
| 505 | HTTP Version Not Supported |

<br>

## Supported MIME types
| File extension | MIME type |
| ----------- | ----------- |
| jpeg|image/jpeg
| jpg|image/jpg
| png|image/png
| bmp|image/bmp
| gif|image/gif
| svg|image/svg+xml
| ico|image/image/x-icon
| txt|text/plain
| pdf|application/pdf
| ppt|application/vnd.ms-powerpoint
| doc|application/msword
| docx|application/vnd.openxmlformats-officedocument.wordprocessingml.document
| htm|text/html
| html|text/html
| xml|text/xml
| css|text/css
| js|text/javascript
| json|application/json
| csv|text/csv
| mp4|video/mp4
| mpeg|video/mpeg
| avi|video/x-msvideo
| 3gpp|video/3gpp
| wmv|video/x-ms-wmv
| flv|video/x-flv
| webm|video/webm
| mp3|audio/mp3
| ogg|audio/ogg
| acc|audio/acc
| amr|audio/amr
| rar|application/vnd.rar
| zip|application/zip
| sql|application/sql

<br>

## To-do
- Implement a logger class.
- Log the number of requests for any resource.
- Add a proper config file instead of relying on ServerInformationClass for dynamic configuration.
- Implement the rest of HTTP methods.
- Implement more MIME types.
- Add "if-modified-since" response header.

# References
This HTTP server is written per the standard HTTP specification initiated by the World-Wide Web global information initiative.

This project adheres to the protocol referred to as "HTTP1/.1".

Link to the specification: [RFC 2616](https://tools.ietf.org/html/rfc2616).
