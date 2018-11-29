package com.jaredscarito.memory_manager.main;

import com.jaredscarito.memory_manager.api.buttons.AddButton;
import com.jaredscarito.memory_manager.api.buttons.CompactButton;
import com.jaredscarito.memory_manager.api.buttons.RemoveButton;
import com.jaredscarito.memory_manager.api.spaces.ProcessBlock;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class Main extends Application {
    private static TextField totalMem;
    private static TextField osMem;
    private static ChoiceBox pidBox;
    private static TextField processSize;

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

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Memory Manager");
        primaryStage.setScene(new Scene(getGrid()));
        primaryStage.getScene().getStylesheets().add("com/jaredscarito/memory_manager/main/style.css");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public GridPane getGrid() {
        /**
         * Grid Setup:
         */
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        grid.setMinSize(800, 550);
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
        ChoiceBox algoBox = new ChoiceBox();
        algoBox.setItems(FXCollections.observableArrayList("First Fit", new Separator(), "Best Fit",
                new Separator(), "Worst Fit"));
        algoBox.getSelectionModel().selectFirst();
        algoBox.setId("algoSelect"); // css
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
        this.totalMem = totalMemoryField;
        this.osMem = osMemoryField;
        this.pidBox = idChoiceBox;
        this.processSize = processSizeField;
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

        // Memory Size Box
        HBox horizContainer = new HBox();
        horizContainer.getStyleClass().add("horiz-container");
        VBox memorySizeVbox = new VBox();
        memorySizeVbox.getStyleClass().add("memory-size-box");
        memorySizeBox = memorySizeVbox;

        // Memory Box
        VBox memoryBox = new VBox();
        memoryBox.getStyleClass().add("memory-box");
        // Memory Container
        VBox memoryContainer = new VBox();
        memoryContainer.setPrefSize(100, 600);
        memoryContainer.getStyleClass().add("memory-container");
        ProcessBlock p1 = new ProcessBlock("P1", 4096, 0);
        memoryContainer.getChildren().add(p1);
        p1.change("P2", 100, 200);

        horizContainer.getChildren().addAll(memorySizeVbox, memoryBox);

        grid.add(horizContainer, 3, 0, 1, 3);
        grid.add(memoryContainer, 4, 0, 1, 3);
        /*
         * End Memory Column
         */
        return grid;
    }

    public static VBox memorySizeBox;

    public static void main(String[] args) {
        launch(args);
    }
}