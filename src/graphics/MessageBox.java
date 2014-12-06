package graphics;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.Main;

import java.awt.event.ActionListener;
import java.util.PriorityQueue;
import java.util.Queue;

public class MessageBox {

    private Main main;
    private Stage stage;
    private Text text;
    private double screenWidth;
    private Queue<String> messages = new PriorityQueue<>();

    public MessageBox(Main main){
        this.main = main;
        stage = new Stage();
        stage.setTitle("Nostalgia message");

        text = new Text();
        text.setFill(Color.WHITE);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(text);
        borderPane.setPadding(new Insets(6));
        borderPane.setStyle("-fx-background-color: rgb(0, 0, 0, 0.95);");

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth();

        applySettings();

        Scene scene = new Scene(borderPane);
        scene.setFill(null);

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.TRANSPARENT);
    }

    public void hide(){
        ActionListener action = e -> Platform.runLater(() ->  {
            if(messages.isEmpty())
                stage.hide();
            else{
                changeText(messages.poll());
                hide();
            }
        });

        javax.swing.Timer timer = new javax.swing.Timer(Main.SETTINGS.getMessageDelay(), action);
        timer.setRepeats(false);
        timer.start();
    }

    public void show(String message){
        changeText(message);
        stage.setY(50);
        stage.show();
        main.returnFocus();
        hide();
    }

    private void changeText(String message){
        text.setText(message);
        stage.setX(screenWidth - (text.getLayoutBounds().getWidth() + 50));
        stage.setWidth(text.getLayoutBounds().getWidth() + 20);
    }

    public boolean isShowing(){
        return stage.isShowing();
    }

    public void applySettings(){
        text.setFont(Font.font(Main.SETTINGS.getMessageFont(), Main.SETTINGS.getMessageFontSize()));
    }

    public void addMessage(String message){
        messages.add(message);
    }
}
