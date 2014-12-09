package graphics.utility;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.Main;
import models.*;

public class CommandView {

    private Stage stage;
    private boolean showingToggleInfo;
    private ComboBox<Function> functionCombobox;
    private ComboBox<Program> programCombobox;
    private TextField delayField;
    private KeyCommand keyCommand;

    private TextField detectKeyPressField;

    private TextField enableTextField;
    private TextField enableMessageField;
    private TextField disableTextField;
    private TextField disableMessageField;
    private boolean ignoreCloseRequest = false;
    private VBox functionCommandBox;

    public CommandView(Stage parent, Command command, Runnable onSave ){
        stage = new Stage();
        stage.setTitle("Commands");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);

        Label commandTypeLabel = new Label("Command Type");
        ComboBox<String> commandTypeCB = new ComboBox<>();
        commandTypeCB.setItems(FXCollections.observableArrayList("Key presses", "Functions", "Programs"));

        Pane swapPane = new Pane();

        GridPane keyCommandBox = createKeyCommandBox();
        functionCommandBox = createFunctionBox();
        HBox programCommandBox = createProgramBox();

        commandTypeCB.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            swapPane.getChildren().removeAll(swapPane.getChildren());

            if(showingToggleInfo){
                stage.getScene().getWindow().setHeight(stage.getScene().getWindow().getHeight() - 140);
                showingToggleInfo = false;
            }

