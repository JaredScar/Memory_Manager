package com.jaredscarito.memory_manager.api;

import com.jaredscarito.memory_manager.api.spaces.ProcessBlock;
import com.jaredscarito.memory_manager.main.Main;
import javafx.application.Platform;

import java.util.TimerTask;

public class Updater extends TimerTask {
    @Override
    public void run() {
        Platform.runLater( () -> {
            if(isInt(Main.getOsMemField().getText()) && isInt(Main.getTotalMemField().getText())) {
                int osSize = API.getInstance().getOSFieldSize();
                int totalMemSize = API.getInstance().getTotalMemSize();
                if(osSize <= totalMemSize) {
                    double sizeCalc = ((580 / Double.parseDouble(Main.getTotalMemField().getText())) * (double) osSize);
                    if (sizeCalc >= 40) {
                        if (API.getInstance().getProcessBlocks().size() == 1) {
                            API.getInstance().removeBlock("OS");
                            API.getInstance().addBlock("OS", osSize, 0);
                        }
                    }
                }

                // Update Memory % Used and Left, Update Memory Val Used and Left
                int memValUsed = 0;
                for(ProcessBlock block : API.getInstance().getProcessBlocks()) {
                    memValUsed += block.getDisplaySize();
                }
                int memValLeft = API.getInstance().getTotalMemSize() - memValUsed;

                int memPercentUsed = (int) Math.round(((double)memValUsed / (double)API.getInstance().getTotalMemSize()) * (double) 100);
                int memPercentLeft = 100 - memPercentUsed;

                Main.memValUsedLabel.setText("Memory Used: " + memValUsed + " KB");
                Main.memValLeftLabel.setText("Memory Left: " + memValLeft + " KB");

                Main.memPercentLeftLabel.setText("Memory Left: " + memPercentLeft + "%");
                Main.memPercentUsedLabel.setText("Memory Used: " + memPercentUsed + "%");
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
