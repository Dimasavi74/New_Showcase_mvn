package org.example.DataContainers.ServerCommandData;

import java.io.Serializable;

public abstract class AbstractServerCommandData implements Serializable, ServerCommandData {
    protected String errorMessage;

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

}
