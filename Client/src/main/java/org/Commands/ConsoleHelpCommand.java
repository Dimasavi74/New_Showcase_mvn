package org.Commands;

import org.Modules.ConsoleOutputModule;

import java.util.Map;

public class ConsoleHelpCommand extends AbstractConsoleCommand {
    private ConsoleOutputModule outputModule;

    public ConsoleHelpCommand(ConsoleOutputModule outputModule) {
        this.outputModule = outputModule;
    }

    public void execute() {
        outputModule.outputLine("Помощь");
    }

    public void collectData() {
        return;
    }

    public void putData(Map<String, String> data) {
        return;
    }

    public void clear() {
        return;
    }

    public String getManual(){
        return "Показывает инструкцию по работе с программой";
    }
}
