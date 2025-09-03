package org.example.Commands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerLoginCommandData;
import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;
import org.example.DataContainers.UserData;

import java.util.Map;

public class ConsoleLoginCommand extends AbstractConsoleCommand {
    private ConsoleInputModule inputModule;
    private ClientCommunicationModule communicationModule;
    private UserData user;

    private String nickname;
    private String mailAddress;
    private String password;

    public ConsoleLoginCommand(ConsoleInputModule inputModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule, UserData user){
        this.inputModule = inputModule;
        this.outputModule = outputModule;
        this.communicationModule = communicationModule;
        this.user = user;

        this.necessaryArgs.put("nickname", false);
        this.necessaryArgs.put("mailAddress", false);
        this.necessaryArgs.put("password", false);
    }

    public void execute() throws Exception {
        ServerLoginCommandData command = null;
        ServerCommandData undefinedCommand = communicationModule.executeCommand(new ServerLoginCommandData(this.nickname, this.mailAddress, this.password));
        if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else if (undefinedCommand.getErrorMessage() != null) {
            throw new Exception(undefinedCommand.getErrorMessage());
        } else {
            command = (ServerLoginCommandData) undefinedCommand;
        }
        if (command.getSuccessState()) {
            user.setAllData(this.nickname, this.mailAddress, this.password);
            this.user.setLoginState(true);
            outputModule.outputLine("Вход успешно выполнен!");
        } else {
            outputModule.outputLine("Неверный пароль!");
        }
    }

    public void putData(Map<String, String> data) {
        if (data.containsKey("nickname")) {
            this.necessaryArgs.put("nickname", true);
            this.nickname = data.get("nickname");
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
            this.necessaryArgs.put("password", true);
            this.password = data.get("password");
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
        return "Выполняет вход \n" +
                "Список аргументов:\n" +
                "nickname - имя пользователя\n" +
                "mailAddress - электронная почта\n" +
                "password - пароль";
    }
}
