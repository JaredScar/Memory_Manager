package com.jaredscarito.memory_manager.api.spaces;

import com.jaredscarito.memory_manager.main.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javax.swing.*;

public class ProcessBlock extends HBox {

    private double size;

    private String pid;

    private int displaySize;
    private double startY;
    private double endY;

    private Label sizeLabel;
    private Label processLabel;

    public ProcessBlock(String pid, int size, double startY) {
        this.pid = pid;
        this.displaySize = size;
        this.startY = startY;
        this.size = ( (600 / Double.parseDouble(Main.getTotalMemField().getText())) * size );
        if(this.size >= 40) {
            this.endY = this.size + this.startY; // Extra digit is for border px
            setLayoutY(this.startY);
            //this.setPrefHeight(this.size);
            this.setPrefWidth(100);

            Label processLabel = new Label(pid);
            processLabel.getStyleClass().add("process-label");
            this.processLabel = processLabel;
            this.processLabel.setPrefWidth(100);
            processLabel.setAlignment(Pos.CENTER);
            this.processLabel.setPrefHeight(this.size);
            processLabel.setLayoutY(startY);

            Label sizeLabel = new Label(size + " KB");
            sizeLabel.getStyleClass().add("size-label");
            this.sizeLabel = sizeLabel;
            this.sizeLabel.setLayoutY(this.endY - 15);

            Main.getSizeLayoutPane().getChildren().add(sizeLabel);
            Main.getBlockLayoutPane().getChildren().add(processLabel);
        } else {
            double minSize = ( (600 / Double.parseDouble(Main.getTotalMemField().getText())) * size );
            double adder = 1;
            while (minSize < 40) {
                minSize = ( (600 / Double.parseDouble(Main.getTotalMemField().getText())) * (size + adder));
                adder++;
            }
            JOptionPane.showMessageDialog(null, "Cannot perform operation - Minimum Size required: " + (size + adder) + " KB");
        }
    }

    public void destroy() {
        this.sizeLabel.setVisible(false);
        this.processLabel.setVisible(false);
        this.setVisible(false);
    }

    public String getPID() {
        return this.pid;
    }

    public int getDisplaySize() {
        return displaySize;
    }
    public double getStartY() {
        return startY;
    }
    public double getEndY() {
        return endY;
    }
}
