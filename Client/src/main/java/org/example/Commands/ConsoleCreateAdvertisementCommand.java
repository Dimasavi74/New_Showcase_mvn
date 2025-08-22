package org.example.Commands;

import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;
import org.apache.commons.lang3.math.NumberUtils;
import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;
import org.example.ServerCommands.ServerCommand;
import org.example.ServerCommands.ServerCreateAdvertisementCommand;

import java.util.Map;

public class ConsoleCreateAdvertisementCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ClientCommunicationModule communicationModule;
    private UserData user;

    private String title;
    private String description = "";
    private Integer price = 0;
    private String contacts;
    private String[] tags;


    public ConsoleCreateAdvertisementCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule, UserData user) {
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.communicationModule = communicationModule;
        this.user = user;

        this.necessaryArgs.put("title", false);
        this.unnecessaryArgs.put("description", false);
        this.necessaryArgs.put("price", false);
        this.necessaryArgs.put("contacts", false);
        this.unnecessaryArgs.put("tags", false);
    }

    public void execute() throws Exception {
        ServerCreateAdvertisementCommand command = null;
        AdvertisementData advertisement = new AdvertisementData(title, description, price, contacts, tags);
        if (!this.user.isLogged) {
            outputModule.outputLine("Для создания объявления необходимо выполнить вход в систему (команда /login)");
            return;
        }
        ServerCommand undefinedCommand = communicationModule.executeCommand(new ServerCreateAdvertisementCommand(advertisement, this.user));
        if (undefinedCommand.getError() != null) {
            throw undefinedCommand.getError();
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerCreateAdvertisementCommand) undefinedCommand;
        }
        if (command.getSuccessState()) {
            outputModule.outputLine("Объявление успешно создано!");
        } else {
            outputModule.outputLine("Объявление не было создано! Повторите попытку позднее");
        }
    }

    public void collectData() {
        Boolean isFilledFlag;
        isFilledFlag = true;
        for (String arg: this.necessaryArgs.keySet()) {
            if (!this.necessaryArgs.get(arg)) {
                isFilledFlag = false;
                break;
            }
        }
        if (!isFilledFlag) {
            outputModule.outputLine("Заполните обязательные аргументы:");
            while (!this.necessaryArgs.get("title")) {
                String line = inputModule.inputLineWithDescription("Введите название товара: ");
                if (line.isEmpty()){
                    outputModule.outputLine("Название не может быть пустым!");
                } else {
                    this.necessaryArgs.put("title", true);
                    this.title = line;
                }
            }
            while (!this.necessaryArgs.get("price")) {
                String line = inputModule.inputLineWithDescription("Введите цену на товар: ");
                if (line.isEmpty()) {
                    break;
                }
                if (NumberUtils.isCreatable(line)) {
                    if (NumberUtils.toInt(line) < 0) {
                        outputModule.outputLine("Цена не может быть отрицательной!");
                    } else {
                        necessaryArgs.put("price", true);
                        price = NumberUtils.toInt(line);
                    }
                } else {
                    outputModule.outputLine(line + " не является числом");
                }
            }
            while (!this.necessaryArgs.get("contacts")) {
                String line = inputModule.inputLineWithDescription("Введите контактную информацию: ");
                if (line.isEmpty()){
                    outputModule.outputLine("Не может быть пустой!");
                } else {
                    this.necessaryArgs.put("contacts", true);
                    this.contacts = line;
                }
            }
        }

        isFilledFlag = true;
        for (String arg: this.unnecessaryArgs.keySet()) {
            if (!this.unnecessaryArgs.get(arg)) {
                isFilledFlag = false;
                break;
            }
        }
        if (!isFilledFlag) {
            outputModule.outputLine("Заполните необязательные аргументы или оставьте строку пустой:");
            while (!this.unnecessaryArgs.get("description")) {
                String line = inputModule.inputLineWithDescription("Введите описание товара: ");
                if (line.isEmpty()){
                    unnecessaryArgs.put("description", true);
                    break;
                } else {
                    this.unnecessaryArgs.put("description", true);
                    this.description = line;
                }
            }
            while (!this.unnecessaryArgs.get("tags")) {
                String line = inputModule.inputLineWithDescription("Введите ключевые слова (теги) через запятую, или оставьте строку пустой: ");
                if (line.isEmpty()) {
                    unnecessaryArgs.put("tags", true);
                    break;
                }
                unnecessaryArgs.put("tags", true);
                String[] rawTags = line.split(",");
                for (int i = 0; i < rawTags.length; i++) {
                    rawTags[i] = rawTags[i].toLowerCase().strip();
                }
                tags = rawTags;
            }

        }
    }

    public void putData(Map<String, String> data) {
        if (data.containsKey("title")) {
            if (data.get("title").isEmpty()){
                outputModule.outputLine("Название не может быть пустым!");
            } else {
                this.necessaryArgs.put("title", true);
                this.title = data.get("title");
            }
        }
        if (data.containsKey("description")) {
            this.unnecessaryArgs.put("description", true);
            this.description = data.get("description");
        }
        if (data.containsKey("price")) {
            if (NumberUtils.isCreatable(data.get("price"))) {
                if (NumberUtils.toInt(data.get("price")) < 0) {
                    outputModule.outputLine("Цена не может быть отрицательной!");
                } else {
                    necessaryArgs.put("price", true);
                    price = NumberUtils.toInt(data.get("price"));
                }
            } else {
                outputModule.outputLine(data.get("price") + " не является числом");
            }
        }
        if (data.containsKey("contacts")) {
            if (data.get("contacts").isEmpty()){
                outputModule.outputLine("Контактная информация не может быть пустой!");
            } else {
                this.necessaryArgs.put("contacts", true);
                this.contacts = data.get("contacts");
            }
        }
        if (data.containsKey("tags")) {
            unnecessaryArgs.put("tags", true);
            String[] rawTags = data.get("tags").split(",");
            for (int i = 0; i < rawTags.length; i++) {
                rawTags[i] = rawTags[i].toLowerCase().strip();
            }
            tags = rawTags;
        }
    }

    public void clear() {
        this.necessaryArgs.replaceAll((a, v) -> false);
        this.unnecessaryArgs.replaceAll((a, v) -> false);
        this.title = null;
        this.description = "";
        this.price = 0;
        this.contacts = null;
        this.tags = null;
    }

    public String getManual() {
        return "Создает новое объявление \n" +
                "Список аргументов:\n" +
                "title - название товара\n" +
                "description (необязательно) - описание товара\n" +
                "price - цена товара (натуральное число)\n" +
                "contacts - ваши контактные данные\n" +
                "tags (необязательно) - поисковые теги\n";
    }
}
