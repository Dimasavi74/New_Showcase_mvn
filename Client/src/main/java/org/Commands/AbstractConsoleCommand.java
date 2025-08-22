package org.Commands;

import org.Modules.ConsoleOutputModule;
import org.Modules.OutputModule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConsoleCommand implements Command {
    protected boolean requestExit = false;
    protected ConsoleOutputModule outputModule;
    protected Map<String, Boolean> necessaryArgs = new HashMap<>(); // Tells if the arg accepted
    protected Map<String, Boolean> unnecessaryArgs = new HashMap<>(); // Tells if the arg accepted

    public boolean isRequestExit() {
        return requestExit;
    }

    public boolean isReady() {
        for (String arg: necessaryArgs.keySet()) {
            if (necessaryArgs.get(arg) != true) {
                return false;
            }
        }
        return true;
    }

    public void safeExecute() {
        try {
            this.execute();
        } catch (IOException e) {
            outputModule.outputLine("Ошибка при взаимодействии с сервером! Повторите попытку позднее!");
        } catch (ClassNotFoundException e) {
            outputModule.outputLine("Ошибка при получении данных с сервера! Повторите попытку позднее!");
        } catch (Exception e) {
            if (e.getMessage().equals("UserNotFound")) {
                outputModule.outputLine("Такого пользователя не существует! Для регистрации используйте команду /register");
            } else if (e.getMessage().equals("WrongPassword")) {
                outputModule.outputLine("Неверный пароль!");
            } else if (e.getMessage().contains("violates foreign key constraint") && e.getMessage().contains("advertisementid")) {
                outputModule.outputLine("Запрашиваемого объявления не существует!");
            } else if (e.getMessage().contains("violates foreign key constraint") && e.getMessage().contains("nickname")) {
                outputModule.outputLine("Запрашиваемого пользователя не существует!");
            }  else if (e.getMessage().contains("duplicate key value violates unique constraint") && e.getMessage().contains("advertisementid")) {
                outputModule.outputLine("Объявление уже добавлено!");
            } else if (e.getMessage().contains("duplicate key value violates unique constraint") && e.getMessage().contains("nickname")) {
                outputModule.outputLine("Пользователь уже добавлен!");
            } else {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
