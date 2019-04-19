package com.common;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.Utility.JsonToObject;
import com.Utility.JsonResponseBuilder;
import com.Utility.Utility;
import com.dataObjects.Category;
import com.dataObjects.IGCItemList;
import com.dataObjects.IGCResource;
import com.dataObjects.Term;
import com.dataObjects.requests.PostCondition;
import com.dataObjects.requests.PostSearchBody;
import com.dataObjects.requests.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import static com.Utility.JsonToObject.toTypeContainer;

/**
 * A custom URLConnection class
 */
public class URLConnection {
    /**
     * Base String for API
     */
    private String urlString;
    /**
     * Default types to be returned in POST based searches
     */
    private List<String> postSearchTypes;

    /**
     * SLF4J logger initialization.
     */
    private final Logger logger = LoggerFactory.getLogger(URLConnection.class);

    //// Constructors ////

    /**
     * Create a new URLConnection object.
     * Default POST search types are defined here.
     * @param url: HTTP URL to make requests to. Include domain name, port,
     *           and general extension that will be used for all requests.
     *           Education test IGC API: 'https://ec2-3-83-75-69.compute-1.amazonaws.com:9443/ibm/iis/igc-rest/v1/'
     * @throws IllegalStateException: Thrown by Utility.disableSslVerification().
     */
    public URLConnection(String url, boolean disableSslVerification) throws IllegalStateException {
        this.urlString = url;
        // Disable disableSsl verification (Workaround) //
        if (disableSslVerification) {
            Utility.disableSslVerification();
        }
        // Define default POST search return types
        String[] searchTypes = {"category", "term"};  // This can be changed as more Resource types are added.
        this.postSearchTypes = Arrays.asList(searchTypes);

        logger.info("URLConnection created with url: " + url);
    }

    /**
     * Create a new URLConnection object.
     * Default POST search types are defined here.
     * @param url: HTTP URL to make requests to. Include domain name, port,
     *           and general extension that will be used for all requests.
     *           Education test IGC API: 'https://ec2-3-83-75-69.compute-1.amazonaws.com:9443/ibm/iis/igc-rest/v1/'
     * @throws IllegalStateException: Thrown by Utility.disableSslVerification().
     */
    public URLConnection(String url, boolean disableSslVerification, String username, String password)
            throws IllegalStateException {
        this.urlString = url;
        // Disable disableSsl verification (Workaround) //
        if (disableSslVerification) {
            Utility.disableSslVerification();
        }
        // Define default POST search return types
        String[] searchTypes = {"category", "term"};  // This can be changed as more Resource types are added.
        this.postSearchTypes = Arrays.asList(searchTypes);

        // Set default authentication credentials.
        Utility.authenticate(username, password);
        logger.info("URLConnection created with url: " + url);
    }

    ////// END CONSTRUCTORS ///////
    //// BEGIN REQUEST METHODS ////

    /**
     * Make an Http Request without a body.  Generally called to make GET requests
     * Additional constructor exists for accepting IGC resources (for POST and PUT) to populate body.
     *
     * @param url: Full URL to make request to (not a String).
     * @param method: HttpMethod Enum (GET, PUT, POST, DELETE)
     * @return String of HTTP response.
     * @throws IOException: Thrown by JsonResponseBuilder.getResponseObject()
     */
    private Response makeHttpRequest(URL url, HttpMethod method) throws IOException {
        // Establish connection and request method //
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.name());
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // If sending output //
        connection.setDoOutput(true);

        // Setting Request Headers //
        connection.setRequestProperty("Content-Type", "application/json");

        // Handling Redirects //
        connection.setInstanceFollowRedirects(true); //Allow redirects for this object's connection

        // Reading the Response Code //
        logger.debug("Retrieving response code for call to: " + url.toString());
        int status = connection.getResponseCode();

