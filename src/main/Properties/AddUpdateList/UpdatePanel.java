package main.Properties.AddUpdateList;
import java.awt.BorderLayout;

import javax.swing.*;

import main.Properties.AddUpdateList.PanelNames.Member.UpdateMemberPanel;

public class UpdatePanel extends JPanel{
    private BackPanel backPanel;

    public UpdatePanel(AddUpdateListPanel addUpdateListPanel, String panelName){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);
        setMainPanel(panelName);
    }

    private void setMainPanel(String panelName){
        if(panelName.equals("Member")) member(panelName);
        else if (panelName.equals("Trainer")) trainer(panelName);
        else if (panelName.equals("Equipment")) equipment(panelName);
        else staff(panelName); //if panel name is Staff
    }

    private void member(String panelName){
        UpdateMemberPanel memberPanel = new UpdateMemberPanel();
        add(memberPanel, BorderLayout.CENTER);
    }

    private void trainer(String panelName){

    }

    private void equipment(String panelName){

    }

    private void staff(String panelName){

    }
}
