package com.jaredscarito.memory_manager.api.spaces;

import com.jaredscarito.memory_manager.api.Dialogs;
import com.jaredscarito.memory_manager.api.MemoryLayout;
import com.jaredscarito.memory_manager.main.Main;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * @author Jared Scarito
 */

public class ProcessBlock extends HBox {
    private static final String[] PALETTE = {
            "#4C7DFF",
            "#1FA971",
            "#E08600",
            "#E85D75",
            "#8B5CF6",
            "#0F9B8E",
            "#E05A2B",
            "#C43B6E",
            "#3A7CA5"
    };

    private double size;
    private double startY;
    private double endY;
    private String pid;
    private int displaySize;
    private Label sizeLabel;
    private Label processLabel;

    public ProcessBlock(String pid, double sizeKb, double startY) {
        double totalKb = Main.getTotalMemField() == null ? 0 : parseTotal();
        double pixelHeight = MemoryLayout.pixelsFor(sizeKb, totalKb);
        if (pixelHeight < MemoryLayout.MIN_BLOCK_PX) {
            int minimum = MemoryLayout.minimumVisibleKb(totalKb);
            Dialogs.error("That block is too small to draw. Use at least " + minimum + " KB.");
            return;
        }

        this.size = pixelHeight;
        this.pid = pid;
        this.displaySize = (int) sizeKb;
        this.startY = startY;
        this.endY = this.startY + this.size;

        processLabel = new Label(pid);
        processLabel.getStyleClass().add("process-label");
        processLabel.setAlignment(Pos.CENTER);
        processLabel.setStyle(colorStyle(colorFor(pid)));
        processLabel.setMinSize(MemoryLayout.BLOCK_WIDTH, this.size);
        processLabel.setPrefSize(MemoryLayout.BLOCK_WIDTH, this.size);
        processLabel.setMaxSize(MemoryLayout.BLOCK_WIDTH, this.size);
        processLabel.setLayoutX(0);
        processLabel.setLayoutY(this.startY);

        sizeLabel = new Label(this.displaySize + " KB");
        sizeLabel.getStyleClass().add("size-label");
        sizeLabel.setAlignment(Pos.CENTER_RIGHT);
        sizeLabel.setPrefSize(MemoryLayout.SCALE_WIDTH, 22);
        sizeLabel.setLayoutX(0);
        sizeLabel.setLayoutY(this.startY + (this.size / 2) - 11);

        Main.getSizeLayoutPane().getChildren().add(sizeLabel);
        Main.getBlockLayoutPane().getChildren().add(processLabel);
    }

    private double parseTotal() {
        try {
            return Double.parseDouble(Main.getTotalMemField().getText().trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public static String colorFor(String processId) {
        if (processId != null && processId.equalsIgnoreCase("OS")) {
            return "#3D5AFE";
        }
        if (processId != null && processId.length() == 2 && (processId.charAt(0) == 'P' || processId.charAt(0) == 'p')) {
            int index = processId.charAt(1) - '1';
            if (index >= 0 && index < PALETTE.length) {
                return PALETTE[index];
            }
        }
        return "#64708C";
    }

    private String colorStyle(String color) {
        return "-fx-background-color: " + color + ";"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 14px;"
                + "-fx-font-weight: bold;"
                + "-fx-border-color: rgba(8, 10, 16, 0.35);"
                + "-fx-border-width: 0 0 1 0;";
    }

    public void destroy() {
        removeFromParent(sizeLabel);
        removeFromParent(processLabel);
    }

    private void removeFromParent(Node node) {
        if (node != null && node.getParent() instanceof Pane) {
            ((Pane) node.getParent()).getChildren().remove(node);
        }
    }

    public String getPID() {
        return this.pid;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    public double getSize() {
        return this.size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }
}
