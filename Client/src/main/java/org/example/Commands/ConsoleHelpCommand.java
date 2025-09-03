package org.example.Commands;

import org.example.Modules.ConsoleOutputModule;

import java.util.Map;

public class ConsoleHelpCommand extends AbstractConsoleCommand {
    private Map<String, AbstractConsoleCommand> consoleCommandObjects;

    public ConsoleHelpCommand(ConsoleOutputModule outputModule, Map<String, AbstractConsoleCommand> consoleCommandObjects) {
        this.outputModule = outputModule;
        this.consoleCommandObjects = consoleCommandObjects;
    }

    public void execute() {
        outputModule.outputLine("Инструкция по работе с программой:");
        outputModule.outputLine("Программа принимает на вход строки вида /commandName arg1{...} arg2{...};");
        outputModule.outputLine("Программа работает в двух режимах ввода - многострочном и однострочном");
        outputModule.outputLine("При многострочном режиме ввода в конце каждой введенной команды обязателен символ окончания ввода (по умолчанию ;)");
        outputModule.outputLine("При однострочном режиме (стоит по умолчанию) данный символ не обязателен");
        outputModule.outputLine("После ввода команды она сама доспрашивает необходимые аргументы");
        outputModule.outputLine("Если аргумент не является обязательным, оставьте строку ввода пустой");
        outputModule.outputLine("Для получения более подробной информации о команде используйте команду /manual");
        outputModule.outputLine("Список доступных команд");
        for (String el: consoleCommandObjects.keySet()) {
            outputModule.outputLine(el);
        }
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
