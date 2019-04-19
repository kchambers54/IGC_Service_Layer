package com.dataObjects;

/**
 * A simple class that only holds a single String: '_type'.
 * This is used in the checking of a Resource's type.
 */
public class TypeContainer {
    private String _type;

    public TypeContainer(String _type) {
        this._type = _type;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }
}
