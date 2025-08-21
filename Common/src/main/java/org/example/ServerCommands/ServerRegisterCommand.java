package org.example.ServerCommands;

import java.sql.SQLException;

public class ServerRegisterCommand extends AbstractServerCommand{
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isRegistered = false;

    public ServerRegisterCommand(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void execute() {
        try {
            this.isRegistered = this.bdManager.register(nickname, mailAddress, password);
        } catch (SQLException e) {
            this.setError(e);
        }
    }

    public boolean getSuccessState() {
        return this.isRegistered;
    }
}