            switch (commandTypeCB.getSelectionModel().getSelectedIndex()){
                case Command.KEY:
                    swapPane.getChildren().add(keyCommandBox);
                    break;
                case Command.FUNCTION:
                    swapPane.getChildren().add(functionCommandBox);
                    if(functionCombobox.getSelectionModel().getSelectedItem().isToggle()){
                        stage.getScene().getWindow().setHeight(stage.getScene().getWindow().getHeight() + 140);
                        showingToggleInfo = true;
                    }
                    break;
                case Command.PROGRAM:
                    swapPane.getChildren().add(programCommandBox);
                    break;
            }
        });

        commandTypeCB.getSelectionModel().select(command.getCommandType());

        Label delayLabel = new Label("Delay");
        delayLabel.setTooltip(new Tooltip("The delay to wait before performing the command"));
        delayField = new NumberTextField(command.getDelay());

        GridPane commandGrid = new GridPane();
        commandGrid.setHgap(10);
        commandGrid.setVgap(10);
        commandGrid.add(commandTypeLabel, 0, 0);
        commandGrid.add(commandTypeCB, 1, 0);
        commandGrid.add(delayLabel, 0, 1);
        commandGrid.add(delayField, 1, 1);

        Button saveButton = new Button("Save");
        saveButton.setMaxWidth(Double.MAX_VALUE);

        saveButton.setOnAction(event -> {
            command.setDelay(Integer.parseInt(delayField.getText()));
            switch (commandTypeCB.getSelectionModel().getSelectedIndex()) {
                case Command.KEY:
                    if (keyCommand.getKeyCode() != null) {
                        command.setKeyCommand(keyCommand);
                    }
                    break;
                case Command.FUNCTION:
                    command.setFunction(functionCombobox.getSelectionModel().getSelectedItem());
                    if (command.getFunction().isToggle()) {
                        command.setEnableMenuText(enableTextField.getText());
                        command.setEnableMessage(enableMessageField.getText());
                        command.setDisableMenuText(disableTextField.getText());
                        command.setDisableMessage(disableMessageField.getText());
                    }
                    break;
                case Command.PROGRAM:
                    command.setProgram(programCombobox.getSelectionModel().getSelectedItem());
                    break;
            }
            onSave.run();
            stage.close();
        });

        VBox swapBox = new VBox(10);
        swapBox.getChildren().addAll(swapPane, saveButton);

        //Prevents the window from closing on alt+f4 press
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F4 && event.isAltDown()) {
                event.consume();
                ignoreCloseRequest = true;
            }
        });

        stage.setOnCloseRequest(event -> {
            if(ignoreCloseRequest) {
                event.consume();
                ignoreCloseRequest = false;
            }
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(commandGrid, swapBox);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
        loadCommand(command);
    }

    private GridPane createKeyCommandBox(){

        Label keyLabel = new Label("Key press");

        detectKeyPressField = new TextField();
        detectKeyPressField.setPromptText("Detect keyPress");
        detectKeyPressField.setEditable(false);

        detectKeyPressField.setOnKeyReleased(event -> {
            event.consume();
            if (!event.getCode().equals(KeyCode.CONTROL) && !event.getCode().equals(KeyCode.ALT)
                    && !event.getCode().equals(KeyCode.SHIFT) && !event.getCode().equals(KeyCode.WINDOWS)){
                keyCommand = new KeyCommand();
                keyCommand.setShift(event.isShiftDown());
                keyCommand.setCtrl(event.isControlDown());
                keyCommand.setAlt(event.isAltDown());
                keyCommand.setWindows(event.isMetaDown());
                keyCommand.setKeyCode(event.getCode());
                detectKeyPressField.setText(keyCommand.toString());
            }
        });

        GridPane botBox = new GridPane();
        botBox.setHgap(10);
        botBox.setVgap(10);
        botBox.add(keyLabel, 0, 0);
        botBox.add(detectKeyPressField, 1, 0);

        return botBox;
    }

    private VBox createFunctionBox(){
        Label functionLabel = new Label("Function");
        functionLabel.setTooltip(new Tooltip("Functions performs system commands"));

        GridPane toggleGrid = createToggleBox();

        functionCombobox = new ComboBox(FXCollections.observableArrayList(Functions.functions));

        functionCombobox.setCellFactory(new Callback<ListView<Function>, ListCell<Function>>() {
            public ListCell<Function> call(ListView<Function> param) {
                return new ListCell<Function>() {

                    @Override
                    public void updateItem(final Function item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            this.setTooltip(new Tooltip(item.getDescription()));
                            setText(item.toString());
                        }
                    }
                };
            }
        });

        VBox vBox = new VBox(10);

        functionCombobox.valueProperty().addListener((ObservableValue<? extends Function> observable, Function oldValue, Function newValue) -> {
            if (newValue.isToggle() && !showingToggleInfo) {
                stage.getScene().getWindow().setHeight(stage.getScene().getWindow().getHeight() + 140);
                vBox.getChildren().add(toggleGrid);
                showingToggleInfo = true;
            } else if (!newValue.isToggle() && showingToggleInfo) {
                vBox.getChildren().remove(toggleGrid);
                stage.getScene().getWindow().setHeight(stage.getScene().getWindow().getHeight() - 140);
                showingToggleInfo = false;
            }
        });

        functionCombobox.getSelectionModel().select(0);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(functionLabel, functionCombobox);

        vBox.getChildren().addAll(hBox);
        return vBox;
    }

    private HBox createProgramBox(){
        Label programLabel = new Label("Program");
        programLabel.setTooltip(new Tooltip("Runs the program or script selected"));

        programCombobox = new ComboBox(FXCollections.observableArrayList(Main.SETTINGS.getPrograms()));

        programCombobox.getSelectionModel().select(0);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(programLabel, programCombobox);
        return hBox;
    }

    private GridPane createToggleBox(){

        Label enableTextLabel = new Label("Enable text");
        enableTextLabel.setTooltip(new Tooltip("The text that will be displayed when the function will be enabled"));
        enableTextField = new TextField("Enable hotkeys");

        Label enableMessageLabel = new Label("Enable message");
        enableMessageLabel.setTooltip(new Tooltip("The text that will be displayed in the messagebox when the function is enabled"));
        enableMessageField = new TextField("Hotkeys enabled");

        Label disableTextLabel = new Label("Disable text");
        disableTextLabel.setTooltip(new Tooltip("The text that will be displayed when the function will be disabled"));
        disableTextField = new TextField("Disable hotkeys");

        Label disableMessageLabel = new Label("Disable message");
        disableMessageLabel.setTooltip(new Tooltip("The text that will be displayed in the messagebox when the function is disabled"));
        disableMessageField = new TextField("hotkeys disabled");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(enableTextLabel, 0, 0);
        grid.add(enableTextField, 1, 0);
        grid.add(enableMessageLabel, 0, 1);
        grid.add(enableMessageField, 1, 1);
        grid.add(disableTextLabel, 0, 2);
        grid.add(disableTextField, 1, 2);
        grid.add(disableMessageLabel, 0, 3);
        grid.add(disableMessageField, 1, 3);

        return grid;
    }

    private void loadCommand(Command command){
        switch (command.getCommandType()){
            case Command.KEY:
                if(command.getKeyCommand() != null)
                    detectKeyPressField.setText(command.getKeyCommand().toString());
                break;
            case Command.FUNCTION:
                functionCombobox.getSelectionModel().select(command.getFunction());
                if(command.getFunction().isToggle()){
                    enableTextField.setText(command.getEnableMenuText());
                    enableMessageField.setText(command.getEnableMessage());
                    disableTextField.setText(command.getDisableMenuText());
                    disableMessageField.setText(command.getDisableMessage());
                }
                break;
            case Command.PROGRAM:
                programCombobox.getSelectionModel().select(command.getProgram());
                break;
        }
    }
}
