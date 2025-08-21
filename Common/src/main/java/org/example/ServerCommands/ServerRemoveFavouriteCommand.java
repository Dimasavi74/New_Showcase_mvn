package org.example.ServerCommands;

import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerRemoveFavouriteCommand extends AbstractServerCommand {
    private Integer advertisementId;
    private UserData user;
    private boolean isDeleted = false;

    public ServerRemoveFavouriteCommand(Integer advertisementId, UserData user) {
        this.advertisementId = advertisementId;
        this.user = user;
    }

    public void execute() {
        try {
            this.isDeleted = this.bdManager.removeFavourite(user, advertisementId);
        } catch (SQLException e) {
            this.setError(e);
        }
    }

    public boolean getSuccessState() {
        return this.isDeleted;
    }
}
