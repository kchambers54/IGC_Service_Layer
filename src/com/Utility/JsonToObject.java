package com.Utility;

import com.dataObjects.Category;
import com.dataObjects.IGCItemList;
import com.dataObjects.Term;
import com.google.gson.Gson;

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
     */
    public static Term toTerm(String jsonString) throws Exception {

        Gson gson = new Gson();

        try {
            System.out.println("Converting json String to Term object...");
            return gson.fromJson(jsonString, Term.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("json to Term conversion failed.\n");
            throw e;
        }
    }

    /**
     * Converts a JSON String to an IGC Category object using the GSON library.
     *
     * @param jsonString A JSON String to convert (must be correctly formatted).
     * @return An IGC Category object containing the properties of the input JSON.
     * @throws Exception:
     */
    public static Category toCategory(String jsonString) throws Exception {

        Gson gson = new Gson();

        try {
            System.out.println("Converting json String to Category object...");
            return gson.fromJson(jsonString, Category.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("json to Category conversion failed.\n");
            throw e;
        }
    }

    /**
     * Converts a JSON String to a IGCItemList object using the GSON library.
     *
     * @param jsonString A JSON String to convert (must be correctly formatted).
     * @return A IGCItemList object containing the properties of the input JSON.
     * @throws Exception:
     */
    public static IGCItemList toIGCItemList(String jsonString) throws Exception {

        Gson gson = new Gson();

        try {
            System.out.println("Converting json String to IGCItemList object...");
            return gson.fromJson(jsonString, IGCItemList.class);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("json to IGCItemList conversion failed.\n");
            throw e;
        }
    }
}
