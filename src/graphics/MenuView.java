package graphics;

import controller.Controller;
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
import javafx.scene.text.Text;
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
    private boolean playSound;

    private int selectedIndex = 0;
    private List<MenuItem> mainMenuItems;
    private boolean mainMenuShowing = true;
    private Controller controller;
    private Rectangle2D primaryScreenBounds;

    private double confirmationListWidth = 0;

    public MenuView(Main main){
        this.main = main;
        stage = new Stage();
        stage.setTitle("Nostalgia menu");
        list = new ListView<>();

        File file = new File("src/resources/button-29.mp3");
        menuSound = new AudioClip(file.toURI().toString());

        primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        controllerLabel = new Label();

        borderPane = new BorderPane();
        borderPane.setTop(controllerLabel);
        borderPane.setCenter(list);

        borderPane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(borderPane, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
        scene.getStylesheets().add("resources/list.css");
        scene.setFill(Color.rgb(0, 0, 0, 0.95));

        setupKeyHandler();
        setupMenuListener();

        applySettings();

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    public void show(List<MenuItem> menuItems, Controller controller){
        this.controller = controller;
        playSound = false;
        mainMenuItems = menuItems;
        updateList(menuItems, false);
        controllerLabel.setText("Controller " + (controller.getControllerNumber() + 1));

        show();
        playSound = true;
    }

    public void hide(){
        main.returnFocus();
        stage.hide();
        controller.deactivateMenu();
        main.command(main.getActiveWindowSettings().getPostMenuCommands());
    }

    public boolean isShowing(){
        return stage.isShowing();
    }

    public void applySettings(){
        setupControllerLabel();
        String[] confirmationListItems = {"OK", "Cancel"};
        confirmationListWidth = calculateWidth(confirmationListItems);
    }

    private void performCommand(){
        MenuItem selectedItem = list.getSelectionModel().getSelectedItem();

        if(selectedItem.isConfirmation()) {
            createConfirmationList();
        }
        else if(selectedItem.getType() == MenuItem.SUBMENU){
            selectedIndex = list.getSelectionModel().getSelectedIndex();
            mainMenuShowing = false;
            updateList(selectedItem.getSubMenu().getMenuItems(), false);
        }
        else {
            hide();
            main.command(list.getSelectionModel().getSelectedItem(), controller);
        }
    }

    private double calculateWidth(String[] items){
        double width = 0;

        for(int i = 0; i < items.length; i++){
            Text text = new Text();
            if(i == 0)
                text.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuSelectedFontSize()));
            else
                text.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));
            text.setText(items[i]);
            width += text.getLayoutBounds().getWidth();
        }
        return width * 2;
    }

    private void updateList(List<MenuItem> menuItems, boolean backtrack){
        playSound = false;
        list.getItems().clear();
        list.setItems(FXCollections.observableArrayList(menuItems));

        if(backtrack)
            list.getSelectionModel().select(selectedIndex);
        else if(!mainMenuShowing)
            list.getSelectionModel().selectFirst();

        playSound = true;
    }

    private void backtrack(){
        if(!mainMenuShowing) {
            mainMenuShowing = true;
            updateList(mainMenuItems, true);
        }
        else {
            hide();
        }
    }

    private void setupMenuListener(){
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
                                        this.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuSelectedFontSize()));
                                        this.setEffect(new Bloom(0.1));
                                    }
                                    else{
                                        this.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));
                                    }
                                    if(item.getType() == MenuItem.SUBMENU) {
                                        Polygon arrow = createSubMenuArrow();
                                        Label text = new Label(item.toString());
                                        text.setTextFill(Color.WHITE);
                                        HBox box = new HBox(20);
                                        box.getChildren().addAll(text, arrow);
                                        box.setAlignment(Pos.CENTER);
                                        setGraphic(box);
                                    }
                                    else {
                                        this.setTextFill(Color.WHITE);
                                        this.setAlignment(Pos.CENTER);
                                        setText(item.toString());
                                    }
                                }
                            }
                        };
                    }
                });
            }
        });
    }

    private void show(){
        //TODO List not always focused. Find a better solution
        stage.show();
        try {
            Robot bot = new Robot();
            bot.mouseMove(0,0);
            bot.mousePress(InputEvent.BUTTON1_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
        catch (AWTException e){ e.printStackTrace();}
    }

    private void createConfirmationList() {
        ListView<String> confirmationList = new ListView();
        confirmationList.getItems().addAll("OK", "Cancel");
        confirmationList.setOrientation(Orientation.HORIZONTAL);

        confirmationList.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                if(confirmationList.getSelectionModel().getSelectedIndex() == 0){
                    borderPane.getChildren().removeAll(confirmationList);
                    borderPane.setCenter(list);
                    hide();
                    main.command(list.getSelectionModel().getSelectedItem(), controller);
                    if(!mainMenuShowing)
                        backtrack();
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

        playSound = false;

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
                                    this.setTextFill(Color.WHITE);
                                    this.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));
                                    this.setText(item);
                                    if(item.equals(newValue)) {
                                        this.setEffect(new Bloom(0.1));
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

        //Centers the confirmation list
        confirmationList.setMaxWidth(confirmationListWidth);

        playSound = true;
    }

    private void setupKeyHandler() {
        list.setOnKeyReleased(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                performCommand();
            }
            if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                backtrack();
            }
        });
    }

    private void setupControllerLabel() {

        Text text = new Text("Controller 0");
        text.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));

        controllerLabel.setTextFill(Color.WHITE);
        controllerLabel.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));

        controllerLabel.setPadding(new Insets(primaryScreenBounds.getHeight() / 5, 0, primaryScreenBounds.getHeight() / 5,
                (primaryScreenBounds.getWidth() / 2) - (text.getLayoutBounds().getWidth() / 2)));
    }

    private void playSound(){
        if(!Main.SETTINGS.menuMuted() && playSound)
            menuSound.play();
    }

    private Polygon createSubMenuArrow(){
        Polygon arrow = new Polygon();

        double smallSide = Main.SETTINGS.getMenuFontSize() / 2.5;

        arrow.getPoints().addAll(new Double[]{0.0, 0.0, smallSide, smallSide, 0.0, smallSide * 2});
        arrow.setFill(Color.WHITE);

        return arrow;
    }
}
