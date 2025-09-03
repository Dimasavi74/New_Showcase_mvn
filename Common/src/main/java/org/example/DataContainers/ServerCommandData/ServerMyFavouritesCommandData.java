package org.example.DataContainers.ServerCommandData;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;

public class ServerMyFavouritesCommandData extends AbstractServerCommandData {
    private UserData user;
    private AdvertisementData[] foundAdvertisements;

    public ServerMyFavouritesCommandData(UserData user) {
        this.user = user;
    }

    public AdvertisementData[] getFoundAdvertisements() {
        return this.foundAdvertisements;
    }

    public UserData getUser() {
        return user;
    }

    public String getCommandName() {
        return "myFavourites";
    }

    public void setFoundAdvertisements(AdvertisementData[] foundAdvertisements) {
        this.foundAdvertisements = foundAdvertisements;
    }
}
