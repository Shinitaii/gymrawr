package main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.App.Main.SubPanels.AddUpdateList.*;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Constants;
import main.Miscellanous.Messages;
import main.Objects.Training;
import main.Properties.Custom.CustomButton;
import main.Properties.Custom.TextLabel;

public class AssignTrainerPanel extends JPanel{

    private BackPanel backPanel; 
    private JTable memberTable, trainerTable;
    private DefaultTableModel memberTableModel, trainerTableModel;
    private JScrollPane memberScrollPane, trainerScrollPane;

    private JTextField searchMemberField;
    private CustomButton searchMember, searchTrainer, assign;
    private JComboBox<String> selection;

    private int[] selectionID;

    private JPanel mainPanel, searchReceiptPanel, formPanel;
    private JTextField receiptNumberField;
    private CustomButton search;
    private JComboBox<String> productCodeSelection;
    private CustomButton useProductCode;
    private String code;

    private ListAssignedTrainerPanel listAssignedTrainerPanel;

    public AssignTrainerPanel(AddUpdateListPanel addUpdateListPanel, ListAssignedTrainerPanel listAssignedTrainerPanel){
        this.listAssignedTrainerPanel = listAssignedTrainerPanel;
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);

        setMainPanel();
    }

    private void setMainPanel(){
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
        useProductCode = new CustomButton("Use product code", null, e -> searchMemberPanel(String.valueOf(productCodeSelection.getSelectedItem())));
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

    private void searchMemberPanel(String code){
        this.code = code;
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
            PreparedStatement statement = conn.prepareStatement("SELECT * from receipt_codes where receipt_id = ? and is_used = false and left(product_code, 1) = 'T'");
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

    private void setButtons(){
        searchMemberField = CommonComponent.configureTextField(formPanel, "Search member", 0, 1, 1, 1);
        searchMember = new CustomButton("Search member", null, e -> searchMember());
        CommonComponent.addComponent(formPanel, searchMember, 0, 4, 1, 1);
        selectionID = new int[Training.getTrainingList().size()];
        selection = CommonComponent.configureTrainingComboBox(selection, selectionID);
        CommonComponent.addComponent(formPanel, selection, 2, 1, 1, 1);
        searchTrainer = new CustomButton("Search trainers", null, e -> searchTrainers(selectionID[selection.getSelectedIndex()]));
        CommonComponent.addComponent(formPanel, searchTrainer, 2, 4, 1, 1);
        assign = new CustomButton("Assign trainer to member", null, e ->assignTrainer());
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

    private void assignTrainer(){
        int duration = Integer.valueOf(code.substring(3));
        int memberID = getName(memberTable);
        int trainerID = getName(trainerTable);
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("insert into appointments values (null, ?, ?, ?, curdate(), date_add(curdate(), interval ? day))");
            statement.setInt(1, trainerID);
            statement.setInt(2, selectionID[selection.getSelectedIndex()]);
            statement.setInt(3, memberID);
            statement.setInt(4, duration);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                Messages.trainerAssignSuccess();
                for (Component component : formPanel.getComponents()) {
                    component.setEnabled(false);
                }
                disableProduct(code);
                searchReceipt();
                listAssignedTrainerPanel.retrieveDataFromDatabase(trainerID, memberID, selectionID[selection.getSelectedIndex()]);
            } else {
                Messages.trainerAssignFailed();
            }
            conn.close();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private int getName(JTable table){
        int selectedRow = table.getSelectedRow();
        int selectedColumn = table.getSelectedColumn();

        if (selectedRow != -1 && selectedColumn != -1) {
            Object selectedValue = table.getValueAt(selectedRow, 0);
    
            if (selectedValue != null) {
                return Integer.valueOf(selectedValue.toString());
            }
        }

        return -1;
    }

    private DefaultTableModel initializeMemberModel(DefaultTableModel tableModel, int select){
        String sql = "";
        if(select == 0) sql = "SELECT members.member_id, CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) AS fullname_members FROM members LEFT JOIN appointments ON members.member_id = appointments.member_id WHERE appointments.member_id IS NULL;";
        else sql = "SELECT members.member_id, CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) AS fullname_members FROM members LEFT JOIN appointments ON members.member_id = appointments.member_id WHERE appointments.member_id IS NULL AND CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) like ?;";
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
            PreparedStatement statement = conn.prepareStatement("SELECT t.trainer_id, CONCAT(t.trainer_firstname, ' ', t.trainer_middlename, ' ', t.trainer_lastname) AS fullname_trainers FROM trainer_specialization ts JOIN trainers t ON ts.trainer_id = t.trainer_id LEFT JOIN appointments a ON ts.trainer_id = a.trainer_id WHERE a.trainer_id IS NULL AND ts.training_id = ? AND t.trainer_id NOT IN (SELECT trainer_id FROM appointments WHERE training_id = ?);");
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
