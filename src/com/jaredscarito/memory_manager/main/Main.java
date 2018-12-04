package com.jaredscarito.memory_manager.main;

import com.jaredscarito.memory_manager.api.API;
import com.jaredscarito.memory_manager.api.Updater;
import com.jaredscarito.memory_manager.api.buttons.AddButton;
import com.jaredscarito.memory_manager.api.buttons.CompactButton;
import com.jaredscarito.memory_manager.api.buttons.RemoveButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Timer;

/**
 * @author Jared Scarito
 */

public class Main extends Application {
    private static TextField totalMem;
    private static TextField osMem;
    private static ChoiceBox pidBox;
    private static TextField processSize;
    private static ChoiceBox algoBox;

    public static ChoiceBox getAlgoBox() {
        return algoBox;
    }

    public static TextField getTotalMemField() {
        return totalMem;
    }

    public static TextField getOsMemField() {
        return osMem;
    }

    public static ChoiceBox getPidBox() {
        return pidBox;
    }

    public static TextField getProcessSizeField() {
        return processSize;
    }

    private static int sizeOfMemContainer;
    public static int getSizeOfMemContainer() {
        return sizeOfMemContainer;
    }

    @Override
    public void start(Stage primaryStage) {
        //Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Memory Manager");
        primaryStage.setScene(new Scene(getGrid()));
        primaryStage.getScene().getStylesheets().add("com/jaredscarito/memory_manager/main/style.css");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.show();

        // Add OS field
        API.getInstance().addBlock("OS", API.getInstance().getOSFieldSize(), 0);
        // Start Updater TimerTask
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Updater(), 1000, 1000);
    }

    public GridPane getGrid() {
        /**
         * Grid Setup:
         */
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        grid.setMinSize(830, 600);
        /*
         * Col 1
         */
        Label dataHeader = new Label("DATA");
        dataHeader.getStyleClass().add("header"); // css
        VBox dataBox = new VBox(); // dataBox
        dataBox.getStyleClass().add("data-col"); // css
        dataBox.getChildren().add(dataHeader);

        HBox selectBox = new HBox(); // selectBox
        selectBox.getStyleClass().add("box"); // css
        Label algoLabel = new Label("Select Algorithm:");
        algoLabel.getStyleClass().add("col-label");
        ChoiceBox algo = new ChoiceBox();
        algoBox = algo;
        algo.setItems(FXCollections.observableArrayList("First Fit", new Separator(), "Best Fit",
                new Separator(), "Worst Fit"));
        algo.getSelectionModel().selectFirst();
        algo.setId("algoSelect"); // css

        selectBox.getChildren().addAll(algoLabel, algoBox); // selectBox
        dataBox.getChildren().add(selectBox); // dataBox

        HBox totalMemoryBox = new HBox(); // totalMemoryBox
        totalMemoryBox.getStyleClass().add("box"); // css
        HBox osMemoryBox = new HBox(); // osMemoryBox
        osMemoryBox.getStyleClass().add("box"); // css
        totalMemoryBox.getStyleClass().add("memory-box"); // css
        osMemoryBox.getStyleClass().add("memory-box"); // css
        Label totalMemoryLabel = new Label("Total Memory:"); // css
        totalMemoryLabel.getStyleClass().add("col-label");
        Label osMemoryLabel = new Label("OS Memory:"); // css
        osMemoryLabel.getStyleClass().add("col-label");
        TextField totalMemoryField = new TextField("4096");
        totalMemoryField.getStyleClass().add("memory-fields"); // css
        TextField osMemoryField = new TextField("400");
        osMemoryField.getStyleClass().add("memory-fields"); // css
        Label labelK = new Label("KB");
        labelK.getStyleClass().add("memory-k"); // css
        Label labelK2 = new Label("KB");
        labelK2.getStyleClass().add("memory-k"); // css
        totalMemoryBox.getChildren().addAll(totalMemoryLabel, totalMemoryField, labelK); // totalMemoryBox
        osMemoryBox.getChildren().addAll(osMemoryLabel, osMemoryField, labelK2); // osMemoryBox
        dataBox.getChildren().add(totalMemoryBox); // dataBox
        dataBox.getChildren().add(osMemoryBox); // dataBox

        grid.add(dataBox, 0, 0); // Add to grid
        /*
         * End Col 1
         */
        /*
         * Col 2
         */
        VBox processBox = new VBox();
        processBox.getStyleClass().add("data-col");
        Label processHeader = new Label("PROCESS");
        processHeader.getStyleClass().add("header");
        processBox.getChildren().add(processHeader);

        HBox processIdBox = new HBox();
        processIdBox.getStyleClass().add("box");
        HBox processSizeBox = new HBox();
        processSizeBox.getStyleClass().add("box");

        Label processIdLabel = new Label("Process ID:");
        processIdLabel.getStyleClass().add("col-label");
        ChoiceBox idChoiceBox = new ChoiceBox();
        idChoiceBox.setItems(FXCollections.observableArrayList("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9"));
        idChoiceBox.setId("processID");
        idChoiceBox.getSelectionModel().selectFirst();
        processIdBox.getChildren().addAll(processIdLabel, idChoiceBox);

        Label processSizeLabel = new Label("Process Size:");
        processSizeLabel.getStyleClass().add("col-label");
        TextField processSizeField = new TextField("400");
        processSizeField.getStyleClass().add("process-fields");
        Label processK = new Label("KB");
        processK.getStyleClass().add("process-k");
        processSizeBox.getChildren().addAll(processSizeLabel, processSizeField, processK);

        processBox.getChildren().add(processIdBox); // processBox
        processBox.getChildren().add(processSizeBox); // processBox

        grid.add(processBox, 0, 1);
        /*
         * End Col 2
         */
        /*
         * Col 3
         */
        VBox buttonBox = new VBox();
        buttonBox.getStyleClass().add("data-col");
        HBox addButtonHbox = new HBox();
        addButtonHbox.getStyleClass().add("box");
        HBox removeButtonHbox = new HBox();
        removeButtonHbox.getStyleClass().add("box");
        HBox compactButtonHbox = new HBox();
        compactButtonHbox.getStyleClass().add("box");

        Button addBtn = new Button("Add Memory Block");
        addBtn.getStyleClass().add("mem-btn");
        Button minusBtn = new Button("Remove Memory Block");
        minusBtn.getStyleClass().add("mem-btn");
        Button compactBtn = new Button("COMPACT");
        compactBtn.getStyleClass().add("mem-btn");

        // Adding functionality to buttons
        addBtn.setOnMouseClicked(new AddButton());
        minusBtn.setOnMouseClicked(new RemoveButton());
        compactBtn.setOnMouseClicked(new CompactButton());

        addButtonHbox.getChildren().add(addBtn);
        removeButtonHbox.getChildren().add(minusBtn);
        compactButtonHbox.getChildren().add(compactBtn);

        buttonBox.getChildren().add(addButtonHbox);
        buttonBox.getChildren().add(removeButtonHbox);
        buttonBox.getChildren().add(compactButtonHbox);

        grid.add(buttonBox, 0, 2);
        /*
         * End Col 3
         */
        totalMem = totalMemoryField;
        osMem = osMemoryField;
        pidBox = idChoiceBox;
        processSize = processSizeField;
        /*
         * Memory Managed Column
         */
        VBox memoryFieldsBox = new VBox();
        memoryFieldsBox.getStyleClass().add("memory-field-col");
        Label fieldM = new Label("M");
        fieldM.getStyleClass().add("memory-letters");
        Label fieldE = new Label("E");
        fieldE.getStyleClass().add("memory-letters");
        Label fieldM2 = new Label("M");
        fieldM2.getStyleClass().add("memory-letters");
        Label fieldO = new Label("O");
        fieldO.getStyleClass().add("memory-letters");
        Label fieldR = new Label("R");
        fieldR.getStyleClass().add("memory-letters");
        Label fieldY = new Label("Y");
        fieldY.getStyleClass().add("memory-letters");

        memoryFieldsBox.getChildren().addAll(fieldM, fieldE, fieldM2, fieldO, fieldR, fieldY);

        memoryFieldsBox.setPrefWidth(100);

        grid.add(memoryFieldsBox, 1, 0, 1, 3);

        /* Memory Manager Col */
        Pane sizeLayout = new Pane();
        Pane blockLayout = new Pane();
        sizeLayout.getStyleClass().add("size-layout");
        blockLayout.getStyleClass().add("block-layout");
        sizeLayoutPane = sizeLayout;
        blockLayoutPane = blockLayout;
        /* TODO tests - get rid of */
        /** /
        API.getInstance().addBlock("P1", 1000, API.getInstance().getEmptySpace()[0]);
        int[][] emptySpaces = API.getInstance().getEmptySpaces();
        for(int i=0; i<emptySpaces.length; i++) {
            System.out.println("After P1: startY = " + emptySpaces[i][0]);
            System.out.println("After P1: size = " + emptySpaces[i][1]);
        }
        API.getInstance().addBlock("P2", 275, API.getInstance().getEmptySpace()[0]);
        int[][] emptySpaces2 = API.getInstance().getEmptySpaces();
        for(int i=0; i<emptySpaces2.length; i++) {
            System.out.println("After P2: startY = " + emptySpaces2[i][0]);
            System.out.println("After P2: size = " + emptySpaces2[i][1]);
        }
        API.getInstance().addBlock("P3", 800, API.getInstance().getEmptySpace()[0]);
        int[][] emptySpaces3 = API.getInstance().getEmptySpaces();
        for(int i=0; i<emptySpaces3.length; i++) {
            System.out.println("After P3: startY = " + emptySpaces3[i][0]);
            System.out.println("After P3: size = " + emptySpaces3[i][1]);
        }
        System.out.println();
        API.getInstance().addBlock("P4", 500, API.getInstance().getEmptySpace()[0]);
        /**/
        /* TODO end tests */
        grid.add(sizeLayout, 2, 0, 1, 3);
        grid.add(blockLayout, 3, 0, 1, 3);
        // Set grid height
        grid.setMaxHeight(580);
        grid.setPrefHeight(580);
        grid.setMinHeight(580);
        //System.out.println("Size of Mem Container: " + sizeOfMemContainer);
        /*
         * End Memory Column
         */

        /*
         * Start Col 5
         */
        /* ROW RESULTS */
        VBox container = new VBox();
        container.getStyleClass().add("result-container");
        container.setAlignment(Pos.TOP_CENTER);
        Label resultHeader = new Label("RESULTS");
        resultHeader.getStyleClass().add("result-header");
        Label memPercentUsedLab = new Label("Memory Used: ");
        memPercentUsedLab.getStyleClass().add("result-label");
        Label memPercentLeftLab = new Label("Memory Left: ");
        memPercentLeftLab.getStyleClass().add("result-label");
        Label memValUsedLab = new Label("Memory Used: ");
        memValUsedLab.getStyleClass().add("result-label");
        Label memValLeftLab = new Label("Memory Left: ");
        memValLeftLab.getStyleClass().add("result-label");

        memPercentUsedLabel = memPercentUsedLab;
        memPercentLeftLabel = memPercentLeftLab;
        memValUsedLabel = memValUsedLab;
        memValLeftLabel = memValLeftLab;

        container.getChildren().addAll(resultHeader, memPercentUsedLabel, memPercentLeftLabel, memValUsedLabel, memValLeftLabel);

        grid.add(container, 4, 0, 1, 2);
        /* END ROW RESULTS */

        /* ROW CREDITS */
        VBox footerContain = new VBox();
        footerContain.getStyleClass().add("footer-container");
        Label footerHeader = new Label("CREATED BY");
        footerHeader.getStyleClass().add("footer-header");
        Label jared = new Label("Jared Scarito");
        jared.getStyleClass().add("footer-author");
        Label ray = new Label("Ray McNamara");
        ray.getStyleClass().add("footer-author");

        footerContain.setAlignment(Pos.TOP_CENTER);
        footerContain.getChildren().addAll(footerHeader, jared, ray);

        grid.add(footerContain, 4, 2);
        /* END ROW CREDITS */
        /*
         * End Col 5
         */
        return grid;
    }
    public static Label memPercentUsedLabel;
    public static Label memPercentLeftLabel;
    public static Label memValUsedLabel;
    public static Label memValLeftLabel;
    public static Pane sizeLayoutPane;
    public static Pane blockLayoutPane;
    public static Pane getSizeLayoutPane() {
        return sizeLayoutPane;
    }
    public static Pane getBlockLayoutPane() {
        return blockLayoutPane;
    }
    public static void main(String[] args) {
        launch(args);
    }
}