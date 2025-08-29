package org.example.DataContainers.ServerCommandData;

public class ServerRegisterCommandData extends AbstractServerCommandData {
    private String nickname;
    private String mailAddress;
    private String password;
    private boolean isRegistered = false;

    public ServerRegisterCommandData(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void setSuccessState(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public boolean getSuccessState() {
        return this.isRegistered;
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
        return "register";
    }
}
