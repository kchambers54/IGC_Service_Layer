package com.Utility;

import com.dataObjects.Category;
import com.dataObjects.IGCItemList;
import com.dataObjects.Term;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * A utility class containing various methods for converting JSON to specific
 * IGC objects.
 */
public class JsonToObject {

    /**
     * Converts a JSON String to an IGC Term object using the GSON library.
     *
     * @param jsonString A JSON String to convert (must be correctly formatted).
     * @return An IGC Term object containing the properties of the input JSON.
     * @throws IllegalArgumentException: JSON is not valid representation of a Term object.
     */
    public static Term toTerm(String jsonString) throws IllegalArgumentException {

        Gson gson = new Gson();

        try {
            System.out.println("Converting json String to Term object...");
            return gson.fromJson(jsonString, Term.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            System.err.println("JsonParseException in JsonToObject.toTerm().\n");
            throw new IllegalArgumentException(); //JsonParseException not checked, throw one that is.
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected Exception in JsonToObject.toTerm()");
            throw e;
        }
    }

    /**
     * Converts a JSON String to an IGC Category object using the GSON library.
     *
     * @param jsonString A JSON String to convert (must be correctly formatted).
     * @return An IGC Category object containing the properties of the input JSON.
     * @throws IllegalArgumentException: JSON is not valid representation of a Category object.
     */
    public static Category toCategory(String jsonString) throws IllegalArgumentException {

        Gson gson = new Gson();

        try {
            System.out.println("Converting json String to Category object...");
            return gson.fromJson(jsonString, Category.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            System.err.println("JsonParseException in JsonToObject.toCategory().\n");
            throw new IllegalArgumentException(); //JsonParseException not checked, throw one that is.
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected Exception in JsonToObject.toCategory()");
            throw e;
        }
    }

    /**
     * Converts a JSON String to a IGCItemList object using the GSON library.
     *
     * @param jsonString A JSON String to convert (must be correctly formatted).
     * @return A IGCItemList object containing the properties of the input JSON.
     * @throws IllegalArgumentException: JSON is not valid representation of a IGCItemList object.
     */
    public static IGCItemList toIGCItemList(String jsonString) throws IllegalArgumentException {

        Gson gson = new Gson();

        try {
            System.out.println("Converting json String to IGCItemList object...");
            return gson.fromJson(jsonString, IGCItemList.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            System.err.println("JsonParseException in JsonToObject.toIGCItemList().\n");
            throw new IllegalArgumentException();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unexpected Exception in JsonToObject.toIGCItemList()");
            throw e;
        }
    }
}
