package graphics;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.Main;
import settings.*;
import utils.NumberTextField;

/**
 * Created by thb on 06.07.2014.
 */
public class SettingsView {

    private Main main;
    private Stage stage;
    private Settings settings;
    private TextField menuFontField;
    private TextField menuFontSizeField;
    private CheckBox muteMenuCB;

    private TextField messageFontField;
    private TextField messageFontSizeField;
    private TextField messageDelayField;
    private CheckBox disableMessagesCB;
    private CheckBox disableSystemMessagesCB;

    private TextField pullDelayField;
    private CheckBox requireActivateCB;
    private CheckBox disableVibrationCB;
    private CheckBox disableControllersCB;
    private CheckBox disableHotkeysCB;

    private TextField windowPullField;
    private TextField windowRefreshDelayField;

    private EditList<WindowSetting> applicationEditList;

    public SettingsView(Main main){
        this.main = main;
        stage = new Stage();
        stage.setTitle("Nostalgia message");

        settings = Settings.load();

        TabPane tabPane = new TabPane();

        tabPane.getTabs().addAll(createApplicationTab(), createGeneralTab());

        Button saveButton = new Button("Save");
        Button applyButton = new Button("Apply");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                save();
                stage.close();
            }
        });

        applyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                save();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        HBox buttons = new HBox(15);
        buttons.getChildren().addAll(saveButton, applyButton, cancelButton);
        buttons.setPadding(new Insets(15));

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(tabPane, buttons);

        Scene scene = new Scene(vBox, 800, 600);
        scene.getStylesheets().add("resources/bordertitlepane.css");
        stage.setScene(scene);
        stage.show();
    }

    private Tab createGeneralTab(){
        BorderTitledPane menuPane = createMenuPane();
        BorderTitledPane messagePane = createMessagePane();
        BorderTitledPane controllerPane = createControllerPane();
        BorderTitledPane windowPane = createWindowPane();

//        HBox hBox = new HBox(20);
//        hBox.getChildren().addAll(menuPane, messagePane);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(24);
//        grid.setPadding(new Insets(15));

        grid.add(menuPane, 0, 0);
        grid.add(messagePane, 1, 0);
        grid.add(controllerPane, 0, 1);
        grid.add(windowPane, 1, 1);

        Tab tab = new Tab();
        tab.setContent(grid);
        tab.setText("General");
        return tab;
    }

    private BorderTitledPane createMenuPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        Label menuFontabel = new Label("Menu font");
        menuFontabel.setTooltip(new Tooltip("The main menu's font"));
        menuFontField = new TextField(settings.getMenuFont());
        menuFontField.setTooltip(new Tooltip("The main menu's font"));

        grid.add(menuFontabel, 0, 0);
        grid.add(menuFontField, 1, 0);

        Label menuFontSizeLabel = new Label("Menu font size");
        menuFontSizeLabel.setTooltip(new Tooltip("The main menu's font size"));
        menuFontSizeField = new NumberTextField(settings.getMenuFontSize());
        menuFontSizeField.setTooltip(new Tooltip("The main menu's font size"));

        grid.add(menuFontSizeLabel, 0, 1);
        grid.add(menuFontSizeField, 1, 1);

        Label muteMenuLabel = new Label("Mute menu");
        muteMenuLabel.setTooltip(new Tooltip("Prevent the menu from playing sounds"));
        muteMenuCB = new CheckBox();
        muteMenuCB.setSelected(settings.menuMuted());
        muteMenuCB.setTooltip(new Tooltip("The main menu's font size"));

        grid.add(muteMenuLabel, 0, 2);
        grid.add(muteMenuCB, 1, 2);

        grid.setPadding(new Insets(15));

        return new BorderTitledPane("Menu", grid, 360, 180);
    }

    private BorderTitledPane createMessagePane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        Label messageFontLabel = new Label("Message font");
        messageFontLabel.setTooltip(new Tooltip("The message box font"));
        messageFontField = new TextField(settings.getMessageFont());
        messageFontField.setTooltip(new Tooltip("The message box font"));

        grid.add(messageFontLabel, 0, 0);
        grid.add(messageFontField, 1, 0);

        Label messageFontSizeLabel = new Label("Menu font size");
        messageFontSizeLabel.setTooltip(new Tooltip("The message box font size"));
        messageFontSizeField = new NumberTextField(settings.getMessageFontSize());
        messageFontSizeField.setTooltip(new Tooltip("The message box font size"));

        grid.add(messageFontSizeLabel, 0, 1);
        grid.add(messageFontSizeField, 1, 1);

        Label messageDelayLabel = new Label("Message time(ms)");
        messageDelayLabel.setTooltip(new Tooltip("How long the message box will be displayed"));
        messageDelayField = new NumberTextField(settings.getMessageDelay());
        messageDelayField.setTooltip(new Tooltip("How long the message box will be displayed"));

        grid.add(messageDelayLabel, 0, 2);
        grid.add(messageDelayField, 1, 2);

        Label disableMessagesLabel = new Label("Disable messages");
        disableMessagesLabel.setTooltip(new Tooltip("This will prevent all messages from being shown"));
        disableMessagesCB = new CheckBox();
        disableMessagesCB.setSelected(settings.isDisableMessages());
        disableMessagesCB.setTooltip(new Tooltip("This will prevent all messages from being shown"));

        grid.add(disableMessagesLabel, 0, 3);
        grid.add(disableMessagesCB, 1, 3);

        Label disableSystemMessagesLabel = new Label("Disable system messages");
        disableSystemMessagesLabel.setTooltip(new Tooltip("This will prevent messages from being shown when for instance a controller connects"));
        disableSystemMessagesCB = new CheckBox();
        disableSystemMessagesCB.setSelected(settings.isDisableSystemMessages());
        disableSystemMessagesCB.setTooltip(new Tooltip("This will prevent messages from being shown when for instance a controller connects"));

        grid.add(disableSystemMessagesLabel, 0, 4);
        grid.add(disableSystemMessagesCB, 1, 4);

        grid.setPadding(new Insets(15));

        return new BorderTitledPane("Message Box", grid, 360, 250);
    }

    private BorderTitledPane createControllerPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(15));

        Label pullDelayLabel = new Label("Controller pull delay(ms)");
        pullDelayLabel.setTooltip(new Tooltip("The delay between controller data pulling"));
        pullDelayField = new NumberTextField(settings.getControllerPullDelay());
        pullDelayField.setTooltip(new Tooltip("The delay between controller data pulling"));

        grid.add(pullDelayLabel, 0, 0);
        grid.add(pullDelayField, 1, 0);

        Label requireActivateLabel = new Label("Require activate");
        requireActivateLabel.setTooltip(new Tooltip("The controller has to be activated before the menu and hotkeys can be used"));
        requireActivateCB = new CheckBox();
        requireActivateCB.setSelected(settings.isRequireActivate());
        requireActivateCB.setTooltip(new Tooltip("The controller has to be activated before the menu and hotkeys can be used"));

        grid.add(requireActivateLabel, 0, 1);
        grid.add(requireActivateCB, 1, 1);

        Label disableVibrationLabel = new Label("Disable vibration");
        disableVibrationLabel.setTooltip(new Tooltip("Disables all vibrations from Nostalgia"));
        disableVibrationCB = new CheckBox();
        disableVibrationCB.setSelected(settings.isDisableVibration());
        disableVibrationCB.setTooltip(new Tooltip("Disables all vibrations from Nostalgia"));

        grid.add(disableVibrationLabel, 0, 2);
        grid.add(disableVibrationCB, 1, 2);

        Label disableControllersLabel = new Label("Disable controller 2-4");
        disableControllersLabel.setTooltip(new Tooltip("Disables all controllers but controller 1"));
        disableControllersCB = new CheckBox();
        disableControllersCB.setSelected(settings.isDisableControllers());
        disableControllersCB.setTooltip(new Tooltip("Disables all controllers but controller 1"));

        grid.add(disableControllersLabel, 0, 3);
        grid.add(disableControllersCB, 1, 3);

        Label disableHotkeysLabel = new Label("Disable hotkeys");
        disableHotkeysLabel.setTooltip(new Tooltip("Disables hotkeys for all applications"));
        disableHotkeysCB = new CheckBox();
        disableHotkeysCB.setSelected(settings.isDisableHotkeys());
        disableHotkeysCB.setTooltip(new Tooltip("Disables hotkeys for all applications"));

        grid.add(disableHotkeysLabel, 0, 4);
        grid.add(disableHotkeysCB, 1, 4);

        return new BorderTitledPane("Controllers", grid, 360, 300);
    }

    private BorderTitledPane createWindowPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        Label windowPullLabel = new Label("Window pull delay(ms)");
        windowPullLabel.setTooltip(new Tooltip("How often window(active application) information will be pulled"));
        windowPullField = new NumberTextField(settings.getWindowPullDelay());
        windowPullField.setTooltip(new Tooltip("How often window(active application) information will be pulled"));

        grid.add(windowPullLabel, 0, 0);
        grid.add(windowPullField, 1, 0);

        Label windowPullRefreshDelayLabel = new Label("Window refresh delay(ms)");
        windowPullRefreshDelayLabel.setTooltip(new Tooltip("How often the window manager will refresh"));
        windowRefreshDelayField = new NumberTextField(settings.getWindowPullRefresh());
        windowRefreshDelayField.setTooltip(new Tooltip("How often the window manager will refresh"));

        grid.add(windowPullRefreshDelayLabel, 0, 1);
        grid.add(windowRefreshDelayField, 1, 1);

        grid.setPadding(new Insets(15));

        return new BorderTitledPane("Window handling", grid, 360, 100);
    }

    private Tab createApplicationTab(){

        applicationEditList = new EditList<>(settings.getWindowSettings());

        applicationEditList.getAddButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openApplicationView(new WindowSetting(), true);
            }
        });

        applicationEditList.getEditButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                openApplicationView(applicationEditList.getSelected(), false);
            }
        });

        Tab tab = new Tab();
        tab.setContent(applicationEditList);
        tab.setText("Applications");
        return tab;
    }

    private void openApplicationView(WindowSetting windowSetting, boolean newSetting){
        if(windowSetting != null)
            new ApplicationView(this, windowSetting, newSetting);
    }

    public void addApplication(WindowSetting windowSetting){
        applicationEditList.getItems().add(windowSetting);
    }

    public void updateApplicationList(){
        applicationEditList.update();
    }

    private void save(){
        settings.setMenuFont(menuFontField.getText());
        settings.setMenuFontSize(Integer.parseInt(menuFontSizeField.getText()));
        settings.setMenuMuted(muteMenuCB.isSelected());

        settings.setMessageFont(messageFontField.getText());
        settings.setMessageFontSize(Integer.parseInt(messageFontSizeField.getText()));
        settings.setMessageDelay(Integer.parseInt(messageDelayField.getText()));
        settings.setDisableMessages(disableMessagesCB.isSelected());
        settings.setDisableSystemMessages(disableSystemMessagesCB.isSelected());

        settings.setControllerPullDelay(Integer.parseInt(pullDelayField.getText()));
        settings.setRequireActivate(requireActivateCB.isSelected());
        settings.setDisableVibration(disableVibrationCB.isSelected());
        settings.setDisableControllers(disableControllersCB.isSelected());
        settings.setDisableHotkeys(disableHotkeysCB.isSelected());

        settings.setWindowPullDelay(Integer.parseInt(windowPullField.getText()));
        settings.setWindowPullRefresh(Integer.parseInt(windowRefreshDelayField.getText()));
        settings.setWindowPullRefreshCount(settings.getWindowPullRefresh() / settings.getWindowPullDelay());


        for(WindowSetting ws : applicationEditList.getItems())
            for(Hotkey hotkey : ws.getHotkeys())
                hotkey.setDelayLoops(hotkey.getDelay() / settings.getControllerPullDelay());
        settings.setWindowSettings(applicationEditList.getItems());

        double selectedFontSize = settings.getMenuFontSize() + (settings.getMenuFontSize() * 0.2);
        settings.setMenuSelectedFontSize((int) selectedFontSize);


        settings.store();
        main.setSettings(settings);
    }
}
