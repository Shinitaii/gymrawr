package main.App.Main.SubPanels.AddUpdateList;
import java.awt.BorderLayout;

import javax.swing.*;

import main.App.Main.SubPanels.AddUpdateList.PanelNames.Equipment.AddEquipmentPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Member.AddMemberPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer.AddTrainerPanel;

public class AddPanel extends JPanel {
    private BackPanel backPanel;

    public AddPanel(AddUpdateListPanel addUpdateListPanel, String panelName, UpdatePanel updatePanel, ListPanel listPanel){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);
        setMainPanel(panelName, updatePanel, listPanel);
    }

    private void setMainPanel(String panelName, UpdatePanel updatePanel, ListPanel listPanel){
        if(panelName.equals("Member")) member(listPanel);
        else if (panelName.equals("Trainer")) trainer(listPanel);
        else if (panelName.equals("Equipment")) equipment(updatePanel, listPanel);
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

    private void equipment(UpdatePanel updatePanel, ListPanel listPanel){
        AddEquipmentPanel equipmentPanel = new AddEquipmentPanel(updatePanel, listPanel);
        add(equipmentPanel, BorderLayout.CENTER);
    }

    private void staff(){

    }
}
