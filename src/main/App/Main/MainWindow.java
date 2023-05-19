package main.App.Main;

import javax.swing.*;
import main.Objects.User;
import main.Miscellanous.Constants;

public class MainWindow {

    private JFrame window;
    private MainPanel mainPanel;

    public MainWindow(User user){
        window = new JFrame(Constants.APP_TITLE);
        window.setIconImage(Constants.APP_ICON.getImage());
        mainPanel = new MainPanel(user);
        window.add(mainPanel);

        window.setSize(800,500);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
    }

    public JFrame getWindow() {
        return window;
    }
}
