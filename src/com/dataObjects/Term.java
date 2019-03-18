package com.dataObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Java Bean class to hold the properties of a Term returned from the IGC API.
 */
public class Term extends IGCResource implements Serializable {

    /**
     * Type of IGC resource.  Should always be "term".
     */
    private String _type;

    /**
     * Name of term
     */
    private String name;
    /**
     * Short description of term.
     */
    private String short_description;
    /**
     * Long description of term.
     */
    private String long_description;
    /**
     * Parent category of term. If NULL, this will be an orphan term.
     */
    private IGCItem parent_category;
    /**
     * Status of term: ACCEPTED, CANDIDATE, STANDARD, etc... (Can be changed to ENUM when options defined).
     */
    private String status;

    // Optional Fields?
    private String abbreviation;
    private String additional_abbreviation;
    private String example;
    private IGCItemList stewards;
    private String usage;
    private IGCItemList has_types; //TODO - What is this?
    private IGCItemList is_a_type_of; //TODO - What is this?

    // Custom Fields - TODO: Should allow for more to be added. Not uniform across current terms.
    //TODO - Perhaps make an object that contains all of these?
    private String custom_Owner;
    private ArrayList<String> custom_Organization;
    private String custom_Database_Name;
    private ArrayList<String> custom_Business_Types;

    // Possibly only set within API.
    private String type; //TODO - What is this?
    private String is_modifier;

    // Only set within IGC API, never by constructors.
    private String modified_on;
    private String _name;
    private String created_by;
    private String created_on;
    private ArrayList<IGCItem> _context;
    private IGCItemList category_path;
    private String modified_by;
    private String _id;
    private String _url;

    /**
     * No argument Constructor.
     */
    public Term() {
        super();
        this._type = "term";
    }

    /**
     * Create a basic term.
     *
     * @param name Name of the term.
     * @param short_description Short description of the term.
     * @param long_description Long description of the term.
     * @param parent_category_id ID of parent category.
     * @param status Status of the term (ACCEPTED, CANDIDATE, STANDARD, etc).
     */
    public Term(String name, String short_description, String long_description, String parent_category_id,
                String status) {
        super();
        this._type = "term";
        this.name = name;
        this.short_description = short_description;
        this.long_description = long_description;
        this.parent_category = new IGCItem(parent_category_id);
        this.status = status;
    }

    //// GETTERS AND SETTERS ////

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

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String getCustom_Owner() {
        return custom_Owner;
    }

    public void setCustom_Owner(String custom_Owner) {
        this.custom_Owner = custom_Owner;
    }

    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getIs_modifier() {
        return is_modifier;
    }

    public void setIs_modifier(String is_modifier) {
        this.is_modifier = is_modifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public ArrayList<IGCItem> get_context() {
        return _context;
    }

    public void set_context(ArrayList<IGCItem> _context) {
        this._context = _context;
    }

    public IGCItemList getCategory_path() {
        return category_path;
    }

    public void setCategory_path(IGCItemList category_path) {
        this.category_path = category_path;
    }

    public IGCItemList getHas_types() {
        return has_types;
    }

    public void setHas_types(IGCItemList has_types) {
        this.has_types = has_types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IGCItem getParent_category() {
        return parent_category;
    }

    public void setParent_category(IGCItem parent_category) {
        this.parent_category = parent_category;
    }

    public String getAdditional_abbreviation() {
        return additional_abbreviation;
    }

    public void setAdditional_abbreviation(String additional_abbreviation) {
        this.additional_abbreviation = additional_abbreviation;
    }

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public ArrayList<String> getCustom_Organization() {
        return custom_Organization;
    }

    public void setCustom_Organization(ArrayList<String> custom_Organization) {
        this.custom_Organization = custom_Organization;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public IGCItemList getIs_a_type_of() {
        return is_a_type_of;
    }

    public void setIs_a_type_of(IGCItemList is_a_type_of) {
        this.is_a_type_of = is_a_type_of;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public IGCItemList getStewards() {
        return stewards;
    }

    public void setStewards(IGCItemList stewards) {
        this.stewards = stewards;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getCustom_Database_Name() {
        return custom_Database_Name;
    }

    public void setCustom_Database_Name(String custom_Database_Name) {
        this.custom_Database_Name = custom_Database_Name;
    }

    public ArrayList<String> getCustom_Business_Types() {
        return custom_Business_Types;
    }

    public void setCustom_Business_Types(ArrayList<String> custom_Business_Types) {
        this.custom_Business_Types = custom_Business_Types;
    }

    //// END GETTERS AND SETTERS ////

    /**
     * Converts Term object into JSON String using GSON library.
     * This allows for readable printing of the object.
     *
     * @return Pretty Printed String for Term object.
     */
    @Override
    public String toString() {
        Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printer
        return gsonPrinter.toJson(this);  //Isn't this great?
    }
}
