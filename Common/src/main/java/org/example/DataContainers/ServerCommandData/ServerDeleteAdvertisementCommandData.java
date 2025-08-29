package org.example.DataContainers.ServerCommandData;

import org.example.DataContainers.UserData;

public class ServerDeleteAdvertisementCommandData extends AbstractServerCommandData {
    private Integer advertisementId;
    private UserData user;
    private boolean isDeleted = false;

    public ServerDeleteAdvertisementCommandData(Integer advertisementId, UserData user) {
        this.advertisementId = advertisementId;
        this.user = user;
    }

    public void setSuccessState(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getSuccessState() {
        return this.isDeleted;
    }

    public UserData getUser() {
        return user;
    }

    public Integer getAdvertisementId() {
        return advertisementId;
    }

    public String getCommandName() {
        return "deleteAdvertisement";
    }
}
