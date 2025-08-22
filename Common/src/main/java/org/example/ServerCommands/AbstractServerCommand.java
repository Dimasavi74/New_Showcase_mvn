package org.example.ServerCommands;

import org.example.BdManager;

public abstract class AbstractServerCommand implements ServerCommand {
    protected transient BdManager bdManager;
    protected transient Exception e;
    protected String errorMessage;

    public void setBdManager(BdManager bdManager) {
        this.bdManager = bdManager;
    }
    public void setError(Exception e) {
        this.e = e;
        this.errorMessage = e.getMessage();
    }
    public Exception getError(){
        return e;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
}
