package org.example.DataContainers.ServerCommandData;

public class ServerLoginCommandData extends AbstractServerCommandData {
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isLoggedIn = false;

    public ServerLoginCommandData(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void setSuccessState(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public boolean getSuccessState() {
        return this.isLoggedIn;
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
        return "login";
    }
}
