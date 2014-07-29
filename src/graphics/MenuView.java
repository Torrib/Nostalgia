package graphics;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.Bloom;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
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

    public MenuView(Main main){
        this.main = main;
        stage = new Stage();
        stage.setTitle("Nostalgia menu");
        list = new ListView<>();

        File file = new File("src/resources/button-29.mp3");
        menuSound = new AudioClip(file.toURI().toString());

        list.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    performCommand();
                }
                if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    hide();
                }
            }
        });

        list.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MenuItem>() {
            @Override
            public void changed(ObservableValue<? extends MenuItem> observable, MenuItem oldValue, MenuItem newValue) {
                playSound();
                list.setCellFactory(new Callback<ListView<MenuItem>, ListCell<MenuItem>>() {
                    @Override
                    public ListCell<MenuItem> call(ListView<MenuItem> param) {
                        ListCell<MenuItem> cell = new ListCell<MenuItem>() {

                            @Override
                            public void updateItem(final MenuItem item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    if(item == newValue) {

                                        this.setTextFill(Color.WHITE);
                                        this.setFont(Font.font(Main.SETTINGS.getMenuSelectedFontSize()));
                                        setEffect(new Bloom(0.1));
                                        setText(item.toString());
                                    }
                                    else{
                                        this.setTextFill(Color.WHITE);
                                        this.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));
                                        setText(item.toString());
                                    }
                                }
                            }
                        };
                        return cell;
                    }
                });
            }
        });

        list.getSelectionModel().selectFirst();

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        controllerLabel = new Label();
        controllerLabel.setTextFill(Color.WHITE);
        controllerLabel.setFont(Font.font(Main.SETTINGS.getMenuFont(), Main.SETTINGS.getMenuFontSize()));
        controllerLabel.setPadding(new Insets(primaryScreenBounds.getHeight() / 5, 0, primaryScreenBounds.getHeight() / 5, (primaryScreenBounds.getWidth() / 2) - 55));

        borderPane = new BorderPane();
        borderPane.setTop(controllerLabel);
        borderPane.setCenter(list);

        borderPane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(borderPane, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
        scene.getStylesheets().add("list.css");
        scene.setFill(Color.rgb(0, 0, 0, 0.95));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    public void show(List<MenuItem> menuItems, int controller){
        stopSound = true;
        list.getItems().clear();
        list.setItems(FXCollections.observableArrayList(menuItems));
        controllerLabel.setText("Controller " + (controller+1));
        stage.show();
        try {
            Robot bot = new Robot();
            bot.mouseMove(0,0);
            bot.mousePress(InputEvent.BUTTON1_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
        catch (AWTException e){ e.printStackTrace();}
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
        main.command(main.getActiveWindowSettings().getPostMenuCommands());
    }

    public boolean isShowing(){
        return stage.isShowing();
    }


    private void performCommand(){
        if(list.getSelectionModel().getSelectedItem().isConfirmation()) {

            ListView<String> confirmationList = new ListView();
            confirmationList.getItems().addAll("OK", "Cancel");
            confirmationList.setOrientation(Orientation.HORIZONTAL);

            confirmationList.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
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
                }
            });

            confirmationList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    playSound();
                    confirmationList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                        @Override
                        public ListCell<String> call(ListView<String> param) {
                            ListCell<String> cell = new ListCell<String>() {

                                @Override
                                public void updateItem(final String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (item != null) {
                                        if(item == newValue) {
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
                            return cell;
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
        else {
            hide();
            main.command(list.getSelectionModel().getSelectedItem(), null);
        }
    }
}
