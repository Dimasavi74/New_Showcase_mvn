package org.example;

import org.example.Modules.ClientCommunicationModule;
import org.example.Modules.ConsoleInputModule;
import org.example.Modules.ConsoleOutputModule;
import org.example.Modules.ParsingModule;

/**
 * Hello world!
 *
 */
public class Main {
    private final static String SERVER_HOST = "localhost";

    public static void main(String[] args) {
        ClientProperties properties = new ClientProperties();
        ConsoleOutputModule outputModule = new ConsoleOutputModule();
        ConsoleInputModule inputModule = new ConsoleInputModule(properties, outputModule);
        ParsingModule parsingModule = new ParsingModule();
        String line = inputModule.inputLineWithDescription("Введите порт: ");
        ClientCommunicationModule communicationModule = new ClientCommunicationModule(SERVER_HOST, Integer.parseInt(line));
        ClientConsoleApp app = new ClientConsoleApp(properties, inputModule, parsingModule, outputModule, communicationModule);
        app.run();
    }
}

