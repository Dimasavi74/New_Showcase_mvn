package org.example;

import org.example.ServerCommands.ServerCommand;

public class ClientData {
    private ServerCommand command;

    public void setCommand(ServerCommand command) {
        this.command = command;
    }

    public ServerCommand getCommand() {
        return command;
    }
}
