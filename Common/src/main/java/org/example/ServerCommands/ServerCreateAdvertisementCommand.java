package org.example.ServerCommands;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerCreateAdvertisementCommand extends AbstractServerCommand {
    private AdvertisementData advertisement;
    private UserData user;
    private boolean isCreated = false;

    public ServerCreateAdvertisementCommand(AdvertisementData advertisement, UserData user) {
        this.advertisement = advertisement;
        this.user = user;
    }

    public void execute() {
        try {
            this.isCreated = this.bdManager.createAdvertisement(advertisement, user);
        } catch (SQLException e) {
            this.setError(e);
        }
    }

    public boolean getSuccessState() {
        return this.isCreated;
    }
}
