package org.example.ServerCommands;

import java.sql.SQLException;

public class ServerEchoCommand extends AbstractServerCommand{
    private String line;

    public ServerEchoCommand(String line) {
        this.line = line;
    }

    public void execute() {
        try {
            this.bdManager.echo(this.line);
        } catch (Exception e) {
            this.setError(e);
        }
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLine() {
        return this.line;
    }
}
