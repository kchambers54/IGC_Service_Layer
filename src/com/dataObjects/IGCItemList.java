package com.dataObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
     * No argument constructor.
     */
    public IGCItemList() {

    }

    //// GETTERS AND SETTERS ////

    public IGCPaging getPaging() {
        return paging;
    }

    public void setPaging(IGCPaging paging) {
        this.paging = paging;
    }

    public ArrayList<IGCItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<IGCItem> items) {
        this.items = items;
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
}
