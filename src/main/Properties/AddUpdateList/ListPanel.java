package main.Properties.AddUpdateList;
import java.awt.BorderLayout;

import javax.swing.*;

import main.Properties.AddUpdateList.PanelNames.Member.ListMemberPanel;

public class ListPanel extends JPanel{
    private BackPanel backPanel;
    private ListMemberPanel memberPanel;

    public ListPanel(AddUpdateListPanel addUpdateListPanel, String panelName){
        setLayout(new BorderLayout());
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel, BorderLayout.NORTH);
        setMainPanel(panelName);
    }

    private void setMainPanel(String panelName){
        if(panelName.equals("Member")) member();
        else if (panelName.equals("Trainer")) trainer();
        else if (panelName.equals("Equipment")) equipment();
        else staff(); //if panel name is Staff
    }

    private void member(){
        memberPanel = new ListMemberPanel();
        add(memberPanel, BorderLayout.CENTER);
    }

    private void trainer(){

    }

    private void equipment(){

    }

    private void staff(){

    }

    public ListMemberPanel getListMemberPanel(){
        return memberPanel;
    }

}
