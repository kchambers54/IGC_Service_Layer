package com.dataObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

/**
 * Contains paging info and a list of MiniIGCItems.
 * Used to store info for JSON fields "category_path" and "has_types".
 */
public class IGCItemList {

    /**
     * Object containing paging data for this IGCItemList.
     */
    private IGCPaging paging;
    /**
     * ArrayList of IGCItems.
     */
    private ArrayList<IGCItem> items;

    /**
     * Set to empty to note that the IGCItemList is empty.
     */
    private boolean failedResponse;


    /**
     * Create an empty IGCItemList.
     * @param failedResponse Indicates whether this IGCITemList represents a failed response, and contains no items.
     */
    public IGCItemList(boolean failedResponse) {
        this.failedResponse = failedResponse;
        this.paging = new IGCPaging(0, "", 0, 0, 0); //Build empty paging info.
        this.items = new ArrayList<>(); //Initialize empty ArrayList.
    }

    /**
     * Create a new populated IGCItemList.
     * @param paging Paging info object.
     * @param items ArrayList of IGCItems.
     */
    public IGCItemList(IGCPaging paging, ArrayList<IGCItem> items) {
        this.paging = paging;
        this.items = items;
    }

    //// GETTERS AND SETTERS ////

    public IGCPaging getPaging() {
        return paging;
    }

    public void setPaging(IGCPaging paging) {
        this.failedResponse = false;
        this.paging = paging;
    }

    public ArrayList<IGCItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<IGCItem> items) {
        this.failedResponse = false;
        this.items = items;
    }

    public boolean isFailedResponse() {
        return failedResponse;
    }

    public void setFailedResponse(boolean failedResponse) {
        this.failedResponse = failedResponse;
    }

    //// END GETTERS AND SETTERS ////

    /**
     * Converts IGCItemList object into JSON String using GSON library.
     * This allows for readable printing of the object.
     *
     * @return Pretty Printed String for IGCItemList object.
     */
    @Override
    public String toString() {
        Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printer
        return gsonPrinter.toJson(this);
    }

    public JsonObject toJsonObject() {
        JsonParser parser = new JsonParser();
        return parser.parse(this.toString()).getAsJsonObject();
    }
}
