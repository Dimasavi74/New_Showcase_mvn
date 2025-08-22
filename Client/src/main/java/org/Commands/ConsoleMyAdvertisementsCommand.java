package org.Commands;

import org.ClientProperties;
import org.Modules.ClientCommunicationModule;
import org.Modules.ConsoleOutputModule;
import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;
import org.example.ServerCommands.ServerCommand;
import org.example.ServerCommands.ServerLoginCommand;
import org.example.ServerCommands.ServerMyAdvertisementsCommand;
import org.example.ServerCommands.ServerSearchCommand;

import java.util.Arrays;
import java.util.Map;

public class ConsoleMyAdvertisementsCommand extends AbstractConsoleCommand {
    private UserData user;
    private ClientCommunicationModule communicationModule;

    public ConsoleMyAdvertisementsCommand(ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule, UserData user){
        this.user = user;
        this.communicationModule = communicationModule;
        this.outputModule = outputModule;
    }

    public void execute() throws Exception {
        ServerMyAdvertisementsCommand command = null;
        if (!this.user.isLogged) {
            outputModule.outputLine("Для вывода ваших объявлений необходимо выполнить вход в систему (команда /login)");
            return;
        }
        ServerCommand undefinedCommand = communicationModule.executeCommand(new ServerMyAdvertisementsCommand(this.user));
        if (undefinedCommand.getError() != null) {
            throw undefinedCommand.getError();
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerMyAdvertisementsCommand) undefinedCommand;
        }
        if (command.getFoundAdvertisements() != null && !Arrays.equals(command.getFoundAdvertisements(), new AdvertisementData[]{})) {
            outputModule.outputLine("Ваши объявления:");
            for (AdvertisementData advertisement: command.getFoundAdvertisements()) {
                outputModule.outputLine("(" + advertisement.id + ") " + advertisement.title + " " + advertisement.price);
            }
        } else {
            outputModule.outputLine("Вы еще не создали ни одного объявления! (команда /createAdvertisement)");
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
        return "Выводит список ваших объявлений";
    }
}
