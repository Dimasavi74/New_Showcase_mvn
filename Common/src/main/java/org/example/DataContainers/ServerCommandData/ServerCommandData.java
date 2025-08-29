package org.example.DataContainers.ServerCommandData;

import java.io.Serializable;

public interface ServerCommandData extends Serializable {
    public String getCommandName();
    public void setErrorMessage(String errorMessage);
    public String getErrorMessage();
}
