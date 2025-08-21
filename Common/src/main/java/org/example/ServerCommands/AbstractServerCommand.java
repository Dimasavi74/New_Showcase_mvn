package org.example.ServerCommands;

import org.example.BdManager;

public abstract class AbstractServerCommand implements ServerCommand{
    protected transient BdManager bdManager;
    protected Exception e;

    public void setBdManager(BdManager bdManager) {
        this.bdManager = bdManager;
    }
    public void setError(Exception e) {
        this.e = e;
    }
    public Exception getError(){
        return e;
    }
}
