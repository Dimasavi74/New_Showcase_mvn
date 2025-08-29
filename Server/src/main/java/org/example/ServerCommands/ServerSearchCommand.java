package org.example.ServerCommands;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerRemoveFavouriteCommandData;
import org.example.DataContainers.ServerCommandData.ServerSearchCommandData;

import java.sql.SQLException;

public class ServerSearchCommand extends AbstractServerCommand {
    private String[] words;
    private String[] tags;
    private Integer minPrice = 0;
    private Integer maxPrice;
    private Integer advertisementId;
    private AdvertisementData[] foundAdvertisements;

    public ServerSearchCommand() {}

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
            this.setErrorMessage(e.getMessage());
        }
    }

    public AdvertisementData[] getFoundAdvertisements() {
        return this.foundAdvertisements;
    }

    public ServerSearchCommand setDataByFields(ServerCommandData data) {
        ServerSearchCommandData specialisedData = (ServerSearchCommandData) data;
        this.advertisementId = specialisedData.getAdvertisementId();
        this.words = specialisedData.getWords();
        this.tags = specialisedData.getTags();
        this.minPrice = specialisedData.getMinPrice();
        this.maxPrice = specialisedData.getMaxPrice();
        return this;
    }

    public ServerSearchCommandData generateServerCommandData() {
        ServerSearchCommandData commandData = new ServerSearchCommandData(this.advertisementId, this.words, this.tags, this.minPrice, this.maxPrice);
        commandData.setFoundAdvertisements(this.foundAdvertisements);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
