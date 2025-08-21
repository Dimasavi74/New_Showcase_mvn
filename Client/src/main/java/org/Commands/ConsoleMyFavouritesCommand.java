package org.Commands;

import org.Modules.ClientCommunicationModule;
import org.Modules.ConsoleOutputModule;
import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;
import org.example.ServerCommands.AbstractServerCommand;
import org.example.ServerCommands.ServerCommand;
import org.example.ServerCommands.ServerMyAdvertisementsCommand;
import org.example.ServerCommands.ServerMyFavouritesCommand;

import java.util.Arrays;
import java.util.Map;

public class ConsoleMyFavouritesCommand extends AbstractConsoleCommand {
    private UserData user;
    private ClientCommunicationModule communicationModule;
    private ConsoleOutputModule outputModule;

    public ConsoleMyFavouritesCommand(ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule, UserData user){
        this.user = user;
        this.communicationModule = communicationModule;
        this.outputModule = outputModule;
    }

    public void execute() {
        ServerMyFavouritesCommand command = null;
        if (!this.user.isLogged) {
            outputModule.outputLine("Для вывода понравившихся объявлений необходимо выполнить вход в систему (команда /login)");
            return;
        }
        try {
            ServerCommand undefinedCommand = communicationModule.executeCommand(new ServerMyFavouritesCommand(this.user));
            if (undefinedCommand.getError() != null) {
                throw undefinedCommand.getError();
            } else {
                command = (ServerMyFavouritesCommand) undefinedCommand;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (command.getFoundAdvertisements() != null && !Arrays.equals(command.getFoundAdvertisements(), new AdvertisementData[]{})) {
            outputModule.outputLine("Понравившиеся объявления:");
            for (AdvertisementData advertisement: command.getFoundAdvertisements()) {
                outputModule.outputLine("(" + advertisement.id + ") " + advertisement.title + " " + advertisement.price);
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
