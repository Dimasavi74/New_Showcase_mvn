package org.Commands;

import org.Modules.ConsoleInputModule;
import org.Modules.ConsoleOutputModule;

import java.util.Map;

public class ConsoleManualCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ConsoleOutputModule outputModule;
    private final Map<String, AbstractConsoleCommand> consoleCommandObjects;

    private String commandName;

    public ConsoleManualCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, Map<String, AbstractConsoleCommand> consoleCommandObjects){
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.consoleCommandObjects = consoleCommandObjects;

        this.necessaryArgs.put("commandName", false);
    }

    public void execute() {
        outputModule.outputLine(consoleCommandObjects.get(commandName).getManual());
    }

    public void putData(Map<String, String> data) {
        if (data.containsKey("commandName")) {
            if (consoleCommandObjects.containsKey(data.get("commandName"))) {
                this.necessaryArgs.put("commandName", true);
                this.commandName = data.get("commandName");
            } else {
                this.outputModule.outputLine("Команды под названием " + data.get("commandName") + " не существует!");
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
            while (!this.necessaryArgs.get("commandName")) {
                String line = inputModule.inputLineWithDescription("Введите название команды: ");
                if (consoleCommandObjects.containsKey(line)) {
                    this.necessaryArgs.put("commandName", true);
                    this.commandName = line;
                } else {
                    this.outputModule.outputLine("Команды под названием " + line + " не существует!");
                }
            }
        }
    }

    public void clear() {
        this.necessaryArgs.replaceAll((a, v) -> false);
        this.unnecessaryArgs.replaceAll((a, v) -> false);
        this.commandName = null;
    }

    public String getManual(){
        return "Показывает информацию о команде. \nСписок аргументов:\ncommandName - название команды, о которой хотите получить информацию";
    }
}
