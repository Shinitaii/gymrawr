package main.App.Main.SubPanels.AddUpdateList;
import java.awt.BorderLayout;

import javax.swing.*;

import main.App.Main.SubPanels.AddUpdateList.PanelNames.Member.AddMemberPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer.AddTrainerPanel;

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
        else if (panelName.equals("Trainer")) trainer(listPanel);
        else if (panelName.equals("Equipment")) equipment();
        else staff(); //if panel name is Staff
    }

    private void member(ListPanel listPanel){
        AddMemberPanel memberPanel = new AddMemberPanel(listPanel.getListMemberPanel());
        add(memberPanel, BorderLayout.CENTER);
    }

    private void trainer(ListPanel listPanel){
        AddTrainerPanel trainerPanel = new AddTrainerPanel(listPanel);
        add(trainerPanel, BorderLayout.CENTER);
    }

    private void equipment(){

    }

    private void staff(){

    }
}
