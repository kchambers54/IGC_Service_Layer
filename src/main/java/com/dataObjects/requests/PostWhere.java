package com.dataObjects.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Contains the list of conditions (PostCondition objects) and matching operator for a POST search of the IGC API.
 */
public class PostWhere {
    /**
     * A List of Condition objects, each of which contains a search criteria.
     */
    private List<PostCondition> conditions;
    /**
     * Defines how List of search criteria should be considered.
     * I.E: "and" - must meet all criteria, "or" - must meet one criteria.
     */
    private String operator;

    /**
     * No argument constructor
     */
    public PostWhere() { }

    /**
     * Creates a PostWhere object to be used in a PostSearchBody object.
     * @param conditions A List of PostCondition objects, each of which contains a search criteria.
     * @param operator Defines how List of search criteria should be considered.
     *                 -- I.E: "and" - must meet all criteria, "or" - must meet one criteria.
     */
    public PostWhere(List<PostCondition> conditions, String operator) {
        this.conditions = conditions;
        this.operator = operator;
    }

    //// GETTERS AND SETTERS ////

    public List<PostCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<PostCondition> conditions) {
        this.conditions = conditions;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    //// END GETTERS AND SETTERS ////

    @Override
    public String toString() {
        Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printer
        return gsonPrinter.toJson(this);
    }
}
