package org.example;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.example.DataContainers.AdvertisementData;
import org.example.DataContainers.UserData;

import java.sql.*;
import java.util.*;

import java.security.MessageDigest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostgreSQLBdManager implements BdManager {
    private String url;
    private Connection connection;
    private String user;
    private String password;
    private Integer ANSWER_LIMIT = 100;


    public PostgreSQLBdManager(String bdLogin, String bdPassword, String bdURL) {
        // -L <local-address>:<local-port>:<remote-address>:<remote-port>
        // ssh -L 5432:pg:5432 -p 2222 s467318@se.ifmo.ru

        user = bdLogin;
        password = bdPassword;
        url = bdURL;
        try {
            connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }

    public String echo(String text) {
        return text;
    }

    public boolean register(String nickname, String mailAddress, String password) throws SQLException {
        if (connection.isClosed()) {
            connect();
        }
        String salt = genSalt();
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, (password + salt).toCharArray());
        String query = "INSERT INTO Person VALUES (DEFAULT, ?, ?, ?, FALSE, ?);";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, nickname);
        ps.setString(2, mailAddress);
        ps.setString(3, bcryptHashString);
        ps.setString(4, salt);
        int numOfInsertedLines = ps.executeUpdate();
        return numOfInsertedLines == 1;
    }

    public boolean login(String nickname, String mailAddress, String password) throws SQLException {
        if (connection.isClosed()) {
            connect();
        }

        String query = "SELECT password, salt FROM Person WHERE nickname = ? AND mailAddress = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, nickname);
        ps.setString(2, mailAddress);
        ResultSet results = ps.executeQuery();
        if (results.next()) {
            BCrypt.Result result = BCrypt.verifyer().verify((password + results.getString(2)).toCharArray(), results.getString(1));
            return result.verified;
        } else {
            throw new SQLException("UserNotFound");
        }

    }

    public AdvertisementData[] search(Integer advertisementId, String[] words, String[] tags, Integer minPrice, Integer maxPrice) throws SQLException {
        PreparedStatement ps;
        String query;
        ResultSet result;

        System.out.println(advertisementId + "|||" + Arrays.toString(words) + Arrays.toString(tags) + minPrice + maxPrice);

        if (advertisementId != 0) {
            query = "SELECT title, description, price, contacts FROM Advertisement WHERE AdvertisementId = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, advertisementId);
            result = ps.executeQuery();

            List<AdvertisementData> advertisements = new ArrayList<AdvertisementData>();
            if (result.next()) {
                Integer id = advertisementId;
                String title = result.getString(1);
                String description = result.getString(2);
                Integer price = result.getInt(3);
                String contacts = result.getString(4);
                advertisements.add(new AdvertisementData(id, title, description, price, contacts, new String[]{}));
            }
            return advertisements.toArray(new AdvertisementData[]{});
        }

        if (words != null && !Arrays.equals(words, new String[0])) {
            if (tags != null && !Arrays.equals(tags, new String[0])) {
                query = "SELECT id, title, price, description, contacts, score(id, ARRAY[?]::text[]) AS score FROM (SELECT filterTags(ARRAY[?]::text[]) AS id)" +
                        " INNER JOIN Advertisement ON id = Advertisement.AdvertisementId WHERE price >= ? AND price <= ? ORDER BY score LIMIT ?;";
                ps = connection.prepareStatement(query);
                ps.setArray(1, connection.createArrayOf("TEXT", words));
                ps.setArray(2, connection.createArrayOf("TEXT", tags));
                ps.setInt(3, minPrice);
                if (maxPrice != null) {
                    ps.setInt(4, maxPrice);
                } else {
                    ps.setInt(4, 2147483647); // Max PostgreSQL integer
                }
                ps.setInt(5, ANSWER_LIMIT);
            } else {
                query = "SELECT advertisementId, title, price, description, contacts, score(advertisementId, ARRAY[?]::text[]) AS score " +
                        "FROM Advertisement WHERE price >= ? AND price <= ? ORDER BY score LIMIT ?;";
                ps = connection.prepareStatement(query);
                ps.setArray(1, connection.createArrayOf("TEXT", words));
                ps.setInt(2, minPrice);
                if (maxPrice != null) {
                    ps.setInt(3, maxPrice);
                } else {
                    ps.setInt(3, 2147483647); // Max PostgreSQL integer
                }
                ps.setInt(4, ANSWER_LIMIT);
            }
        } else {
            if (tags != null && !Arrays.equals(tags, new String[0])) {
                query = "SELECT id, title, price, description, contacts FROM (SELECT filterTags(ARRAY[?]::text[]) AS id) INNER JOIN Advertisement ON " +
                        "id = Advertisement.AdvertisementId WHERE price >= ? AND price <= ? LIMIT ?;";
                ps = connection.prepareStatement(query);
                ps.setArray(1, connection.createArrayOf("TEXT", tags));
                ps.setInt(2, minPrice);
                if (maxPrice != null) {
                    ps.setInt(3, maxPrice);
                } else {
                    ps.setInt(3, 2147483647); // Max PostgreSQL integer
                }
                ps.setInt(4, ANSWER_LIMIT);
            } else {
                query = "SELECT advertisementId, title, price, description, contacts FROM Advertisement WHERE price >= ? AND price <= ? LIMIT ?;";
                ps = connection.prepareStatement(query);
                ps.setInt(1, minPrice);
                if (maxPrice != null) {
                    ps.setInt(2, maxPrice);
                } else {
                    ps.setInt(2, 2147483647); // Max PostgreSQL integer
                }
                ps.setInt(3, ANSWER_LIMIT);
            }
        }

        List<AdvertisementData> advertisements = new ArrayList<AdvertisementData>();
        result = ps.executeQuery();
        while (result.next()) {
            Integer id = result.getInt(1);
            String title = result.getString(2);
            Integer price = result.getInt(3);
            String description = result.getString(4);
            String contacts = result.getString(5);
            advertisements.add(new AdvertisementData(id, title, description, price, contacts, new String[]{}));
        }

        return advertisements.toArray(new AdvertisementData[]{});
    }

    public boolean createAdvertisement(AdvertisementData advertisement, UserData user) throws SQLException {
        if (connection.isClosed()) {
            connect();
        }
        connection.setAutoCommit(false);
        Savepoint save = connection.setSavepoint();
        try {
//            if (!login(user.nickname, user.mailAddress, user.password)) {
//                throw new SQLException("WrongPassword");
//            }
            int userId = getUserIdSecured(user);
            String query = "INSERT INTO Advertisement VALUES (DEFAULT, ?, ?, ?, ?, ?) RETURNING advertisementId;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, advertisement.title);
            ps.setString(3, advertisement.description);
            ps.setString(4, advertisement.contacts);
            ps.setInt(5, advertisement.price);
            ResultSet results = ps.executeQuery();
            results.next();
            int advertisementId = results.getInt(1);

            query = "INSERT INTO CountedWordToAdvertisement VALUES (?, ?, ?);";
            ps = connection.prepareStatement(query);
            String[] adWords = String.join(" ", Arrays.asList(((advertisement.title + " " + advertisement.description).toLowerCase()).split("\n"))).split(" ");
            Map<String, Integer> frequency = new HashMap<>();
            for (String el: adWords) {
                if (!el.isEmpty() && !frequency.containsKey(el)) {
                    frequency.put(el, Collections.frequency(List.of(adWords), el));
                }
            }
            for (String el : frequency.keySet()) {
                ps.setInt(1, advertisementId);
                ps.setString(2, el.toLowerCase());
                ps.setInt(3, frequency.get(el));
                ps.execute();
            }

            if (advertisement.tags != null && !Arrays.equals(advertisement.tags, new String[0])) {
                query = "INSERT INTO AdvertisementTags VALUES (?, ?);";
                ps = connection.prepareStatement(query);
                List<String> uniqueTags = Stream.of(advertisement.tags).distinct().collect(Collectors.toList());
                for (String tag : uniqueTags) {
                    ps.setInt(1, advertisementId);
                    ps.setString(2, tag);
                    ps.execute();
                }
            }
        } catch (Exception e) {
            connection.rollback(save);
            connection.commit();
            connection.setAutoCommit(true);
            throw e;
        }
        connection.commit();
        connection.setAutoCommit(true);
        return true;
    }

    public AdvertisementData[] userAdvertisements(UserData user) throws SQLException {
        if (connection.isClosed()) {connect();}
        Integer userId = getUserIdSecured(user);
        String query = "SELECT advertisementId, title, price, description, contacts FROM Advertisement WHERE personId = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet result = ps.executeQuery();
        List<AdvertisementData> advertisements = new ArrayList<AdvertisementData>();
        while (result.next()) {
            Integer id = result.getInt(1);
            String title = result.getString(2);
            Integer price = result.getInt(3);
            String description = result.getString(4);
            String contacts = result.getString(5);
            advertisements.add(new AdvertisementData(id, title, description, price, contacts, new String[]{}));
        }
        return advertisements.toArray(new AdvertisementData[]{});
    }

    public boolean deleteAdvertisement(int advertisementId, UserData user) throws SQLException {
        if (connection.isClosed()) {connect();}
        int userId = getUserIdSecured(user);
        String query = "DELETE FROM Advertisement WHERE advertisementId = ? AND personId = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, advertisementId);
        ps.setInt(2, userId);
        return ps.executeUpdate() > 0;
    }

    public boolean deleteUser(String nickname, String mailAddress, String password) throws SQLException {
        if (connection.isClosed()) {connect();}
        UserData userData = new UserData(nickname, mailAddress, password);
        Integer userId = getUserIdSecured(userData);
        String query = "DELETE FROM Person WHERE personId = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        return ps.executeUpdate() > 0;
    }

    public boolean addFavourite(UserData user, int advertisementId) throws SQLException {
        if (connection.isClosed()) {connect();}
        int userId = getUserIdSecured(user);
        String query = "INSERT INTO PersonFavourite VALUES (?, ?);";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ps.setInt(2, advertisementId);
        return ps.executeUpdate() > 0;
    }

    public boolean removeFavourite(UserData user, int advertisementId) throws SQLException {
        if (connection.isClosed()) {connect();}
        int userId = getUserIdSecured(user);
        String query = "DELETE FROM PersonFavourite WHERE advertisementId = ? AND personId = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, advertisementId);
        ps.setInt(2, userId);
        return ps.executeUpdate() > 0;
    }

    public AdvertisementData[] userFavourites(UserData user) throws SQLException {
        if (connection.isClosed()) {connect();}
        Integer userId = getUserIdSecured(user);
        String query = "SELECT Advertisement.advertisementId, title, price, description, contacts FROM (SELECT advertisementId FROM PersonFavourite WHERE personId = ?) AS PersonFavourite INNER JOIN Advertisement ON PersonFavourite.advertisementId = Advertisement.advertisementId;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet result = ps.executeQuery();
        List<AdvertisementData> advertisements = new ArrayList<AdvertisementData>();
        while (result.next()) {
            Integer id = result.getInt(1);
            String title = result.getString(2);
            Integer price = result.getInt(3);
            String description = result.getString(4);
            String contacts = result.getString(5);
            advertisements.add(new AdvertisementData(id, title, description, price, contacts, new String[]{}));
        }
        return advertisements.toArray(new AdvertisementData[]{});
    }

    private Integer getUserId(UserData user) throws SQLException {
        String query = "SELECT personId FROM Person WHERE nickname = ? AND mailAddress = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user.nickname);
        ps.setString(2, user.mailAddress);
        ResultSet results = ps.executeQuery();
        if (results.next()) {
            return results.getInt(1);
        } else {
            throw new SQLException("UserNotFound");
        }
    }

    private Integer getUserIdSecured(UserData user) throws SQLException {
        String query = "SELECT personId, password, salt FROM Person WHERE nickname = ? AND mailAddress = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, user.nickname);
        ps.setString(2, user.mailAddress);
        ResultSet results = ps.executeQuery();
        if (results.next()) {
            BCrypt.Result result = BCrypt.verifyer().verify((user.password + results.getString(3)).toCharArray(), results.getString(2));
            if (result.verified) {
                return results.getInt(1);
            } else {
                throw new SQLException("WrongPassword");
            }
        } else {
            throw new SQLException("UserNotFound");
        }
    }

    private String genSalt(){
        String alphabet = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM!@#$%^&*()-_+=`~,.<>/?;:";
        String salt = "";
        for (int i = 0; i < 30; i++) {
            salt += alphabet.charAt((int) (Math.random() * alphabet.length()));
        }
        return salt;
    }
}
