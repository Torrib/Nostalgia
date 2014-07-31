package graphics.settings;

import graphics.utility.EditList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Command;
import models.Program;
import models.Hotkey;
import models.MenuItem;
import settings.WindowSetting;

import java.util.List;

public class WindowSettingsView {

    private Stage stage;
    private EditList<MenuItem> menuEditList;
    private EditList<Hotkey> hotkeyList;
    private List<Program> programs;

    private List<Command> preMenuCommands;
    private List<Command> postMenuCommands;

    public WindowSettingsView(SettingsView settingsView, WindowSetting windowSetting, boolean newItem, List<Program> programs){
        stage = new Stage();
        stage.setTitle("Application");
        stage.initOwner(settingsView.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        this.programs = programs;
        preMenuCommands = windowSetting.getPreMenuComands();
        postMenuCommands = windowSetting.getPostMenuCommands();

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

        Label preMenuLabel = new Label("Pre-menu Command");
        Button preMenuButton = new Button("Edit");
        preMenuLabel.setTooltip(new Tooltip("Command that will be executed before the menu is opened"));
        preMenuButton.setTooltip(new Tooltip("Command that will be executed before the menu is opened"));

        Label postMenuLabel = new Label("Post-menu Command");
        Button postMenuButton = new Button("Edit");
        postMenuLabel.setTooltip(new Tooltip("Command that will be executed after the menu is closed"));
        postMenuButton.setTooltip(new Tooltip("Command that will be executed after the menu is closed"));

        preMenuButton.setOnAction(event -> openMenuCommandView(preMenuCommands, true));
        postMenuButton.setOnAction(event -> openMenuCommandView(postMenuCommands, false));

        GridPane menuCommandPane = new GridPane();
        menuCommandPane.setVgap(10);
        menuCommandPane.setHgap(10);
        menuCommandPane.add(preMenuLabel, 0, 0);
        menuCommandPane.add(preMenuButton, 1, 0);
        menuCommandPane.add(postMenuLabel, 0, 1);
        menuCommandPane.add(postMenuButton, 1, 1);

        HBox centerBox = new HBox(20);
        centerBox.getChildren().addAll(cbGrid, cbGrid2, new Label("        "), menuCommandPane);

        menuEditList = new EditList<>(windowSetting.getMenuItems(), true);
        menuEditList.getAddButton().setOnAction(event -> openMenuItemView(new MenuItem(), true));
        menuEditList.getEditButton().setOnAction(event -> openMenuItemView(menuEditList.getList().getSelectionModel().getSelectedItem(), false));

        hotkeyList = new EditList<>(windowSetting.getHotkeys(), false);

        hotkeyList.getAddButton().setOnAction(event -> openHotkeyView( new Hotkey(), true));
        hotkeyList.getEditButton().setOnAction(event -> openHotkeyView(hotkeyList.getSelected(), false));

        VBox menuBox = new VBox(10);
        menuBox.getChildren().addAll(new Label("Menu items"), menuEditList);

        VBox hotkeyBox = new VBox(10);
        hotkeyBox.getChildren().addAll(new Label("Hotkeys"), hotkeyList);


        HBox listBox = new HBox(20);
        listBox.getChildren().addAll(menuBox, hotkeyBox);

        Button saveButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            if(nameField.getText().isEmpty()){
                nameField.setStyle("-fx-border-color: red;-fx-border-style: round|outside");
                return;
            }
            if(applicationField.getText().isEmpty()){
                applicationField.setStyle("-fx-border-color: red;-fx-border-style: round|outside");
                return;
            }
            windowSetting.setMenuItems(menuEditList.getItems());
            windowSetting.setDisableMessages(disableMessagesCB.isSelected());
            windowSetting.setName(nameField.getText());
            windowSetting.setWindowName(applicationField.getText());
            windowSetting.setDisableVibration(disableVibrationCB.isSelected());
            windowSetting.setRemoveBorders(removeBorderCB.isSelected());
            windowSetting.setDisableHotkeys(disableHotkeysCB.isSelected());
            windowSetting.setTopmost(onTopCB.isSelected());
            windowSetting.setHotkeys(hotkeyList.getItems());
            windowSetting.setPreMenuComands(preMenuCommands);
            windowSetting.setPostMenuCommands(postMenuCommands);

            if(newItem)
                settingsView.addApplication(windowSetting);
            else
                settingsView.updateApplicationList();
            stage.close();
        });

        cancelButton.setOnAction(event -> stage.close());

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

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

    private void openMenuCommandView(List<Command> commands, boolean preCommand){
        new MenuCommandView(this, commands, programs, preCommand);
    }

    public void setPreMenuCommands(List<Command> commands){
        preMenuCommands = commands;
    }

    public void setPostMenuCommands(List<Command> commands){
        postMenuCommands = commands;
    }
}
