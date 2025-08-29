package org.example.DataContainers;

import java.io.Serializable;

public class UserData implements Serializable {
    protected String nickname;
    protected String mailAddress;
    protected String password;
    protected boolean isLogged = false;

    public UserData() {}

    public UserData(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public void setAllData(String nickname, String mailAddress, String password) {
        this.nickname = nickname;
        this.mailAddress = mailAddress;
        this.password = password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getMailAddress() {
        return this.mailAddress;
    }

    public String getPassword() {
        return this.password;
    }

    public boolean getLoginState() {
        return this.isLogged;
    }

    public void setLoginState(boolean loginState) {
        this.isLogged = loginState;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
