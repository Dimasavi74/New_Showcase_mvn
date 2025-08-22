package org.example.Commands;

import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;
import org.example.ServerCommands.ServerCommand;
import org.example.ServerCommands.ServerDeleteUserCommand;

import java.util.Map;

public class ConsoleDeleteUserCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ClientCommunicationModule communicationModule;

    private String nickname;
    private String mailAddress;
    private String password;

    public ConsoleDeleteUserCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule){
        this.inputModule = inputModule;
        this.communicationModule = communicationModule;

        this.necessaryArgs.put("nickname", false);
        this.necessaryArgs.put("mailAddress", false);
        this.necessaryArgs.put("password", false);
    }

    public void execute() throws Exception {
        ServerDeleteUserCommand command = null;
        ServerCommand undefinedCommand = communicationModule.executeCommand(new ServerDeleteUserCommand(this.nickname, this.mailAddress, this.password));
        if (undefinedCommand.getError() != null) {
            throw undefinedCommand.getError();
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerDeleteUserCommand) undefinedCommand;
        }
        if (command.getSuccessState()) {
            outputModule.outputLine("Пользователь " + this.nickname + " успешно удален!");
        } else {
            outputModule.outputLine("При удалении пользователя произошла ошибка! Повторите попытку позднее!");
        }
    }

    public void putData(Map<String, String> data) {
        if (data.containsKey("nickname")) {
            if (!data.get("nickname").isEmpty()) {
                this.necessaryArgs.put("nickname", true);
                this.nickname = data.get("nickname");
            } else {
                outputModule.outputLine("Имя пользователя не может быть пустым!");
            }
        }
        if (data.containsKey("mailAddress")) {
            if (data.get("mailAddress").contains("@")) {
                this.necessaryArgs.put("mailAddress", true);
                this.mailAddress = data.get("mailAddress");
            } else {
                outputModule.outputLine("Некорректный адрес почты. Перепроверьте ваш адрес и повторите попытку");
            }
        }
        if (data.containsKey("password")) {
            if (!data.get("password").isEmpty()) {
                this.necessaryArgs.put("password", true);
                this.password = data.get("password");
            } else {
                outputModule.outputLine("Пароль не может быть пустым!");
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
            while (!this.necessaryArgs.get("nickname")) {
                String line = inputModule.inputLineWithDescription("Введите имя пользователя: ");
                if (!line.isEmpty()) {
                    this.necessaryArgs.put("nickname", true);
                    this.nickname = line;
                } else {
                    outputModule.outputLine("Имя пользователя не может быть пустым!");
                }
            }
            while (!this.necessaryArgs.get("mailAddress")) {
                String line = inputModule.inputLineWithDescription("Введите адрес электронный почты: ");
                if (line.contains("@")) {
                    this.necessaryArgs.put("mailAddress", true);
                    this.mailAddress = line;
                } else {
                    this.outputModule.outputLine("Некорректный адрес электронной почты: " + line + ". Перепроверьте почту и введите еще раз");
                }
            }
            while (!this.necessaryArgs.get("password")) {
                String line = inputModule.inputLineWithDescription("Введите пароль: ");
                if (!line.isEmpty()) {
                    this.necessaryArgs.put("password", true);
                    this.password = line;
                } else {
                    outputModule.outputLine("Пароль не может быть пустым!");
                }
            }
        }
    }

    public void clear() {
        this.necessaryArgs.replaceAll((a, v) -> false);
        this.unnecessaryArgs.replaceAll((a, v) -> false);
        this.nickname = null;
        this.mailAddress = null;
        this.password = null;
    }

    public String getManual(){
        return "Удаляет пользователя \n" +
                "Список аргументов:\n" +
                "nickname - имя пользователя\n" +
                "mailAddress - электронная почта\n" +
                "password - пароль";
    }
}
