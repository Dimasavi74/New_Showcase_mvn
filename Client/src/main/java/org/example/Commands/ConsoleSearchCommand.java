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

import java.util.*;

public class ConsoleSearchCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ClientCommunicationModule communicationModule;

    private String[] words = {};
    private String[] tags = {};
    private Integer minPrice = 0;
    private Integer maxPrice;
    private Integer advertisementId = 0; // Id starts with 1, 0 is nonexistent id

    public ConsoleSearchCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule) {
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.communicationModule = communicationModule;

        this.unnecessaryArgs.put("words", false);
        this.unnecessaryArgs.put("tags", false);
        this.unnecessaryArgs.put("minPrice", false);
        this.unnecessaryArgs.put("maxPrice", false);
        this.unnecessaryArgs.put("advertisementId", false);
    }

    public void execute() throws Exception {
        ServerSearchCommandData command = null;
        ServerCommandData undefinedCommand = communicationModule.executeCommand(new ServerSearchCommandData(this.advertisementId, this.words, this.tags, this.minPrice, this.maxPrice));
        if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerSearchCommandData) undefinedCommand;
        }
        if (command.getFoundAdvertisements() != null && !Arrays.equals(command.getFoundAdvertisements(), new AdvertisementData[]{})) {
            outputModule.outputLine("По вашему запросу найдено следующее:");
            for (AdvertisementData advertisement: command.getFoundAdvertisements()) {
                outputModule.outputLine("(" + ((AdvertisementWithIdData) advertisement).getId() + ") " + advertisement.getTitle() + " " + advertisement.getPrice());
            }
        } else {
            outputModule.outputLine("По вашему запросу ничего не найдено!");
        }
    }

    public void collectData() {
        Boolean isFilledFlag = true;
        for (String arg: this.unnecessaryArgs.keySet()) {
            if (!this.unnecessaryArgs.get(arg)) {
                isFilledFlag = false;
                break;
            }
        }
        if (!isFilledFlag) {
            outputModule.outputLine("Заполните необязательные аргументы или оставьте строку пустой:");
            while (!this.unnecessaryArgs.get("advertisementId")) {
                String line = inputModule.inputLineWithDescription("Введите id искомого объявления, или оставьте строку пустой: ");
                if (line.isEmpty()) {
                    unnecessaryArgs.put("advertisementId", true);
                    break;
                }
                if (NumberUtils.isCreatable(line)) {
                    unnecessaryArgs.put("advertisementId", true);
                    advertisementId = NumberUtils.toInt(line);
                } else {
                    outputModule.outputLine(line + " не является числом");
                }
            }
            while (!this.unnecessaryArgs.get("words")) {
                String line = inputModule.inputLineWithDescription("Введите поисковый запрос, или оставьте строку пустой: ");
                if (line.isEmpty()) {
                    this.unnecessaryArgs.put("words", true);
                    break;
                }
                this.unnecessaryArgs.put("words", true);
                String[] rawWords = line.split(",");
                for (int i = 0; i < rawWords.length; i++) {
                    rawWords[i] = rawWords[i].toLowerCase().strip();
                }
                List<String> readyWords = new ArrayList<String>();
                for (int i = 0; i < rawWords.length; i++) {
                    readyWords.addAll(List.of(rawWords[i].split(" ")));
                }
                words = readyWords.toArray(new String[0]);
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
            while (!this.unnecessaryArgs.get("minPrice")) {
                String line = inputModule.inputLineWithDescription("Введите минимальую цену, или оставьте строку пустой: ");
                if (line.isEmpty()) {
                    unnecessaryArgs.put("minPrice", true);
                    break;
                }
                if (NumberUtils.isCreatable(line)) {
                    if (NumberUtils.toInt(line) < 0) {
                        outputModule.outputLine("Цена не может быть отрицательной!");
                    } else {
                        unnecessaryArgs.put("minPrice", true);
                        minPrice = NumberUtils.toInt(line);
                    }
                } else {
                    outputModule.outputLine(line + " не является числом");
                }
            }
            while (!this.unnecessaryArgs.get("maxPrice")) {
                String line = inputModule.inputLineWithDescription("Введите максимальную цену, или оставьте строку пустой: ");
                if (line.isEmpty()) {
                    unnecessaryArgs.put("maxPrice", true);
                    break;
                }
                if (NumberUtils.isCreatable(line)) {
                    if (minPrice > NumberUtils.toInt(line)) {
                        outputModule.outputLine("Введенная максимальная цена " + line + " меньше введенной минимальной " + minPrice);
                    } else {
                        unnecessaryArgs.put("maxPrice", true);
                        maxPrice = NumberUtils.toInt(line);
                    }
                } else {
                    outputModule.outputLine(line + " не является числом");
                }
            }
        }
    }

    public void putData(Map<String, String> data) {
        if (data.containsKey("words")) {
            this.unnecessaryArgs.put("words", true);
            String[] rawWords = data.get("words").split(",");
            for (int i = 0; i < rawWords.length; i++) {
                rawWords[i] = rawWords[i].toLowerCase().strip();
            }
            List<String> readyWords = new ArrayList<String>();
            for (int i = 0; i < rawWords.length; i++) {
                readyWords.addAll(List.of(rawWords[i].split(" ")));
            }
            words = readyWords.toArray(new String[0]);
        }
        if (data.containsKey("tags")) {
            unnecessaryArgs.put("tags", true);
            String[] rawTags = data.get("tags").split(",");
            for (int i = 0; i < rawTags.length; i++) {
                rawTags[i] = rawTags[i].toLowerCase().strip();
            }
            tags = rawTags;
        }
        if (data.containsKey("minPrice")) {
            if (NumberUtils.isCreatable(data.get("minPrice"))) {
                if (NumberUtils.toInt(data.get("minPrice")) < 0) {
                    outputModule.outputLine("Цена не может быть отрицательной!");
                } else {
                    unnecessaryArgs.put("minPrice", true);
                    minPrice = NumberUtils.toInt(data.get("minPrice"));
                }
            } else {
                outputModule.outputLine(data.get("minPrice") + " не является числом");
            }
        }
        if (data.containsKey("maxPrice")) {
            if (NumberUtils.isCreatable(data.get("maxPrice"))) {
                if (minPrice > maxPrice) {
                    outputModule.outputLine("Введенная максимальная цена " + data.get("maxPrice") + " меньше минимальной " + data.get("minPrice"));
                } else {
                    unnecessaryArgs.put("maxPrice", true);
                    maxPrice = NumberUtils.toInt(data.get("maxPrice"));
                }
            } else {
                outputModule.outputLine(data.get("minPrice") + " не является числом");
            }
        }
        if (data.containsKey("advertisementId")) {
            if (NumberUtils.isCreatable(data.get("advertisementId"))) {
                unnecessaryArgs.put("advertisementId", true);
                advertisementId = NumberUtils.toInt(data.get("advertisementId"));
            } else {
                outputModule.outputLine(data.get("advertisementId") + " не является числом");
            }
        }
    }

    public void clear() {
        this.necessaryArgs.replaceAll((a, v) -> false);
        this.unnecessaryArgs.replaceAll((a, v) -> false);
        this.words = new String[]{};
        this.tags = new String[]{};
        this.minPrice = 0;
        this.maxPrice = null;
        this.advertisementId = 0;
    }

    public String getManual() {
        return "Выполняет поиск объявлений \n" +
                "Список аргументов:\n" +
                "words (необязательно) - ваш поисковый запрос\n" +
                "tags (необязательно) - поисковые теги\n" +
                "minPrice (необязательно) - минимальная цена товара (натуральное число)\n" +
                "maxPrice (необязательно) - максимальная цена товара (натуральное число)\n" +
                "advertisementId (необязательно) - номер конкретного объявления";
    }
}
