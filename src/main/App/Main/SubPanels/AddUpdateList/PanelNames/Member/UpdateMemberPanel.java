package main.App.Main.SubPanels.AddUpdateList.PanelNames.Member;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.Database.MySQL;
import main.Miscellanous.*;
import main.Properties.Focus;
import main.Properties.Custom.*;

public class UpdateMemberPanel extends JPanel{
    private JPanel searchMemberPanel, editMemberPanel;
    private TextLabel fullNameLabel;
    private JTextField fullNameField, receiptNumberField;
    private CustomButton check;

    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private CustomButton update, clear, cancel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JComboBox<String> selection;
    private CustomButton search;
    private JCheckBox ifRenewal;
    private TextLabel duration;

    public UpdateMemberPanel(){
        searchMemberPanel = new JPanel(new GridBagLayout());
        add(searchMemberPanel);

        tableModel = new DefaultTableModel();
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int selectedRow = target.getSelectedRow();
    
                if (selectedRow >= 0) {
                    checkMember(target.getValueAt(selectedRow, 0).toString());
                    searchMemberPanel.setVisible(false);
                    editMemberPanel.setVisible(true);
                }
            }
        });
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(350,250));
        CommonComponent.addComponent(searchMemberPanel, scrollPane, 0, 0, 3, 1);
        
        fullNameLabel = new TextLabel("Enter name:", 12);
        CommonComponent.addComponent(searchMemberPanel, fullNameLabel, 0, 1, 1, 1);
        fullNameField = CommonComponent.configureTextField(searchMemberPanel, "Name", 1, 1, 1, 1);
        check = new CustomButton("Check member", null, e -> retrieveDataFromDatabase());
        CommonComponent.addComponent(searchMemberPanel, check, 2, 1, 1,1);
        ifRenewal = new JCheckBox("Does the user ordered a membership for renewal?");
        ifRenewal.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if(!ifRenewal.isSelected()){
                    receiptNumberField.setEnabled(false);
                    selection.setEnabled(false);
                    search.setEnabled(false);
                    selection.removeAllItems();
                } else {
                    receiptNumberField.setEnabled(true);
                    selection.setEnabled(true);
                    search.setEnabled(true);
                }
                revalidate();
                repaint();
            }   
        });
        CommonComponent.addComponent(searchMemberPanel, ifRenewal, 0, 2, 3, 1);
        TextLabel instruction = new TextLabel("After results shown from the table, click any one of them to edit their details.", 12);
        CommonComponent.addComponent(searchMemberPanel, instruction, 0, 3, 3, 1);
        editMemberPanel = new JPanel(new GridBagLayout());
        add(editMemberPanel);
        editMemberPanel.setVisible(false);

        //firstname txtfield
        TextLabel firstNameLabel = new TextLabel("First name:", 12);
        CommonComponent.addComponent(editMemberPanel, firstNameLabel, 0,0,1,1);
        firstNameField = CommonComponent.configureTextField(editMemberPanel, "First name", 1, 0, 4, 1);
        //middlename
        TextLabel middleNameLabel = new TextLabel("Middle name:", 12);
        CommonComponent.addComponent(editMemberPanel, middleNameLabel, 0,1,1,1);
        middleNameField = CommonComponent.configureTextField(editMemberPanel, "Middle name", 1, 1, 4, 1);
        //lastname
        TextLabel lastNameLabel = new TextLabel("Last name:", 12);
        CommonComponent.addComponent(editMemberPanel, lastNameLabel, 0,2,1,1);
        lastNameField = CommonComponent.configureTextField(editMemberPanel, "Last name", 1, 2, 4, 1); 

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(editMemberPanel, birthLabel, 0, 3, 1, 1);         
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(editMemberPanel, dateField, 1, 3, 1, 1); 
        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(editMemberPanel, contactLabel, 2, 3, 1, 1); 
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(editMemberPanel, contactNumberField, 3, 3, 3, 1);  

        TextLabel receiptNumberLabel = new TextLabel("Receipt Number:", 12);
        CommonComponent.addComponent(editMemberPanel, receiptNumberLabel, 0, 4, 1, 1);
        receiptNumberField = CommonComponent.configureReceiptNumberField();
        CommonComponent.addComponent(editMemberPanel, receiptNumberField, 1, 4, 2, 1);
        selection = new JComboBox<>();
        CommonComponent.addComponent(editMemberPanel, selection, 3, 4, 1, 1);
        search = new CustomButton("Search receipt", null, e ->searchReceipt());
        CommonComponent.addComponent(editMemberPanel, search, 4, 4, 1, 1);
        TextLabel durationLabel = new TextLabel("Membership duration in days left:",12);
        CommonComponent.addComponent(editMemberPanel, durationLabel, 0, 5, 1, 1);
        duration = new TextLabel("", 12);
        duration.setForeground(Color.RED);
        CommonComponent.addComponent(editMemberPanel, duration, 1, 5, 1, 1);
        update = new CustomButton("Update", null, e -> updateMember());  
        CommonComponent.addComponent(editMemberPanel, update, 2, 5, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(editMemberPanel, clear, 3, 5, 1, 1);
        cancel = new CustomButton("Cancel", null, e-> cancel());
        CommonComponent.addComponent(editMemberPanel, cancel, 4,5, 1, 1);

        receiptNumberField.setEnabled(false);
        selection.setEnabled(false);
        search.setEnabled(false);
    }

    private void searchReceipt(){
        selection.removeAllItems();
        try(Connection conn = MySQL.getConnection()){
            String receipt = receiptNumberField.getText().toString();
            String trimmedString = receipt.replaceFirst("^0+", "");
            PreparedStatement statement = conn.prepareStatement("SELECT * from receipt_codes where receipt_id = ? and is_used = false and left(product_code, 1) = 'M'");
            statement.setString(1, trimmedString);
            ResultSet result = statement.executeQuery();
            boolean found = false;
            while (result.next()) {
                found = true;
                selection.setEnabled(true);
                selection.addItem(result.getString("product_code"));
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

    private void retrieveDataFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT concat(member_firstname, ' ', member_middlename, ' ', member_lastname) as fullname FROM members where concat(member_firstname, ' ', member_middlename, ' ', member_lastname) like ?");
            statement.setString(1, "%" +fullNameField.getText()+"%");
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Object[] columnNames = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames[i - 1] = columnName;
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setModel(tableModel);

        table.repaint();
        table.revalidate();
    }

    private void checkMember(String foundName){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select member_firstname, member_middlename, member_lastname,member_birthdate, member_contactnumber, datediff(membership_enddate, curdate()) duration from members where concat(member_firstname, ' ', member_middlename, ' ', member_lastname) like ?");
            statement.setString(1, "%"+foundName+"%");
            ResultSet result = statement.executeQuery();
            if(result.next()){
                firstNameField.setText(result.getString("member_firstname"));
                middleNameField.setText(result.getString("member_middlename"));
                lastNameField.setText(result.getString("member_lastname"));
                dateField.setText(result.getString(String.valueOf("member_birthdate")));
                contactNumberField.setText(String.valueOf(result.getLong("member_contactnumber")));
                duration.setText(result.getInt("duration") + " days left.");
                repaint();
                revalidate();
            }
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }
    
    private int determineCode(){
        if(!ifRenewal.isSelected()) return 0;
        else return Integer.valueOf(selection.getSelectedItem().toString().substring(3));
        
    }

    private void updateMember(){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("update members set member_firstname = ?, member_middlename = ?, member_lastname = ?, member_birthdate = ?, member_contactnumber = ?, membership_enddate = DATE_ADD(membership_enddate, INTERVAL ? DAY) where concat(member_firstname, ' ', member_middlename, ' ', member_lastname) like ?");
            statement.setString(1, firstNameField.getText());
            statement.setString(2, middleNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setDate(4, Date.valueOf(dateField.getText()));
            statement.setLong(5, Long.valueOf(contactNumberField.getText()));
            statement.setInt(6, Integer.valueOf(determineCode()));
            statement.setString(7, "%" + fullNameField.getText() + "%");
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                Messages.memberUpdated();
                disableProduct(selection.getSelectedItem().toString());
                searchReceipt();
                editMemberPanel.setVisible(false);
                searchMemberPanel.setVisible(true);
                clearForm();
                cancel();
                revalidate();
                repaint();
            } else Messages.memberNotUpdated();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void cancel(){
        searchMemberPanel.setVisible(true);
        editMemberPanel.setVisible(false);
        revalidate();
        repaint();
    }

    private void clearForm(){
        Focus.setPlaceholder(firstNameField, "First name");
        Focus.setPlaceholder(middleNameField, "Middle name");
        Focus.setPlaceholder(lastNameField, "Last name");
        dateField.setText("");
        contactNumberField.setText("0");
    }
}