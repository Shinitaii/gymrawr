package main.Properties.Custom;

import java.awt.event.ActionListener;

import javax.swing.*;

public class CustomPanel extends JPanel{ // custompanel is for client, trainer, equipment and staff panels
    private TextPanel textPanel;
    private ButtonPanel buttonPanel;

    public CustomPanel(String panelName, ImageIcon icon, ImageIcon secondIcon, ActionListener listener){
        textPanel = new TextPanel(panelName, icon);
        add(textPanel);
        buttonPanel = new ButtonPanel();
        addButtons(buttonPanel, panelName, secondIcon, listener);
        add(buttonPanel);
    }

    // creates add, update, and list buttons for the custom panels.
    private void addButtons(ButtonPanel buttonPanel, String name, ImageIcon icon, ActionListener listener){
        buttonPanel.addButton("New " + name, icon, listener, "Add");
        buttonPanel.addButton("Update " + name, icon, listener, "Update");
        buttonPanel.addButton("List of " + name, icon, listener, "List");
    }

}
