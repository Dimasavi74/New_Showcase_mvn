package org.example.ServerCommands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerDeleteUserCommandData;

import java.sql.SQLException;

public class ServerDeleteUserCommand extends AbstractServerCommand {
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isDeleted = false;

    public ServerDeleteUserCommand() {}

    public ServerDeleteUserCommand(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void execute() {
        try {
            this.isDeleted = this.bdManager.deleteUser(nickname, mailAddress, password);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public boolean getSuccessState() {
        return this.isDeleted;
    }

    public ServerDeleteUserCommand setDataByFields(ServerCommandData data) {
        ServerDeleteUserCommandData specialisedData = (ServerDeleteUserCommandData) data;
        this.nickname = specialisedData.getNickname();
        this.mailAddress = specialisedData.getMailAddress();
        this.password = specialisedData.getPassword();
        return this;
    }

    public ServerDeleteUserCommandData generateServerCommandData() {
        ServerDeleteUserCommandData commandData = new ServerDeleteUserCommandData(this.nickname, this.mailAddress, this.password);
        commandData.setSuccessState(isDeleted);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
