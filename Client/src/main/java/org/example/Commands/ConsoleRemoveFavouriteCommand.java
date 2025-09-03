package org.example.Commands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerDeleteAdvertisementCommandData;
import org.example.DataContainers.ServerCommandData.ServerRemoveFavouriteCommandData;
import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.DataContainers.UserData;

import java.util.Map;

public class ConsoleRemoveFavouriteCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ClientCommunicationModule communicationModule;
    private UserData user;

    private Integer advertisementId = 0;

    public ConsoleRemoveFavouriteCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule, UserData user){
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.communicationModule = communicationModule;
        this.user = user;

        this.necessaryArgs.put("advertisementId", false);
    }

    public void execute() throws Exception {
        ServerRemoveFavouriteCommandData command = null;
        if (!this.user.getLoginState()) {
            outputModule.outputLine("Для удаления объявлений из понравившихся необходимо выполнить вход в систему (команда /login)");
            return;
        }
        ServerCommandData undefinedCommand = communicationModule.executeCommand(new ServerRemoveFavouriteCommandData(advertisementId, this.user));
        if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerRemoveFavouriteCommandData) undefinedCommand;
        }
        if (command.getSuccessState()) {
            outputModule.outputLine("Объявление успешно удалено!");
        } else {
            outputModule.outputLine("У вас нет такого объявления!");
        }
    }

    public void putData(Map<String, String> data) {
        if (data.containsKey("advertisementId")) {
            if (NumberUtils.isCreatable(data.get("advertisementId"))) {
                necessaryArgs.put("advertisementId", true);
                advertisementId = NumberUtils.toInt(data.get("advertisementId"));
            } else {
                outputModule.outputLine(data.get("advertisementId") + " не является числом");
            }
        }
    }

    public void collectData() {
        Boolean isFilledFlag = true;
        for (String arg: this.necessaryArgs.keySet()) {
            if (!this.necessaryArgs.get(arg)) {
                isFilledFlag = false;
                break;
            }
        }
        if (!isFilledFlag) {
            outputModule.outputLine("Заполните обязательные аргументы:");
            while (!this.necessaryArgs.get("advertisementId")) {
                String line = inputModule.inputLineWithDescription("Введите id убираемого объявления: ");
                if (line.equals("0")) {
                    necessaryArgs.put("advertisementId", true);
                    this.advertisementId = 0;
                    break;
                }
                if (NumberUtils.isCreatable(line)) {
                    if (NumberUtils.toInt(line) >= 0) {
                        necessaryArgs.put("advertisementId", true);
                        advertisementId = NumberUtils.toInt(line);
                    } else {
                        outputModule.outputLine(line + " меньше нуля");
                    }
                } else {
                    outputModule.outputLine(line + " не является числом");
                }
            }
        }
    }

    public void clear() {
        this.necessaryArgs.replaceAll((a, v) -> false);
        this.unnecessaryArgs.replaceAll((a, v) -> false);
        this.advertisementId = 0;
    }

    public String getManual(){
        return "Удаляет ваше объявление из списка понравившихся по id. \nСписок аргументов:\nadvertisementId - id объявления (натуральное число)";
    }
}
