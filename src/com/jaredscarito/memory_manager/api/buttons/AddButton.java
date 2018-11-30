package com.jaredscarito.memory_manager.api.buttons;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

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
        int i = 0;
        while(added == false && i < pDimensions.length)
        {
            //attempt to add

            if(pDimensions[i][1] >= pSize)
            {
                API.getInstance().addBlock(Pid, pSize, pDimensions[i][0]);
                added = true;
            }
            i++;
        }
        if(added == false)//failure to add block
        {
            System.out.println("Process not added. No block large enough");
        }
        for(int p = 0;p<pDimensions.length;p++)
        {
            System.out.println(pDimensions[p][0]);
            System.out.println(pDimensions[p][1]);
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
        int i = 0;
        while(added == false && i < pDimensions.length)
        {
            //attempt to add

            if(pDimensions[i][1] >= pSize)
            {
                API.getInstance().addBlock(Pid, pSize, pDimensions[i][0]);
                added = true;
            }
            i++;
        }
        if(added == false)//failure to add block
        {
            System.out.println("Process not added. No block large enough");
        }









    }
}
