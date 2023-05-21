package main.Properties.AddUpdateList.PanelNames;
import java.awt.GridLayout;

import javax.swing.*;

import main.Properties.Focus;

public class MemberPanel extends JPanel{
    private JTextField firstNameField, middleNameField, lastNameField;
    
    public MemberPanel(String command, String panelName){
        if(command.equals("Add")) add(panelName); 
        else if(command.equals("Update")) update(panelName);
        else list(panelName);   
    }

    private void add(String panelName){
        setLayout(new GridLayout(0,1, 20, 20));
        firstNameField = new JTextField(30);
        Focus.setPlaceholder(firstNameField, "First name");
        firstNameField.addFocusListener(new Focus(firstNameField, "First name"));
        add(firstNameField);
        middleNameField = new JTextField(30);
        Focus.setPlaceholder(middleNameField, "Middle name");
        middleNameField.addFocusListener(new Focus(middleNameField, "Middle name"));
        add(middleNameField);
        lastNameField = new JTextField(30);
        Focus.setPlaceholder(lastNameField, "Last name");
        lastNameField.addFocusListener(new Focus(lastNameField, "Last name"));   
        add(lastNameField);
    }

    private void update(String panelName){

    }

    private void list(String panelName){
        
    }
}
