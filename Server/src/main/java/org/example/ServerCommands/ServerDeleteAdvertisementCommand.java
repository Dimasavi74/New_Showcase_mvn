package org.example.ServerCommands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerDeleteAdvertisementCommandData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerDeleteAdvertisementCommand extends AbstractServerCommand {
    private Integer advertisementId;
    private UserData user;
    private boolean isDeleted = false;

    public ServerDeleteAdvertisementCommand() {}

    public ServerDeleteAdvertisementCommand(Integer advertisementId, UserData user) {
        this.advertisementId = advertisementId;
        this.user = user;
    }

    public void execute() {
        try {
            this.isDeleted = this.bdManager.deleteAdvertisement(advertisementId, user);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public boolean getSuccessState() {
        return this.isDeleted;
    }

    public ServerDeleteAdvertisementCommand setDataByFields(ServerCommandData data) {
        ServerDeleteAdvertisementCommandData specialisedData = (ServerDeleteAdvertisementCommandData) data;
        this.advertisementId = specialisedData.getAdvertisementId();
        this.user = specialisedData.getUser();
        return this;
    }

    public ServerDeleteAdvertisementCommandData generateServerCommandData() {
        ServerDeleteAdvertisementCommandData commandData = new ServerDeleteAdvertisementCommandData(this.advertisementId, this.user);
        commandData.setSuccessState(isDeleted);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
