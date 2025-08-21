package org.Commands;

import org.Modules.ClientCommunicationModule;
import org.Modules.ConsoleInputModule;
import org.Modules.ConsoleOutputModule;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.DataContainers.UserData;
import org.example.ServerCommands.ServerCommand;
import org.example.ServerCommands.ServerDeleteAdvertisementCommand;

import java.util.Map;

public class ConsoleDeleteAdvertisementCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ConsoleOutputModule outputModule;
    private ClientCommunicationModule communicationModule;
    private UserData user;

    private Integer advertisementId = 0;

    public ConsoleDeleteAdvertisementCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule, UserData user){
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.communicationModule = communicationModule;
        this.user = user;

        this.necessaryArgs.put("advertisementId", false);
    }

    public void execute() {
        ServerDeleteAdvertisementCommand command = null;
        if (!this.user.isLogged) {
            outputModule.outputLine("Для удаления объявлений необходимо выполнить вход в систему (команда /login)");
            return;
        }
        try {
            ServerCommand undefinedCommand = communicationModule.executeCommand(new ServerDeleteAdvertisementCommand(advertisementId, this.user));
            if (undefinedCommand.getError() != null) {
                throw undefinedCommand.getError();
            } else {
                command = (ServerDeleteAdvertisementCommand) undefinedCommand;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                String line = inputModule.inputLineWithDescription("Введите id удаляемого объявления: ");
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
        return "Удаляет ваше объявление по id. \nСписок аргументов:\nadvertisementId - id объявления (натуральное число)";
    }
}
