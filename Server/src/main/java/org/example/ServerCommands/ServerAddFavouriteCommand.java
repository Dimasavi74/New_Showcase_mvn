package org.example.ServerCommands;

import org.example.DataContainers.ServerCommandData.ServerAddFavouriteCommandData;
import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerAddFavouriteCommand extends AbstractServerCommand {
    private Integer advertisementId;
    private UserData user;
    private boolean isAdded = false;

    public ServerAddFavouriteCommand() {}

    public ServerAddFavouriteCommand(Integer advertisementId, UserData user) {
        this.advertisementId = advertisementId;
        this.user = user;
    }

    public void execute() {
        try {
            this.isAdded = this.bdManager.addFavourite(user, advertisementId);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public boolean getSuccessState() {
        return this.isAdded;
    }

    public ServerAddFavouriteCommand setDataByFields(ServerCommandData data) {
        ServerAddFavouriteCommandData specialisedData = (ServerAddFavouriteCommandData) data;
        this.advertisementId = specialisedData.getAdvertisementId();
        this.user = specialisedData.getUser();
        return this;
    }

    public ServerAddFavouriteCommandData generateServerCommandData() {
        ServerAddFavouriteCommandData commandData = new ServerAddFavouriteCommandData(this.advertisementId, this.user);
        commandData.setSuccessState(isAdded);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
