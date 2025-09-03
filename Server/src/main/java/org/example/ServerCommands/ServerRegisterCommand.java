package org.example.ServerCommands;

import org.example.DataContainers.ServerCommandData.ServerCommandData;
import org.example.DataContainers.ServerCommandData.ServerRegisterCommandData;

import java.sql.SQLException;

public class ServerRegisterCommand extends AbstractServerCommand{
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isRegistered = false;

    public ServerRegisterCommand() {}

    public ServerRegisterCommand(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void execute() {
        try {
            this.isRegistered = this.bdManager.register(nickname, mailAddress, password);
        } catch (SQLException e) {
            this.setErrorMessage(e.getMessage());
        }
    }

    public boolean getSuccessState() {
        return this.isRegistered;
    }

    public ServerRegisterCommand setDataByFields(ServerCommandData data) {
        ServerRegisterCommandData specialisedData = (ServerRegisterCommandData) data;
        this.nickname = specialisedData.getNickname();
        this.mailAddress = specialisedData.getMailAddress();
        this.password = specialisedData.getPassword();
        return this;
    }

    public ServerRegisterCommandData generateServerCommandData() {
        ServerRegisterCommandData commandData = new ServerRegisterCommandData(this.nickname, this.mailAddress, this.password);
        commandData.setSuccessState(this.isRegistered);
        commandData.setErrorMessage(errorMessage);
        return commandData;
    }
}
