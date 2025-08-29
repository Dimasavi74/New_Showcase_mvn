package org.example.ServerCommands;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.ServerCommandData.ServerAddFavouriteCommandData;
import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerCreateAdvertisementCommandData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerCreateAdvertisementCommand extends AbstractServerCommand {
    private AdvertisementData advertisement;
    private UserData user;
    private boolean isCreated = false;

    public ServerCreateAdvertisementCommand() {}

    public ServerCreateAdvertisementCommand(AdvertisementData advertisement, UserData user) {
        this.advertisement = advertisement;
        this.user = user;
    }

    public void execute() {
        try {
            this.isCreated = this.bdManager.createAdvertisement(advertisement, user);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public boolean getSuccessState() {
        return this.isCreated;
    }

    public ServerCreateAdvertisementCommand setDataByFields(ServerCommandData data) {
        ServerCreateAdvertisementCommandData specialisedData = (ServerCreateAdvertisementCommandData) data;
        this.advertisement = specialisedData.getAdvertisement();
        this.user = specialisedData.getUser();
        return this;
    }

    public ServerCreateAdvertisementCommandData generateServerCommandData() {
        ServerCreateAdvertisementCommandData commandData = new ServerCreateAdvertisementCommandData(this.advertisement, this.user);
        commandData.setSuccessState(isCreated);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
