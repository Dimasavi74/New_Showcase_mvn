package org.example.DataContainers.ServerCommandData;

import org.example.DataContainers.UserData;

public class ServerRemoveFavouriteCommandData extends AbstractServerCommandData {
    private Integer advertisementId;
    private UserData user;
    private boolean isDeleted = false;

    public ServerRemoveFavouriteCommandData(Integer advertisementId, UserData user) {
        this.advertisementId = advertisementId;
        this.user = user;
    }

    public void setSuccessState(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getSuccessState() {
        return isDeleted;
    }

    public Integer getAdvertisementId() {
        return advertisementId;
    }

    public UserData getUser() {
        return user;
    }

    public String getCommandName() {
        return "removeFavourite";
    }
}
