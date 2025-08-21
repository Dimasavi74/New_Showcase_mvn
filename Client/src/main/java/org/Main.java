package org;

import org.Modules.ClientCommunicationModule;
import org.Modules.ConsoleInputModule;
import org.Modules.ConsoleOutputModule;
import org.Modules.ParsingModule;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) {
        ClientProperties properties = new ClientProperties();
        ConsoleInputModule inputModule = new ConsoleInputModule(properties);
        ParsingModule parsingModule = new ParsingModule();
        ConsoleOutputModule outputModule = new ConsoleOutputModule();
        Scanner console = new Scanner(System.in);
        System.out.print("Введите порт: ");
        String line = console.nextLine();
        ClientCommunicationModule communicationModule = new ClientCommunicationModule("localhost", Integer.parseInt(line));
        ClientConsoleApp app = new ClientConsoleApp(properties, inputModule, parsingModule, outputModule, communicationModule);
        app.run();
    }
}

