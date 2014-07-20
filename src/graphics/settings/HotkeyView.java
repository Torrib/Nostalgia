package graphics.settings;

import graphics.utility.CommandBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Program;
import models.Hotkey;

import java.util.List;

/**
 * Created by thb on 08.07.2014.
 */
public class HotkeyView {

    public HotkeyView(WindowSettingsView applicationView, Hotkey hotkey, boolean newItem, List<Program> programs){
        Stage stage = new Stage();
        stage.setTitle("Menu item");
        stage.initOwner(applicationView.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        Label buttonLabel = new Label("Button");
        ComboBox buttonCB = new ComboBox(FXCollections.observableArrayList(hotkey.getButtonList()));
        buttonCB.getSelectionModel().select(hotkey.getButton());
        Label messageLabel = new Label("Message");
        TextField messageField = new TextField(hotkey.getMessage());
        Label displayTimeLabel = new Label("Hold time");
        TextField displayTimeField = new TextField(""+hotkey.getDisplayTime());
        Label vibrateLabel = new Label("Vibrate");
        CheckBox vibrateCB = new CheckBox();
        vibrateCB.setSelected(hotkey.vibrate());
        CommandBox commandBox = new CommandBox(hotkey.getCommands(), programs);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                hotkey.setButton(buttonCB.getSelectionModel().getSelectedIndex());
                hotkey.setMessage(messageField.getText());
                hotkey.setDisplayTime(Integer.parseInt(displayTimeField.getText()));
                hotkey.setVibrate(vibrateCB.isSelected());
                hotkey.setCommands(commandBox.getItems());

                if(newItem)
                    applicationView.addHotkey(hotkey);
                else
                    applicationView.updateHotkeyList();
                stage.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(buttonLabel, 0, 0);
        grid.add(buttonCB, 1, 0);
        grid.add(messageLabel, 0, 1);
        grid.add(messageField, 1, 1);
        grid.add(displayTimeLabel, 0, 2);
        grid.add(displayTimeField, 1, 2);
        grid.add(vibrateLabel, 0, 3);
        grid.add(vibrateCB, 1, 3);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(grid, commandBox);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}