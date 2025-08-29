package org.example.DataContainers.ServerCommandData;

public class ServerEchoCommandData extends AbstractServerCommandData {
    private String line;

    public ServerEchoCommandData(String line) {
        this.line = line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLine() {
        return this.line;
    }

    public String getCommandName() {
        return "echo";
    }
}
