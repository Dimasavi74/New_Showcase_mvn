package org.example.DataContainers.ServerCommandData;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;

public class ServerCreateAdvertisementCommandData extends AbstractServerCommandData {
    private AdvertisementData advertisement;
    private UserData user;
    private boolean isCreated = false;

    public ServerCreateAdvertisementCommandData(AdvertisementData advertisement, UserData user) {
        this.advertisement = advertisement;
        this.user = user;
    }

    public void setSuccessState(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public boolean getSuccessState() {
        return this.isCreated;
    }

    public UserData getUser() {
        return user;
    }

    public AdvertisementData getAdvertisement() {
        return advertisement;
    }

    public String getCommandName() {
        return "createAdvertisement";
    }
}
