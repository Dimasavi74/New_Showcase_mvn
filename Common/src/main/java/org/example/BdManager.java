package org.example;

import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.AdvertisementWithIdData;
import org.example.DataContainers.UserData;

import java.sql.SQLException;

public interface BdManager {
    public String echo(String line) throws SQLException;
    public boolean register(String nickname, String mailAddress, String password) throws SQLException;
    public AdvertisementData[] search(Integer advertisementId, String[] words, String[] tags, Integer minPrice, Integer maxPrice) throws SQLException;
    public boolean login(String nickname, String mailAddress, String password) throws SQLException;
    public boolean createAdvertisement(AdvertisementData advertisement, UserData user) throws SQLException;
    public boolean deleteAdvertisement(int advertisementId, UserData user) throws SQLException;
    public boolean deleteUser(String nickname, String mailAddress, String password) throws SQLException;
    public AdvertisementWithIdData[] userAdvertisements(UserData user) throws SQLException;
    public boolean addFavourite(UserData user, int advertisementId) throws SQLException;
    public boolean removeFavourite(UserData user, int advertisementId) throws SQLException;
    public AdvertisementWithIdData[] userFavourites(UserData user) throws SQLException;
}
