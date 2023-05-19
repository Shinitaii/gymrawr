package main.Properties;

import java.awt.event.*;

public class CustomListener implements ActionListener{

    private int panelNumber;

    public CustomListener(int panelNumber){
        this.panelNumber = panelNumber;
    }

    public void actionPerformed(ActionEvent e) {
        switch(this.panelNumber){
            case 0: memberListener(); break; //member
            case 1: trainerListener(); break; //trainer
            case 2: equipmentListener(); break; //equipment
            case 3: staffListener(); break; //staff
        }
    }

    private void memberListener(){

    }

    private void trainerListener(){

    }

    private void equipmentListener(){

    }

    private void staffListener(){
        
    }
    
}
