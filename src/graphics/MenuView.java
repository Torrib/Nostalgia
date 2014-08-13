package graphics;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.Bloom;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import main.Main;
import models.MenuItem;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.List;

public class MenuView {

    private Main main;
    private Stage stage;
    private ListView<MenuItem> list;
    private BorderPane borderPane;
    private Label controllerLabel;

    private AudioClip menuSound;
    private boolean stopSound;

    private int selectedIndex = 0;
    private List<MenuItem> mainMenuItems;
    private boolean mainMenuShowing = true;

    public MenuView(Main main){
        this.main = main;
        stage = new Stage();
        stage.setTitle("Nostalgia menu");
        list = new ListView<>();

        File file = new File("src/resources/button-29.mp3");
        menuSound = new AudioClip(file.toURI().toString());

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        setupControllerLabel(primaryScreenBounds);

        borderPane = new BorderPane();
        borderPane.setTop(controllerLabel);
        borderPane.setCenter(list);

        borderPane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(borderPane, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
        scene.getStylesheets().add("resources/list.css");
        scene.setFill(Color.rgb(0, 0, 0, 0.95));

        setupKeyHandler();
        setupListener();

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    private void setupKeyHandler() {
        list.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                performCommand();
            }
            if (keyEvent.getCode() == KeyCode.BACK_SPACE || keyEvent.getCode().equals(KeyCode.LEFT)) {
                backtrack();
            }
        });
    }

    private void setupControllerLabel(Rectangle2D primaryScreenBounds) {
        controllerLabel = new Label();
        controllerLabel.setTextFill(Color.WHITE);
        controllerLabel.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));
        controllerLabel.setPadding(new Insets(primaryScreenBounds.getHeight() / 5, 0, primaryScreenBounds.getHeight() / 5,
                (primaryScreenBounds.getWidth() / 2) - 55));
    }

    public void show(List<MenuItem> menuItems, int controller){
        stopSound = true;
        mainMenuItems = menuItems;
        updateList(menuItems);
        controllerLabel.setText("Controller " + (controller+1));
        show();
        stopSound = false;
    }

    public void playSound(){
        if(!Main.SETTINGS.menuMuted())
            if(!stopSound)
                menuSound.play();
    }

    public void hide(){
        main.returnFocus();
        stage.hide();
        main.command(main.getActiveWindowSettings().getPostMenuCommands(), 0);
    }

    public boolean isShowing(){
        return stage.isShowing();
    }

    private void performCommand(){
        MenuItem selectedItem = list.getSelectionModel().getSelectedItem();

        if(selectedItem.isConfirmation()) {
            createConfirmationList();
        }
        else if(selectedItem.getType() == MenuItem.SUBMENU){
            selectedIndex = list.getSelectionModel().getSelectedIndex();
            mainMenuShowing = false;
            updateList(selectedItem.getSubMenu().getMenuItems());
        }
        else {
            hide();
            main.command(list.getSelectionModel().getSelectedItem(), null);
        }
    }

    private void createConfirmationList() {
        ListView<String> confirmationList = new ListView();
        confirmationList.getItems().addAll("OK", "Cancel");
        confirmationList.setOrientation(Orientation.HORIZONTAL);

        confirmationList.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if(confirmationList.getSelectionModel().getSelectedIndex() == 0){
                    hide();
                    main.command(list.getSelectionModel().getSelectedItem(), null);
                }
                else{
                    borderPane.getChildren().removeAll(confirmationList);
                    borderPane.setCenter(list);
                }
            }
            if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                borderPane.getChildren().removeAll(confirmationList);
                borderPane.setCenter(list);
            }
        });

        confirmationList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                playSound();
                confirmationList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                    @Override
                    public ListCell<String> call(ListView<String> param) {
                        return new ListCell<String>() {

                            @Override
                            public void updateItem(final String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    if(item.equals(newValue)) {
                                        this.setTextFill(Color.WHITE);
                                        this.setFont(Font.font(Main.SETTINGS.getMenuSelectedFontSize()));
                                        setEffect(new Bloom(0.1));
                                        setText(item);
                                    }
                                    else{
                                        this.setTextFill(Color.WHITE);
                                        this.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));
                                        setText(item);
                                    }
                                }
                            }
                        };
                    }
                });
            }
        });

        confirmationList.getSelectionModel().selectLast();

        borderPane.getChildren().removeAll(list);
        borderPane.setCenter(confirmationList);
        //TODO fix better centering
        confirmationList.setMaxWidth(300);
    }

    private void updateList(List<MenuItem> menuItems){
        list.getItems().clear();
        list.setItems(FXCollections.observableArrayList(menuItems));
        if(mainMenuShowing)
            list.getSelectionModel().select(selectedIndex);
        else
            list.getSelectionModel().selectFirst();
    }

    private void backtrack(){
        if(mainMenuShowing) {
            hide();
        }
        else {
            mainMenuShowing = true;
            updateList(mainMenuItems);
        }
    }

    private void setupListener(){
        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MenuItem>() {
            @Override
            public void changed(ObservableValue<? extends MenuItem> observable, MenuItem oldValue, MenuItem newValue) {
                playSound();
                list.setCellFactory(new Callback<ListView<MenuItem>, ListCell<MenuItem>>() {
                    @Override
                    public ListCell<MenuItem> call(ListView<MenuItem> param) {
                        return new ListCell<MenuItem>() {
                            @Override
                            public void updateItem(final MenuItem item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    if(item == newValue) {
                                        this.setFont(Font.font(Main.SETTINGS.getMenuSelectedFontSize()));
                                        setEffect(new Bloom(0.1));
                                    }
                                    else{
                                        this.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));
                                    }
                                    this.setTextFill(Color.WHITE);
                                    this.setAlignment(Pos.CENTER);
                                    if(item.getType() == MenuItem.SUBMENU) {
                                        Polygon arrow = new Polygon();
                                        arrow.getPoints().addAll(new Double[]{0.0, 0.0, 10.0, 10.0, 0.0, 20.0});
                                        arrow.setFill(javafx.scene.paint.Paint.valueOf("#FFFFFF"));
                                        Label text = new Label(item.toString());
                                        text.setTextFill(javafx.scene.paint.Paint.valueOf("#FFFFFF"));
                                        HBox box = new HBox(20);
                                        box.getChildren().addAll(text, arrow);
                                        box.setAlignment(Pos.CENTER);
                                        setGraphic(box);
                                    }
                                    else
                                        setText(item.toString());
                                }
                            }
                        };
                    }
                });
            }
        });
    }

    private void show(){
        stage.show();
        try {
            Robot bot = new Robot();
            bot.mouseMove(0,0);
            bot.mousePress(InputEvent.BUTTON1_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
        catch (AWTException e){ e.printStackTrace();}
    }
}
