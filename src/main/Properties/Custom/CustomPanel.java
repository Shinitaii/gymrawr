package main.Properties.Custom;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

public class CustomPanel extends JPanel{ // custompanel is for client, trainer, equipment and staff panels
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private TextPanel textPanel;
    private ButtonPanel buttonPanel;

    public CustomPanel(String panelName, ImageIcon icon, int size){
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, "Main");
        textPanel = new TextPanel(panelName, icon, size);
        mainPanel.add(textPanel, BorderLayout.NORTH);
        buttonPanel = new ButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    protected ButtonPanel getButtonPanel(){
        return buttonPanel;
    }

    public CardLayout getCardLayout(){
        return cardLayout;
    }

    public void showMain(){
        getCardLayout().show(this, "Main");
    }

    public void addButton(String name, ImageIcon icon, ActionListener listener){
        getButtonPanel().addButton(name, icon, listener);
    }

    public void removeButton(String name){
        getButtonPanel().removeButton(name);
    }
}
