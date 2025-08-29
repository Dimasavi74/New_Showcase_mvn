package org.example;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class Main {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.print("Введите порт: ");
        int port = Integer.parseInt(console.nextLine());
        ExecutorService executorService = Executors.newFixedThreadPool(16);
        System.out.print("Введите пользователя бд: ");
        String user = console.nextLine();
        System.out.print("Введите пароль от бд: ");
        String password = console.nextLine();
        BdManager bdManager = new PostgreSQLBdManager(user, password, "jdbc:postgresql://localhost:5432/studs");
        ServerCommunicationModule communicationModule = new ServerCommunicationModule(bdManager, port);
        ServerApp app = new ServerApp(communicationModule, executorService);
        app.run();
    }
}
