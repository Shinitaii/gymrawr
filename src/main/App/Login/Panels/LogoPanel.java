package main.App.Login.Panels;
import javax.swing.*;
import java.awt.*;
import main.Miscellanous.Constants;

public class LogoPanel extends JPanel{

    public LogoPanel(){
        setLayout(new BorderLayout());
        JLabel label = new JLabel(Constants.APP_ICON);
        add(label, BorderLayout.NORTH);
        JLabel login = new JLabel("Log-in");
        add(login, BorderLayout.SOUTH);
    }

}
