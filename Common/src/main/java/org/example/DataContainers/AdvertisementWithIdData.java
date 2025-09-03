package org.example.DataContainers;

public class AdvertisementWithIdData extends AdvertisementData {
    protected Integer id;

    public AdvertisementWithIdData(Integer id, String title, String description, int price, String contacts, String[] tags) {
        super(title, description, price, contacts, tags);
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }
}
