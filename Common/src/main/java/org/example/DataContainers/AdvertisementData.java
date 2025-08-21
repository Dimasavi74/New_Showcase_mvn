package org.example.DataContainers;

import java.io.Serializable;

public class AdvertisementData implements Serializable {
    public Integer id;
    public String title;
    public String description;
    public Integer price;
    public String contacts;
    public String[] tags;

    public AdvertisementData(Integer id, String title, String description, int price, String contacts, String[] tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.contacts = contacts;
        this.tags = tags;
    }

    public AdvertisementData(String title, String description, int price, String contacts, String[] tags) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.contacts = contacts;
        this.tags = tags;
    }
}