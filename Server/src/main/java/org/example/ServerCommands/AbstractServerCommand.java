package org.example.ServerCommands;

import org.example.BdManager;

public abstract class AbstractServerCommand implements ServerCommand {
    protected transient BdManager bdManager;
    protected String errorMessage;

    public void setBdManager(BdManager bdManager) {
        this.bdManager = bdManager;
    }
    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