        // Check response code for a redirect indication
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM) {
            String location = connection.getHeaderField("Location");
            logger.warn("Http request resulted in a redirect.");
            logger.warn("Redirecting to: " + location);
            URL newUrl = new URL(location);
            connection = (HttpURLConnection) newUrl.openConnection();
        }

        return JsonResponseBuilder.getResponseObject(connection); //Use this to return only the message of the response.
    }

    /**
     * Make an HTTP request, using an IGCResource object to populate the body.
     * Often used by POST and PUT requests
     *
     * @param url URL at which to make the request.
     * @param method: HttpMethod Enum (GET, PUT, POST, DELETE)
     * @param requestBody Resource to be formatted into the body of the request.
     * @return Response object of HTTP response message and ID if any new resource that was created.
     * @throws IOException: Thrown by JsonResponseBuilder.getResponseObject()
     */
    private Response makeHttpRequest(URL url, HttpMethod method, Object requestBody) throws IOException {
        // Establish connection and request method //
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.name());
        connection.setConnectTimeout(5000); //Setting to 5 seconds for default.
        connection.setReadTimeout(5000);

        // Setting Request Headers //
        connection.setRequestProperty("Content-Type", "application/json");

        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            // Apply parameters to connection object if request body is required.
            logger.debug("About to " + method.name() + ":\n" + requestBody.toString());
            connection.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(requestBody.toString());
            out.flush();
            out.close();
        }

        // Cookie Handling // - Doesn't look like this is needed...
