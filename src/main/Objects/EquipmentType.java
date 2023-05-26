package main.Objects;

import java.sql.*;
import java.util.ArrayList;

import main.Database.MySQL;
import main.Miscellanous.Messages;

public class EquipmentType {

    public static ArrayList<EquipmentType> equipmentTypeList = new ArrayList<EquipmentType>();

    private int equipmentTypeID;
    private String equipmentName;

    private EquipmentType(int equipmentTypeID, String equipmentName){
        this.equipmentTypeID = equipmentTypeID;
        this.equipmentName = equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public void setEquipmentTypeID(int equipmentTypeID) {
        this.equipmentTypeID = equipmentTypeID;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public int getEquipmentTypeID() {
        return equipmentTypeID;
    }

    public static ArrayList<EquipmentType> getEquipmentTypeList() {
        return equipmentTypeList;
    }

    public static void initializeEquipmentTypes(){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select * from equipment_types");
            ResultSet result = statement.executeQuery();
            while(result.next()){
                int equipmentTypeID = result.getInt("equipment_type_id");
                String equipmentName = result.getString("equipment_type_name");

                EquipmentType equipmentType = new EquipmentType(equipmentTypeID, equipmentName);
                equipmentTypeList.add(equipmentType);
            }
            conn.close();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }


}
