package org.example.DataContainers;

import java.io.Serializable;

public class AdvertisementData implements Serializable {
    protected String title;
    protected String description;
    protected Integer price;
    protected String contacts;
    protected String[] tags;


    public AdvertisementData(String title, String description, int price, String contacts, String[] tags) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.contacts = contacts;
        this.tags = tags;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Integer getPrice() {
        return this.price;
    }

    public String getContacts() {
        return this.contacts;
    }

    public String[] getTags() {
        return this.tags;
    }
}