//        String cookiesHeader = connection.getHeaderField("Set-Cookie");
//        List<HttpCookie> cookies = HttpCookie.parse(cookiesHeader);
//        CookieManager cookieManager = new CookieManager();
//
//        cookies.forEach(cookie -> cookieManager.getCookieStore().add(null, cookie));
//
//        Optional<HttpCookie> usernameCookie = cookies.stream()
//                .findAny().filter(cookie -> cookie.getName().equals("username"));
//        if (usernameCookie == null) {
//            cookieManager.getCookieStore().add(null, new HttpCookie("username", "user1"));
//        }
//
//        connection.disconnect();
//        connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Cookie",
//                StringUtils.join(cookieManager.getCookieStore().getCookies(), ";"));

        // Handling Redirects //
        connection.setInstanceFollowRedirects(true); //Allow redirects for this object's connection

        // Reading the Response Code //
        logger.debug("Retrieving response code for call to: " + url.toString());
        int status = connection.getResponseCode();

        // Check response code for a redirect indication
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM) {
            String location = connection.getHeaderField("Location");
            logger.warn("Http request resulted in a redirect.");
            logger.warn("Redirecting to: " + location);
            URL newUrl = new URL(location);
            connection = (HttpURLConnection) newUrl.openConnection();
        }

        return JsonResponseBuilder.getResponseObject(connection); //Use this to return a Response object.
    }

    //// END REQUEST METHODS ////

    //////// GET METHODS ////////

    /**
     * Submit a GET request to the URL associated with the URLConnection object.
     * Does not return a Resource object like other GET based methods, simply makes a GET call with the
     *      combined base URL and extension argument and returns the String response.
     * @param extension: Extension to be appended to the object URL for a specific GET request.
     *                 Leave extension as an empty string if none is desired.
     * @deprecated  Use more specific GET methods when making calls to IGC (See URLConnection methods.)
     * @throws IOException: Thrown by makeHttpRequest()
     */
    public String get(String extension, boolean useAuth) throws IOException {
        String urlWithExtension = this.urlString + extension;
//        logger.info("get method called for URL: " + urlWithExtension);
        URL getUrl = new URL(urlWithExtension);
        try {
            return makeHttpRequest(getUrl, HttpMethod.GET, useAuth).getMessage();
        } catch (Exception e) {
            logger.error("XXX - failed get()");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Submit a GET request expecting only a TERM in return.
     * Use getIGCResourceById for a more general method that can return any IGCResource.
     * @param id ID of Term to request (URL extension for GET request).
     * @return a Term object representing the received JSON. Returns an empty Term with failedResponse set to true
     *         if the Term does not exist or if the server does not respond.
     * @throws IOException: Thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public Term getIGCTermById(String id) throws IOException, IllegalArgumentException {
        String urlWithExtension = this.urlString + "assets/" + id;
//        logger.info("getIGCTermById called for URL: " + urlWithExtension);
        URL getUrl = new URL(urlWithExtension);

        try {
            Response response = makeHttpRequest(getUrl, HttpMethod.GET, true);
            if (response.getResponseCode() < 300) {
                if (JsonToObject.toTypeContainer(response.getMessage()).get_type().equals("term")) { //Check if Term
                    return JsonToObject.toTerm(response.getMessage());
                } else {
                    throw new IllegalArgumentException("Called getIGCTermById on a non-term.");
                }
            } else { // Request failed - Can add more specific handling here.
                return new Term();
            }
        } catch (Exception e) {
            logger.error("XXX - failed getIGCTermById()");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Submit a GET request expecting only a CATEGORY in return.
     * **Use getIGCResourceById for a more general method that can return any IGCResource.
     * @param id ID of category to request (URL extension for GET request).
     * @return a Category object representing the received JSON. Returns an empty Category with failedResponse set
     *         to true if the Category does not exist or if the server does not respond.
     * @throws IOException: Thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public Category getIGCCategoryById(String id) throws IOException, IllegalArgumentException {
        String urlWithExtension = this.urlString + "assets/" + id;
//        logger.info("getIGCCategoryById called for URL: " + urlWithExtension);
        URL getUrl = new URL(urlWithExtension);

        try {
            Response response = makeHttpRequest(getUrl, HttpMethod.GET, true);
            if (response.getResponseCode() < 300) {
                if (JsonToObject.toTypeContainer(response.getMessage()).get_type().equals("category")) { //Check if cat
                    return JsonToObject.toCategory(response.getMessage());
                } else {
                    throw new IllegalArgumentException("Called getIGCCategoryById on a non-category.");
                }
            } else { // Request failed. Can add more specific handling here.
                return new Category();
            }
        } catch (Exception e) {
            logger.error("XXX - failed getIGCCategoryById()");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Return a full IGCResource with a given id.  Will automatically return either a Category or Term based on
     *      the result.
     * TODO *** New cases will need to be made as new IGC POJO class types are created ***
     * @param id ID of category to request (URL extension for GET request).
     * @return An IGCResource object representing the received JSON. Returns an empty Term with failedResponse set
     *         to true if the Resource does not exist or the server does not respond.
     * @throws IOException: Thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public IGCResource getIGCResourceById(String id) throws IOException, IllegalArgumentException {
        String urlWithExtension = this.urlString + "assets/" + id;
        URL getUrl = new URL(urlWithExtension);

        try {
            Response response = makeHttpRequest(getUrl, HttpMethod.GET, true);
            if (response.getResponseCode() < 300) {
                // Determine '_type' to know what POJO to cast String to.
                String type = JsonToObject.toTypeContainer(response.getMessage()).get_type();
                if (type.equals("category")) {
                    return JsonToObject.toCategory(response.getMessage());
                } else if (type.equals("term")) {
                    return JsonToObject.toTerm(response.getMessage());
                }/*else if (type.equals("NEW_IGC_TYPE") {     // Example for when new IGC type.
                return JsonTo_NEW_IGC_TYPE(receivedString);
                }*/ else {
                    throw new IllegalArgumentException(
                            "Attempted to GET an IGCResource for which there is no Java Object");
                }
            } else { // Can add more specific handling here...
                return new Term(true); // Just returns an empty Term as a placeholder
            }                                                   // since the type of resource to be returned is unknown.
        } catch (Exception e) {
            logger.error("XXX - getIGCResourceById() failed");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Get the ID of a Category's parent category.
     * @param id ID of Category to look up parent's ID.
     * @return String of parent category's ID. Returns empty String if ID does not exist or
     *         if the request cannot be completed.
     * @throws IOException: Thrown by makeHttpRequest()
     */
    public String getResourceParentId(String id) throws IOException, IllegalArgumentException {
        return this.getIGCResourceById(id).getParent_category().get_id();
    }

    /**
     * Get the ID of a Term's parent category.
     * @param id ID of Term to look up parent's ID.
     * @return String of parent category's ID. Returns empty String if ID does not exist or
     *         if the request cannot be completed.
     * @deprecated Use getResourceParentId() for a more general method that can return any IGCResource.
     * @throws IOException: Thrown by makeHttpRequest()
     */
    public String getTermParentId(String id) throws IOException, IllegalArgumentException {
        if (isResourceOfType(id, "term")) {
            return this.getIGCTermById(id).getParent_category().get_id();
        } else {
            throw new IllegalArgumentException("Attempted to call getTermParentId() on a non-term.");
        }
    }

    /**
     * Get the ID of a Category's parent category.
     * @param id ID of Category to look up parent's ID.
     * @return String of parent category's ID. Returns empty String if ID does not exist or
     *         if the request cannot be completed.
     * @deprecated Use getResourceParentId() for a more general method that can return any IGCResource.
     * @throws IOException: Thrown by makeHttpRequest()
     */
    public String getCategoryParentId(String id) throws IOException, IllegalArgumentException {
        if (isResourceOfType(id, "category")) {
            return this.getIGCCategoryById(id).getParent_category().get_id();
        } else {
            throw new IllegalArgumentException("Attempted to call getCategoryParentId() on a non-category.");
        }
    }

    /**
     * Submit a GET request search of all IGC categories.
     * @param pageSize How many categories to return.
     * @return An IGCItemList containing a list of categories (IGCItems) and paging info.
     *         Returns an empty IGCItemList with 'failedRequest' set to true if the request cannot be completed.
     * @throws IOException: Thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public IGCItemList getIGCCategoryList(int pageSize) throws IOException, IllegalArgumentException {
        String urlWithExtension = this.urlString + "search?types=category&pageSize=" + pageSize;
        URL getUrl = new URL(urlWithExtension);
        try {
            Response response = makeHttpRequest(getUrl, HttpMethod.GET, true);
            if (response.getResponseCode() < 300) {
                return JsonToObject.toIGCItemList(response.getMessage());
            } else {
                return new IGCItemList(true);
            }
        } catch (IOException e) {
            logger.error("XXX - failed getIGCCategoryList, pageSize=" + pageSize);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Submit a GET request of all IGC terms.
     * @param pageSize How many terms to return
     * @return An IGCItemList containing a list of terms (IGCItems) and paging info.
     * @throws IOException: Thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public IGCItemList getIGCTermList(int pageSize) throws IOException {
        String urlWithExtension = this.urlString + "search?types=term&pageSize=" + pageSize;
        URL getUrl = new URL(urlWithExtension);
        try {
            Response response = makeHttpRequest(getUrl, HttpMethod.GET, true);
            if (response.getResponseCode() < 300) {
                return JsonToObject.toIGCItemList(response.getMessage());
            } else {
                return new IGCItemList(true);
            }
        } catch (IOException e) {
            logger.error("XXX - failed getIGCTermList, pageSize=" + pageSize);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Check if a Resource is of a certain type (term, category, etc...).
     * @param id ID of Resource to check.
     * @param type Type to check Resource against.
     * @return boolean of whether Resource matches type. Returns false if ID cannot be found, or if bad request.
     */
    public boolean isResourceOfType(String id, String type) throws IOException {
        String urlWithExtension = this.urlString + "assets/" + id;
        logger.debug("isResourceOfType() called id: " + id + "\nand type: " + type);
        URL getUrl = new URL(urlWithExtension);

        try {
            Response response = makeHttpRequest(getUrl, HttpMethod.GET, true);
            if (response.getResponseCode() < 300) {
                String apiType = JsonToObject.toTypeContainer(response.getMessage()).get_type();
                return (type.equals(apiType));
            } else if (response.getResponseCode() == 404) { //Id could not be found.
                return false;
            } else {
                return false;  // Better way to handle other errors?
            }
        } catch (Exception e) {
            logger.error("XXX - makeHttpRequest failed when called from isResourceOfType()");
            e.printStackTrace();
            throw e;
        }
    }

    //// PUT METHODS - Update ////
    /* Updates can easily be done by creating an empty new Resource object (Term, Category, etc), and
     * then using setter methods to add the fields that are to be updated. */

    /**
     * Update an existing IGC API resource (Category, Term, etc...).
     * @param id ID of the resource to update.
     * @param updateResource IGC object (Term, Category, etc) whose properties will be applied to an
     *                       existing term in the IGC API.
     * @return Response object from the API request.
     * @throws IOException: Thrown by makeHttpRequest()
     * Note:
     *  - Seems that getting a Resource from the API, changing a field and then using that Resource
     *    here as an argument causes a server error.
     *  - May have something to do with PUTing a Resource object with API set fields.
     *  --> Should instead create an empty term object, using setters to add field values that are to be updated.
     */
    public Response updateIGCResource(String id, IGCResource updateResource) throws IOException {
        String urlWithExtension = this.urlString + "assets/" + id;
        URL putUrl = new URL(urlWithExtension);
        try {
            logger.info("Attempting to update IGCResource: " + id);
            logger.debug("New properties to PUT:\n" + updateResource);
            return makeHttpRequest(putUrl, HttpMethod.PUT, updateResource);
        } catch (Exception e) {
            logger.error("XXX - makeHttpRequest failed when called from updateIGCResource()");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Update the name of an IGC Term with a given ID.
     * @param id ID of the term to update.
     * @param name New name for the term.
     * @return Response object from the API request.
     * @throws IOException: Thrown by makeHttpRequest()
     */
    public Response updateIGCTermName(String id, String name) throws IOException {
        Term updateTerm = new Term();
        updateTerm.setName(name);
        return this.updateIGCResource(id, updateTerm);
    }

    /**
     * Update the name of an IGC Category with a given ID.
     * @param id ID of the term to update.
     * @param name New name for the term.
     * @return Response object from the API request.
     * @throws IOException: Thrown by makeHttpRequest()
     */
    public Response updateIGCCategoryName(String id, String name) throws IOException {
        Category updateCategory = new Category();
        updateCategory.setName(name);
        return this.updateIGCResource(id, updateCategory);
    }

    //// POST METHODS - Create and Search ////

    /**
     * Create a new IGCResource (Term, Category, etc...) in the IGC API.
     *
     * @param newResource IGCResource object to POST to the API.
     * @return Response object from the API request
     * @throws IOException: thrown by makeHttpRequest()
     */
    public Response createIGCResource(IGCResource newResource) throws IOException {
        String urlWithExtension = this.urlString + "assets/";
        URL postUrl = new URL(urlWithExtension);
        try {
            logger.info("Attempting to create new IGCResource");
            logger.debug("Resource to be created:\n" + newResource);
            return makeHttpRequest(postUrl, HttpMethod.POST, newResource);
        } catch (Exception e) {
            logger.error("XXX - makeHttpRequest failed when called from createIGCResource()");
            e.printStackTrace();
            throw e;
        }
    }

    // Search methods //
    /**
     * Given Lists of types and search conditions, search the IGC API.
     * @param types List(String) of types such as category, term, etc... to search for
     * @param conditions List(PostCondition) of PostCondition objects, each of which define a search condition.
     * @param operator Boolean operator ('or', 'and') for search conditions.
     * @return A Response object, containing the JSON message and response code/message.
     * @throws IOException: thrown by makeHttpRequest()
     */
    public Response searchIGC (List<String> types, List<PostCondition> conditions, String operator)
            throws IOException, IllegalStateException {
        String urlWithExtension = this.urlString + "search/";
        URL postUrl = new URL(urlWithExtension);

        //Not quite sure how this works... Seems to return some basic properties plus these listed.
        //Currently fills out entire IGCItem object.
        String[] properties = {"modified_on", "short_description"};
        List<String> propertiesList = Arrays.asList(properties);
        try {
            PostSearchBody searchBody = new PostSearchBody(propertiesList, types, conditions, operator);
            logger.debug("Attempting to POST-search IGC with search body:\n" + searchBody);
            Response response =  makeHttpRequest(postUrl, HttpMethod.POST, searchBody);
//            if (response.getResponseCode() < 300) {
            return response;
//            } else { //No JSON message to build a pojo from. Throw exception.
//                throw new IllegalStateException("Received bad response when searching IGC: " +
//                        response.getResponseCode() + " - " + response.getCodeMessage());
//            }
        } catch (Exception e) {
            logger.error("Error during POST search request to IGC API. Method: (searchIGC)\n" +
                    "Ensure API is available.\n");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Search the IGC API for resources that contain the entered searchTerm in their name, descriptions, ...
     * @param searchTerm String to search for in IGC Resources
     * @return IGCItemList of search results.
     * @throws IOException: thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public IGCItemList searchIGCResource(String searchTerm) throws IOException, IllegalArgumentException {
        // Set up search conditions
        PostCondition nameCondition = new PostCondition("name", "like %{0}%", searchTerm);
        PostCondition sDescCondition = new PostCondition("short_description", "like %{0}%", searchTerm);
        PostCondition lDescCondition = new PostCondition("long_description", "like %{0}%", searchTerm);

        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(nameCondition);
        conditions.add(sDescCondition);
        conditions.add(lDescCondition);

        logger.info("Attempting to search for IGCResource with term: " + searchTerm);
        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        if (response.getResponseCode() < 300) {
            return JsonToObject.toIGCItemList(response.getMessage());
        } else {
            return new IGCItemList(true);
        }
    }

    /**
     * Search the IGC API for resources with a specific name.
     * Abstraction of searchIGC method.
     * @param name Name to search for.
     * @return IGCItemList of search results.
     * @throws IOException: thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public IGCItemList searchIGCResourceName(String name) throws IOException, IllegalArgumentException {
        // Set up search conditions
        PostCondition nameCondition = new PostCondition("name", "like %{0}%", name);
        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(nameCondition);

        logger.info("Attempting to search IGCResource with name: " + name);
        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        if (response.getResponseCode() < 300) {
            return JsonToObject.toIGCItemList(response.getMessage());
        } else {
            return new IGCItemList(true);
        }
    }

    /**
     * Searches the IGC API for resources modified between the two provided dates/times.
     * @param min String of lower bound time: 'yyyy/MM/dd HH:mm:ss'.
     * @param max String of upper bound time: 'yyyy/MM/dd HH:mm:ss'.
     * @return IGCItemList of search results.
     * @throws IOException: thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     * @throws ParseException: Thrown if dates cannot be properly parsed.
     */
    public IGCItemList searchIGCResourceModifiedBetween(String min, String max)
            throws IOException, IllegalArgumentException, ParseException {
        //Parse dates.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date date1 = dateFormat.parse(min);
            Date date2 = dateFormat.parse(max);

            // Set up search conditions
            PostCondition nameCondition = new PostCondition("modified_on", "between",
                    null, false, date1.getTime(), date2.getTime());
            List<PostCondition> conditions = new ArrayList<>();
            conditions.add(nameCondition);

            logger.info("Attempting to search IGCResource modified between " + min + " and " + max);
            Response response = searchIGC(this.postSearchTypes, conditions, "or");
            if (response.getResponseCode() < 300) {
                return JsonToObject.toIGCItemList(response.getMessage());
            } else {
                return new IGCItemList(true);
            }
        } catch (ParseException e) {
            logger.error("Could not parse date String(s).");
            throw e;
        }
    }

    /**
     * Searches the IGC API for resources modified between the two provided dates/times.
     * @param min String of lower bound time: 'yyyy/MM/dd HH:mm:ss'.
     * @param max String of upper bound time: 'yyyy/MM/dd HH:mm:ss'.
     * @return IGCItemList of search results.
     * @throws IOException: thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     * @throws ParseException: Thrown if dates cannot be properly parsed.
     */
    public IGCItemList searchIGCResourceCreatedBetween(String min, String max)
            throws IOException, IllegalArgumentException, ParseException {
        //Parse dates.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date date1 = dateFormat.parse(min);
            Date date2 = dateFormat.parse(max);

            // Set up search conditions
            PostCondition nameCondition = new PostCondition("created_on", "between",
                    null, false, date1.getTime(), date2.getTime());
            List<PostCondition> conditions = new ArrayList<>();
            conditions.add(nameCondition);

            logger.info("Attempting to search IGCResource created between " + min + " and " + max);
            Response response = searchIGC(this.postSearchTypes, conditions, "or");
            if (response.getResponseCode() < 300) {
                return JsonToObject.toIGCItemList(response.getMessage());
            } else {
                return new IGCItemList(true);
            }
        } catch (ParseException e) {
            logger.error("Could not parse date String(s)");
            throw e;
        }
    }

    /**
     * Search the IGC API for resources created or modified by a user.
     * Abstraction of searchIGC method.
     * @param name Name of the user to search resources for.
     * @return IGCItemList of search results.
     * @throws IOException: thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public IGCItemList searchIGCResourceByUser(String name) throws IOException, IllegalArgumentException {
        // Set up search conditions
        PostCondition modCondition = new PostCondition("modified_by", "like %{0}%", name);
//        PostCondition createCondition = new PostCondition("created_by", "like %{0}%", name);

        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(modCondition);
//        conditions.add(createCondition);

        logger.info("Attempting to search IGCResource by user: " + name);
        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        if (response.getResponseCode() < 300) {
            return JsonToObject.toIGCItemList(response.getMessage());
        } else {
            return new IGCItemList(true);
        }
    }

    /**
     * Search the IGC API for resources with a given name for a specified property.
     * @param property Property to search the IGC API for (name, parent_category, parent_category._id, etc).
     * @param name Search term.
     * @return IGCItemList of search results.
     * @throws IOException: thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public IGCItemList searchIGCResourceCustom(String property, String name)
            throws IOException, IllegalArgumentException {
        // Set up search conditions
        PostCondition custCondition = new PostCondition(property, "like %{0}%", name);

        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(custCondition);

        logger.info("Attempting to search IGCResource by custom property: " + property + " with search term: " + name);
        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        if (response.getResponseCode() < 300) {
            return JsonToObject.toIGCItemList(response.getMessage());
        } else {
            return new IGCItemList(true);
        }
    }

    /**
     * Returns an IGCItemList of all Resources that are missing a given property.
     * @return IGCItemList of search results
     * @throws IOException: IOException thrown by makeHttpRequest()
     * @throws IllegalArgumentException: Thrown by JsonToObject.toIGCItemList()
     */
    public IGCItemList searchIGCResourceNullProp(String property) throws IOException, IllegalArgumentException {
        PostCondition condition = new PostCondition(property, "isNull", null);

        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(condition);

        logger.info("Attempting to search IGCResource with 'NULL' for property: " + property);
        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        if (response.getResponseCode() < 300) {
            return JsonToObject.toIGCItemList(response.getMessage());
        } else {
            return new IGCItemList(true);
        }
    }

    //// DELETE METHODS - Delete ////

    /**
     * Delete an IGCResource by its unique ID (cannot be undone!)
     * @param id ID of resource to delete.
     * @return Response object with message and response code returned by HTTP DELETE request.
     * @throws IOException: IOException thrown by makeHttpRequest()
     */
    public Response deleteIGCResource(String id) throws IOException {
        String urlWithExtension = this.urlString + "assets/" + id;
        URL deleteUrl = new URL(urlWithExtension);
        try {
            return makeHttpRequest(deleteUrl, HttpMethod.DELETE, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
