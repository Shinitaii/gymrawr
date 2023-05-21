package main.Properties.AddUpdateList;
import javax.swing.*;

import main.Miscellanous.Constants;
import main.Properties.Custom.CustomButton;

public class BackPanel extends JPanel{
    private CustomButton backButton;

    public BackPanel(AddUpdateListPanel addUpdateListPanel){
        backButton = new CustomButton("Back", Constants.BACK_ICON, e -> addUpdateListPanel.showMain());
        add(backButton);
    }
}
