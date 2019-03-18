package com.dataObjects.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Holds the information needed for the body of a POST search of the IGC API.
 */
public class PostSearchBody {
    /**
     * A List of properties to return in the search response.
     * I.E: name, modified_on, short_description, labels, parent_category, etc...
     *  - Seems to return a basic set or properties plus those in this List.
     *  - Make sure that any additional properties are also added to the IGCItem Class.
     */
    private List<String> properties;
    /**
     * List of resource types to search for
     * I.E: term, category, database_table, etc...
     */
    private List<String> types;
    /**
     * PostWhere object that contains the search criteria
     */
    private PostWhere where;
    /**
     * Page size of POST search results. TODO Setting to 1000 in constructor for now...
     */
    private int pageSize;


    /**
     * Creates a PostSearchBody object.  Currently auto-sets pageSize to 1000.
     * @param properties List of properties to return in the search response.
     *                   I.E: name, short_description, parent_category._id, etc...
     * @param types List of types of resources to return (term, category, etc...)
     * @param conditions A List of PostCondition objects, each defining a search criteria.
     * @param operator A String defining how the List of search criteria should be considerd.
     *                 I.E: "and" - must meet all criteria, "or" - must meet one criteria.
     */
    public PostSearchBody(List<String> properties, List<String> types, List<PostCondition> conditions, String operator) {
        this.properties = properties;
        this.types = types;
        this.where = new PostWhere(conditions, operator);
        //Just setting pageSize to 1000 for now
        this.pageSize = 1000;
    }

    ////GETTERS AND SETTERS ////


    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public PostWhere getWhere() {
        return where;
    }

    public void setWhere(PostWhere where) {
        this.where = where;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    //// END GETTERS AND SETTERS ////

    @Override
    public String toString() {
        Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printer
        return gsonPrinter.toJson(this);
    }
}
