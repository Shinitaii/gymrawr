package main.Properties.AddUpdateList;
import javax.swing.*;

public class ListPanel extends JPanel{
    private BackPanel backPanel;

    public ListPanel(AddUpdateListPanel addUpdateListPanel, String panelName){
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel);
    }
}
