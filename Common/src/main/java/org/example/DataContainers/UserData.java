package org.example.DataContainers;

import java.io.Serializable;

public class UserData implements Serializable {
    public String nickname;
    public String mailAddress;
    public String password;
    public boolean isLogged = false;

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
}
