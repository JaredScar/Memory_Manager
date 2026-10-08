package com.jaredscarito.memory_manager.api;

import javafx.scene.control.Alert;

public final class Dialogs {
    private Dialogs() {
    }

    public static void error(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Memory Manager");
        alert.setHeaderText("That change didn't fit");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
