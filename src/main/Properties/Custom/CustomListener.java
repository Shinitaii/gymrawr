package main.Properties.Custom;
import java.awt.event.*;
import java.sql.*;
import main.Database.MySQL;
import main.Miscellanous.Messages;

public class CustomListener implements ActionListener{

    private int panelNumber;

    public CustomListener(int panelNumber){
        this.panelNumber = panelNumber;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        listener(command, panelNumber);
    }

    private void listener(String command, int panelNumber){
        String panelName = "";
        switch(panelNumber){
            case 0: panelName = "members"; break;
            case 1: panelName = "trainers"; break;
            case 2: panelName = "equipments"; break;
            case 3: panelName = "staffs"; break;
        }

        if(command.equals("Add")) addListener(panelName);
        else if(command.equals("Update")) updateListener(panelName);
        else listListener(panelName); // "List" 
    }
    
    private void addListener(String panelName){
        try(Connection conn = MySQL.getConnection()){
            
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void updateListener(String panelName){
        try(Connection conn = MySQL.getConnection()){
            
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void listListener(String panelName){
        try(Connection conn = MySQL.getConnection()){
            
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }
}
