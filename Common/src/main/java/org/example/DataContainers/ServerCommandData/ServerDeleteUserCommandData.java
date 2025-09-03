package org.example.DataContainers.ServerCommandData;

public class ServerDeleteUserCommandData extends AbstractServerCommandData {
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isDeleted = false;

    public ServerDeleteUserCommandData(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void setSuccessState(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getSuccessState() {
        return this.isDeleted;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public String getCommandName() {
        return "deleteUser";
    }
}
