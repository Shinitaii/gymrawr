package main.Objects;

public class User {
    private String username, password;
    private int userID, userLevel;

    public User(String username, String password, int userID, int userLevel){
        this.username = username;
        this.password = password;
        this.userID = userID;
        this.userLevel = userLevel;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getUserID() {
        return userID;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
