package org.example.ServerCommands;

import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerAddFavouriteCommand extends AbstractServerCommand {
    private Integer advertisementId;
    private UserData user;
    private boolean isAdded = false;

    public ServerAddFavouriteCommand(Integer advertisementId, UserData user) {
        this.advertisementId = advertisementId;
        this.user = user;
    }

    public void execute() {
        try {
            this.isAdded = this.bdManager.addFavourite(user, advertisementId);
        } catch (SQLException e) {
            this.setError(e);
        }
    }

    public boolean getSuccessState() {
        return this.isAdded;
    }
}
