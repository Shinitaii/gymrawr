package main.App.Main.SubPanels.AddUpdateList;
import java.awt.BorderLayout;

import javax.swing.*;

import main.Miscellanous.Constants;
import main.Properties.Custom.CustomButton;
import main.Properties.Custom.CustomPanel;

public class BackPanel extends JPanel{
    private CustomButton backButton;

    public BackPanel(AddUpdateListPanel addUpdateListPanel){
        setLayout(new BorderLayout());
        backButton = new CustomButton("Back", Constants.scaledImage(Constants.BACK_ICON), e -> addUpdateListPanel.showMain());
        add(backButton, BorderLayout.WEST);
    }

    public BackPanel(CustomPanel customPanel){
        backButton = new CustomButton("Back", Constants.scaledImage(Constants.BACK_ICON), e -> customPanel.showMain());
        add(backButton, BorderLayout.WEST);
    }
}
