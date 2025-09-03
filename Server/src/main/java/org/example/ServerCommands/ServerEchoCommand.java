package org.example.ServerCommands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerEchoCommandData;

public class ServerEchoCommand extends AbstractServerCommand{
    private String line;

    public ServerEchoCommand() {}

    public ServerEchoCommand(String line) {
        this.line = line;
    }

    public void execute() {
        try {
            this.line = this.bdManager.echo(this.line);
        } catch (Exception e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLine() {
        return this.line;
    }

    public ServerEchoCommand setDataByFields(ServerCommandData data) {
        ServerEchoCommandData specialisedData = (ServerEchoCommandData) data;
        this.line = specialisedData.getLine();
        return this;
    }

    public ServerEchoCommandData generateServerCommandData() {
        ServerEchoCommandData commandData = new ServerEchoCommandData(this.line);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
