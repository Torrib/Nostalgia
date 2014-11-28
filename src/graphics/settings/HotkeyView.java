package graphics.settings;

import graphics.utility.CommandBox;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Hotkey;

public class HotkeyView {

    public HotkeyView(Stage parent, Hotkey hotkey, Runnable onSave){
        Stage stage = new Stage();
        stage.setTitle("Menu item");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);


        Label nameLabel = new Label("Name");
        TextField nameField = new TextField(hotkey.getName());
        Label buttonLabel = new Label("Button");
        ComboBox<jgamepad.enums.Button> buttonCB = new ComboBox(FXCollections.observableArrayList(jgamepad.enums.Button.values()));
        buttonCB.getSelectionModel().select(hotkey.getButtonValue());
        Label messageLabel = new Label("Message");
        TextField messageField = new TextField(hotkey.getMessage());
        Label displayTimeLabel = new Label("Hold time");
        TextField displayTimeField = new TextField(""+hotkey.getDelay());
        Label vibrateLabel = new Label("Vibrate");
        CheckBox vibrateCB = new CheckBox();
        vibrateCB.setSelected(hotkey.vibrate());
        CommandBox commandBox = new CommandBox(hotkey.getCommands(), stage);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(buttonLabel, 0, 1);
        grid.add(buttonCB, 1, 1);
        grid.add(messageLabel, 0, 2);
        grid.add(messageField, 1, 2);
        grid.add(displayTimeLabel, 0, 3);
        grid.add(displayTimeField, 1, 3);
        grid.add(vibrateLabel, 0, 4);
        grid.add(vibrateCB, 1, 4);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event ->{
                if (commandBox.isIgnoreCloseRequest()) {
                    event.consume();
                    commandBox.setIgnoreCloseRequest(false);
                } else {
                    hotkey.setName(nameField.getText());
                    hotkey.setButton(buttonCB.getSelectionModel().getSelectedItem());
                    hotkey.setMessage(messageField.getText());
                    hotkey.setDelay(Integer.parseInt(displayTimeField.getText()));
                    hotkey.setVibrate(vibrateCB.isSelected());
                    hotkey.setCommands(commandBox.getItems());

                    onSave.run();

                    stage.close();
                }
        });

        cancelButton.setOnAction(event -> stage.close());

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(grid, commandBox, buttonBox);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
