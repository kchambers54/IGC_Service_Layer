package com.dataObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Java Bean class to hold the properties of an "Item" returned from the
 * IGC API.
 * These items are returned as references to Terms and Categories, and are used
 * in "_context", "category_path", "has_type", "parent_category", (etc...)
 * fields in API JSON response.
 */
public class IGCItem implements Serializable {

    private String _name;
    private ArrayList<IGCItem> _context; //Optional field used in search result items.
    private String _type;
    private String _id;
    private String _url;

    // Optional Fields used for certain POST search results.
    private String modified_on;
    private String short_description;

    /**
     * No argument Constructor.
     */
    public IGCItem() {
        super();
    }

    /** Create a new IGCItem with only an _id.
     *
     * @param _id ID of resource represented by this IGCItem.
     */
    public IGCItem(String _id) {
        this._id = _id;
    }

    /**
     * Create a new IGCItem with all fields.
     *
     * @param _name Name of resource.
     * @param _context Resource's context (parent categories).
     * @param _type Type of resource (category, api, etc).
     * @param _id ID of resource represented by this IGCItem.
     * @param _url URL at which this resource can be found in the IGC API.
     */
    public IGCItem(String _name, ArrayList<IGCItem> _context, String _type, String _id, String _url) {
        this._name = _name;
        this._context = _context;
        this._type = _type;
        this._id = _id;
        this._url = _url;
    }

    //// GETTERS AND SETTERS ////

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public ArrayList<IGCItem> get_context() {
        return _context;
    }

    public void set_context(ArrayList<IGCItem> _context) {
        this._context = _context;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    //// END GETTERS AND SETTERS ////

    @Override
    public String toString() {
        Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printer
        return gsonPrinter.toJson(this);
    }
}
