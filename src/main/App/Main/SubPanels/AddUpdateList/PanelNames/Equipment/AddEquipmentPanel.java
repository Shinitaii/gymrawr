package main.App.Main.SubPanels.AddUpdateList.PanelNames.Equipment;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

import main.App.Main.SubPanels.AddUpdateList.ListPanel;
import main.App.Main.SubPanels.AddUpdateList.UpdatePanel;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Objects.EquipmentType;
import main.Properties.Focus;
import main.Properties.Custom.*;

public class AddEquipmentPanel extends JPanel{

    private JTextField equipmentNameField;
    private TextLabel equipmentNameLabel, equipmentQuantityLabel;
    private JFormattedTextField equipmentQuantity;
    private JComboBox<String> selection;
    private CustomButton clear, add;

    private int[] selectionEquipmentType;

    public AddEquipmentPanel(UpdatePanel updatePanel, ListPanel listPanel){
        setLayout(new GridBagLayout());
        equipmentNameLabel = new TextLabel("Equipment Name:", 12);
        CommonComponent.addComponent(this, equipmentNameLabel, 0, 0, 1, 1);
        equipmentNameField = CommonComponent.configureTextField(this, "Equipment name", 1, 0, 1, 1);
        equipmentQuantityLabel = new TextLabel("Enter the quantity:", 12);
        CommonComponent.addComponent(this, equipmentQuantityLabel, 2, 0, 1, 1);
        equipmentQuantity = CommonComponent.configureNumberField(equipmentQuantity);
        CommonComponent.addComponent(this, equipmentQuantity, 3, 0, 1, 1);
        selectionEquipmentType = new int[EquipmentType.getEquipmentTypeList().size()];
        TextLabel selectionLabel = new TextLabel("Enter the quantity:", 12);
        CommonComponent.addComponent(this, selectionLabel, 0, 1, 1, 1);
        selection = CommonComponent.configureEquipmentTypeComboBox(selection, selectionEquipmentType);
        CommonComponent.addComponent(this, selection, 1, 1, 1, 1);

        add = new CustomButton("Add", null, e -> addEquipment(updatePanel.getUpdateEquipmentPanel(), listPanel));
        CommonComponent.addComponent(this, add, 2, 1, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(this, clear, 3,1, 1, 1);
    }

    private void addEquipment(UpdateEquipmentPanel updateEquipmentList, ListPanel listPanel){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO equipments values (null, ?, ?, true)");
            statement.setInt(1, selectionEquipmentType[selection.getSelectedIndex()]);
            statement.setString(2, equipmentNameField.getText());
            int rowsAffected = 0;
            for(int i = 0; i < Integer.valueOf(equipmentQuantity.getText()); i++) rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) {
                updateEquipmentList.searchEquipment();
                listPanel.getListEquipmentPanel().retrieveDataFromDatabase();
                Messages.equipmentAdded(); 
            }
            else Messages.equipmentAddFailed();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void clearForm(){
        Focus.setPlaceholder(equipmentNameField, "Equipment name");
        equipmentQuantity.setText("0");
    }
    
}
