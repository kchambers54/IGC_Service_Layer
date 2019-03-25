package com.dataObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

/**
 * Java Bean class for storing paging info within an IGCItemList.
 */
public class IGCPaging implements Serializable {

    /**
     * Total number of items in associated list.
     */
    private int numTotal;
    /**
     * link to next page.
     */
    private String next;
    /**
     * Page size.
     */
    private int pageSize;
    /**
     * Index of last item on this page.
     */
    private int end;
    /**
     * Index of first item on this page.
     */
    private int begin;

    /**
     * No argument constructor
     */
    public IGCPaging() {
        super();
    }

    public IGCPaging(int numTotal, String next, int pageSize, int end, int begin) {
        this.numTotal = numTotal;
        this.next = next;
        this.pageSize = pageSize;
        this.end = end;
        this.begin = begin;
    }

    //// GETTERS AND SETTERS ////


    public int getNumTotal() {
        return numTotal;
    }

    public void setNumTotal(int numTotal) {
        this.numTotal = numTotal;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    ////END GETTERS AND SETTERS ////

    /**
     * Converts IGCPaging object into JSON String using GSON library.
     * This allows for readable printing of the object.
     *
     * @return Pretty Printed String for IGCPaging object.
     */
    @Override
    public String toString() {
        Gson gsonPrinter = new GsonBuilder().setPrettyPrinting().create(); //Pretty Printer
        return gsonPrinter.toJson(this);
    }
}
