package com.dataObjects;

import java.util.ArrayList;

/**
 * Abstract class used to have a common parent class to refer to all IGC resouce types (term, category, etc...)
 * Used as a place holder when any IGCResource may be taken as an argument (see URLConnection.createIGCResource).
 * TODO - May need to remove content from this class as new child classes with less in common are added.
 *      - Currently added all common getters and setters (for Terms and Categories).
 */
public abstract class IGCResource {

    abstract public String getModified_on();

    abstract public void setModified_on(String modified_on);

    abstract public String getShort_description();

    abstract public void setShort_description(String short_description);

    abstract public String get_name();

    abstract public void set_name(String _name);

    abstract public String get_type();

    abstract public void set_type(String _type);

    abstract public String getCustom_Owner();

    abstract public void setCustom_Owner(String custom_Owner);

    abstract public String getLong_description();

    abstract public void setLong_description(String long_description);

    abstract public String getCreated_by();

    abstract public void setCreated_by(String created_by);

    abstract public String getCreated_on();

    abstract public void setCreated_on(String created_on);

    abstract public ArrayList<IGCItem> get_context();

    abstract public void set_context(ArrayList<IGCItem> _context);

    abstract public IGCItemList getCategory_path();

    abstract public void setCategory_path(IGCItemList category_path);

    abstract public String getName();

    abstract public void setName(String name);

    abstract public IGCItem getParent_category();

    abstract public void setParent_category(IGCItem parent_category);

    abstract public String get_id();

    abstract public void set_id(String _id);

    abstract public String get_url();

    abstract public void set_url(String _url);
}
