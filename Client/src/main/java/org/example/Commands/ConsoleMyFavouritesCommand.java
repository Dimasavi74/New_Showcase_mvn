package org.example.Commands;

import org.example.DataContainers.AdvertisementWithIdData;
import org.example.DataContainers.ServerCommandData.AbstractServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerMyFavouritesCommandData;
import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleOutputModule;
import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;

import java.util.Arrays;
import java.util.Map;

public class ConsoleMyFavouritesCommand extends AbstractConsoleCommand {
    private UserData user;
    private ClientCommunicationModule communicationModule;

    public ConsoleMyFavouritesCommand(ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule, UserData user){
        this.user = user;
        this.communicationModule = communicationModule;
        this.outputModule = outputModule;
    }

    public void execute() throws Exception {
        ServerMyFavouritesCommandData command = null;
        if (!this.user.getLoginState()) {
            outputModule.outputLine("Для вывода понравившихся объявлений необходимо выполнить вход в систему (команда /login)");
            return;
        }
        ServerCommandData undefinedCommand = communicationModule.executeCommand(new ServerMyFavouritesCommandData(this.user));
        if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerMyFavouritesCommandData) undefinedCommand;
        }
        if (command.getFoundAdvertisements() != null && !Arrays.equals(command.getFoundAdvertisements(), new AdvertisementData[]{})) {
            outputModule.outputLine("Понравившиеся объявления:");
            for (AdvertisementData advertisement: command.getFoundAdvertisements()) {
                outputModule.outputLine("(" + ((AdvertisementWithIdData) advertisement).getId() + ") " + advertisement.getTitle() + " " + advertisement.getPrice());
            }
        } else {
            outputModule.outputLine("Вы еще не добавили ни одного объявления! (команда /addFavourite)");
        }
    }

    public void clear(){
        return;
    }

    public boolean isReady(){
        return true;
    }

    public void collectData(){
        return;
    }

    public void putData(Map<String, String> data) {
        return;
    }

    public String getManual(){
        return "Выводит список понравившихся объявлений";
    }
}
