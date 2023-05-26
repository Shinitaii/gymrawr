package main.App.Main.SubPanels.AddUpdateList;
import java.awt.BorderLayout;

import javax.swing.*;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Member.ListMemberPanel;
import main.App.Main.SubPanels.AddUpdateList.PanelNames.Trainer.ListTrainerPanel;

public class ListPanel extends JPanel{
    private BackPanel backPanel;
    private ListMemberPanel memberPanel;
    private ListTrainerPanel trainerPanel;

    public ListPanel(AddUpdateListPanel addUpdateListPanel, String panelName){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);
        setMainPanel(panelName);
    }

    private void setMainPanel(String panelName){
        if(panelName.equals("Member")) member();
        else if (panelName.equals("Trainer")) trainer();
        else if (panelName.equals("Staff")) staff();
        // equipment update and list merged.
    }

    private void member(){
        memberPanel = new ListMemberPanel();
        add(memberPanel, BorderLayout.CENTER);
    }

    private void trainer(){
        trainerPanel = new ListTrainerPanel();
        add(trainerPanel, BorderLayout.CENTER);
    }

    private void staff(){

    }

    public ListMemberPanel getListMemberPanel(){
        return memberPanel;
    }

    public ListTrainerPanel getListTrainerPanel(){
        return trainerPanel;
    }

}
