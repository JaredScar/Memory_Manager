package com.jaredscarito.memory_manager.api.buttons;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import com.jaredscarito.memory_manager.api.API;
import javax.swing.*;

/**
 * @author Raymond McNamara
 */

public class RemoveButton implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        String Pid = API.getInstance().getSelectedPid();

        if(API.getInstance().removeBlock(Pid) == false)
        {
            JOptionPane.showMessageDialog(null, "ERROR: Process does not exist in memory!");
        }


    }
}
