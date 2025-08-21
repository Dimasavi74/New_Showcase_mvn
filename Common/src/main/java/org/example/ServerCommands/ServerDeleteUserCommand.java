package org.example.ServerCommands;

import java.sql.SQLException;

public class ServerDeleteUserCommand extends AbstractServerCommand {
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isDeleted = false;

    public ServerDeleteUserCommand(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void execute() {
        try {
            this.isDeleted = this.bdManager.deleteUser(nickname, mailAddress, password);
        } catch (SQLException e) {
            this.setError(e);
        }
    }

    public boolean getSuccessState() {
        return this.isDeleted;
    }
}
