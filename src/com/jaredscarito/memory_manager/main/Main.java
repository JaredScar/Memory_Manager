package com.jaredscarito.memory_manager.main;

import com.jaredscarito.memory_manager.api.API;
import com.jaredscarito.memory_manager.api.MemoryLayout;
import com.jaredscarito.memory_manager.api.buttons.AddButton;
import com.jaredscarito.memory_manager.api.buttons.CompactButton;
import com.jaredscarito.memory_manager.api.buttons.RemoveButton;
import com.jaredscarito.memory_manager.api.spaces.MemoryPane;
import com.jaredscarito.memory_manager.api.spaces.ProcessBlock;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Jared Scarito
 */

public class Main extends Application {
    private static TextField totalMem;
    private static TextField osMem;
    private static ComboBox<String> pidBox;
    private static TextField processSize;
    private static ComboBox<String> algoBox;
    private static Label algoCopy;
    private static Label minHint;
    private static Label statusLabel;
    private static Label placementLabel;
    private static Label usagePercent;
    private static Label usageCaption;
    private static Label usedLine;
    private static Label freeLine;
    private static ProgressBar usageBar;
    private static VBox processList;
    private static Pane sizeLayoutPane;
    private static Pane blockLayoutPane;
    private static int lastTotalKb = -1;

    private Stage stage;

    public static ComboBox<String> getAlgoBox() {
        return algoBox;
    }

    public static TextField getTotalMemField() {
        return totalMem;
    }

    public static TextField getOsMemField() {
        return osMem;
    }

    public static ComboBox<String> getPidBox() {
        return pidBox;
    }

    public static TextField getProcessSizeField() {
        return processSize;
    }

    public static Pane getSizeLayoutPane() {
        return sizeLayoutPane;
    }

    public static Pane getBlockLayoutPane() {
        return blockLayoutPane;
    }

    public static void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        BorderPane root = new BorderPane();
        root.getStyleClass().add("app-root");
        root.setTop(buildHeader());
        HBox body = new HBox(20, buildControls(), buildMemory(), buildStats());
        body.setAlignment(Pos.TOP_CENTER);
        body.setPadding(new Insets(2, 20, 8, 20));
        root.setCenter(body);
        root.setBottom(buildFooter());

