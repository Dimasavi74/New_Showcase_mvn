package org.example.Commands;

import org.example.Modules.ConsoleOutputModule;
import org.example.DataContainers.UserData;

import java.util.Map;

public class ConsoleLogoutCommand extends AbstractConsoleCommand {
    private UserData user;

    public ConsoleLogoutCommand(ConsoleOutputModule outputModule, UserData user){
        this.user = user;
        this.outputModule = outputModule;
    }

    public void execute() {
        if (!this.user.isLogged) {
            outputModule.outputLine("Для того, чтобы выйти из системы, необходимо войти в систему (команда /login)");
            return;
        }
        user.nickname = "";
        user.mailAddress = "";
        user.password = "";
        user.isLogged = false;
        outputModule.outputLine("Вы вышли из аккаунта!");
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
        return "Выполняет выход из аккаунта";
    }
}
