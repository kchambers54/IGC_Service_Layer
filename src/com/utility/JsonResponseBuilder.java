// Inspired by "https://www.baeldung.com/java-http-request"

package com.utility;

import com.dataObjects.requests.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;

/**
 * A utility class for getting / building a response from an established
 * HttpURLConnection object.
 */
public class JsonResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(JsonResponseBuilder.class);


    /**
     * TO BE USED WHEN POSTING / CREATING NEW RESOURCES.
     *      Returns a Response object containing
     *      is ready to make a request.
     *      This method will return the response message and the location of the new object.
     *      -- To receive only the message, use "getMessage(HttpURLConnection connection)" --
     *
     * @param connection: HttpURLConnection object to receive a response for.
     * @return Returns a Response object containing the response message and new resource location.
     * @throws IOException:
     */
    public static Response getResponseObject(HttpURLConnection connection) throws IOException, IllegalArgumentException{
        StringBuilder responseBuilder = new StringBuilder();

        try {
            // Get Location //
            connection.getHeaderFields()
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() != null)
                    .forEach(entry -> {

                        if (entry.getKey().equals("Location")) {
                            responseBuilder.append(entry.getKey())
                                    .append(": ");

                            List<String> headerValues = entry.getValue();
                            Iterator<String> it = headerValues.iterator();
                            if (it.hasNext()) { // Should always be the case
                                responseBuilder.append(it.next());
                            } else {
                                responseBuilder.append("NO LOCATION FOUND");
                            }
                            responseBuilder.append("\n");
                        }
                    });

            // GET ID from Location URL //
            String[] segments = responseBuilder.toString().split("/");  //Get last part of uri.
            String id = segments[segments.length - 1];

            // Get Message //
            Reader streamReader = null;

            // Get error stream if response code 300 or higher.
            if (connection.getResponseCode() > 299) {
                logger.warn("Attempting to build response with code: " + connection.getResponseCode() +
                        " - " + connection.getResponseMessage());
                streamReader = new InputStreamReader(connection.getErrorStream());
            } else {
                streamReader = new InputStreamReader(connection.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();

            return new Response(content.toString(), id, connection.getResponseCode(), connection.getResponseMessage());
        } catch (IOException e) {
            logger.error("IOException in getResponseObject(), caused when accessing httpUrlConnection " +
                    "or BufferedReader objects");
            throw e;
        } catch (Exception e) {
            // Catches an Exceptions (other than IOExceptions) that could result.
            // This will likely be triggered if a bad call is made to the API.
            logger.error("Could not build response from httpUrlConnection object in getResponseObject().");
            throw new IllegalArgumentException("Could not build response from httpUrlConnection object.");
        }
    }


//    /**
//     * Returns a FULL response String for an HttpURLConnection object that is ready to make a request.
//     *      This method will return a String including ALL parts of the response, including any and all
//     *      headers and additional information in addition to the response message.
//     *      To receive only the message, use "getMessage(HttpURLConnection connection)".
//     *
//     * @param connection: HttpURLConnection object to receive a response for.
//     * @return Returns a full response (String)
//     * @throws IOException:
//     */
//    public static String getFullResponse(HttpURLConnection connection) throws IOException {
//        StringBuilder responseBuilder = new StringBuilder();
//
//        responseBuilder.append(connection.getResponseCode())
//                .append(" ")
//                .append(connection.getResponseMessage())
//                .append("\n");
//
//        connection.getHeaderFields()
//                .entrySet()
//                .stream()
//                .filter(entry -> entry.getKey() != null)
//                .forEach(entry -> {
//
//                    responseBuilder.append(entry.getKey())
//                            .append(": ");
//
//                    List<String> headerValues = entry.getValue();
//                    Iterator<String> it = headerValues.iterator();
//                    if (it.hasNext()) {
//                        responseBuilder.append(it.next());
//
//                        while (it.hasNext()) {
//                            responseBuilder.append(", ")
//                                    .append(it.next());
//                        }
//                    }
//                    responseBuilder.append("\n");
//                });
//
//        Reader streamReader = null;
//
//        if (connection.getResponseCode() > 299) {
//            streamReader = new InputStreamReader(connection.getErrorStream());
//        } else {
//            streamReader = new InputStreamReader(connection.getInputStream());
//        }
//
//        BufferedReader in = new BufferedReader(streamReader);
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//
//        in.close();
//
//        responseBuilder.append("Response: ")
//                .append(content);
//
//        return responseBuilder.toString();
//    }
//
//    /**
//     * Returns only the mesponse message for a given HttpURLConnection object
//     *      (with all request parameters and headers set)
//     *      To receive the FULL reponse, use "getMessage(HttpURLConnection connection)", which will include
//     *      all headers and other additional information returned in the response.
//     *
//     * @param connection: An HttpURLConnection object ready to receive a response
//     * @return A String of the received response.
//     * @throws IOException:
//     */
//    public static String getResponse(HttpURLConnection connection, boolean prettyPrint) throws IOException {
//        StringBuilder responseBuilder = new StringBuilder();
//        Reader streamReader = null;
//
//        // Get error stream if response code 300 or higher
//        if (connection.getResponseCode() > 299) {
//            streamReader = new InputStreamReader(connection.getErrorStream());
//        } else {
//            streamReader = new InputStreamReader(connection.getInputStream());
//        }
//
//        BufferedReader in = new BufferedReader(streamReader);
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//
//        in.close();
//
//        responseBuilder.append(content);
//
//        if (prettyPrint) {
//            JsonParser parser = new JsonParser();
//            JsonObject jsonString = parser.parse(responseBuilder.toString()).getAsJsonObject();
//
//            Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printer
//            return gsonPrinter.toJson(jsonString);
//        } else {
//            return responseBuilder.toString();
//        }
//
//    }

}
