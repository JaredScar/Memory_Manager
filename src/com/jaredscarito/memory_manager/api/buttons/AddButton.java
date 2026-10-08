package com.jaredscarito.memory_manager.api.buttons;

import com.jaredscarito.memory_manager.api.API;
import com.jaredscarito.memory_manager.api.Dialogs;
import com.jaredscarito.memory_manager.main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Raymond McNamara
 */

public class AddButton implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        String algorithm = API.getInstance().getSelectedAlgorithm();
        if (algorithm.equalsIgnoreCase("first fit")) {
            place(algorithm, (left, right) -> Double.compare(left[0], right[0]));
        } else if (algorithm.equalsIgnoreCase("best fit")) {
            place(algorithm, (left, right) -> Double.compare(left[1], right[1]));
        } else if (algorithm.equalsIgnoreCase("worst fit")) {
            place(algorithm, (left, right) -> Double.compare(right[1], left[1]));
        } else {
            Main.setStatus("Choose First Fit, Best Fit, or Worst Fit.");
        }
        Main.onModelChanged();
    }

    private void place(String algorithm, Comparator<double[]> order) {
        API api = API.getInstance();
        String pid = api.getSelectedPid();
        if (pid.isEmpty()) {
            Main.setStatus("Choose a process id.");
            Dialogs.error("Choose a process id.");
            return;
        }
        if (api.getTotalMemSize() <= 0 || api.getOSFieldSize() <= 0) {
            Main.setStatus("Set total memory and OS memory before adding a process.");
            Dialogs.error("Set total memory and OS memory before adding a process.");
            return;
        }
        if (api.hasProcessById(pid)) {
            Main.setStatus(pid + " is already in memory. Remove it before adding it again.");
            Dialogs.error(pid + " is already in memory. Each process can be loaded once.");
            return;
        }

        int processSize = api.getInputMemSize();
        if (processSize <= 0) {
            Main.setStatus("Process size has to be a whole number of KB greater than zero.");
            Dialogs.error("Process size has to be a whole number of KB greater than zero.");
            return;
        }

        double[][] holes = api.getEmptySpaces();
        if (holes == null || holes.length == 0) {
            Main.setStatus("Memory is full. Remove a process or compact before adding another.");
            Dialogs.error("Memory is full. Remove a process or compact before adding another.");
            return;
        }

        double[][] ordered = Arrays.copyOf(holes, holes.length);
        Arrays.sort(ordered, order);
        for (double[] hole : ordered) {
            if (hole[1] >= processSize) {
                if (api.addBlock(pid, processSize, hole[0])) {
                    Main.setStatus("Added " + pid + " (" + processSize + " KB) with " + algorithm + ".");
                } else {
                    Main.setStatus(pid + " is below the smallest size this map can draw.");
                }
                return;
            }
        }

        Main.setStatus("No hole is large enough for " + pid + " (" + processSize + " KB).");
        Dialogs.error("No hole is large enough for " + pid + " (" + processSize + " KB).");
    }
}
