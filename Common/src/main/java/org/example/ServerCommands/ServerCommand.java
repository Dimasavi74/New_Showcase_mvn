package org.example.ServerCommands;

import org.example.BdManager;

import java.io.Serializable;

public interface ServerCommand extends Serializable {
    public void execute();
    public void setBdManager(BdManager bdManager);
    public void setError(Exception e);
    public Exception getError();
    public String getErrorMessage();
}
