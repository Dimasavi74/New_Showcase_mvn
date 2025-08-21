package org.example.ServerCommands;

import org.example.DataContainers.AdvertisementData;

import java.sql.SQLException;

public class ServerSearchCommand extends AbstractServerCommand {
    private String[] words;
    private String[] tags;
    private Integer minPrice = 0;
    private Integer maxPrice;
    private Integer advertisementId;
    private AdvertisementData[] foundAdvertisements;

    public ServerSearchCommand(Integer advertisementId, String[] words, String[] tags, Integer minPrice, Integer maxPrice) {
        this.advertisementId = advertisementId;
        this.words = words;
        this.tags = tags;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public void execute() {
        try {
            this.foundAdvertisements = this.bdManager.search(advertisementId, words, tags, minPrice, maxPrice);
        } catch (SQLException e) {
            this.setError(e);
        }
    }

    public AdvertisementData[] getFoundAdvertisements() {
        return this.foundAdvertisements;
    }
}
