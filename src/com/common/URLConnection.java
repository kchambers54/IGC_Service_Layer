package com.common;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.Utility.JsonToObject;
import com.Utility.JsonResponseBuilder;
import com.Utility.Utility;
import com.dataObjects.Category;
import com.dataObjects.IGCResource;
import com.dataObjects.IGCItemList;
import com.dataObjects.Term;
import com.dataObjects.requests.PostCondition;
import com.dataObjects.requests.PostSearchBody;
import com.dataObjects.requests.Response;

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
     * Create a new URLConnection object.
     * Default POST search types are defined here.
     * @param url: HTTP URL to make requests to. Include domain name, port,
     *           and general extension that will be used for all requests.
     *           Education test IGC API: 'https://ec2-3-83-75-69.compute-1.amazonaws.com:9443/ibm/iis/igc-rest/v1/'
     */
    public URLConnection(String url) {
        this.urlString = url;
        // Disable ssl verification (Workaround) //
        Utility.disableSslVerification();
        // Define default POST search return types
        String[] searchTypes = {"category", "term"};  // This can be changed as more Resource types are added.
        this.postSearchTypes = Arrays.asList(searchTypes);
    }

    /**
     * Make an Http Request without a body.  Generally called to make GET requests
     * Additional constructor exists for accepting IGC resources (for POST and PUT) to populate body.
     *
     * @param url: Full URL to make request to (not a String).
     * @param method: HttpMethod Enum (GET, PUT, POST, DELETE)
     * @param useAuth Set to true if sending request to IGC API. This enables the required authentication.
     * @return String of HTTP response.
     * @throws IOException:
     */
    private Response makeHttpRequest(URL url, HttpMethod method, boolean useAuth) throws IOException {
        // Establish connection and request method //
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.name());
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // Enable Authentication if desired //
        if (useAuth) {
            Utility.authenticate();
        }

        // If sending output //
        connection.setDoOutput(true);

        // Setting Request Headers //
        connection.setRequestProperty("Content-Type", "application/json");

        // Handling Redirects //
        connection.setInstanceFollowRedirects(true); //Allow redirects for this object's connection

        // Reading the Response Code //
        System.out.println("Retrieving response code for call to: " + url.toString());
        int status = connection.getResponseCode();

        // Check response code for a redirect indication
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM) {
            String location = connection.getHeaderField("Location");
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
     * @param useAuth Set to true if sending request to IGC API. This enables the required authentication
     * @param requestBody Resource to be formatted into the body of the request.
     * @return Response object of HTTP response message and ID if any new resource that was created.
     * @throws IOException:
     */
    private Response makeHttpRequest(URL url, HttpMethod method, boolean useAuth, Object requestBody) throws IOException {
        // Establish connection and request method //
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.name());
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // Enable Authentication if desired //
        if (useAuth) {
            Utility.authenticate();
        }

        // Setting Request Headers //
        connection.setRequestProperty("Content-Type", "application/json");

        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            // Apply parameters to connection object if request body is required.
            System.out.println("About to " + method.name() + ":\n" + requestBody.toString());
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
        System.out.println("Retrieving response code for call to: " + url.toString());
        int status = connection.getResponseCode();

        // Check response code for a redirect indication
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM) {
            String location = connection.getHeaderField("Location");
            URL newUrl = new URL(location);
            connection = (HttpURLConnection) newUrl.openConnection();
        }

        return JsonResponseBuilder.getResponseObject(connection); //Use this to return a Response object.
    }

    //// GET METHODS ////

    /**
     * Submit a GET request to the URL associated with the URLConnection object.
     * @param extension: Extension to be appended to the object URL for a specific GET request.
     *                 Leave extension as an empty string if none is desired.
     * @throws Exception:
     */
    public String get(String extension, boolean useAuth) throws Exception {
        String urlWithExtension = this.urlString + extension;
        System.out.println("get method called for URL: " + urlWithExtension);
        URL getUrl = new URL(urlWithExtension);
        return makeHttpRequest(getUrl, HttpMethod.GET, useAuth).getMessage();
    }

    /**
     * Submit a GET request expecting a TERM in return.
     * @param extension ID of Term to request (URL extension for GET request).
     * @return a Term object representing the received JSON.
     * @throws Exception:
     */
    public Term getIGCTermById(String extension) throws Exception {
        String urlWithExtension = this.urlString + "assets/" + extension;
        System.out.println("getIGCTermById called for URL: " + urlWithExtension);
        URL getUrl = new URL(urlWithExtension);

        String receivedString = makeHttpRequest(getUrl, HttpMethod.GET, true).getMessage();
        return JsonToObject.toTerm(receivedString);
    }

    /**
     * Submit a GET request expecting a Category in return.
     * @param extension ID of category to request (URL extension for GET request).
     * @return a Category object representing the received JSON.
     * @throws Exception:
     */
    public Category getIGCCategoryById(String extension) throws Exception {
        String urlWithExtension = this.urlString + "assets/" + extension;
        System.out.println("getIGCCategoryById called for URL: " + urlWithExtension);
        URL getUrl = new URL(urlWithExtension);

        String receivedString = makeHttpRequest(getUrl, HttpMethod.GET, true).getMessage();
        return JsonToObject.toCategory(receivedString);
    }

    /**
     * Get the ID of a Category's parent category.
     * @param id ID of Category to look up parent's ID.
     * @return String of parent category's ID.
     * @throws Exception:
     */
    public String getCategoryParentId(String id) throws Exception {
        return this.getIGCCategoryById(id).getParent_category().get_id();
    }

    /**
     * Get the ID of a Term's parent category.
     * @param id ID of Category to look up parent's ID.
     * @return String of parent category's ID.
     * @throws Exception:
     */
    public String getTermParentId(String id) throws Exception {
        return this.getIGCTermById(id).getParent_category().get_id();
    }

    /**
     * Submit a GET request search of all IGC categories.
     * @param pageSize How many categories to return.
     * @return An IGCItemList containing a list of categories (IGCItems) and paging info.
     * @throws Exception:
     */
    public IGCItemList getIGCCategoryList(int pageSize) throws Exception {
        String urlWithExtension = this.urlString + "search?types=category&pageSize=" + pageSize;
        URL getUrl = new URL(urlWithExtension);
        try {
            String receivedString = makeHttpRequest(getUrl, HttpMethod.GET, true).getMessage();
            return JsonToObject.toIGCItemList(receivedString);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Submit a GET request of all IGC terms.
     * @param pageSize How many terms to return
     * @return An IGCItemList containing a list of terms (IGCItems) and paging info.
     * @throws Exception:
     */
    public IGCItemList getIGCTermList(int pageSize) throws Exception {
        String urlWithExtension = this.urlString + "search?types=term&pageSize=" + pageSize;
        URL getUrl = new URL(urlWithExtension);
        try {
            String receivedString = makeHttpRequest(getUrl, HttpMethod.GET, true).getMessage();
            return JsonToObject.toIGCItemList(receivedString);
        } catch (Exception e) {
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
     * @throws Exception:
     *
     *  - Seems that getting a Resource from the API, changing a field and then using that Resource
     *    here as an argument causes a server error.
     *  - May have something to do with PUTing a Resource object with API set fields.
     *  - Can instead create an empty term object, using setters to add updated field values.
     */
    public Response updateIGCResource(String id, IGCResource updateResource) throws Exception {
        String urlWithExtension = this.urlString + "assets/" + id;
        URL putUrl = new URL(urlWithExtension);
        try {
            Response response = makeHttpRequest(putUrl, HttpMethod.PUT, true, updateResource);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Update the name of an IGC Term with a given ID.
     * @param id ID of the term to update.
     * @param name New name for the term.
     * @return Response object from the API request.
     * @throws Exception:
     */
    public Response updateIGCTermName(String id, String name) throws Exception {
        Term updateTerm = new Term();
        updateTerm.setName(name);
        return this.updateIGCResource(id, updateTerm);
    }

    /**TODO TEST
     * Update the name of an IGC Category with a given ID.
     * @param id ID of the term to update.
     * @param name New name for the term.
     * @return Response object from the API request.
     * @throws Exception:
     */
    public Response updateIGCCategoryName(String id, String name) throws Exception {
        Category updateCategory = new Category();
        updateCategory.setName(name);
        return this.updateIGCResource(id, updateCategory);
    }

    //// POST METHODS - Create and Search ////

    /**
     * Create a new IGCResource (Term, Category, etc...) in the IGC API.
     *
     * @param newResource IGCResource object to POST to the API.
     * @return Response String from the API request
     * @throws Exception:
     */
    public Response createIGCResource(IGCResource newResource) throws Exception {
        String urlWithExtension = this.urlString + "assets/";
        URL postUrl = new URL(urlWithExtension);
        try {
            Response response = makeHttpRequest(postUrl, HttpMethod.POST, true, newResource);
            return response;
        } catch (Exception e) {
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
     * @throws Exception:
     */
    public Response searchIGC (List<String> types, List<PostCondition> conditions, String operator) throws Exception {
        String urlWithExtension = this.urlString + "search/";
        URL postUrl = new URL(urlWithExtension);

        //Not quite sure how this works... Seems to return some basic properties plus these listed.
        //Currently fills out entire IGCItem object.
        String[] properties = {"modified_on", "short_description"};
        List<String> propertiesList = Arrays.asList(properties);
        try {
            PostSearchBody searchBody = new PostSearchBody(propertiesList, types, conditions, operator);
            return makeHttpRequest(postUrl, HttpMethod.POST, true, searchBody);
        } catch (Exception e) {
            System.err.println("Error during POST search request to IGC API. Method: (searchIGC)\n" +
                    "Ensure API is available.\n");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Search the IGC API for resources that contain the entered searchTerm in their name, descriptions, ...
     * @param searchTerm String to search for in IGC Resources
     * @return IGCItemList of search results.
     * @throws Exception:
     */
    public IGCItemList searchIGCResource(String searchTerm) throws Exception {
        // Set up search conditions
        PostCondition nameCondition = new PostCondition("name", "like %{0}%", searchTerm);
        PostCondition sDescCondition = new PostCondition("short_description", "like %{0}%", searchTerm);
        PostCondition lDescCondition = new PostCondition("long_description", "like %{0}%", searchTerm);

        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(nameCondition);
        conditions.add(sDescCondition);
        conditions.add(lDescCondition);

        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        return JsonToObject.toIGCItemList(response.getMessage());
    }

    /**
     * Search the IGC API for resources with a specific name.
     * Abstraction of searchIGC method.
     * @param name Name to search for.
     * @return IGCItemList of search results.
     * @throws Exception:
     */
    public IGCItemList searchIGCResourceName(String name) throws Exception {
        // Set up search conditions
        PostCondition nameCondition = new PostCondition("name", "like %{0}%", name);
        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(nameCondition);

        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        return JsonToObject.toIGCItemList(response.getMessage());
    }

    /**
     * Searches the IGC API for resources modified between the to provided time arguments
     * @param min lower bound UNIX time in milliseconds.
     * @param max uppder bound UNIX time in milliseconds.
     * @return IGCItemList of search results.
     * @throws Exception:
     */
    public IGCItemList searchIGCResourceModifiedBetween(long min, long max) throws Exception {
        // Set up search conditions
        PostCondition nameCondition = new PostCondition("modified_on", "between",
                null, false, min, max);
        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(nameCondition);

        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        return JsonToObject.toIGCItemList(response.getMessage());
    }

    /**
     * Search the IGC API for resources created or modified by a user.
     * Abstraction of searchIGC method.
     * @param name Name of the user to search resources for.
     * @return IGCItemList of search results.
     * @throws Exception:
     */
    public IGCItemList searchIGCResourceByUser(String name) throws Exception {
        // Set up search conditions
        PostCondition modCondition = new PostCondition("modified_by", "like %{0}%", name);
        PostCondition createCondition = new PostCondition("created_by", "like &{0}%", name);

        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(modCondition);
        conditions.add(createCondition);

        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        return JsonToObject.toIGCItemList(response.getMessage());
    }

    /**TODO TEST
     * Search the IGC API for resources with a given name for a specified property.
     * @param property Property to search the IGC API for (name, parent_category, parent_category._id, etc).
     * @param name Search term.
     * @return IGCItemList of search results.
     * @throws Exception:
     */
    public IGCItemList searchIGCResourceCustom(String property, String name) throws Exception {
        // Set up search conditions
        PostCondition custCondition = new PostCondition(property, "like %{0}%", name);

        List<PostCondition> conditions = new ArrayList<>();
        conditions.add(custCondition);

        Response response = searchIGC(this.postSearchTypes, conditions, "or");
        return JsonToObject.toIGCItemList(response.getMessage());
    }

    //// DELETE METHODS - Delete ////

    /**
     * Delete an IGCResource by its unique ID (cannot be undone!)
     * @param id ID of resource to delete.
     * @return Response object with message and response code returned by HTTP DELETE request.
     */
    public Response deleteIGCResource(String id) throws Exception {
        String urlWithExtension = this.urlString + "assets/" + id;
        URL deleteUrl = new URL(urlWithExtension);
        try {
            Response response = makeHttpRequest(deleteUrl, HttpMethod.DELETE, true, null);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
