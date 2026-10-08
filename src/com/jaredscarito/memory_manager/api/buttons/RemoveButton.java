package com.jaredscarito.memory_manager.api.buttons;

import com.jaredscarito.memory_manager.api.API;
import com.jaredscarito.memory_manager.api.Dialogs;
import com.jaredscarito.memory_manager.main.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * @author Raymond McNamara
 */

public class RemoveButton implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        String pid = API.getInstance().getSelectedPid();
        if (API.getInstance().removeBlock(pid)) {
            Main.setStatus("Removed " + pid + ". The gap it left is free memory.");
        } else {
            Main.setStatus(pid + " is not in memory.");
            Dialogs.error(pid + " is not in memory.");
        }
        Main.onModelChanged();
    }
}
