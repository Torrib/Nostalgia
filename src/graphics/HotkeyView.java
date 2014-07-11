package graphics;

import input.HotkeyConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import settings.Hotkey;
import settings.MenuItem;

/**
 * Created by thb on 08.07.2014.
 */
public class HotkeyView {

    private Hotkey hotkey;

    public HotkeyView(ApplicationView applicationView, Hotkey hotkey, boolean newItem){
        Stage stage = new Stage();
        stage.setTitle("Menu item");

        Label buttonLabel = new Label("Button");
        TextField buttonField = new TextField(hotkey.getButton());
        Label messageLabel = new Label("Message");
        TextField messageField = new TextField(hotkey.getMessage());
        Label commandLabel = new Label("Command");
        TextField commandField = new TextField(hotkey.getCommand());
        Label delayLabel = new Label("Delay");
        TextField delayField = new TextField(""+hotkey.getDelay());
        Label vibrateLabel = new Label("Vibrate");
        CheckBox vibrateCB = new CheckBox();
        vibrateCB.setSelected(hotkey.isVibrate());

        Button saveButton = new Button("Save");

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hotkey.setButton(buttonField.getText());
                hotkey.setMessage(messageField.getText());
                hotkey.setCommand(commandField.getText());
                hotkey.setDelay(Integer.parseInt(delayField.getText()));
                hotkey.setVibrate(vibrateCB.isSelected());
                hotkey.setButtonNumber(HotkeyConverter.getHotkeyValue(buttonField.getText()));

                if(newItem)
                    applicationView.addHotkey(hotkey);
                else
                    applicationView.updateHotkeyList();
                stage.close();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(buttonLabel, 0, 0);
        grid.add(buttonField, 1, 0);
        grid.add(messageLabel, 0, 1);
        grid.add(messageField, 1, 1);
        grid.add(commandLabel, 0, 2);
        grid.add(commandField, 1, 2);
        grid.add(delayLabel, 0, 3);
        grid.add(delayField, 1, 3);
        grid.add(vibrateLabel, 0, 4);
        grid.add(vibrateCB, 1, 4);

        grid.add(saveButton, 0, 5);
        grid.add(cancelButton, 1, 5);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }
}
