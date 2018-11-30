package com.jaredscarito.memory_manager.api.buttons;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import com.jaredscarito.memory_manager.api.API;
import javax.swing.*;

public class RemoveButton implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        String Pid = API.getInstance().getSelectedPid();

        if(API.getInstance().removeBlock(Pid) == false)
        {
            JOptionPane.showMessageDialog(null, "Process does not exist memory");
        }


    }
}
