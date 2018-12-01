package com.jaredscarito.memory_manager.api.buttons;

import com.jaredscarito.memory_manager.api.API;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javax.swing.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Raymond McNamara
 */

public class AddButton implements EventHandler<MouseEvent> {

    @Override
   public void handle(MouseEvent event) {
        if(API.getInstance().getSelectedAlgorithm().equalsIgnoreCase("first fit")){
            firstFit();
        }
        else if(API.getInstance().getSelectedAlgorithm().equalsIgnoreCase("best fit")){
            bestFit();
        }
        else if(API.getInstance().getSelectedAlgorithm().equalsIgnoreCase("worst fit")){
            worstFit();
        }
    }
    public static void firstFit() {
        String Pid;
        // get process id number
        Pid = API.getInstance().getSelectedPid();
        if(!API.getInstance().hasProcessById(Pid)) {
            //get empty spaces and sizes
            double pDimensions[][] = API.getInstance().getEmptySpaces();
            Arrays.sort(pDimensions, new Comparator<double[]>() {//sort array by startY
                public int compare(double[] s1, double[] s2) {
                    if (s1[0] > s2[0])
                        return 1;
                    else if (s1[0] < s2[0])
                        return -1;
                    else {

                        return 0;
                    }
                }
            });

            //get process size
            int pSize = API.getInstance().getInputMemSize();
            //add block
            boolean added = false;
            int i = 0;
            //find first hole
            while (added == false && i < pDimensions.length) {
                if (pDimensions[i][1] >= pSize)//check hole is big enough for process
                {
                    API.getInstance().addBlock(Pid, pSize, pDimensions[i][0]);
                    added = true;//add process
                }
                i++;
            }
            if (added == false)//failure to add block
            {
                JOptionPane.showMessageDialog(null, "ERROR: Process not added. No memory block large enough...");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR: Process already exists!");
        }
    }
    public static void bestFit(){
        String Pid;
        // get process id number
        Pid = API.getInstance().getSelectedPid();
        if(!API.getInstance().hasProcessById(Pid)) {
            //get empty spaces and sizes
            double pDimensions[][] = API.getInstance().getEmptySpaces();
            Arrays.sort(pDimensions, new Comparator<double[]>() {//sort array by size
                public int compare(double[] s1, double[] s2) {
                    if (s1[1] > s2[1])
                        return 1;
                    else if (s1[1] < s2[1])
                        return -1;
                    else {

                        return 0;
                    }
                }
            });

            //get process size
            int pSize = API.getInstance().getInputMemSize();
            //add block
            boolean added = false;
            int i = 0;
            //find first hole
            while (added == false && i < pDimensions.length) {
                if (pDimensions[i][1] >= pSize)//check hole is big enough for process
                {
                    API.getInstance().addBlock(Pid, pSize, pDimensions[i][0]);
                    added = true;//add process
                }
                i++;
            }
            if (added == false)//failure to add block
            {
                JOptionPane.showMessageDialog(null, "ERROR: Process not added. No memory block large enough...");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR: Process already exists!");
        }
    }
    public static void worstFit(){
        String Pid;
        // get process id number
        Pid = API.getInstance().getSelectedPid();
        if(!API.getInstance().hasProcessById(Pid)) {
            //get empty spaces and sizes
            double pDimensions[][] = API.getInstance().getEmptySpaces();
            Arrays.sort(pDimensions, new Comparator<double[]>() {//sort array by size backwards
                public int compare(double[] s1, double[] s2) {
                    if (s1[1] < s2[1])
                        return 1;
                    else if (s1[1] > s2[1])
                        return -1;
                    else {

                        return 0;
                    }
                }
            });

            //get process size
            int pSize = API.getInstance().getInputMemSize();
            //add block
            boolean added = false;
            int i = 0;
            //find first hole
            while (added == false && i < pDimensions.length) {
                if (pDimensions[i][1] >= pSize)//check hole is big enough for process
                {
                    API.getInstance().addBlock(Pid, pSize, pDimensions[i][0]);
                    added = true;//add process
                }
                i++;
            }
            if (added == false)//failure to add block
            {
                JOptionPane.showMessageDialog(null, "ERROR: Process not added. No memory block large enough...");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR: Process already exists!");
        }
    }
}
