package main.App.Main.SubPanels.AddUpdateList.PanelNames.Equipment;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.App.Main.SubPanels.AddUpdateList.AddUpdateListPanel;
import main.App.Main.SubPanels.AddUpdateList.BackPanel;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Constants;
import main.Miscellanous.Messages;
import main.Objects.EquipmentType;
import main.Properties.Custom.CustomButton;
import main.Properties.Custom.TextLabel;

public class ReserveEquipmentPanel extends JPanel{
    private ListReserveEquipmentPanel listReserveEquipmentPanel;
    private BackPanel backPanel;
    private JPanel mainPanel;
    private JPanel searchReceiptPanel;
    private JTextField receiptNumberField;
    private CustomButton search;
    private JComboBox<String> productCodeSelection;
    private CustomButton useProductCode;
    private JPanel formPanel;
    private JTextField searchMemberField;
    private CustomButton searchMember;
    private int[] selectionID;
    private JComboBox<String> selection;
    private JScrollPane memberScrollPane, trainerScrollPane;
    private CustomButton searchEquipment;
    private CustomButton assign;
    private DefaultTableModel memberTableModel, trainerTableModel;
    private JTable memberTable, trainerTable;

    public ReserveEquipmentPanel(AddUpdateListPanel addUpdateListPanel, ListReserveEquipmentPanel listReserveEquipmentPanel){
        this.listReserveEquipmentPanel = listReserveEquipmentPanel;
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);

