package org.example.ServerCommands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerRemoveFavouriteCommandData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerRemoveFavouriteCommand extends AbstractServerCommand {
    private Integer advertisementId;
    private UserData user;
    private boolean isDeleted = false;

    public ServerRemoveFavouriteCommand() {}

    public ServerRemoveFavouriteCommand(Integer advertisementId, UserData user) {
        this.advertisementId = advertisementId;
        this.user = user;
    }

    public void execute() {
        try {
            this.isDeleted = this.bdManager.removeFavourite(user, advertisementId);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public boolean getSuccessState() {
        return this.isDeleted;
    }

    public ServerRemoveFavouriteCommand setDataByFields(ServerCommandData data) {
        ServerRemoveFavouriteCommandData specialisedData = (ServerRemoveFavouriteCommandData) data;
        this.advertisementId = specialisedData.getAdvertisementId();
        this.user = specialisedData.getUser();
        return this;
    }

    public ServerRemoveFavouriteCommandData generateServerCommandData() {
        ServerRemoveFavouriteCommandData commandData = new ServerRemoveFavouriteCommandData(this.advertisementId, this.user);
        commandData.setSuccessState(this.isDeleted);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
