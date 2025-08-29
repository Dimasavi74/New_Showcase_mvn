package org.example.DataContainers.ServerCommandData;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;

public class ServerMyAdvertisementsCommandData extends AbstractServerCommandData {
    private UserData user;
    private AdvertisementData[] foundAdvertisements;

    public ServerMyAdvertisementsCommandData(UserData user) {
        this.user = user;
    }

    public AdvertisementData[] getFoundAdvertisements() {
        return this.foundAdvertisements;
    }

    public UserData getUser() {
        return user;
    }

    public String getCommandName() {
        return "myAdvertisements";
    }

    public void setFoundAdvertisements(AdvertisementData[] foundAdvertisements) {
        this.foundAdvertisements = foundAdvertisements;
    }
}
