package org.Commands;

import org.Modules.ClientCommunicationModule;
import org.Modules.ConsoleInputModule;
import org.Modules.ConsoleOutputModule;
import org.example.ServerCommands.ServerCommand;
import org.example.ServerCommands.ServerEchoCommand;

import java.io.IOException;
import java.util.Map;

public class ConsoleEchoCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ConsoleOutputModule outputModule;
    private ClientCommunicationModule communicationModule;

    private String line;

    public ConsoleEchoCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule){
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.communicationModule = communicationModule;

        this.necessaryArgs.put("line", false);
    }

    public void execute() {
        ServerEchoCommand command = null;
        try {
            ServerCommand undefinedCommand = communicationModule.executeCommand(new ServerEchoCommand(this.line));
            if (undefinedCommand.getError() != null) {
                throw undefinedCommand.getError();
            } else {
                command = (ServerEchoCommand) undefinedCommand;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        outputModule.outputLine(command.getLine());
    }

    public void putData(Map<String, String> data) {
        if (data.containsKey("line")) {
            this.necessaryArgs.put("line", true);
            this.line = data.get("line");
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
            while (!this.necessaryArgs.get("line")) {
                String line = inputModule.inputLineWithDescription("Введите строку: ");
                this.necessaryArgs.put("line", true);
                this.line = line;
            }
        }
    }

    public void clear() {
        this.necessaryArgs.replaceAll((a, v) -> false);
        this.unnecessaryArgs.replaceAll((a, v) -> false);
        this.line = null;
    }

    public String getManual(){
        return "Возвращает введенную строку с сервера";
    }
}
