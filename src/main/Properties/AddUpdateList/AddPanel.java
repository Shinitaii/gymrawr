package main.Properties.AddUpdateList;
import java.awt.BorderLayout;

import javax.swing.*;
import main.Properties.AddUpdateList.PanelNames.Member.AddMemberPanel;

public class AddPanel extends JPanel {
    private BackPanel backPanel;

    public AddPanel(AddUpdateListPanel addUpdateListPanel, String panelName, ListPanel listPanel){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);
        setMainPanel(panelName, listPanel);
    }

    private void setMainPanel(String panelName, ListPanel listPanel){
        if(panelName.equals("Member")) member(listPanel);
        else if (panelName.equals("Trainer")) trainer();
        else if (panelName.equals("Equipment")) equipment();
        else staff(); //if panel name is Staff
    }

    private void member(ListPanel listPanel){
        AddMemberPanel memberPanel = new AddMemberPanel(listPanel.getListMemberPanel());
        add(memberPanel, BorderLayout.CENTER);
    }

    private void trainer(){

    }

    private void equipment(){

    }

    private void staff(){

    }
}
