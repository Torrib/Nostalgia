package graphics.settings;

import graphics.utility.ButtonView;
import graphics.utility.CommandView;
import graphics.utility.EditList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jgamepad.enums.*;
import models.ButtonPlaceholder;
import models.Command;
import models.Hotkey;

public class HotkeyView {

    private Stage stage;
    private EditList<Command> commandEditList;
    private EditList<jgamepad.enums.Button> buttonEditList;

    public HotkeyView(Stage parent, Hotkey hotkey, Runnable onSave){
        stage = new Stage();
        stage.setTitle("Hotkeys");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);


        Label nameLabel = new Label("Name");
        TextField nameField = new TextField(hotkey.getName());
        Label buttonsLabel = new Label("Buttons");
        buttonEditList = new EditList<>(hotkey.getButtons(), false);
        buttonEditList.getAddButton().setOnAction(event -> openButtonView(new ButtonPlaceholder(), true));
        buttonEditList.getEditButton().setOnAction(event -> openButtonView(new ButtonPlaceholder(buttonEditList.getSelected()), false));
        buttonEditList.setMaxHeight(60);

        Label messageLabel = new Label("Message");
        TextField messageField = new TextField(hotkey.getMessage());
        Label displayTimeLabel = new Label("Hold time");
        TextField displayTimeField = new TextField(""+hotkey.getDelay());
        Label vibrateLabel = new Label("Vibrate");
        CheckBox vibrateCB = new CheckBox();
        vibrateCB.setSelected(hotkey.vibrate());

        Label commandsLabel = new Label("Commands");
        commandsLabel.setPadding(new Insets(10,0,0,0));
        commandEditList = new EditList<>(hotkey.getCommands(), true);
        commandEditList.getAddButton().setOnAction(event -> openCommandView(new Command(), true));
        commandEditList.getEditButton().setOnAction(event -> openCommandView(commandEditList.getSelected(), false));
        commandEditList.setPrefHeight(100);

        GridPane grid1 = new GridPane();
        grid1.setVgap(10);
        grid1.setHgap(10);
        grid1.add(nameLabel, 0, 0);
        grid1.add(nameField, 1, 0);

        GridPane grid2 = new GridPane();
        grid2.setVgap(10);
        grid2.setHgap(10);
        grid2.add(messageLabel, 0, 2);
        grid2.add(messageField, 1, 2);
        grid2.add(displayTimeLabel, 0, 3);
        grid2.add(displayTimeField, 1, 3);
        grid2.add(vibrateLabel, 0, 4);
        grid2.add(vibrateCB, 1, 4);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event ->{
                    hotkey.setName(nameField.getText());
                    hotkey.setButtons(buttonEditList.getItems());
                    hotkey.setMessage(messageField.getText());
                    hotkey.setDelay(Integer.parseInt(displayTimeField.getText()));
                    hotkey.setVibrate(vibrateCB.isSelected());
                    hotkey.setCommands(commandEditList.getItems());

                    onSave.run();

                    stage.close();
        });

        cancelButton.setOnAction(event -> stage.close());

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(grid1, buttonsLabel, buttonEditList, grid2, commandsLabel, commandEditList, buttonBox);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    private void openCommandView(Command command, boolean newItem){

        Runnable onSave = () -> {
            if(newItem)
                commandEditList.getItems().add(command);
            else
                commandEditList.update();
        };

        if(command != null)
            new CommandView(stage, command, onSave);
    }

    private void openButtonView(ButtonPlaceholder placeholder, boolean newItem){

        Runnable onSave = () -> {
            if(newItem)
                buttonEditList.getItems().add(placeholder.getButton());
            else {
                buttonEditList.getItems().remove(buttonEditList.getSelected());
                buttonEditList.getItems().add(placeholder.getButton());
            }
        };

        if(placeholder != null)
            new ButtonView(stage, placeholder, onSave);
    }
}
