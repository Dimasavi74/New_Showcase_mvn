package org.example.ServerCommands;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerMyAdvertisementsCommand extends AbstractServerCommand {
    private UserData user;
    private AdvertisementData[] foundAdvertisements;

    public ServerMyAdvertisementsCommand(UserData user) {
        this.user = user;
    }

    public void execute() {
        try {
            this.foundAdvertisements = this.bdManager.userAdvertisements(user);
        } catch (SQLException e) {
            this.setError(e);
        }
    }

    public AdvertisementData[] getFoundAdvertisements() {
        return this.foundAdvertisements;
    }
}
