package graphics.settings;

import graphics.utility.EditList;
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
import models.Program;
import models.Hotkey;
import models.MenuItem;
import settings.WindowSetting;

import java.util.List;

public class WindowSettingsView {

    Stage stage;
    EditList<MenuItem> menuEditList;
    EditList<Hotkey> hotkeyList;
    List<Program> programs;

    public WindowSettingsView(SettingsView settingsView, WindowSetting windowSetting, boolean newItem, List<Program> programs){
        stage = new Stage();
        stage.setTitle("Application");
        stage.initOwner(settingsView.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        this.programs = programs;

        Label nameLabel = new Label("Name");
        TextField nameField = new TextField(windowSetting.getName());
        Label applicationStringLabel = new Label("Application text");
        TextField applicationField = new TextField(windowSetting.getWindowName());

        GridPane topGrid = new GridPane();
        topGrid.setVgap(10);
        topGrid.setHgap(10);
        topGrid.add(nameLabel, 0, 0);
        topGrid.add(nameField, 1, 0);
        topGrid.add(applicationStringLabel, 0, 1);
        topGrid.add(applicationField, 1, 1);

        Label disableMessagesLabel = new Label("Disable messages");
        disableMessagesLabel.setTooltip(new Tooltip("This will prevent any messages from being shown when this application is active"));
        CheckBox disableMessagesCB = new CheckBox();
        disableMessagesCB.setSelected(windowSetting.isDisableMessages());
        disableMessagesCB.setTooltip(new Tooltip("This will prevent any messages from being shown when this application is active"));

        Label disableVibrationLabel = new Label("Disable vibration");
        disableVibrationLabel.setTooltip(new Tooltip("This will prevent controller vibration when this application is active"));
        CheckBox disableVibrationCB = new CheckBox();
        disableVibrationCB.setSelected(windowSetting.isDisableVibration());
        disableVibrationCB.setTooltip(new Tooltip("This will prevent controller vibration when this application is active"));

        Label disableHotkeysLabel= new Label("Disable hotkeys");
        CheckBox disableHotkeysCB = new CheckBox();
        disableHotkeysLabel.setTooltip(new Tooltip("This will disable all hotkeys for the application"));
        disableHotkeysCB.setSelected(windowSetting.isDisableHotkeys());
        disableHotkeysCB.setTooltip(new Tooltip("This will disable all hotkeys for the application"));

        GridPane cbGrid = new GridPane();
        cbGrid.setVgap(10);
        cbGrid.setHgap(10);
        cbGrid.add(disableMessagesLabel, 0,0);
        cbGrid.add(disableMessagesCB, 1,0);
        cbGrid.add(disableVibrationLabel, 0,1);
        cbGrid.add(disableVibrationCB, 1,1);
        cbGrid.add(disableHotkeysLabel, 0,2);
        cbGrid.add(disableHotkeysCB, 1,2);

        Label onTopLabel = new Label("Window on top");
        onTopLabel.setTooltip(new Tooltip("Puts the application on top of other windows"));
        CheckBox onTopCB = new CheckBox();
        onTopCB.setSelected(windowSetting.isTopmost());
        onTopCB.setTooltip(new Tooltip("Puts the application on top of other windows"));

        Label removeBorderLabel = new Label("Remove borders");
        removeBorderLabel.setTooltip(new Tooltip("Removes the windows border and menu"));
        CheckBox removeBorderCB = new CheckBox();
        removeBorderCB.setSelected(windowSetting.isRemoveBorders());
        removeBorderCB.setTooltip(new Tooltip("Removes the windows border and menu"));

        GridPane cbGrid2 = new GridPane();
        cbGrid2.setVgap(10);
        cbGrid2.setHgap(10);
        cbGrid2.add(onTopLabel, 0, 0);
        cbGrid2.add(onTopCB, 1, 0);
        cbGrid2.add(removeBorderLabel, 0, 1);
        cbGrid2.add(removeBorderCB, 1, 1);

        HBox centerBox = new HBox(20);
        centerBox.getChildren().addAll(cbGrid, cbGrid2);

        menuEditList = new EditList<MenuItem>(windowSetting.getMenuItems());
        menuEditList.getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openMenuItemView(new MenuItem(), true);
            }
        });

        menuEditList.getEditButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openMenuItemView(menuEditList.getList().getSelectionModel().getSelectedItem(), false);
            }
        });

        hotkeyList = new EditList<>(windowSetting.getHotkeys());

        hotkeyList.getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openHotkeyView( new Hotkey(), true);
            }
        });

        hotkeyList.getEditButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                openHotkeyView(hotkeyList.getSelected(), false);
            }
        });





        HBox listBox = new HBox(20);
        listBox.getChildren().addAll(menuEditList, hotkeyList);

        Button saveButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                windowSetting.setMenuItems(menuEditList.getItems());
                windowSetting.setDisableMessages(disableMessagesCB.isSelected());
                windowSetting.setName(nameField.getText());
                windowSetting.setWindowName(applicationField.getText());
                windowSetting.setDisableVibration(disableVibrationCB.isSelected());
                windowSetting.setRemoveBorders(removeBorderCB.isSelected());
                windowSetting.setDisableHotkeys(disableHotkeysCB.isSelected());
                windowSetting.setTopmost(onTopCB.isSelected());
                windowSetting.setHotkeys(hotkeyList.getItems());

                if(newItem)
                    settingsView.addApplication(windowSetting);
                else
                    settingsView.updateApplicationList();
                stage.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        HBox buttons = new HBox(50);
        buttons.getChildren().addAll(saveButton, cancelButton);

        VBox vBox = new VBox(25);
        vBox.getChildren().addAll(topGrid, centerBox, listBox, buttons);

        vBox.setPadding(new Insets(15));



        Scene scene = new Scene(vBox, 600, 450);
        stage.setScene(scene);
        stage.show();
    }

    private void openMenuItemView(MenuItem menuItem, boolean newItem){
        if(menuItem != null)
            new MenuItemView(this, menuItem, newItem, programs);
    }

    public void addMenuItem(MenuItem menuItem){
        menuEditList.getItems().add(menuItem);
    }

    public void updateMenuItemList(){
        menuEditList.update();
    }

    private void openHotkeyView(Hotkey hotkey, boolean newItem){
        if(hotkey != null)
            new HotkeyView(this, hotkey, newItem, programs);
    }

    public void addHotkey(Hotkey hotkey){
        hotkeyList.getItems().add(hotkey);
    }

    public void updateHotkeyList(){
        hotkeyList.update();
    }

    public Stage getStage(){
        return stage;
    }
}