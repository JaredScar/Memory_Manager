package com.jaredscarito.memory_manager.api.buttons;

import com.jaredscarito.memory_manager.api.API;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * @author Jared Scarito
 */

public class CompactButton implements EventHandler<MouseEvent> {

    @Override
    public void handle(MouseEvent event) {
        API.getInstance().compactMemory();
    }
}
