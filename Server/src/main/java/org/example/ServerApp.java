package org.example;

import java.util.Set;
import java.util.concurrent.ExecutorService;

public class ServerApp {
    public boolean run = false;
    ServerCommunicationModule communicationModule;
    ExecutorService executorService;

    public ServerApp(ServerCommunicationModule communicationModule, ExecutorService executorService) {
        this.communicationModule = communicationModule;
        this.executorService = executorService;
    }

    public void run() {
        run = true;
        while (run) {
            Set<Task> tasks = communicationModule.checkTasks();
            for (Task task: tasks) {
                System.out.println("Задача №" + task.getId() + " в обработке");
                executorService.execute(task);
            }
        }
    }
}