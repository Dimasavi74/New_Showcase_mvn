package org.example.DataContainers.ServerCommandData;

import org.example.DataContainers.UserData;

public class ServerAddFavouriteCommandData extends AbstractServerCommandData {
    private Integer advertisementId;
    private UserData user;
    private boolean isAdded = false;

    public ServerAddFavouriteCommandData(Integer advertisementId, UserData user) {
        this.advertisementId = advertisementId;
        this.user = user;
    }

    public boolean getSuccessState() {
        return isAdded;
    }

    public UserData getUser() {
        return user;
    }

    public Integer getAdvertisementId() {
        return advertisementId;
    }

    public String getCommandName() {
        return "addFavourite";
    }

    public void setSuccessState(boolean state) {
        this.isAdded = state;
    }
}
