package org.example.Commands;

import org.example.DataContainers.AdvertisementWithIdData;
import org.example.DataContainers.ServerCommandData.AbstractServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerSearchCommandData;
import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.DataContainers.AdvertisementData;

import java.util.Arrays;
import java.util.Map;

public class ConsoleShowAdvertisementCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ClientCommunicationModule communicationModule;

    private Integer advertisementId = 0;

    public ConsoleShowAdvertisementCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule){
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.communicationModule = communicationModule;

        this.necessaryArgs.put("advertisementId", false);
    }

    public void execute() throws Exception {
        ServerSearchCommandData command = null;
        ServerCommandData undefinedCommand = communicationModule.executeCommand(new ServerSearchCommandData(this.advertisementId, new String[0], new String[0], 0, null));
        if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerSearchCommandData) undefinedCommand;
        }
        if (command.getFoundAdvertisements() != null && !Arrays.equals(command.getFoundAdvertisements(), new AdvertisementData[]{})) {
            for (AdvertisementData advertisement: command.getFoundAdvertisements()) {
                outputModule.outputLine("(" + ((AdvertisementWithIdData) advertisement).getId() + ")");
                outputModule.outputLine(advertisement.getTitle());
                outputModule.outputLine("Описание:");
                outputModule.outputLine(advertisement.getDescription());
                outputModule.outputLine("Цена: " + advertisement.getPrice());
                outputModule.outputLine("Контакты: " + advertisement.getContacts());
            }
        } else {
            outputModule.outputLine("Объявление с таким id не найдено!");
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
                String line = inputModule.inputLineWithDescription("Введите id просматриваемого объявления: ");
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
        return "Показывает подробную информацию об объявлении по id. \nСписок аргументов:\nadvertisementId - id объявления (натуральное число)";
    }
}
