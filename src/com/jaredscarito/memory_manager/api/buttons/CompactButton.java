package com.jaredscarito.memory_manager.api.buttons;

import com.jaredscarito.memory_manager.api.API;
import com.jaredscarito.memory_manager.main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Jared Scarito
 */

public class CompactButton implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        double[][] before = API.getInstance().getEmptySpaces();
        boolean alreadyPacked = before == null || before.length <= 1;
        boolean packed = API.getInstance().compactMemory();
        Main.onModelChanged();
        if (alreadyPacked) {
            Main.setStatus("Memory is already compacted. Free space is a single hole.");
        } else if (packed) {
            Main.setStatus("Compacted memory. Processes slid together and free space is one hole.");
        } else {
            Main.setStatus("Compaction could not pack every block.");
        }
    }
}
