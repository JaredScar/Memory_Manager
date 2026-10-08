package com.jaredscarito.memory_manager.api.spaces;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * A pane that honors each child's preferred size and layout position.
 * The memory map places blocks by address, so a normal layout manager would stack them instead.
 */
public class MemoryPane extends Pane {
    @Override
    protected void layoutChildren() {
        for (Node child : getChildren()) {
            if (!(child instanceof Region)) {
                continue;
            }
            Region region = (Region) child;
            double width = region.getPrefWidth();
            double height = region.getPrefHeight();
            if (width <= 0) {
                width = region.prefWidth(-1);
            }
            if (height <= 0) {
                height = region.prefHeight(-1);
            }
            region.resizeRelocate(region.getLayoutX(), region.getLayoutY(), width, height);
        }
    }
}
