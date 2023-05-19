package main.Miscellanous;

import javax.swing.JOptionPane;

public class Messages {
    public static void databaseConnectionFailed(){
        JOptionPane.showMessageDialog(null, "Failed to connect to database!\nPlease check if your MySQL server in your computer is enabled.", Constants.APP_TITLE, JOptionPane.ERROR_MESSAGE);
    }
    public static void loginSuccessful(String username){
        JOptionPane.showMessageDialog(null, "Successful login.\nWelcome, " + username + "!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }
    public static void loginFailed(){
        JOptionPane.showMessageDialog(null, "Wrong username and/or password!\nNOTE: Both credentials are case-sensitive!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }
}
