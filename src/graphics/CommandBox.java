package graphics;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import settings.Command;
import settings.Functions;
import utils.NumberTextField;

import java.util.List;


public class CommandBox extends VBox{

    private ComboBox<String> functionCombobox;
    private Command command;
    private TextField delayField;
    private ListView<Command> commandList;

    public CommandBox(List<Command> items){
        super(10);
        command = new Command();

        commandList = new ListView<>();
        commandList.setItems(FXCollections.observableArrayList(items));
        commandList.setMaxHeight(100);

        Label commandTypeLabel = new Label("Command Type");
        ComboBox<String> commandTypeCB = new ComboBox<>();
        commandTypeCB.setItems(FXCollections.observableArrayList(new String[]{"Key presses", "Functions", "Programs"}));

        Pane swapPane = new Pane();

        HBox keyCommandBox = createKeyCommandBox();
        HBox functionCommandBox = createFunctionBox();

        commandTypeCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                swapPane.getChildren().removeAll(swapPane.getChildren());

                switch (commandTypeCB.getSelectionModel().getSelectedIndex()){
                    case 0:
                        swapPane.getChildren().add(keyCommandBox);
                        break;
                    case 1:
                        swapPane.getChildren().add(functionCommandBox);
                        break;
                    case 2:
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

        Button addButton = new Button("+");

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                switch (commandTypeCB.getSelectionModel().getSelectedIndex()){
                    case Command.KEY:
                        commandList.getItems().add(new Command(command.isCtrl(), command.isAlt(),
                                command.isShift(), command.getKeyCode(), Integer.parseInt(delayField.getText())));
                        break;
                    case Command.FUNCTION:
                        commandList.getItems().add(new Command(functionCombobox.getSelectionModel().getSelectedIndex(),
                                Integer.parseInt(delayField.getText()), functionCombobox.getSelectionModel().getSelectedItem().toString()));
                }
            }
        });

        HBox swapBox = new HBox(10);
        swapBox.getChildren().addAll(swapPane, addButton);

        this.getChildren().addAll(commandList, commandGrid, swapBox);
    }

    private HBox createKeyCommandBox(){

        TextField detectKeyPressField = new TextField();
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
                }
                detectKeyPressField.setText(command.toString());
            }
        });

        HBox botBox = new HBox(5);
        botBox.getChildren().addAll(detectKeyPressField);

        return botBox;
    }

    public HBox createFunctionBox(){
        Label functionLabel = new Label("Function");
        functionLabel.setTooltip(new Tooltip("Functions performs system commands"));

        Functions functions = new Functions();

        functionCombobox = new ComboBox(FXCollections.observableArrayList(functions.getFunctions().keySet()));

        functionCombobox.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            public ListCell<String> call(ListView<String> param) {
                ListCell<String> cell = new ListCell<String>() {

                    @Override
                    public void updateItem(final String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            this.setTooltip(new Tooltip(functions.getFunctions().get(item)));
                            setText(item);
                        }
                    }
                };
                return cell;
            }
        });

        functionCombobox.getSelectionModel().select(0);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(functionLabel, functionCombobox);
        return hBox;
    }

    public List<Command> getItems(){
        return commandList.getItems();
    }
}
