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

    public static void trainerAdded(){
        JOptionPane.showMessageDialog(null, "Successfully added a new trainer!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void trainerAddFailed(){
        JOptionPane.showMessageDialog(null, "Failed to add a new trainer!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void trainerFound(){
        JOptionPane.showMessageDialog(null, "Trainer found!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void trainerNotFound(){
        JOptionPane.showMessageDialog(null, "Trainer not found!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void trainerUpdated(){
        JOptionPane.showMessageDialog(null, "Trainer updated!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void trainerNotUpdated(){
        JOptionPane.showMessageDialog(null, "Trainer cannot be updated!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void trainerAssignSuccess(){
        JOptionPane.showMessageDialog(null, "Trainer successfully assigned to member!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void trainerAssignFailed(){
        JOptionPane.showMessageDialog(null, "Trainer failed to assign to a member!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void equipmentAdded(){
        JOptionPane.showMessageDialog(null, "Successfully added a new equipment!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void equipmentAddFailed(){
        JOptionPane.showMessageDialog(null, "Failed to add a new equipment!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void equipmentUpdated(){
        JOptionPane.showMessageDialog(null, "Equipment updated!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void equipmentNotUpdated(){
        JOptionPane.showMessageDialog(null, "Equipment cannot be updated!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void equipmentDeleted(){
        JOptionPane.showMessageDialog(null, "Equipment deleted!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void equipmentNotDeleted(){
        JOptionPane.showMessageDialog(null, "Equipment cannot be deleted!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void clockInMemberSuccessfully(){
        JOptionPane.showMessageDialog(null, "Member successfully clock in!", Constants.APP_TITLE, JOptionPane.PLAIN_MESSAGE);
    }

    public static void clockInMemberFailed(){
        JOptionPane.showMessageDialog(null, "Member failed to clock in!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static void memberAlreadyClockedIn(){
        JOptionPane.showMessageDialog(null, "Member already clocked in!", Constants.APP_TITLE, JOptionPane.WARNING_MESSAGE);
    }
}
