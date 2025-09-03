package org.example.Commands;

import java.util.Map;

public interface Command {
    public void execute() throws Exception;
    public void safeExecute();
    public void collectData();
    public void putData(Map<String, String> data);
    public void clear();
    public boolean isReady();
    public String getManual();
}
