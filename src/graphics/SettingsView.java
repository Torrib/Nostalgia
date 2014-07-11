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

/**
 * Created by thb on 06.07.2014.
 */
public class SettingsView {

    private Main main;
    private Stage stage;
    private Settings settings;
    private TextField menuFontField;
    private TextField menuFontSizeField;
    private TextField messageFontField;
    private TextField messageFontSizeField;
    private TextField messageDelayField;
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
        scene.getStylesheets().add("bordertitlepane.css");
        stage.setScene(scene);
        stage.show();
    }

    private Tab createGeneralTab(){
        BorderTitledPane menuPane = getMenuPane();
        BorderTitledPane messagePane = getMessagePane();

        HBox hBox = new HBox(20);
        hBox.getChildren().addAll(menuPane, messagePane);

        Tab tab = new Tab();
        tab.setContent(hBox);
        tab.setText("General");
        return tab;
    }

    private BorderTitledPane getMenuPane() {
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

        menuFontSizeField = new TextField(""+settings.getMenuFontSize());
        menuFontSizeField.setTooltip(new Tooltip("The main menu's font size"));

        grid.add(menuFontSizeLabel, 0, 1);
        grid.add(menuFontSizeField, 1, 1);
        grid.setPadding(new Insets(15));

        return new BorderTitledPane("Menu", grid, 300, 150);
    }

    private BorderTitledPane getMessagePane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);

        Label messageFontabel = new Label("Message font");
        messageFontabel.setTooltip(new Tooltip("The message box font"));

        messageFontField = new TextField(settings.getMessageFont());
        messageFontField.setTooltip(new Tooltip("The message box font"));

        grid.add(messageFontabel, 0, 0);
        grid.add(messageFontField, 1, 0);

        Label messageFontSizeLabel = new Label("Menu font size");
        messageFontSizeLabel.setTooltip(new Tooltip("The message box font size"));

        messageFontSizeField = new TextField(""+settings.getMessageFontSize());
        messageFontSizeField.setTooltip(new Tooltip("The message box font size"));

        grid.add(messageFontSizeLabel, 0, 1);
        grid.add(messageFontSizeField, 1, 1);

        Label messageDelayLabel = new Label("Message time(ms)");
        messageFontSizeLabel.setTooltip(new Tooltip("How long the message box will be displayed"));
        messageDelayField = new TextField(""+settings.getMessageDelay());
        messageDelayField.setTooltip(new Tooltip("How long the message box will be displayed"));

        grid.add(messageDelayLabel, 0, 2);
        grid.add(messageDelayField, 1, 2);
        grid.setPadding(new Insets(15));

        return new BorderTitledPane("Message Box", grid, 330, 200);
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

        settings.setMessageFont(messageFontField.getText());
        settings.setMessageFontSize(Integer.parseInt(messageFontSizeField.getText()));
        settings.setMessageDelay(Integer.parseInt(messageDelayField.getText()));

        for(WindowSetting s : applicationEditList.getItems())
            for(Hotkey h : s.getHotkeys())
                h.setDelayLoops(h.getDelay() / settings.getControllerPullDelay());
        settings.setWindowSettings(applicationEditList.getItems());

        double selectedFontSize = settings.getMenuFontSize() + (settings.getMenuFontSize() * 0.2);
        settings.setMenuSelectedFontSize((int) selectedFontSize);


        settings.store();
        main.setSettings(settings);
    }
}