        Scene scene = new Scene(root, 1120, 800);
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        primaryStage.setTitle("Memory Manager");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1040);
        primaryStage.setMinHeight(760);
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.show();

        API.getInstance().addBlock("OS", API.getInstance().getOSFieldSize(), 0);
        lastTotalKb = API.getInstance().getTotalMemSize();
        onModelChanged();
        setStatus("Address 0 is at the top. Add a process and watch where the selected fit puts it.");

        totalMem.textProperty().addListener((obs, oldValue, newValue) -> syncOsFromFields());
        osMem.textProperty().addListener((obs, oldValue, newValue) -> syncOsFromFields());
        algoBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            algoCopy.setText(descriptionFor(newValue));
            if (placementLabel != null && newValue != null) {
                placementLabel.setText(newValue);
            }
            if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
                setStatus(newValue + " will place the next process. Blocks already in memory stay put.");
            }
        });

        if (getParameters().getRaw().contains("--capture")) {
            runCaptureSequence();
        }
    }

    private Node buildHeader() {
        Label title = new Label("Memory Manager");
        title.getStyleClass().add("app-title");
        Label subtitle = new Label("See how first fit, best fit, and worst fit place processes in memory.");
        subtitle.getStyleClass().add("app-subtitle");
        VBox titles = new VBox(2, title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label course = new Label("Operating systems");
        course.getStyleClass().add("course-badge");

        HBox header = new HBox(16, titles, spacer, course);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getStyleClass().add("header-bar");
        return header;
    }

    private Node buildControls() {
        Label algoLabel = section("ALGORITHM");
        algoBox = new ComboBox<String>();
        algoBox.getItems().addAll("First Fit", "Best Fit", "Worst Fit");
        algoBox.getSelectionModel().selectFirst();
        algoBox.setMaxWidth(Double.MAX_VALUE);
        algoBox.setTooltip(new Tooltip("Choosing a new algorithm does not move processes that are already loaded."));

        algoCopy = new Label(descriptionFor(algoBox.getValue()));
        algoCopy.getStyleClass().add("algo-copy");
        algoCopy.setWrapText(true);

        Label memoryLabel = section("MEMORY");
        totalMem = numberField("4096");
        osMem = numberField("400");
        minHint = new Label(" ");
        minHint.getStyleClass().add("hint");
        minHint.setWrapText(true);

        Label processLabel = section("PROCESS");
        pidBox = new ComboBox<String>();
        pidBox.getItems().addAll("P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9");
        pidBox.getSelectionModel().selectFirst();
        pidBox.setPrefWidth(140);

        processSize = numberField("400");
        processSize.setTooltip(new Tooltip("Kilobytes this process needs. It must fit in a free hole."));

        Button addBtn = new Button("Add process");
        addBtn.getStyleClass().add("primary");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setDefaultButton(true);
        addBtn.setOnAction(new AddButton());
        addBtn.setTooltip(new Tooltip("Place the selected process with the current fit algorithm."));

        Button removeBtn = new Button("Remove process");
        removeBtn.getStyleClass().add("secondary");
        removeBtn.setMaxWidth(Double.MAX_VALUE);
        removeBtn.setOnAction(new RemoveButton());
        removeBtn.setTooltip(new Tooltip("Free the memory held by the selected process."));

        Button compactBtn = new Button("Compact memory");
        compactBtn.getStyleClass().add("quiet");
        compactBtn.setMaxWidth(Double.MAX_VALUE);
        compactBtn.setOnAction(new CompactButton());
        compactBtn.setTooltip(new Tooltip("Slide processes together so free space becomes one hole at the bottom."));

        VBox card = new VBox(8,
                algoLabel,
                algoBox,
                algoCopy,
                memoryLabel,
                fieldRow("Total memory", totalMem),
                fieldRow("OS memory", osMem),
                minHint,
                processLabel,
                labeledRow("Process", pidBox, null),
                fieldRow("Process size", processSize),
                addBtn,
                removeBtn,
                compactBtn);
        card.getStyleClass().add("side-card");
        card.setPrefWidth(332);
        card.setMinWidth(300);
        VBox.setMargin(memoryLabel, new Insets(8, 0, 0, 0));
        VBox.setMargin(processLabel, new Insets(8, 0, 0, 0));
        VBox.setMargin(addBtn, new Insets(8, 0, 0, 0));
        BorderPane.setMargin(card, new Insets(0, 8, 8, 18));
        return card;
    }

    private Node buildMemory() {
        Label sizeCaption = new Label("SIZE");
        sizeCaption.getStyleClass().add("scale-caption");
        sizeCaption.setPrefWidth(MemoryLayout.SCALE_WIDTH);
        sizeCaption.setAlignment(Pos.CENTER_RIGHT);

        Label mapCaption = new Label("MEMORY");
        mapCaption.getStyleClass().add("scale-caption");
        mapCaption.setPrefWidth(MemoryLayout.BLOCK_WIDTH);
        mapCaption.setAlignment(Pos.CENTER);

        sizeLayoutPane = memoryPane(MemoryLayout.SCALE_WIDTH);
        blockLayoutPane = memoryPane(MemoryLayout.BLOCK_WIDTH);
        blockLayoutPane.getStyleClass().add("block-layout");

        HBox captions = new HBox(14, sizeCaption, mapCaption);
        HBox map = new HBox(14, sizeLayoutPane, blockLayoutPane);
        map.setAlignment(Pos.TOP_LEFT);

        Label note = new Label("Address 0 is at the top. The operating system stays resident.");
        note.getStyleClass().add("map-note");
        note.setWrapText(true);
        note.setMaxWidth(MemoryLayout.SCALE_WIDTH + MemoryLayout.BLOCK_WIDTH + 14);
        note.setAlignment(Pos.CENTER);
        note.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox column = new VBox(10, captions, map, note);
        column.setAlignment(Pos.TOP_CENTER);
        StackPane wrap = new StackPane(column);
        wrap.setPadding(new Insets(6, 8, 8, 8));
        StackPane.setAlignment(column, Pos.TOP_CENTER);
        return wrap;
    }

    private Node buildStats() {
        Label placementHeading = section("PLACEMENT");
        placementLabel = new Label(algoBox.getValue());
        placementLabel.getStyleClass().add("placement-name");

        Label usageHeading = section("USAGE");
        usagePercent = new Label("0%");
        usagePercent.getStyleClass().add("usage-percent");
        usageCaption = new Label("in use, including the operating system");
        usageCaption.getStyleClass().add("usage-caption");
        usageCaption.setWrapText(true);
        usageBar = new ProgressBar(0);
        usageBar.setMaxWidth(Double.MAX_VALUE);
        usedLine = new Label("Used 0 KB");
        usedLine.getStyleClass().add("stat-line");
        freeLine = new Label("Free 0 KB");
        freeLine.getStyleClass().add("stat-line");

        Label allocatedHeading = section("IN MEMORY");
        processList = new VBox(8);

        VBox card = new VBox(8,
                placementHeading,
                placementLabel,
                usageHeading,
                usagePercent,
                usageCaption,
                usageBar,
                usedLine,
                freeLine,
                allocatedHeading,
                processList);
        card.getStyleClass().add("side-card");
        card.setPrefWidth(280);
        card.setMinWidth(250);
        VBox.setMargin(usageHeading, new Insets(10, 0, 0, 0));
        VBox.setMargin(allocatedHeading, new Insets(10, 0, 0, 0));
        BorderPane.setMargin(card, new Insets(0, 18, 8, 8));
        return card;
    }

    private Node buildFooter() {
        statusLabel = new Label(" ");
        statusLabel.getStyleClass().add("status-text");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(statusLabel, Priority.ALWAYS);

        Label credits = new Label("Jared Scarito   ·   Ray McNamara");
        credits.getStyleClass().add("credits");

        HBox footer = new HBox(18, statusLabel, credits);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("footer-bar");
        return footer;
    }

    private MemoryPane memoryPane(double width) {
        MemoryPane pane = new MemoryPane();
        pane.setMinSize(width, MemoryLayout.VIEW_HEIGHT);
        pane.setPrefSize(width, MemoryLayout.VIEW_HEIGHT);
        pane.setMaxSize(width, MemoryLayout.VIEW_HEIGHT);
        return pane;
    }

    private Label section(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-label");
        return label;
    }

    private HBox fieldRow(String name, Node field) {
        return labeledRow(name, field, "KB");
    }

    private HBox labeledRow(String name, Node field, String unitText) {
        Label label = new Label(name);
        label.getStyleClass().add("field-label");
        label.setPrefWidth(112);
        HBox.setHgrow(field, Priority.ALWAYS);
        if (field instanceof Region) {
            ((Region) field).setMaxWidth(Double.MAX_VALUE);
        }
        Label unit = new Label(unitText == null ? "" : unitText);
        unit.getStyleClass().add("unit");
        unit.setPrefWidth(26);
        HBox row = new HBox(8, label, field, unit);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private TextField numberField(String initial) {
        TextField field = new TextField();
        field.setTextFormatter(new TextFormatter<Object>(change -> {
            String next = change.getControlNewText();
            if (next.matches("\\d{0,7}")) {
                return change;
            }
            return null;
        }));
        field.setText(initial);
        return field;
    }

    private void syncOsFromFields() {
        if (!isPositiveInt(totalMem.getText()) || !isPositiveInt(osMem.getText())) {
            setStatus("Enter total memory and OS memory as whole numbers of KB.");
            return;
        }
        int total = Integer.parseInt(totalMem.getText().trim());
        int os = Integer.parseInt(osMem.getText().trim());
        if (os > total) {
            setStatus("OS memory cannot be larger than total memory.");
            return;
        }
        int minimum = MemoryLayout.minimumVisibleKb(total);
        minHint.setText("Smallest block that stays readable: " + minimum + " KB");
        if (os < minimum) {
            setStatus("OS memory must be at least " + minimum + " KB so the block stays readable.");
            return;
        }
        if (API.getInstance().getProcessBlocks().size() <= 1) {
            ProcessBlock existing = API.getInstance().getProcessBlockById("OS");
            boolean changed = existing == null || existing.getDisplaySize() != os || lastTotalKb != total;
            if (changed) {
                API.getInstance().removeBlock("OS");
                API.getInstance().addBlock("OS", os, 0);
                lastTotalKb = total;
            }
        }
        onModelChanged();
    }

    public static void onModelChanged() {
        syncFieldLock();
        refreshStats();
        refreshHoles();
        if (isPositiveInt(totalMem.getText())) {
            int minimum = MemoryLayout.minimumVisibleKb(Integer.parseInt(totalMem.getText().trim()));
            minHint.setText("Smallest block that stays readable: " + minimum + " KB");
        }
    }

    private static void syncFieldLock() {
        boolean lock = API.getInstance().getProcessBlocks().size() > 1;
        setLocked(totalMem, lock);
        setLocked(osMem, lock);
    }

    private static void setLocked(TextField field, boolean locked) {
        field.setEditable(!locked);
        if (locked && !field.getStyleClass().contains("locked")) {
            field.getStyleClass().add("locked");
        }
        if (!locked) {
            field.getStyleClass().remove("locked");
        }
    }

    private static void refreshStats() {
        int total = API.getInstance().getTotalMemSize();
        int used = 0;
        for (ProcessBlock block : API.getInstance().getProcessBlocks()) {
            used += block.getDisplaySize();
        }
        int free = total - used;
        int usedPercent = total <= 0 ? 0 : (int) Math.round((used * 100.0) / total);
        if (usedPercent < 0) {
            usedPercent = 0;
        }
        if (usedPercent > 100) {
            usedPercent = 100;
        }
        int freePercent = 100 - usedPercent;
        double ratio = total <= 0 ? 0 : Math.max(0, Math.min(1, used / (double) total));

        usagePercent.setText(usedPercent + "%");
        usedLine.setText("Used  " + String.format("%,d", used) + " KB");
        freeLine.setText("Free  " + String.format("%,d", free) + " KB   ·   " + freePercent + "%");
        usageBar.setProgress(ratio);
        usageBar.getStyleClass().removeAll("warn", "danger");
        if (ratio >= 0.9) {
            usageBar.getStyleClass().add("danger");
        } else if (ratio >= 0.75) {
            usageBar.getStyleClass().add("warn");
        }

        processList.getChildren().clear();
        ArrayList<ProcessBlock> blocks = new ArrayList<ProcessBlock>(API.getInstance().getProcessBlocks());
        blocks.sort(Comparator.comparingDouble(ProcessBlock::getStartY));
        for (ProcessBlock block : blocks) {
            Region swatch = new Region();
            swatch.setPrefSize(12, 12);
            swatch.setMinSize(12, 12);
            swatch.setMaxSize(12, 12);
            swatch.setStyle("-fx-background-color: " + ProcessBlock.colorFor(block.getPID()) + "; -fx-background-radius: 3;");

            Label name = new Label(block.getPID());
            name.getStyleClass().add("process-name");
            Region grow = new Region();
            HBox.setHgrow(grow, Priority.ALWAYS);
            Label size = new Label(String.format("%,d KB", block.getDisplaySize()));
            size.getStyleClass().add("process-size");

            HBox row = new HBox(8, swatch, name, grow, size);
            row.setAlignment(Pos.CENTER_LEFT);
            processList.getChildren().add(row);
        }
    }

    private static void refreshHoles() {
        blockLayoutPane.getChildren().removeIf(node -> "hole".equals(node.getUserData()));
        double[][] holes = API.getInstance().getEmptySpaces();
        if (holes == null) {
            return;
        }
        double total = API.getInstance().getTotalMemSize();
        for (double[] hole : holes) {
            double height = MemoryLayout.pixelsFor(hole[1], total);
            if (height < 24) {
                continue;
            }
            boolean roomy = height >= 64;
            String kb = String.format("%,d KB", Math.round(hole[1]));
            Label free = new Label(roomy ? "Free\n" + kb : kb);
            free.setUserData("hole");
            free.setAlignment(Pos.CENTER);
            free.setWrapText(true);
            if (roomy) {
                free.getStyleClass().add("hole-label");
            } else {
                free.setStyle("-fx-text-fill: #d5dcf0; -fx-font-size: 11px; -fx-font-weight: bold; -fx-background-color: #1a2030; -fx-background-radius: 6;");
            }
            double insetX = roomy ? 8 : 4;
            double insetY = roomy ? 6 : 2;
            double width = MemoryLayout.BLOCK_WIDTH - (insetX * 2);
            double boxHeight = Math.max(18, height - (insetY * 2));
            free.setPrefSize(width, boxHeight);
            free.setMinSize(width, boxHeight);
            free.setMaxSize(width, boxHeight);
            free.setLayoutX(insetX);
            free.setLayoutY(hole[0] + insetY);
            blockLayoutPane.getChildren().add(0, free);
        }
    }

    private static String descriptionFor(String algorithm) {
        if ("Best Fit".equalsIgnoreCase(algorithm)) {
            return "Puts the process in the smallest hole that can hold it.";
        }
        if ("Worst Fit".equalsIgnoreCase(algorithm)) {
            return "Puts the process in the largest hole, leaving a bigger leftover.";
        }
        return "Puts the process in the first hole that is large enough.";
    }

    private static boolean isPositiveInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            return Integer.parseInt(value.trim()) > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void runCaptureSequence() {
        File frames = new File("target/frames");
        if (!frames.exists() && !frames.mkdirs()) {
            return;
        }
        Runnable[] steps = new Runnable[] {
                () -> {
                    setStatus("The operating system sits at address 0. Everything below it is free.");
                    savePng(new File("docs/images/start.png"));
                    savePng(new File(frames, "00.png"));
                },
                () -> {
                    place("P1", "900");
                    savePng(new File(frames, "01.png"));
                },
                () -> {
                    place("P2", "700");
                    savePng(new File(frames, "02.png"));
                },
                () -> {
                    place("P3", "500");
                    savePng(new File(frames, "03.png"));
                },
                () -> {
                    takeAway("P2");
                    savePng(new File(frames, "04.png"));
                },
                () -> {
                    place("P4", "360");
                    savePng(new File("docs/images/memory-manager.png"));
                    savePng(new File(frames, "05.png"));
                },
                () -> {
                    new CompactButton().handle(null);
                    savePng(new File("docs/images/after-compact.png"));
                    savePng(new File(frames, "06.png"));
                }
        };

        Timeline timeline = new Timeline();
        for (int i = 0; i < steps.length; i++) {
            final int index = i;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(700L * (i + 1)), event -> steps[index].run()));
        }
        timeline.setOnFinished(event -> Platform.exit());
        timeline.play();
    }

    private void place(String pid, String size) {
        pidBox.getSelectionModel().select(pid);
        processSize.setText(size);
        new AddButton().handle(null);
    }

    private void takeAway(String pid) {
        pidBox.getSelectionModel().select(pid);
        new RemoveButton().handle(null);
    }

    private void savePng(File file) {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            Scene scene = stage.getScene();
            scene.getRoot().applyCss();
            scene.getRoot().layout();
            WritableImage image = scene.snapshot(null);
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            BufferedImage buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            PixelReader reader = image.getPixelReader();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    buffered.setRGB(x, y, reader.getArgb(x, y));
                }
            }
            ImageIO.write(buffered, "png", file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
