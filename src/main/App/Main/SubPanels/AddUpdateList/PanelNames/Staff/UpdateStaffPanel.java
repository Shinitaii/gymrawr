package main.App.Main.SubPanels.AddUpdateList.PanelNames.Staff;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.Database.MySQL;
import main.Miscellanous.CommonComponent;
import main.Miscellanous.Messages;
import main.Properties.Focus;
import main.Properties.Custom.CustomButton;
import main.Properties.Custom.TextLabel;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class UpdateStaffPanel extends JPanel{
    private JPanel searchMemberPanel, editMemberPanel;
    private TextLabel fullNameLabel;
    private JTextField fullNameField;

    private JTextField firstNameField, middleNameField, lastNameField;
    private JFormattedTextField dateField, contactNumberField;
    private CustomButton update, clear, cancel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private CustomButton check;

    private JComboBox<String> educationLevelBox, userLevel;
    private JCheckBox showPassword;
    private JTextField addressField, usernameField;
    private JPasswordField passwordField;

    public UpdateStaffPanel(){
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
                    checkStaff(target.getValueAt(selectedRow, 0).toString());
                    searchMemberPanel.setVisible(false);
                    editMemberPanel.setVisible(true);
                }
            }
        });
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(350,250));
        CommonComponent.addComponent(searchMemberPanel, scrollPane, 0, 0, 3, 1);
        
        fullNameLabel = new TextLabel("Enter username:", 12);
        CommonComponent.addComponent(searchMemberPanel, fullNameLabel, 0, 1, 1, 1);
        fullNameField = CommonComponent.configureTextField(searchMemberPanel, "Name", 1, 1, 1, 1);
        check = new CustomButton("Check user", null, e -> retrieveDataFromDatabase());
        CommonComponent.addComponent(searchMemberPanel, check, 2, 1, 1,1);
        TextLabel instruction = new TextLabel("After results shown from the table, click any one of them to edit their details.", 12);
        CommonComponent.addComponent(searchMemberPanel, instruction, 0, 3, 3, 1);
        
        editMemberPanel = new JPanel(new GridBagLayout());
        add(editMemberPanel);
        editMemberPanel.setVisible(false);

        //firstname txtfield
        TextLabel firstNameLabel = new TextLabel("First name:", 12);
        CommonComponent.addComponent(editMemberPanel, firstNameLabel, 0,0,1,1);
        firstNameField = CommonComponent.configureTextField(editMemberPanel, "First name", 1, 0, 1, 1);
        //middlename
        TextLabel middleNameLabel = new TextLabel("Middle name:", 12);
        CommonComponent.addComponent(editMemberPanel, middleNameLabel, 0,1,1,1);
        middleNameField = CommonComponent.configureTextField(editMemberPanel, "Middle name", 1, 1, 1, 1);
        //lastname
        TextLabel lastNameLabel = new TextLabel("Last name:", 12);
        CommonComponent.addComponent(editMemberPanel, lastNameLabel, 0,2,1,1);
        lastNameField = CommonComponent.configureTextField(editMemberPanel, "Last name", 1, 2, 1, 1); 
        TextLabel addressLabel = new TextLabel("Address: ", 12);
        CommonComponent.addComponent(editMemberPanel, addressLabel, 0, 3, 1, 1);
        addressField = CommonComponent.configureTextField(editMemberPanel, "Address", 1, 3, 1, 1);
        TextLabel usernameLabel = new TextLabel("Username:", 12);
        CommonComponent.addComponent(editMemberPanel, usernameLabel, 0, 5, 1, 1);
        usernameField = CommonComponent.configureTextField(editMemberPanel, "Username", 1, 5,1,1);
        TextLabel passwordLabel = new TextLabel("Password", 12);
        CommonComponent.addComponent(editMemberPanel, passwordLabel, 0, 6, 1, 1);
        passwordField = new JPasswordField(25);
        Focus.setPlaceholder(passwordField, "Password");
        passwordField.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e){
                String password = String.valueOf(passwordField.getPassword());
                if(!showPassword.isSelected())passwordField.setEchoChar('●');
                if(password.equals("Password")) passwordField.setText("");
                passwordField.setForeground(Color.BLACK);
            }
            public void focusLost(FocusEvent e){
                String password = String.valueOf(passwordField.getPassword());
                if(password.isEmpty()) Focus.setPlaceholder(passwordField, "Password");
            }
        });
        CommonComponent.addComponent(editMemberPanel, passwordField, 1, 6, 1, 1);

        TextLabel birthLabel = new TextLabel("Birth Date: (YYYY/MM/DD)", 12);
        CommonComponent.addComponent(editMemberPanel, birthLabel, 2, 0, 1, 1);         
        dateField = CommonComponent.configureDateField(dateField);
        CommonComponent.addComponent(editMemberPanel, dateField,3, 0, 1, 1); 
        TextLabel contactLabel = new TextLabel("Contact Number: ", 12);
        CommonComponent.addComponent(editMemberPanel, contactLabel, 2, 1, 1, 1); 
        contactNumberField = CommonComponent.configureContactNumberField(contactNumberField);
        CommonComponent.addComponent(editMemberPanel, contactNumberField, 3, 1, 1, 1);
        TextLabel educationLevelLabel = new TextLabel("Education Level:", 12);
        CommonComponent.addComponent(editMemberPanel, educationLevelLabel, 2, 2, 1,1); 
        educationLevelBox = new JComboBox<>(new String[] {"Elementary", "High School", "Senior High", "Vocational", "University"});
        CommonComponent.addComponent(editMemberPanel, educationLevelBox, 3, 2, 1,1); 
        TextLabel userLevelLabel = new TextLabel("Role:", 12);
        CommonComponent.addComponent(editMemberPanel, userLevelLabel,2, 3, 1,1); 
        userLevel = new JComboBox<>(new String[] {"Admin", "Staff"});
        CommonComponent.addComponent(editMemberPanel, userLevel, 3, 3, 1, 1);
        showPassword = new JCheckBox("Show password");
        showPassword.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (showPassword.isSelected()) passwordField.setEchoChar((char)0);
                else 
                    if(!String.valueOf(passwordField.getPassword()).equals("Password"))passwordField.setEchoChar('●');
            }
        });
        CommonComponent.addComponent(editMemberPanel, showPassword, 2, 6, 1, 1);   
        update = new CustomButton("Update", null, e -> updateStaff());  
        CommonComponent.addComponent(editMemberPanel, update, 2, 9, 1, 1); 
        clear = new CustomButton("Clear", null, e -> clearForm());
        CommonComponent.addComponent(editMemberPanel, clear, 3, 9, 1, 1);
        cancel = new CustomButton("Cancel", null, e-> cancel());
        CommonComponent.addComponent(editMemberPanel, cancel, 4,9, 1, 1);
    }

    private void retrieveDataFromDatabase() {
        tableModel.setRowCount(0);
        try (Connection conn = MySQL.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("SELECT user_username as username from users where user_username like ?");
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

    private void checkStaff(String foundName){
        try (Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("select s.staff_firstname, s.staff_middlename, s.staff_lastname, s.staff_birthdate, s.staff_contactnumber, s.staff_address, s.staff_educlevel, u.user_username, u.user_password from users u join staffs s on s.user_id = u.user_id where u.user_username like ?;");
            statement.setString(1, "%"+foundName+"%");
            ResultSet result = statement.executeQuery();
            if(result.next()){
                firstNameField.setText(result.getString("s.staff_firstname"));
                middleNameField.setText(result.getString("s.staff_middlename"));
                lastNameField.setText(result.getString("s.staff_lastname"));
                dateField.setText(result.getString(String.valueOf("s.staff_birthdate")));
                contactNumberField.setText(String.valueOf(result.getLong("s.staff_contactnumber")));
                addressField.setText(result.getString("s.staff_address"));
                educationLevelBox.setSelectedItem(result.getString("s.staff_educlevel"));
                usernameField.setText(result.getString("u.user_username"));
                passwordField.setText(result.getString("u.user_password"));
                repaint();
                revalidate();
            }
        } catch (SQLException e) {
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }
    
    private void updateStaff(){
        try(Connection conn = MySQL.getConnection()){
            PreparedStatement statement = conn.prepareStatement("update staff set staff_firstname = ?, staff_middlename = ?, staff_lastname = ?, staff_birthdate = ?, staff_contactnumber = ?, staff_address = ?, staff_educlevel = ? where concat(staff_firstname, ' ', staff_middlename, ' ', staff_lastname) like ?");
            statement.setString(1, firstNameField.getText());
            statement.setString(2, middleNameField.getText());
            statement.setString(3, lastNameField.getText());
            statement.setDate(4, Date.valueOf(dateField.getText()));
            statement.setLong(5, Long.valueOf(contactNumberField.getText()));
            statement.setString(6, addressField.getText());
            statement.setString(7, educationLevelBox.getSelectedItem().toString());
            statement.setString(8, "%" + fullNameField.getText() + "%");
            int rowsAffected = statement.executeUpdate();
            if(rowsAffected > 0){
                Messages.memberUpdated();
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
        Focus.setPlaceholder(addressField, "Address");
        Focus.setPlaceholder(usernameField, "Username");
        Focus.setPlaceholder(passwordField, "Password");
        dateField.setText("");
        contactNumberField.setText("0");
    }
    
}
