package main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.App.Main.SubPanels.AddUpdateList.*;
import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Objects.Products;
import main.Objects.Training;
import main.Properties.Custom.CustomButton;

public class AssignTrainerPanel extends JPanel{

    private BackPanel backPanel; 
    private JTable memberTable, trainerTable;
    private DefaultTableModel memberTableModel, trainerTableModel;
    private JScrollPane memberScrollPane, trainerScrollPane;

    private JTextField searchMemberField;
    private CustomButton searchMember, searchTrainer, assign;
    private JComboBox<String> selection, productSelection;

    private int[] selectionID, selectionDate;

    private JPanel mainPanel;

    public AssignTrainerPanel(AddUpdateListPanel addUpdateListPanel){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);

        setMainPanel();
    }

    private void setMainPanel(){
        mainPanel = new JPanel(new GridBagLayout());
        add(mainPanel, BorderLayout.CENTER);
        setMemberTable();
        setTrainerTable();
        setButtons();
        CommonComponent.addComponent(mainPanel, memberScrollPane, 0, 0, 2, 1);
        CommonComponent.addComponent(mainPanel, trainerScrollPane, 2, 0, 2, 1);
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
        searchMemberField = CommonComponent.configureTextField(mainPanel, "Search member", 0, 1, 1, 1);
        searchMember = new CustomButton("Search member", null, e -> searchMember());
        CommonComponent.addComponent(mainPanel, searchMember, 0, 4, 1, 1);
        selectionID = new int[Training.getTrainingList().size()];
        selection = CommonComponent.configureTrainingComboBox(selection, selectionID);
        CommonComponent.addComponent(mainPanel, selection, 2, 1, 1, 1);
        selectionDate = new int[Products.getProductList().size()];
        productSelection = CommonComponent.configureProductComboBox(productSelection, selectionDate, 2);
        CommonComponent.addComponent(mainPanel, productSelection, 3, 1, 1, 1);
        searchTrainer = new CustomButton("Search trainers", null, e -> searchTrainers(selectionID[selection.getSelectedIndex()]));
        CommonComponent.addComponent(mainPanel, searchTrainer, 2, 4, 1, 1);
        assign = new CustomButton("Assign trainer to member", null, e ->assignTrainer());
        CommonComponent.addComponent(mainPanel, assign, 3, 4, 1, 1);
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
        String memberName = getName(memberTable);
        String trainerName = getName(trainerTable);
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("INSERT INTO appointments SELECT null, (SELECT trainer_id from trainers), (SELECT member_id from members), CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY) FROM members, trainers WHERE CONCAT(members.member_firstname, ' ', members.member_middlename, ' ', members.member_lastname) = ? and CONCAT(trainers.trainer_firstname, ' ', trainers.trainer_middlename, ' ', trainers.trainer_lastname) = ?");
            statement.setInt(1, selectionDate[productSelection.getSelectedIndex()]);
            statement.setString(2, memberName);
            statement.setString(3, trainerName);
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0) Messages.trainerAssignSuccess();
            else Messages.trainerAssignFailed();
            conn.close();
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
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

    private DefaultTableModel initializeMemberModel(DefaultTableModel tableModel, int select){
        String sql = "";
        if(select == 0) sql = "SELECT CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) AS fullname_members FROM members LEFT JOIN appointments ON members.member_id = appointments.member_id WHERE appointments.member_id IS NULL;";
        else sql = "SELECT CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) AS fullname_members FROM members LEFT JOIN appointments ON members.member_id = appointments.member_id WHERE appointments.member_id IS NULL AND CONCAT(member_firstname, ' ', member_middlename, ' ', member_lastname) = ?;";
        tableModel = new DefaultTableModel();
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(sql);
            if(select > 0) statement.setString(1, searchMemberField.getText());
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
            PreparedStatement statement = conn.prepareStatement("SELECT CONCAT(trainer_firstname, ' ', trainer_middlename, ' ', trainer_lastname) AS fullname_trainers FROM trainers LEFT JOIN appointments ON trainers.trainer_id = appointments.trainer_id WHERE appointments.trainer_id IS NULL and trainers.training_id = ?;");
            statement.setInt(1, selectionID);
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
    }
}
