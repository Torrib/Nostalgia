package main;

import graphics.MenuView;
import graphics.MessageBox;
import graphics.settings.SettingsView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import models.MenuItem;

import java.util.List;

public class GuiManager extends Application{

    private Main main;
    private MenuView menu;
    private MessageBox messageBox;

    public static void main(String[] args){
        launch();
    }


    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Nostalgia");
        main = new Main(this);

        Platform.setImplicitExit(false);

        menu = new MenuView(main);
        messageBox = new MessageBox(main);

        main.start();

        if(Main.SETTINGS.isFirstRun())
            showConfig();
    }

    public void showMenu(List<MenuItem> menuItems, int controller){
        Platform.runLater(() -> menu.show(menuItems, controller));
    }

    public void hideMenu(){
        Platform.runLater(() -> menu.hide());
    }

    public boolean isMenuShowing(){
        return menu.isShowing();
    }

    public void showMessageBox(String message){
        Platform.runLater(() -> messageBox.show(message));
    }

    public void showConfig(){
        Platform.runLater(() -> new SettingsView(main));
    }
}
