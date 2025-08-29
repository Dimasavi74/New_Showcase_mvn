package org.example.ServerCommands;

import org.example.BdManager;
import org.example.DataContainers.ServerCommandData.ServerCommandData;

public interface ServerCommand {
    public void execute();
    public void setBdManager(BdManager bdManager);
    public void setErrorMessage(String message);
    public String getErrorMessage();
    public ServerCommand setDataByFields(ServerCommandData data);
    public ServerCommandData generateServerCommandData();
}
