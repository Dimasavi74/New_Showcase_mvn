package org.example.Commands;

import org.example.ClientProperties;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;
import org.example.Modules.ParsingModule;

import java.io.IOException;
import java.util.*;

public class ConsoleExecuteFileCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ParsingModule parsingModule;
    private ClientProperties properties;
    private Map<String, AbstractConsoleCommand> consoleCommandObjects;

    private String filePath;

    public ConsoleExecuteFileCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ParsingModule parsingModule, ClientProperties properties, Map<String, AbstractConsoleCommand> consoleCommandObjects){
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.parsingModule = parsingModule;
        this.properties = properties;
        this.consoleCommandObjects = consoleCommandObjects;

        this.necessaryArgs.put("filePath", false);
    }

    public void execute() {
        try {
            List<String> lines = new ArrayList<String>();
            String text = "";
            String inputText = this.inputModule.readFile(this.filePath);
            switch (properties.getInputMode()) {
                case "singleLine":
                    text = "";
                    for (int i = 0; i < inputText.length(); i++) {
                        if (inputText.charAt(i) != '\n') {
                            text += inputText.charAt(i);
                        } else {
                            lines.add(text);
                            text = "";
                        }
                    }
                    break;
                case "multipleLines":
                    text = "";
                    for (int i = 0; i < inputText.length(); i++) {
                        if (!String.valueOf(inputText.charAt(i)).equals(properties.getMultipleInputEndSymbol())) {
                            text += inputText.charAt(i);
                        } else {
                            lines.add(text);
                            text = "";
                        }
                    }
                    break;
                default:
                    throw new RuntimeException("Wrong input mode in properties");
            }
        for (String line: lines) {
            Map<String, String> parsedText;
            switch (properties.getInputSyntax()) {
                case "variableNames":
                    parsedText = this.parsingModule.parseVariableNamesSyntaxLine(line);
                    break;
                default:
                    throw new RuntimeException("Wrong input syntax in properties");
            }

            System.out.println(parsedText);

            if (consoleCommandObjects.containsKey(parsedText.get("command"))){
                if (parsedText.get("command").equals("executeFile")) {
                    throw new IOException("В файле не может содержаться команда executeFile");
                }
                AbstractConsoleCommand command = consoleCommandObjects.get(parsedText.get("command"));
                command.putData(parsedText);
                boolean exitFlag = false;
                if (command.isReady()) {
                    outputModule.outputLine("Исполняется команда " + parsedText.get("command"));
                    command.execute();
                    command.clear();
                } else {
                    outputModule.outputLine("Не все поля команды определены. Информация о правилах написания команды в файле доступна по команде /manual");
                }
            } else {
                outputModule.outputLine("Введенной вами команды не существует. Введенная команда: " + parsedText.get("command") + ". Для большей информации введите /help");
            }
        }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void putData(Map<String, String> data) {
        if (data.containsKey("filePath")) {
            if (!data.get("filePath").isEmpty()) {
                this.necessaryArgs.put("filePath", true);
                this.filePath = data.get("filePath");
            } else {
                this.outputModule.outputLine("Путь не может быть пустым!");
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
            while (!this.necessaryArgs.get("filePath")) {
                String line = inputModule.inputLineWithDescription("Введите путь до файла с командами: ");
                if (!line.isEmpty()) {
                    this.necessaryArgs.put("filePath", true);
                    this.filePath = line;
                } else {
                    this.outputModule.outputLine("Путь не может быть пустым!");
                }
            }
        }
    }

    public void clear() {
        this.necessaryArgs.replaceAll((a, v) -> false);
        this.unnecessaryArgs.replaceAll((a, v) -> false);
        this.filePath = null;
    }

    public String getManual(){
        return "Исполняет команды из файла. \n" +
                "ВАЖНО!!! ВСЕ обязательные аргументы исполняемой команды должны быть определены! \n" +
                "Пример: /manual commandName{help} \n" +
                "Список аргументов:\n" +
                "filePath - абсолютный путь до файла с командами";
    }
}
