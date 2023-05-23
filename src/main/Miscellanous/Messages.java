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

    public static void memberAdded(){
        JOptionPane.showMessageDialog(null, "Successfully added a new member!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void memberAddFailed(){
        JOptionPane.showMessageDialog(null, "Failed to add a new member!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void invalidDateFormat(){
        JOptionPane.showMessageDialog(null, "Invalid date format! Please follow the format (YYYY-MM-DD)", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void invalidGender(){
        JOptionPane.showMessageDialog(null, "Please select a gender!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void memberFound(){
        JOptionPane.showMessageDialog(null, "Member found!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void memberNotFound(){
        JOptionPane.showMessageDialog(null, "Member not found!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void memberUpdated(){
        JOptionPane.showMessageDialog(null, "Member updated!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void memberNotUpdated(){
        JOptionPane.showMessageDialog(null, "Member cannot be updated!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }
}
