package org.example.ServerCommands;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerMyFavouritesCommandData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public class ServerMyFavouritesCommand extends AbstractServerCommand {
    private UserData user;
    private AdvertisementData[] foundAdvertisements;

    public ServerMyFavouritesCommand() {}

    public ServerMyFavouritesCommand(UserData user) {
        this.user = user;
    }

    public void execute() {
        try {
            this.foundAdvertisements = this.bdManager.userFavourites(user);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public AdvertisementData[] getFoundAdvertisements() {
        return this.foundAdvertisements;
    }

    public ServerMyFavouritesCommand setDataByFields(ServerCommandData data) {
        ServerMyFavouritesCommandData specialisedData = (ServerMyFavouritesCommandData) data;
        this.user = specialisedData.getUser();
        return this;
    }

    public ServerMyFavouritesCommandData generateServerCommandData() {
        ServerMyFavouritesCommandData commandData = new ServerMyFavouritesCommandData(this.user);
        commandData.setFoundAdvertisements(this.foundAdvertisements);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
