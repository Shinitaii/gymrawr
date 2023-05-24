package main.Objects;

import java.sql.*;
import java.util.ArrayList;

import main.Database.MySQL;
import main.Miscellanous.Messages;

public class Training {

    public static ArrayList<Training> trainingList = new ArrayList<Training>();
    
    private int trainingID;
    private String trainingName;

    private Training(int id, String name){
        trainingID = id;
        trainingName = name;
    }

    public int getTrainingID() {
        return trainingID;
    }

    public static ArrayList<Training> getTrainingList() {
        return trainingList;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingID(int trainingID) {
        this.trainingID = trainingID;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public static void initializeTrainings(){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select * from trainings");
            ResultSet result = statement.executeQuery();
            while(result.next()){
                int trainingID = result.getInt("training_id");
                String trainingName = result.getString("training_name");

                Training training = new Training(trainingID, trainingName);
                trainingList.add(training);
            }
            conn.close();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }
}
