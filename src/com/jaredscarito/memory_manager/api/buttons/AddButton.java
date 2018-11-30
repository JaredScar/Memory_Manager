package com.jaredscarito.memory_manager.api.buttons;

import com.jaredscarito.memory_manager.api.API;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javax.swing.*;
import java.util.Arrays;

public class AddButton implements EventHandler<MouseEvent> {

    @Override
   public void handle(MouseEvent event) {

        String Pid;
        // get process id number
        Pid = API.getInstance().getSelectedPid();
        //get empty spaces and sizes
        int pDimensions[][] = API.getInstance().getEmptySpaces();
        Arrays.toString(pDimensions);
        //get process size
        int pSize = API.getInstance().getInputMemSize();
        //add block
        boolean added = false;
        //find first hole
        int startY = pDimensions[0][0];
        int size = pDimensions[0][1];
        for(int p = 0;p<pDimensions.length;p++)
        {//find the first hole in memory
            if(pDimensions[p][0]< startY && pDimensions[p][1]>=pSize)
            {
                startY = pDimensions[p][0];
                size = pDimensions[p][1];

            }
        }
        if(size >= pSize)//check hole is big enough for process
        {
            API.getInstance().addBlock(Pid, pSize, startY);
            added = true;//add process
        }
        if(added == false)//failure to add block
        {
            JOptionPane.showMessageDialog(null, "Process not added. Memory block not large enough");
        }
       








    }
    public static void firstFit(){
        String Pid;
        // get process id number
        Pid = API.getInstance().getSelectedPid();
        //get empty spaces and sizes
        int pDimensions[][] = API.getInstance().getEmptySpaces();
        Arrays.toString(pDimensions);
        //get process size
        int pSize = API.getInstance().getInputMemSize();
        //add block
        boolean added = false;
        //find first hole
        int startY = pDimensions[0][0];
        int size = pDimensions[0][1];
        for(int p = 0;p<pDimensions.length;p++)
        {//find the first hole in memory
            if(pDimensions[p][0]< startY && pDimensions[p][1]>pSize)
            {
                startY = pDimensions[p][0];
                size = pDimensions[p][1];

            }
        }
        if(size > pSize)//check hole is big enough for process
        {
            API.getInstance().addBlock(Pid, pSize, startY);
            added = true;//add process
        }
        if(added == false)//failure to add block
        {
            System.out.println("Process not added. No block large enough");
        }








    }
}
