package org.example.DataContainers.ServerCommandData;

import org.example.DataContainers.AdvertisementData;

public class ServerSearchCommandData extends AbstractServerCommandData {
    private String[] words;
    private String[] tags;
    private Integer minPrice = 0;
    private Integer maxPrice;
    private Integer advertisementId;
    private AdvertisementData[] foundAdvertisements;

    public ServerSearchCommandData(Integer advertisementId, String[] words, String[] tags, Integer minPrice, Integer maxPrice) {
        this.advertisementId = advertisementId;
        this.words = words;
        this.tags = tags;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public AdvertisementData[] getFoundAdvertisements() {
        return this.foundAdvertisements;
    }

    public String[] getWords() {
        return words;
    }

    public String[] getTags() {
        return tags;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public Integer getAdvertisementId() {
        return advertisementId;
    }

    public String getCommandName() {
        return "search";
    }

    public void setFoundAdvertisements(AdvertisementData[] foundAdvertisements) {
        this.foundAdvertisements = foundAdvertisements;
    }
}
