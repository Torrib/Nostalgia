package graphics.settings;

import graphics.utility.EditList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import main.Main;
import models.Hotkey;
import models.Program;
import models.SubMenu;
import settings.*;
import graphics.utility.NumberTextField;

public class SettingsView {

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
    private CheckBox disableHotkeysCB;

    private CheckBox disableController1;
    private CheckBox disableController2;
    private CheckBox disableController3;
    private CheckBox disableController4;

    private TextField windowPullField;
    private TextField windowRefreshDelayField;
    private CheckBox runOnStartupCB;

    private EditList<WindowSetting> windowEditList;
    private EditList<Program> programEditList;
    private EditList<SubMenu> subMenuEditList;

    private Main main;

    public SettingsView(Main main){
        this.main = main;
        stage = new Stage();
        stage.setTitle("Nostalgia settings");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/controller.png")));

        settings = Main.SETTINGS;

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        tabPane.getTabs().addAll(createMenuTab(), createMessageTab(), createControllerTab(),
                createWindowsTab(), createProgramTab(), createSubMenuTab(), createAdvancedTab());

        Button saveButton = new Button("Save");
        Button applyButton = new Button("Apply");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            save();
            stage.close();
        });

        applyButton.setOnAction(event -> save());
        cancelButton.setOnAction(event -> stage.close());

        HBox buttons = new HBox(5);
        buttons.getChildren().addAll(saveButton, applyButton, cancelButton);
        buttons.setPadding(new Insets(15));
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(tabPane, buttons);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("resources/bordertitlepane.css");
        scene.getStylesheets().add(("resources/settings.css"));
        stage.setScene(scene);
        stage.show();
    }

    private Tab createMenuTab(){
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

        Tab tab = new Tab();
        tab.setContent(grid);
        tab.setText("Menu");

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/resources/menu.png")));
        tab.setGraphic(image);
        return tab;
    }

    private Tab createMessageTab() {
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

        Tab tab = new Tab();
        tab.setContent(grid);
        tab.setText("Message");

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/resources/message.png")));
        tab.setGraphic(image);
        return tab;
    }

    private Tab createControllerTab() {
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

        Label disableHotkeysLabel = new Label("Disable hotkeys");
        disableHotkeysLabel.setTooltip(new Tooltip("Disables hotkeys for all applications"));
        disableHotkeysCB = new CheckBox();
        disableHotkeysCB.setSelected(settings.isDisableHotkeys());
        disableHotkeysCB.setTooltip(new Tooltip("Disables hotkeys for all applications"));

        grid.add(disableHotkeysLabel, 0, 3);
        grid.add(disableHotkeysCB, 1, 3);

        Label disableC1Label = new Label("Disable controller 1");
        disableC1Label.setTooltip(new Tooltip("Prevents Controller 1 from using Nostalgia functionality"));
        disableController1 = new CheckBox();
        disableController1.setSelected(settings.isDisableController1());
        disableController1.setTooltip(new Tooltip("Prevents Controller 1 from using Nostalgia functionality"));

        Label disableC2Label = new Label("Disable controller 2");
        disableC2Label.setTooltip(new Tooltip("Prevents Controller 2 from using Nostalgia functionality"));
        disableController2 = new CheckBox();
        disableController2.setSelected(settings.isDisableController2());
        disableController2.setTooltip(new Tooltip("Prevents Controller 2 from using Nostalgia functionality"));

        Label disableC3Label = new Label("Disable controller 3");
        disableC3Label.setTooltip(new Tooltip("Prevents Controller 3 from using Nostalgia functionality"));
        disableController3 = new CheckBox();
        disableController3.setSelected(settings.isDisableController3());
        disableController3.setTooltip(new Tooltip("Prevents Controller 3 from using Nostalgia functionality"));

        Label disableC4Label = new Label("Disable controller 4");
        disableC4Label.setTooltip(new Tooltip("Prevents Controller 4 from using Nostalgia functionality"));
        disableController4 = new CheckBox();
        disableController4.setSelected(settings.isDisableController4());
        disableController4.setTooltip(new Tooltip("Prevents Controller 4 from using Nostalgia functionality"));

        GridPane disableControllersGrid = new GridPane();
        disableControllersGrid.setHgap(10);
        disableControllersGrid.setVgap(12);
        disableControllersGrid.setPadding(new Insets(15));

        disableControllersGrid.add(disableC1Label, 0, 0);
        disableControllersGrid.add(disableController1, 1, 0);
        disableControllersGrid.add(disableC2Label, 0, 1);
        disableControllersGrid.add(disableController2, 1, 1);
        disableControllersGrid.add(disableC3Label, 0, 2);
        disableControllersGrid.add(disableController3, 1, 2);
        disableControllersGrid.add(disableC4Label, 0, 3);
        disableControllersGrid.add(disableController4, 1, 3);

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(grid, disableControllersGrid);

        Tab tab = new Tab();
        tab.setContent(vBox);
        tab.setText("Controllers");

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/resources/controller.png")));
        tab.setGraphic(image);
        return tab;
    }

    private Tab createAdvancedTab() {
        Label windowPullLabel = new Label("Window pull delay(ms)");
        windowPullLabel.setTooltip(new Tooltip("How often window(active application) information will be pulled"));
        windowPullField = new NumberTextField(settings.getWindowPullDelay());
        windowPullField.setTooltip(new Tooltip("How often window(active application) information will be pulled"));

        Label windowPullRefreshDelayLabel = new Label("Window refresh delay(ms)");
        windowPullRefreshDelayLabel.setTooltip(new Tooltip("How often the window manager will refresh"));
        windowRefreshDelayField = new NumberTextField(settings.getWindowPullRefresh());
        windowRefreshDelayField.setTooltip(new Tooltip("How often the window manager will refresh"));

        Label runOnStartupLabel = new Label("Run on startup");
        runOnStartupCB = new CheckBox();
        runOnStartupCB.setSelected(settings.isRunOnStartup());
        runOnStartupLabel.setTooltip(new Tooltip("Run Nostalgia when the computer starts (Requires administrator rights)"));
        runOnStartupCB.setTooltip(new Tooltip("Run Nostalgia when the computer starts (Requires administrator rights)"));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.add(windowPullLabel, 0, 0);
        grid.add(windowPullField, 1, 0);
        grid.add(windowPullRefreshDelayLabel, 0, 1);
        grid.add(windowRefreshDelayField, 1, 1);
        grid.add(runOnStartupLabel, 0, 2);
        grid.add(runOnStartupCB, 1, 2);

        grid.setPadding(new Insets(15));

        Tab tab = new Tab();
        tab.setContent(grid);
        tab.setText("Advanced");

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/resources/advanced.png")));
        tab.setGraphic(image);
        return tab;
    }

    private Tab createWindowsTab(){

        windowEditList = new EditList<>(settings.getWindowSettings(), false);

        windowEditList.getAddButton().setOnAction(event -> {
            WindowSetting windowSetting = new WindowSetting();
            new WindowSettingsView(stage, windowSetting, () -> windowEditList.getItems().add(windowSetting));
        });
        windowEditList.getEditButton().setOnAction(event -> {
            WindowSetting windowSetting = windowEditList.getSelected();
            new WindowSettingsView(stage, windowSetting, () -> windowEditList.update());
        });

        windowEditList.setPadding(new Insets(25));

        Tab tab = new Tab();
        tab.setContent(windowEditList);
        tab.setText("Windows");
        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/resources/monitor.png")));
        tab.setGraphic(image);

        return tab;
    }

    private Tab createProgramTab(){

        programEditList = new EditList<>(settings.getPrograms(), false);

        programEditList.getAddButton().setOnAction(event -> {
            Program program = new Program();
            new ProgramView(stage, program, () -> programEditList.getItems().add(program));
        });
        programEditList.getEditButton().setOnAction(event -> {
            Program program = programEditList.getSelected();
            new ProgramView(stage, program, () -> programEditList.update());
        });

        programEditList.setPadding(new Insets(25));

        Tab tab = new Tab();
        tab.setContent(programEditList);
        tab.setText("Programs");

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/resources/program.png")));
        tab.setGraphic(image);
        return tab;
    }

    private Tab createSubMenuTab(){

        subMenuEditList = new EditList<>(settings.getSubMenus(), false);

        subMenuEditList.getAddButton().setOnAction(event -> {
            SubMenu subMenu = new SubMenu();
            new SubMenuView(stage, subMenu, () -> subMenuEditList.getItems().add(subMenu));
        });
        subMenuEditList.getEditButton().setOnAction(event -> {
            SubMenu subMenu = subMenuEditList.getSelected();
            new SubMenuView(stage, subMenu, () -> subMenuEditList.update());
        });

        subMenuEditList.setPadding(new Insets(25));

        Tab tab = new Tab();
        tab.setContent(subMenuEditList);
        tab.setText("Sub menus");

        ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/resources/program.png")));
        tab.setGraphic(image);
        return tab;
    }

    private void save(){
        settings.setFirstRun(false);

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
        settings.setDisableHotkeys(disableHotkeysCB.isSelected());

        settings.setDisableController1(disableController1.isSelected());
        settings.setDisableController2(disableController2.isSelected());
        settings.setDisableController3(disableController3.isSelected());
        settings.setDisableController4(disableController4.isSelected());

        settings.setWindowPullDelay(Integer.parseInt(windowPullField.getText()));
        settings.setWindowPullRefresh(Integer.parseInt(windowRefreshDelayField.getText()));
        settings.setWindowPullRefreshCount(settings.getWindowPullRefresh() / settings.getWindowPullDelay());
        settings.setRunOnStartup(runOnStartupCB.isSelected());

        settings.setPrograms(programEditList.getItems());


        for(WindowSetting ws : windowEditList.getItems())
            for(Hotkey hotkey : ws.getHotkeys())
                hotkey.setDelayLoops(hotkey.getDisplayTime() / settings.getControllerPullDelay());
        settings.setWindowSettings(windowEditList.getItems());

        settings.setSubMenus(subMenuEditList.getItems());

        double selectedFontSize = settings.getMenuFontSize() + (settings.getMenuFontSize() * 0.2);
        settings.setMenuSelectedFontSize((int) selectedFontSize);

        settings.store();
        main.updateControllerStatus();
        main.handleRunOnStartup();
    }

    public Stage getStage(){
        return stage;
    }
}
