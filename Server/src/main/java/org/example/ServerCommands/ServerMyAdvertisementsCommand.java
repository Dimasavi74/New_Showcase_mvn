package org.example.ServerCommands;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerLoginCommandData;
import org.example.DataContainers.ServerCommandData.ServerMyAdvertisementsCommandData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerMyAdvertisementsCommand extends AbstractServerCommand {
    private UserData user;
    private AdvertisementData[] foundAdvertisements;

    public ServerMyAdvertisementsCommand() {}

    public ServerMyAdvertisementsCommand(UserData user) {
        this.user = user;
    }

    public void execute() {
        try {
            this.foundAdvertisements = this.bdManager.userAdvertisements(user);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public AdvertisementData[] getFoundAdvertisements() {
        return this.foundAdvertisements;
    }

    public ServerMyAdvertisementsCommand setDataByFields(ServerCommandData data) {
        ServerMyAdvertisementsCommandData specialisedData = (ServerMyAdvertisementsCommandData) data;
        this.user = specialisedData.getUser();
        return this;
    }

    public ServerMyAdvertisementsCommandData generateServerCommandData() {
        ServerMyAdvertisementsCommandData commandData = new ServerMyAdvertisementsCommandData(this.user);
        commandData.setFoundAdvertisements(this.foundAdvertisements);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
