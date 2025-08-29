package org.example.Commands;

import org.example.DataContainers.ServerCommandData.AbstractServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerEchoCommandData;
import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;

import java.util.Map;

public class ConsoleEchoCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ClientCommunicationModule communicationModule;

    private String line;

    public ConsoleEchoCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule){
        this.inputModule = inputModule;
        this.communicationModule = communicationModule;
        this.outputModule = outputModule;

        this.necessaryArgs.put("line", false);
    }

    public void execute() throws Exception {
        ServerEchoCommandData command = null;
        ServerCommandData undefinedCommand = communicationModule.executeCommand(new ServerEchoCommandData(this.line));
        if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerEchoCommandData) undefinedCommand;
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
