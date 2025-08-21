package org.Commands;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConsoleCommand implements Command {
    protected boolean requestExit = false;
    protected Map<String, Boolean> necessaryArgs = new HashMap<>(); // Tells if the arg accepted
    protected Map<String, Boolean> unnecessaryArgs = new HashMap<>(); // Tells if the arg accepted

    public boolean isRequestExit() {
        return requestExit;
    }

    public boolean isReady() {
        for (String arg: necessaryArgs.keySet()) {
            if (necessaryArgs.get(arg) != true) {
                return false;
            }
        }
        return true;
    }
}
