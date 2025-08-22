package org.Commands;

import org.ClientProperties;
import org.Modules.ConsoleOutputModule;

import java.util.Map;

public class ConsoleChangeInputModeCommand extends AbstractConsoleCommand {
    private ClientProperties properties;

    public ConsoleChangeInputModeCommand(ClientProperties properties, ConsoleOutputModule outputModule){
        this.properties = properties;
        this.outputModule = outputModule;
    }

    public void execute() {
        if (properties.getInputMode().equals("singleLine")) {
            properties.setInputMode("multipleLines");
            outputModule.outputLine("Режим изменен на многострочный");
        } else {
            properties.setInputMode("singleLine");
            outputModule.outputLine("Режим изменен на однострочный");
        }
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
        return "Меняет режим ввода команд (с однострочного на многострочный и наоборот)";
    }
}
