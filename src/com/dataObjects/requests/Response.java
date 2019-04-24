package com.dataObjects.requests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;

/**
 * Java Bean class to hold both an HTTP message message and an IGC resource ID.
 * This class is used as the return type for HTTP Requests that include an IGCResource
 * used in the body of the request.
 *
 * This is so that when POSTing a new resource, the newly generated ID of that resource is returned
 * as "_id" in addition to any message message.
 */
public class Response implements Serializable {
    /**
     * Response message of the HTTP request.
     */
    private String message;
    /**
     * ID of any newly created resource.
     */
    private String _id;

    /**
     * Response code for HTTP request.
     */
    private int responseCode;

    private String codeMessage;

    public Response(String message, String _id, int responseCode, String codeMessage) {
        this.message = message;
        this._id = _id;
        this.responseCode = responseCode;
        this.codeMessage = codeMessage;
    }

    //// GETTERS AND SETTERS ////


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getCodeMessage() {
        return codeMessage;
    }

    public void setCodeMessage(String codeMessage) {
        this.codeMessage = codeMessage;
    }

    //// END GETTERS AND SETTERS ////

    /**
     * Coverts object to a JSON String.
     *
     * @return Json String of object.
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
