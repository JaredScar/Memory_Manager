package com.jaredscarito.memory_manager.api;

import com.jaredscarito.memory_manager.api.spaces.ProcessBlock;
import com.jaredscarito.memory_manager.main.Main;
import java.util.Timer;
import javafx.application.Platform;

import java.util.TimerTask;

public class Updater extends TimerTask {
    private boolean wasSet = false;
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
                    } else {
                        double minSizeReq = getMinSizeRequired(osSize);
                        API.getInstance().removeBlock("OS");
                        API.getInstance().addBlock("OS", minSizeReq, 0);
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
    public double getMinSizeRequired(double size) {
        double minSize = ( (580 / Double.parseDouble(Main.getTotalMemField().getText())) * size );
        double adder = 1;
        while (minSize < 40) {
            minSize = ( (580 / Double.parseDouble(Main.getTotalMemField().getText())) * (size + adder));
            adder++;
        }
        double displayMinSize = (( (minSize) / (double) 580 ) * Double.parseDouble(Main.getTotalMemField().getText()));
        return Math.ceil(displayMinSize);
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