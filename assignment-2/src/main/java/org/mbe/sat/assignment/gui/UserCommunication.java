package org.mbe.sat.assignment.gui;

import javax.swing.*;

public class UserCommunication {

    public static void errorDialog(String title, String message){
        //check input

        if(title==null || title.isEmpty() || title.equalsIgnoreCase(" ")){
            title="ERROR-DIALOG";
        }

        if(message==null || message.isEmpty() || message.equalsIgnoreCase(" ")){
            message="Something went wrong!";
        }

        //show dialog

        JOptionPane.showMessageDialog(null,
                message, title, JOptionPane.ERROR_MESSAGE);
    }


    public static boolean confirmAction(String title, String message){
        boolean result=false;

        //check input

        if(title==null || title.isEmpty() || title.equalsIgnoreCase(" ")){
            title="CONFIRM-DIALOG : ";
        }

        if(message==null || message.isEmpty() || message.equalsIgnoreCase(" ")){
            message="Please confirm!";
        }

        int choice=JOptionPane.showConfirmDialog(null,
                message, title, JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if(choice==JOptionPane.YES_OPTION){
            result=true;
        }

        return result;
    }

}
