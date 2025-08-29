package org.example.ServerCommands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerDeleteUserCommandData;
import org.example.DataContainers.ServerCommandData.ServerEchoCommandData;
import org.example.DataContainers.ServerCommandData.ServerEmptyCommandData;

public class ServerEmptyCommand extends AbstractServerCommand {
    public void execute() {}

    public ServerEmptyCommand() {}

    public ServerEmptyCommand setDataByFields(ServerCommandData data) {
        return this;
    }

    public ServerEmptyCommandData generateServerCommandData() {
        ServerEmptyCommandData commandData = new ServerEmptyCommandData();
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
