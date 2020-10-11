# Multi-Threaded-HTTP-Web-Server-Java
A Multi Threaded HTTP Web Server in Java

# Introduction
This is a non-blocking, multi-threaded HTTP web server written in Java. It uses the native ServerSocket and Socket classes. It serves at the moment static files only.

# Structure
These are the current main classes of the project:
- HTTPServerInformation
    - This class is used as a configuration clas for the server, and contains all HTTP information about the server. Such as: Supported HTTP methods, forbidden directories...
- HTTPServer
    - The main HTTP server class that starts the server and allows for a continuous running and listening to incoming connections by clients.
- ClientHandler
    - Responsible for handling the client's HTTP request and related response.
- HTTPRequest
    - Handles, parses, and extract all the information related to the client's HTTP request.
- HTTPResonse
    - Handles, parses, and write the HTTP response appropriate to the output steam of the client depending on its HTTP request.
- MEME_TYPES
    - This class contains all the supported MEME types and  allows determining the correct type based on a resource's file extension.
- HTTPMethod
    - An enum of supported HTTP methods of the server.
- HTTPServerLogger (Yet to be implemented)
    - This class logs all information regarding the server: Errors, HTTP requests and responses...

# Usage



# Documentation:
https://hodaifa98.github.io/Multi-Threaded-HTTP-Web-Server-Java/
