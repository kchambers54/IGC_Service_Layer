package com.common;

import com.Utility.JsonToObject;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.dataObjects.Category;
import com.dataObjects.Term;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;


public class LambdaHandler implements RequestStreamHandler, RequestHandler<Object, Object> {

    /**
     * SLF4J logger.
     */
    private static Logger logger;

    /**
     * Catches calls made without InputStream, OutputStream method.  Not used, could possibly remove.
     * @param input - X
     * @param context - X
     * @return null
     */
    @Override
    public Object handleRequest(Object input, Context context) {
        return null;
    }

    /**
     * Initial handler for incoming requests to AWS Lambda Function.
     * @param inputStream Incoming JSON request received by AWS Lambda.
     * @param outputStream Outgoing response JSON to be sent from AWS Lambda.
     * @param context Object containing AWS Lambda statuses.
     * @throws IOException:
     */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException{
        // Initialize slf4j logger.
        logger = LoggerFactory.getLogger(Main.class);
        logger.info("Logger started!");

        JsonParser jsonParser = new JsonParser();
        try {
            // Read InputStream JSON
            JsonObject inputJson = (JsonObject) jsonParser.parse(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)
            );
            logger.info("Received input: " + inputJson.toString());

            //Call performRequest() and perform requested function.
            JsonObject outputJson = performRequest(inputJson);

            //Write OutputStream
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(outputJson.toString());
            outputStreamWriter.close();
        } catch (ParseException p) {
            logger.error("ParseException caught in handleRequest(): " + p);
        } catch(IOException i) {
            logger.error("IOException caught in handleRequest(): " + i);
            throw i;
        } catch (Exception e) {
            logger.error("Lambda request handler error: " + e);
            throw e;
        }
        // DONE //
    }

    /**
     * Determines and calls the action requested by the incoming JSON.
     * @param inputJson Incoming JSON. Determines the function to be performed, and
     *                   contains the arguments used by the function.
     * @return JsonObject to be returned to the caller.
     */
    private static JsonObject performRequest(JsonObject inputJson) throws IOException, ParseException {
        // Get variables and establish URLConnection.
        String url = System.getenv("URL");
        boolean disableSSL = System.getenv("disableSSL").equals("true");
        String password = inputJson.get("password").getAsString();
        String username = inputJson.get("username").getAsString();

        // Establish URLConnection
        URLConnection connection = new URLConnection(url, disableSSL, username, password);

        // Extract input resource and function from JSON.
        String requestType = inputJson.get("function").getAsString();
        JsonObject inputResource = inputJson.getAsJsonObject("resource");
        JsonObject inputSearch = inputJson.getAsJsonObject("search");
        logger.info("Resource: " + inputResource);
        logger.debug("Search: " + inputSearch);

        JsonParser jsonParser = new JsonParser();
        JsonObject outputJson;

        // GET Methods
        switch (requestType) {
            case "getIGCResourceById": {
                String resourceId = inputResource.get("_id").getAsString();
                outputJson = connection.getIGCResourceById(resourceId).toJsonObject();
                break;
            }
            case "getResourceParentId": {
                String resourceId = inputResource.get("_id").getAsString();
                String parent_id = connection.getResourceParentId(resourceId);
                outputJson = jsonParser.parse(parent_id).getAsJsonObject();
                break;
            }
            case "getIGCCategoryList": {
                outputJson = connection.getIGCCategoryList(inputJson.get("pageSize").getAsInt()).toJsonObject();
                break;
            }
            case "getIGCTermList": {
                outputJson = connection.getIGCTermList(inputJson.get("pageSize").getAsInt()).toJsonObject();
                break;
            }
            // PUT Methods
            case "updateIGCResource": { // TODO: Having 500 - server errors with custom attributes.
                outputJson = callUpdateIGCResource(inputJson, connection, inputResource);
                break;
            }
            // ADD UPDATE TERM AND CATEGORY NAMES...
            // POST Methods
            case "createIGCResource": {
                outputJson = callCreateIGCResource(connection, inputResource);
                break;
            }
            // POST SEARCHES
            case "searchIGCResource": {
                String searchTerm = inputSearch.get("term").getAsString();
                logger.info("Searching for: " + searchTerm);
                outputJson = connection.searchIGCResource(searchTerm).toJsonObject();
                break;
            }
            case "searchIGCResourceModifiedBetween": {
                String startDate = inputSearch.get("startDate").getAsString();
                String endDate = inputSearch.get("endDate").getAsString();
                outputJson = connection.searchIGCResourceModifiedBetween(startDate, endDate).toJsonObject();
                break;
            }
            case "searchIGCResourceCreatedBetween": {
                String startDate = inputSearch.get("startDate").getAsString();
                String endDate = inputSearch.get("endDate").getAsString();
                outputJson = connection.searchIGCResourceCreatedBetween(startDate, endDate).toJsonObject();
                break;
            }
            case "searchIGCResourceByUser": {
                String userName = inputSearch.get("userName").getAsString();
                outputJson = connection.searchIGCResourceByUser(userName).toJsonObject();
                break;
            }
            case "searchIGCResourceCustom": {
                String searchTerm = inputSearch.get("term").getAsString();
                String searchProp = inputSearch.get("prop").getAsString();
                outputJson = connection.searchIGCResourceCustom(searchProp, searchTerm).toJsonObject();
                break;
            }
            case "searchIGCResourceNullProp": {
                String searchProp = inputSearch.get("prop").getAsString();
                outputJson = connection.searchIGCResourceNullProp(searchProp).toJsonObject();
                break;
            }
            // DELETE Methods
            case "deleteIGCResource": {
                String resourceId = inputResource.get("_id").getAsString();
                outputJson = connection.deleteIGCResource(resourceId).toJsonObject();
                break;
            }
            default: {
                outputJson = new JsonObject();
                outputJson.addProperty("Error", "Could not recognize function" + requestType);
                logger.warn("Could not recognize function: " + requestType);
            }
        }

        return outputJson;
    }

    /**
     * Called by performRequest() when updating a resource in IGC.  Abstracted to improve readability.
     * @param inputJson JsonObject received by AWS Lambda.
     * @param connection URLConnection object to interact with IGC API.
     * @param inputResource JsonObject representing inputJson.get("resource").
     * @return JsonObject response that will be sent back to caller of the AWS Lambda function.
     * @throws IOException:
     */
    private static JsonObject callUpdateIGCResource(JsonObject inputJson, URLConnection connection, JsonObject inputResource) throws IOException {
        String type;
        JsonObject outputJson;
        type = inputResource.get("_type").getAsString();

        String idToUpdate = inputResource.get("_id").getAsString(); // Id to update in inputResource.get(_id)
        JsonObject update = inputJson.get("update").getAsJsonObject(); // New values in inputJson.get(update)

        if (type.equals("term")) {
            Term updateTerm = JsonToObject.toTerm(update.toString());
            logger.info("Term created for update: " + updateTerm);
            outputJson = connection.updateIGCResource(idToUpdate, updateTerm).toJsonObject();
        } else if (type.equals("category")) {
            Category updateCategory = JsonToObject.toCategory(update.toString());
            logger.info("Category created for update: " + updateCategory);
            outputJson = connection.updateIGCResource(idToUpdate, updateCategory).toJsonObject();
        } else {
            logger.error("Attempted to update resource with unknown type: " + type);
            throw new IllegalArgumentException("Attempted to update resource with unknown type: " + type);
        }
        return outputJson;
    }

    /**
     * Called by performRequest() when creating a resource in IGC.  Abstracted to improve readability.
     * @param connection URLConnection object to interact with IGC API.
     * @param inputResource JsonObject representing the 'resource' received by AWS Lambda. This is the
     *                      resource that will be created in IGC.
     * @return JsonObject response that will be sent back to caller of the AWS Lambda function.
     * @throws IOException:
     */
    private static JsonObject callCreateIGCResource(URLConnection connection, JsonObject inputResource) throws IOException {
        String type;
        JsonObject outputJson;
        type = inputResource.get("_type").getAsString();

        if (type.equals("term")) {
            Term newTerm = new Term(
                    inputResource.get("name").getAsString(),
                    inputResource.get("short_description").getAsString(),
                    inputResource.get("long_description").getAsString(),
                    inputResource.get("parent_category_id").getAsString(),
                    inputResource.get("status").getAsString()
            );
            outputJson = connection.createIGCResource(newTerm).toJsonObject();
        } else if (type.equals("category")) {
            Category newCategory = new Category(
                    inputResource.get("name").getAsString(),
                    inputResource.get("short_description").getAsString(),
                    inputResource.get("long_description").getAsString(),
                    inputResource.get("parent_category_id").getAsString()
            );
            outputJson = connection.createIGCResource(newCategory).toJsonObject();
        } else {  // Type not recognized
            logger.error("Attempted to create resource with unknown type: " + type);
            throw new IllegalArgumentException("Attempted to create Resource with unknown type.");
        }
        return outputJson;
    }
}
