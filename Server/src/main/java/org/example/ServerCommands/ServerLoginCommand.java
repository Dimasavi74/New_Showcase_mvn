package org.example.ServerCommands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerLoginCommandData;

import java.sql.SQLException;

public class ServerLoginCommand extends AbstractServerCommand {
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isLoggedIn = false;

    public ServerLoginCommand() {}

    public ServerLoginCommand(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void execute() {
        try {
            this.isLoggedIn = this.bdManager.login(nickname, mailAddress, password);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public boolean getSuccessState() {
        return this.isLoggedIn;
    }

    public ServerLoginCommand setDataByFields(ServerCommandData data) {
        ServerLoginCommandData specialisedData = (ServerLoginCommandData) data;
        this.nickname = specialisedData.getNickname();
        this.mailAddress = specialisedData.getMailAddress();
        this.password = specialisedData.getPassword();
        return this;
    }

    public ServerLoginCommandData generateServerCommandData() {
        ServerLoginCommandData commandData = new ServerLoginCommandData(this.nickname, this.mailAddress, this.password);
        commandData.setSuccessState(this.isLoggedIn);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
