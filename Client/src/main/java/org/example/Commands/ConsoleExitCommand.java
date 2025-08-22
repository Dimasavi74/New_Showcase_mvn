package org.example.Commands;

import org.example.ClientProperties;
import org.example.Modules.ConsoleOutputModule;

import java.util.Map;

public class ConsoleExitCommand extends AbstractConsoleCommand {
    private ClientProperties properties;

    public ConsoleExitCommand(ClientProperties properties, ConsoleOutputModule outputModule){
        this.properties = properties;
        this.outputModule = outputModule;
    }

    public void execute() {
        properties.isRunning = false;
        outputModule.outputLine("Программа остановлена!");
    }

    public void clear(){
        return;
    }

    public boolean isReady(){
        return true;
    }

    public void collectData(){
        return;
    }

    public void putData(Map<String, String> data) {
        return;
    }

    public String getManual(){
        return "Выполняет выход из программы";
    }
}
