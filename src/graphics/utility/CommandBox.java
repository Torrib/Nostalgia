package graphics.utility;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import models.Function;
import models.Program;
import models.Command;
import models.Functions;

import java.util.List;


public class CommandBox extends VBox{

    private ComboBox<Function> functionCombobox;
    private ComboBox<Program> programCombobox;
    private Command command;
    private TextField delayField;
    private ListView<Command> commandList;

    private TextField detectKeyPressField;

    private TextField enableTextField;
    private TextField enableMessageField;
    private TextField disableTextField;
    private TextField disableMessageField;
    private boolean ignoreCloseRequest = false;

    boolean showingToggleInfo = false;

    public CommandBox(List<Command> items, List<Program> programs, Stage stage){
        super(10);
        command = new Command();

        ObservableList<Command> observableCommands = FXCollections.observableArrayList(items);

        commandList = new ListView<>();
        commandList.setItems(observableCommands);
        commandList.setMaxHeight(100);

        commandList.setCellFactory(new Callback<ListView<Command>, ListCell<Command>>() {
            @Override
            public ListCell<Command> call(ListView<Command> param) {
                return new RemovableCell<>(observableCommands, 100);
            }
        });

        Label commandTypeLabel = new Label("Command Type");
        ComboBox<String> commandTypeCB = new ComboBox<>();
        commandTypeCB.setItems(FXCollections.observableArrayList(new String[]{"Key presses", "Functions", "Programs"}));

        Pane swapPane = new Pane();

        GridPane keyCommandBox = createKeyCommandBox();
        VBox functionCommandBox = createFunctionBox();
        HBox programCommandBox = createProgramBox(programs);

        commandTypeCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                swapPane.getChildren().removeAll(swapPane.getChildren());

                if(showingToggleInfo){
                    getScene().getWindow().setHeight(getScene().getWindow().getHeight() - 140);
                    showingToggleInfo = false;
                }

                switch (commandTypeCB.getSelectionModel().getSelectedIndex()){
                    case Command.KEY:
                        swapPane.getChildren().add(keyCommandBox);
                        break;
                    case Command.FUNCTION:
                        swapPane.getChildren().add(functionCommandBox);
                        if(functionCombobox.getSelectionModel().getSelectedItem().isToggle()){
                            getScene().getWindow().setHeight(getScene().getWindow().getHeight() + 140);
                            showingToggleInfo = true;
                        }
                        break;
                    case Command.PROGRAM:
                        swapPane.getChildren().add(programCommandBox);
                        break;
                }
            }
        });

        commandTypeCB.getSelectionModel().select(0);

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

        Button addButton = new Button();
        addButton.setMaxWidth(Double.MAX_VALUE);

        ImageView addImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/add.png")));
        addImage.setFitHeight(15);
        addImage.setFitWidth(15);

        addButton.setGraphic(addImage);

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                switch (commandTypeCB.getSelectionModel().getSelectedIndex()){
                    case Command.KEY:
                        if(command.getKeyCode() != null) {
                            commandList.getItems().add(new Command(command.isCtrl(), command.isAlt(),
                                    command.isShift(), command.getKeyCode(), Integer.parseInt(delayField.getText())));
                        }
                        break;
                    case Command.FUNCTION:
                        Command command = new Command(functionCombobox.getSelectionModel().getSelectedItem(),
                                Integer.parseInt(delayField.getText()));
                        if(command.getFunction().isToggle()){
                            command.setEnableDisplay(enableTextField.getText());
                            command.setEnableMessage(enableMessageField.getText());
                            command.setDisableDisplay(disableTextField.getText());
                            command.setDisableMessage(disableMessageField.getText());
                        }

                        commandList.getItems().add(command);

                        break;
                    case Command.PROGRAM:
                        commandList.getItems().add(new Command(programCombobox.getSelectionModel().getSelectedItem(),
                                Integer.parseInt(delayField.getText())));
                        break;
                }
            }
        });

        commandList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Command>() {
            @Override
            public void changed(ObservableValue<? extends Command> observable, Command oldValue, Command newValue) {
                if(newValue == null)
                    return;

                commandTypeCB.getSelectionModel().select(newValue.getCommandType());

                switch (newValue.getCommandType()){
                    case Command.KEY:
                        detectKeyPressField.setText(newValue.toString());
                        break;
                    case Command.FUNCTION:
                        functionCombobox.getSelectionModel().select(newValue.getFunction());
                        enableTextField.setText(newValue.getEnableDisplay());
                        enableMessageField.setText(newValue.getEnableMessage());
                        disableTextField.setText(newValue.getDisableDisplay());
                        disableMessageField.setText(newValue.getDisableMessage());
                        break;
                    case Command.PROGRAM:
                        programCombobox.getSelectionModel().select(newValue.getProgram());
                        break;
                }
                delayField.setText(""+newValue.getDelay());
            }
        });

        VBox swapBox = new VBox(10);
        swapBox.getChildren().addAll(swapPane, addButton);

        //Prevents the window from closing on alt+f4 press
        stage.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event) {
                if (event.getCode() == KeyCode.F4 && event.isAltDown()) {
                    event.consume();
                    ignoreCloseRequest = true;
                }
            }
        });

        this.getChildren().addAll(commandList, commandGrid, swapBox);
    }

    private GridPane createKeyCommandBox(){

        Label keyLabel = new Label("Key press");

        detectKeyPressField = new TextField();
        detectKeyPressField.setPromptText("Detect keyPress");
        detectKeyPressField.setEditable(false);

        detectKeyPressField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (!event.getCode().equals(KeyCode.CONTROL) && !event.getCode().equals(KeyCode.ALT) && !event.getCode().equals(KeyCode.SHIFT)){
                    command.setShift(event.isShiftDown());
                    command.setCtrl(event.isControlDown());
                    command.setAlt(event.isAltDown());
                    command.setKeyCode(event.getCode());
                    detectKeyPressField.setText(command.toString());
                }
                event.consume();
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

        Functions functions = new Functions();
        GridPane toggleGrid = createToggleBox();

        functionCombobox = new ComboBox(FXCollections.observableArrayList(functions.getFunctions()));

        functionCombobox.setCellFactory(new Callback<ListView<Function>, ListCell<Function>>() {
            public ListCell<Function> call(ListView<Function> param) {
                ListCell<Function> cell = new ListCell<Function>() {

                    @Override
                    public void updateItem(final Function item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            this.setTooltip(new Tooltip(item.getDescription()));
                            setText(item.toString());
                        }
                    }
                };
                return cell;
            }
        });

        VBox vBox = new VBox(10);

        functionCombobox.valueProperty().addListener(new ChangeListener<Function>() {
            @Override
            public void changed(ObservableValue<? extends Function> observable, Function oldValue, Function newValue) {
                if (newValue.isToggle() && !showingToggleInfo) {
                    getScene().getWindow().setHeight(getScene().getWindow().getHeight() + 140);
                    vBox.getChildren().add(toggleGrid);
                    showingToggleInfo = true;
                } else if (!newValue.isToggle() && showingToggleInfo) {
                    vBox.getChildren().remove(toggleGrid);
                    getScene().getWindow().setHeight(getScene().getWindow().getHeight() - 140);
                    showingToggleInfo = false;
                }
            }
        });

        functionCombobox.getSelectionModel().select(0);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(functionLabel, functionCombobox);

        vBox.getChildren().addAll(hBox);
        return vBox;
    }

    private HBox createProgramBox(List<Program> programs){
        Label programLabel = new Label("Program");
        programLabel.setTooltip(new Tooltip("Runs the program or script selected"));

        programCombobox = new ComboBox(FXCollections.observableArrayList(programs));

        programCombobox.getSelectionModel().select(0);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(programLabel, programCombobox);
        return hBox;
    }

    public List<Command> getItems(){
        return commandList.getItems();
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

    public boolean isIgnoreCloseRequest() {
        return ignoreCloseRequest;
    }

    public void setIgnoreCloseRequest(boolean ignoreCloseRequest) {
        this.ignoreCloseRequest = ignoreCloseRequest;
    }
}
