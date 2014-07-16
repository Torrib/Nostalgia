package graphics;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import main.Main;
import settings.MenuItem;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.File;
import java.util.List;

public class MenuView {

    private Main main;
    private Stage stage;
    private ListView<MenuItem> list;
    private Label controllerLabel;
    private AudioClip menuSound;

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
                    main.returnFocus();
                    hide();
                    main.command(list.getSelectionModel().getSelectedItem(), null);
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
                                    String text;
                                    if(item.toString().equals("Toggle hotkeys")) {
                                        if(main.getActiveWindowSettings().isDisableHotkeys())
                                            text = "Enable hotkeys";
                                        else
                                            text = "Disable hotkeys";
                                    }
                                    else
                                        text = item.toString();

                                    if(item == newValue) {

                                        this.setTextFill(Color.WHITE);
                                        this.setFont(Font.font(main.getSettings().getMenuSelectedFontSize()));
                                        setEffect(new Bloom(0.1));
                                        setText(text);
                                    }
                                    else{
                                        this.setTextFill(Color.WHITE);
                                        this.setFont(Font.font(main.getSettings().getMenuFont(), main.getSettings().getMenuFontSize()));
                                        setText(text);
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

        controllerLabel = new Label();
        controllerLabel.setTextFill(Color.WHITE);
        controllerLabel.setFont(Font.font(main.getSettings().getMenuFont(), main.getSettings().getMenuFontSize()));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(controllerLabel);
        borderPane.setBottom(list);

//        list.setMaxHeight(200);
//        list.setLayoutY(500);

        borderPane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(borderPane, 1200, 600);
        scene.getStylesheets().add("list.css");
        scene.setFill(Color.rgb(0, 0, 0, 0.95));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
    }

    public void show(List<MenuItem> menuItems, int controller){
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
    }

    public void playSound(){
        if(!main.getSettings().menuMuted())
            menuSound.play();
    }

    public void hide(){
        stage.hide();
    }

    public boolean isShowing(){
        return stage.isShowing();
    }
}
