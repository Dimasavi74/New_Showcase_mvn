package org.example;

import org.example.Commands.*;
import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;
import org.example.Modules.ParsingModule;
import org.example.DataContainers.UserData;

import java.util.HashMap;
import java.util.Map;

public class ClientConsoleApp {
    private ClientProperties properties;
    private ConsoleInputModule inputModule;
    private ParsingModule parsingModule;
    private ConsoleOutputModule outputModule;
    private ClientCommunicationModule communicationModule;
    private UserData user = new UserData();
    private final Map<String, AbstractConsoleCommand> consoleCommandObjects = new HashMap<String, AbstractConsoleCommand>();


    public ClientConsoleApp(ClientProperties properties, ConsoleInputModule inputModule, ParsingModule parsingModule, ConsoleOutputModule outputModule, ClientCommunicationModule communicationModule){
        this.properties = properties;
        this.inputModule = inputModule;
        this.parsingModule = parsingModule;
        this.outputModule = outputModule;
        this.communicationModule = communicationModule;

        this.consoleCommandObjects.put("exit", new ConsoleExitCommand(properties, outputModule));
        this.consoleCommandObjects.put("manual", new ConsoleManualCommand(inputModule, outputModule, consoleCommandObjects));
        this.consoleCommandObjects.put("help", new ConsoleHelpCommand(outputModule, consoleCommandObjects));
        this.consoleCommandObjects.put("echo", new ConsoleEchoCommand(inputModule, outputModule, communicationModule));
        this.consoleCommandObjects.put("register", new ConsoleRegisterCommand(inputModule, outputModule, communicationModule));
        this.consoleCommandObjects.put("search", new ConsoleSearchCommand(inputModule, outputModule, communicationModule));
        this.consoleCommandObjects.put("login", new ConsoleLoginCommand(inputModule, outputModule, communicationModule, user));
        this.consoleCommandObjects.put("createAdvertisement", new ConsoleCreateAdvertisementCommand(inputModule, outputModule, communicationModule, user));
        this.consoleCommandObjects.put("deleteAdvertisement", new ConsoleDeleteAdvertisementCommand(inputModule, outputModule, communicationModule, user));
        this.consoleCommandObjects.put("deleteUser", new ConsoleDeleteUserCommand(inputModule, outputModule, communicationModule));
        this.consoleCommandObjects.put("logout", new ConsoleLogoutCommand(outputModule, user));
        this.consoleCommandObjects.put("addFavourite", new ConsoleAddFavouriteCommand(inputModule, outputModule, communicationModule, user));
        this.consoleCommandObjects.put("removeFavourite", new ConsoleRemoveFavouriteCommand(inputModule, outputModule, communicationModule, user));
        this.consoleCommandObjects.put("myAdvertisements", new ConsoleMyAdvertisementsCommand(outputModule, communicationModule, user));
        this.consoleCommandObjects.put("myFavourites", new ConsoleMyFavouritesCommand(outputModule, communicationModule, user));
        this.consoleCommandObjects.put("showAdvertisement", new ConsoleShowAdvertisementCommand(inputModule, outputModule, communicationModule));
        this.consoleCommandObjects.put("executeFile", new ConsoleExecuteFileCommand(inputModule, outputModule, parsingModule, properties, consoleCommandObjects));
        this.consoleCommandObjects.put("changeInputMode", new ConsoleChangeInputModeCommand(properties, outputModule));
    }

    public void run(){
        properties.isRunning = true;
        outputModule.outputLine("Программа запущена!");
        while (properties.isRunning) {
            String inputText;

            switch (properties.getInputMode()) {
                case SINGLE_LINE:
                    inputText = inputModule.inputLine();
                    break;
                case MULTIPLE_LINES:
                    inputText = inputModule.inputMultipleLines();
                    break;
                default:
                    throw new RuntimeException("Wrong input mode in properties");
            }

            Map<String, String> parsedText;
            switch (properties.getInputSyntax()) {
                case VARIABLE_NAMES:
                    parsedText = this.parsingModule.parseVariableNamesSyntaxLine(inputText);
                    break;
                default:
                    throw new RuntimeException("Wrong input syntax in properties");
            }

            if (consoleCommandObjects.containsKey(parsedText.get("command"))){
                AbstractConsoleCommand command = consoleCommandObjects.get(parsedText.get("command"));
                command.putData(parsedText);
                boolean exitFlag = false;
                command.collectData();
                while (!command.isReady()) {
                    command.collectData();
                    if (command.isRequestExit()) {
                        exitFlag = true;
                        break;
                    }
                }
                if (exitFlag) {
                    continue;
                }
                command.safeExecute();
                command.clear();
            } else {
                outputModule.outputLine("Введенной вами команды не существует. Введенная команда: " + parsedText.get("command") + ". Для большей информации введите /help");
            }
        }
    }
}
