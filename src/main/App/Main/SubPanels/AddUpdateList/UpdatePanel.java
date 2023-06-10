package main.App.Main.SubPanels.AddUpdateList;
import java.awt.BorderLayout;
import javax.swing.*;

import main.App.Main.SubPanels.AddUpdateList.PanelNames.Equipment.UpdateEquipmentPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Member.UpdateMemberPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Staff.UpdateStaffPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer.UpdateTrainerPanel;

public class UpdatePanel extends JPanel{
    private BackPanel backPanel;

    private UpdateEquipmentPanel equipmentPanel;

    private UpdateStaffPanel staffPanel;

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
        UpdateTrainerPanel trainerPanel = new UpdateTrainerPanel();
        add(trainerPanel, BorderLayout.CENTER);
    }

    private void equipment(String panelName){
        equipmentPanel = new UpdateEquipmentPanel();
        add(equipmentPanel, BorderLayout.CENTER);
    }

    private void staff(String panelName){
        staffPanel = new UpdateStaffPanel();
        add(staffPanel, BorderLayout.CENTER);
    }

    public UpdateEquipmentPanel getUpdateEquipmentPanel(){
        return equipmentPanel;
    }
}
