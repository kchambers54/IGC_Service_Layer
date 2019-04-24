package com.utility;

import com.dataObjects.*;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A utility class containing various methods for converting JSON to specific
 * IGC objects.
 */
public class JsonToObject {

    /**
     * SLF4J logger initialization.
     */
    private static final Logger logger = LoggerFactory.getLogger(JsonToObject.class);

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
            logger.debug("Converting json String to Term object...");
            return gson.fromJson(jsonString, Term.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            logger.error("JsonParseException in JsonToObject.toTerm().\n");
            throw new IllegalArgumentException("JsonParseException thrown by JsonToObject.toTerm()");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected Exception in JsonToObject.toTerm()");
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
            logger.debug("Converting json String to Category object...");
            return gson.fromJson(jsonString, Category.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            logger.error("JsonParseException in JsonToObject.toCategory().\n");
            throw new IllegalArgumentException("JsonParseException thrown by JsonToObject.toCategory()");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected Exception in JsonToObject.toCategory()");
            throw e;
        }
    }

    //TODO: Will need to create more methods when new Resource types are added.

    /**
     * Converts a JSON String to an IGCItem object using the GSON library.
     * Used when checking a Resource's type.
     * @param jsonString A JSON String to convert (must be correctly formatted).
     * @return An IGCItem object containing properties of the input JSON.
     * @throws IllegalArgumentException: JSON is not valid representation of a Term object.
     */
    public static TypeContainer toTypeContainer(String jsonString) throws IllegalArgumentException {

        Gson gson = new Gson();

        try {
            logger.debug("Converting json String to TypeContainer object...");
            return gson.fromJson(jsonString, TypeContainer.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            logger.error("JsonParseException in JsonToObject.toTypeContainer().\n");
            throw new IllegalArgumentException("JsonParseException thrown by JsonToObject.toTypeContainer()");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected Exception in JsonToObject.toTypeContainer()");
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
            logger.debug("Converting json String to IGCItemList object...");
            return gson.fromJson(jsonString, IGCItemList.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
            logger.error("JsonParseException in JsonToObject.toIGCItemList().\n");
            throw new IllegalArgumentException("JsonParseException thrown by JsonToObject.toIGCItemList()");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unexpected Exception in JsonToObject.toIGCItemList()");
            throw e;
        }
    }
}
