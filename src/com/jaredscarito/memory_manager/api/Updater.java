package com.jaredscarito.memory_manager.api;

import com.jaredscarito.memory_manager.main.Main;
import javafx.application.Platform;

import java.util.TimerTask;

public class Updater extends TimerTask {
    @Override
    public void run() {
        Platform.runLater( () -> {
            if(isInt(Main.getOsMemField().getText())) {
                int osSize = API.getInstance().getOSFieldSize();
                double sizeCalc = ((580 / Double.parseDouble(Main.getTotalMemField().getText())) * (double) osSize);
                if (sizeCalc >= 40) {
                    if (API.getInstance().getProcessBlocks().size() == 1) {
                        API.getInstance().removeBlock("OS");
                        API.getInstance().addBlock("OS", osSize, 0);
                    }
                }
            }
        });
    }
    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
