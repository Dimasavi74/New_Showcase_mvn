package org.example.ServerCommands;

import java.sql.SQLException;

public class ServerLoginCommand extends AbstractServerCommand {
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isLoggedIn = false;

    public ServerLoginCommand(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void execute() {
        try {
            this.isLoggedIn = this.bdManager.login(nickname, mailAddress, password);
        } catch (SQLException e) {
            this.setError(e);
        }
    }

    public boolean getSuccessState() {
        return this.isLoggedIn;
    }
}
