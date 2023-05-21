package main.Properties.AddUpdateList;
import javax.swing.*;

public class UpdatePanel extends JPanel{
    private BackPanel backPanel;

    public UpdatePanel(AddUpdateListPanel addUpdateListPanel, String panelName){
        backPanel = new BackPanel(addUpdateListPanel);
        add(backPanel);
    }
}
