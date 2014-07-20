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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBox {

    private Main main;
    private Stage stage;
    private Text text;
    private double width;

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
        width = primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth();
        stage.setY(primaryScreenBounds.getMinY() + 50);

        Scene scene = new Scene(borderPane);
        scene.setFill(null);

        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.TRANSPARENT);
    }

    public void hide(){
        ActionListener action = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        stage.hide();
                    }
                });
            }
        };
        javax.swing.Timer timer = new javax.swing.Timer(Main.SETTINGS.getMessageDelay(), action);
        timer.setRepeats(false);
        timer.start();
    }

    public void show(String message){
        text.setText(message);
        text.setFont(Font.font(Main.SETTINGS.getMessageFont(), Main.SETTINGS.getMessageFontSize()));
        stage.setX(width - (text.getLayoutBounds().getWidth() + 50));
        stage.show();
        main.returnFocus();
        hide();
    }

    public boolean isShowing(){
        return stage.isShowing();
    }
}