        setMainPanel();
    }

    private void setMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        searchReceiptPanel = new JPanel(new GridBagLayout());
        TextLabel receiptNumberLabel = new TextLabel("Receipt Number:", 12);
        CommonComponent.addComponent(searchReceiptPanel, receiptNumberLabel, 0, 0, 1, 1);
        receiptNumberField = CommonComponent.configureReceiptNumberField();
        CommonComponent.addComponent(searchReceiptPanel, receiptNumberField, 1, 0, 1, 1);
        search = new CustomButton("Search receipt number", null, e->searchReceipt());
        CommonComponent.addComponent(searchReceiptPanel, search, 2, 0, 1, 1);
        TextLabel productCodeLabel = new TextLabel("Select product code once receipt number is found:", 12);
        CommonComponent.addComponent(searchReceiptPanel, productCodeLabel, 0, 2, 1,1);
        productCodeSelection = new JComboBox<>();
        productCodeSelection.setEnabled(false);
        CommonComponent.addComponent(searchReceiptPanel, productCodeSelection, 1, 2, 1, 1);
        useProductCode = new CustomButton("Use product code", null, e -> searchEquipmentPanel(String.valueOf(productCodeSelection.getSelectedItem())));
        CommonComponent.addComponent(searchReceiptPanel, useProductCode, 2, 2, WIDTH, HEIGHT);
        mainPanel.add(searchReceiptPanel, BorderLayout.NORTH);
        formPanel = new JPanel(new GridBagLayout());
        mainPanel.add(formPanel, BorderLayout.CENTER);
        setMemberTable();
        setTrainerTable();
        setButtons();
        CommonComponent.addComponent(formPanel, memberScrollPane, 0, 0, 2, 1);
        CommonComponent.addComponent(formPanel, trainerScrollPane, 2, 0, 2, 1);
        for(Component component : formPanel.getComponents()){
            component.setEnabled(false);
        }
    }

    private void setButtons(){
        searchMemberField = CommonComponent.configureTextField(formPanel, "Search member", 0, 1, 1, 1);
        searchMember = new CustomButton("Search member", null, e -> searchMember());
        CommonComponent.addComponent(formPanel, searchMember, 0, 4, 1, 1);
        selectionID = new int[EquipmentType.getEquipmentTypeList().size()];
        selection = CommonComponent.configureEquipmentTypeComboBox(selection, selectionID);
        CommonComponent.addComponent(formPanel, selection, 2, 1, 1, 1);
        searchEquipment = new CustomButton("Search equipment", null, e -> searchTrainers(selectionID[selection.getSelectedIndex()]));
        CommonComponent.addComponent(formPanel, searchEquipment, 2, 4, 1, 1);
        assign = new CustomButton("Reserve equipment to member", null, e ->assignEquipment());
        CommonComponent.addComponent(formPanel, assign, 3, 4, 1, 1);
    }

    private void searchMember(){
        memberTableModel = initializeMemberModel(memberTableModel, 1);
        memberTable.setModel(memberTableModel);
    }

    private void searchTrainers(int selectionID){
        trainerTableModel = initializeTrainerModel(trainerTableModel, selectionID);
        trainerTable.setModel(trainerTableModel);
    }

    private void setMemberTable(){
        memberTableModel = initializeMemberModel(memberTableModel, 0);
        memberTable = new JTable(memberTableModel);
        memberScrollPane = new JScrollPane(memberTable);
        memberScrollPane.setPreferredSize(new Dimension(200, 200));
    }

    private void setTrainerTable(){
        trainerTable = new JTable();
        trainerScrollPane = new JScrollPane(trainerTable);
        trainerScrollPane.setPreferredSize(new Dimension(200, 200));
    }
    
    private String getName(JTable table){
        int selectedRow = table.getSelectedRow();
        int selectedColumn = table.getSelectedColumn();

        if (selectedRow != -1 && selectedColumn != -1) {
            Object selectedValue = table.getValueAt(selectedRow, selectedColumn);
    
            if (selectedValue != null) {
                return selectedValue.toString();
            }
        }

        return "";
    }

    private int getID(JTable table){
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            Object selectedValue = table.getValueAt(selectedRow, 1);
    
            if (selectedValue != null) {
                return Integer.valueOf(selectedValue.toString());
            }
        }

        return -1;
    }
    private void assignEquipment() {
        // Get the necessary values
        String memberName = getName(memberTable);
        int equipmentId = getID(trainerTable);

            try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO reservations (reservation_id, equipment_type_id, equipment_id, member_id, reservation_date) SELECT NULL, ?, e.equipment_id, m.member_id, TIMESTAMPADD(MINUTE, ?, CURRENT_TIMESTAMP) FROM equipments e JOIN members m ON CONCAT(m.member_firstname, ' ', m.member_middlename, ' ', m.member_lastname) = ? LEFT JOIN reservations r ON e.equipment_id = r.equipment_id AND m.member_id = r.member_id WHERE e.equipment_id = ? AND r.reservation_id IS NULL;");
            statement.setInt(1, selectionID[selection.getSelectedIndex()]);
            statement.setInt(2, Integer.valueOf(productCodeSelection.getSelectedItem().toString().substring(3)));
            statement.setString(3, memberName);
            statement.setInt(4, equipmentId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                Messages.equipmentAssignSuccess();
                disableAvailability(equipmentId);
                disableProduct(productCodeSelection.getSelectedItem().toString());
                searchReceipt();
                listReserveEquipmentPanel.retrieveDataFromDatabase();
                revalidate();
                repaint();
            } else {
                Messages.equipmentAssignFailed();
            }

            conn.close();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void disableAvailability(int id) {
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("update equipments set equipment_availability = false where equipment_id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void searchEquipmentPanel(String code){
        for(Component component : formPanel.getComponents()){
            component.setEnabled(true);
        }
    }

     private void disableProduct(String code){
        try(Connection conn = MySQL.getConnection()){
            String receipt = receiptNumberField.getText().toString();
            String trimmedString = receipt.replaceFirst("^0+", "");
            PreparedStatement statement = conn.prepareStatement("UPDATE receipt_codes set is_used = true where receipt_id = ? and product_code = ?");
            statement.setInt(1, Integer.valueOf(trimmedString));
            statement.setString(2, code);
            statement.executeUpdate();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
            Messages.databaseConnectionFailed();
        }
    }

    private void searchReceipt(){
        productCodeSelection.removeAllItems();
        try(Connection conn = MySQL.getConnection()){
            String receipt = receiptNumberField.getText().toString();
            String trimmedString = receipt.replaceFirst("^0+", "");
            PreparedStatement statement = conn.prepareStatement("SELECT * from receipt_codes where receipt_id = ? and is_used = false and left(product_code, 1) = 'E'");
            statement.setString(1, trimmedString);
            ResultSet result = statement.executeQuery();
            boolean found = false;
            while (result.next()) {
                found = true;
                productCodeSelection.setEnabled(true);
                productCodeSelection.addItem(result.getString("product_code"));
            } 
            if(!found) {
                JOptionPane.showMessageDialog(null, "The receipt cannot be found or all of the product codes in this receipt are already used up.", Constants.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            }
            revalidate();
            repaint();
        } catch(SQLException e){
            e.printStackTrace();
            Messages.databaseConnectionFailed();
        }
    }

    private DefaultTableModel initializeMemberModel(DefaultTableModel tableModel, int select){
        String sql = "";
        if (select == 0) {
            sql = "SELECT CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) AS fullname_members FROM members LEFT JOIN reservations ON members.member_id = reservations.member_id WHERE reservations.member_id IS NULL;";
        } else {
           sql = "SELECT CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) AS fullname_members FROM members LEFT JOIN reservations ON members.member_id = reservations.member_id WHERE reservations.member_id IS NULL AND CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) like ?;";
        }
        tableModel = new DefaultTableModel();
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            if(select > 0) statement.setString(1, "%" + searchMemberField.getText() + "%");
            ResultSet result = statement.executeQuery();
            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();

            Object[] columnNames = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames[i - 1] = columnName;
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (result.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = result.getObject(i);
                }
                tableModel.addRow(rowData);
            }
            conn.close();
        } catch (SQLException e) {
           Messages.databaseConnectionFailed();
           e.printStackTrace();
        }
        return tableModel;
    }

    private DefaultTableModel initializeTrainerModel(DefaultTableModel tableModel, int selectionID){
        tableModel = new DefaultTableModel();
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT e.equipment_name, e.equipment_id FROM equipments e LEFT JOIN reservations r ON e.equipment_id = r.equipment_id WHERE r.equipment_id IS NULL AND e.equipment_type_id = ? AND e.equipment_id NOT IN (SELECT equipment_id FROM reservations WHERE equipment_type_id = ?);");
            statement.setInt(1, selectionID);
            statement.setInt(2, selectionID);

            ResultSet result = statement.executeQuery();
            ResultSetMetaData metaData = result.getMetaData();
            int columnCount = metaData.getColumnCount();

            Object[] columnNames = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames[i - 1] = columnName;
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (result.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = result.getObject(i);
                }
                tableModel.addRow(rowData);
            }
            conn.close();
        } catch (SQLException e) {
           Messages.databaseConnectionFailed();
           e.printStackTrace();
        }
        return tableModel;
    }

    public void updateMemberTableData() {
        memberTableModel.setRowCount(0);
        initializeMemberModel(memberTableModel, 0);
        revalidate();
        repaint();
    }
}
