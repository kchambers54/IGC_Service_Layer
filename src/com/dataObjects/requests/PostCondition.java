package com.dataObjects.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Individual Condition object that makes up the List of search conditions in a PostWhere object.
 */
public class PostCondition implements Serializable {

    //// Mandatory Fields ////
    /**
     * IGC Resource property to consider.
     * I.E: "name", "labels.name", "modified_on", "_id", "parent_category._id", etc...
     */
    private String property;
    /**
     * Operator for search criteria. Can be any of the following (possibly more):
     *
     * - "like":  -- Requires a 'value' String to be defined --
     *          Searches for a resource where the property is like the defined 'value' String
     *          - - - Formatting - - -
     *          "like {0}":      Appears equivalent to '='.
     *          "like {0}%":     String 'value' is in front of the rest of the property.
     *          "like %{0}":     String 'value' is at the end of the property.
     *          "like %{0}%":    String 'value' can appear anywhere in the property.
     *
     * - "isNull":
     *          Searches for resources where the property is Null.  No 'value' String required.
     *          Can set 'negated' boolean to true to make this the same as 'notNull'.
     *
     * - "in":    -- Requires a 'value' List to be defined --
     *          Searches for resources where the property is in the List 'value'.
     *          *TODO-- 'value' is currently a String.
     *          *    -- "in" likely wont work unless the 'value' String is a JSON array.
     *
     * - "=":     -- Requires a 'value' String to be defined --
     *        Searches for resources where the property is equal to the 'value' String.
     *
     *  - "containsWord":  -- Requires a 'value' String to be defined --
     *          Searches for resources where the property contains the word in the 'value' String.
     *
     *  - "<=":    -- Requires a 'value' String (or int) be set to an integer value --
     *          Searches for resources where the property is less than or equal to the 'value'.
     *          Can also use: ">", "<", ">=", etc...
     *
     *  - "between":  -- Requires that 'min' and 'max' be defined (rather than 'value) --
     *          Searches for resources where the property is between the 'min' and 'max' integers.
     *          This can be useful for searching for a range of UNIX time.
     */
    private String operator;

    //// Optional Fields ////
    /**
     * String for which the defined (above) operator is to act on.
     * Optional, depending on the operator used.
     */
    private String value;
    /**
     * Optional boolean that will negate (inverse) the operator used.
     * I.E: If used in combination with the operator 'isNull', will convert to 'notNull'.
     */
    private boolean negated;
    /**
     * Lower bound used with the 'between' operator.
     */
    private long min;
    /**
     * Upper bound used with the 'between' operator.
     */
    private long max;


    /**
     * No argument Constructor.
     */
    public PostCondition() {
        super();
    }

    /**
     * General use constructor.  Does not define 'negate', 'min', or 'max'.
     * @param property IGC Resource property to consider
     *              I.E: "name", "labels.name", "modified_on", "_id", "parent_category._id", etc...
     * @param operator Search operator to use. See PostCondition 'operator' JavaDoc for documentation.
     * @param value String for which the defined (above) operator is to act on.
     *              Optional, depending on the operator used.
     */
    public PostCondition(String property, String operator, String value) {
        super();
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    /**
     * Full PostCondition constructor with all fields.
     * @param property IGC Resource property to consider
     *              I.E: "name", "labels.name", "modified_on", "_id", "parent_category._id", etc...
     * @param operator Search operator to use. See PostCondition 'operator' JavaDoc for documentation.
     * @param value String for which the defined (above) operator is to act on.
     *              Optional, depending on the operator used.
     * @param negated Optional - negates the search operator.
     * @param min Optional - Sets min bound for 'between' operator.
     * @param max Optional - Sets max bound for 'between' operator.
     */
    public PostCondition(String property, String operator, String value, boolean negated, long min, long max) {
        super();
        this.property = property;
        this.operator = operator;
        this.value = value;
        this.negated = negated;
        this.min = min;
        this.max = max;
    }

    //// GETTERS AND SETTERS ////


    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(boolean negated) {
        this.negated = negated;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    //// END GETTERS AND SETTERS ////

    @Override
    public String toString() {
        Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printer
        return gsonPrinter.toJson(this);
    }
}
