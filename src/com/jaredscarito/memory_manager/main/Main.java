package com.jaredscarito.memory_manager.main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

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
        grid.setMinSize(800, 800);
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

        Button addBtn = new Button("+ Memory Block");
        addBtn.getStyleClass().add("mem-btn");
        Button minusBtn = new Button("- Memory Block");
        minusBtn.getStyleClass().add("mem-btn");
        Button compactBtn = new Button("COMPACT");
        compactBtn.getStyleClass().add("mem-btn");

        // Adding functionality to buttons
        //TODO

        addButtonHbox.getChildren().add(addBtn);
        removeButtonHbox.getChildren().add(minusBtn);
        compactButtonHbox.getChildren().add(compactBtn);

        buttonBox.getChildren().add(addButtonHbox);
        buttonBox.getChildren().add(removeButtonHbox);
        buttonBox.getChildren().add(compactButtonHbox);

        grid.add(buttonBox, 0, 2);
        /*
         * End Col 2
         */
        /*
         * Memory Managed Column
         */
        // TODO
        /*
         * End Memory Column
         */
        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}