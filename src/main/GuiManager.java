package main;

import graphics.MenuView;
import graphics.MessageBox;
import graphics.settings.SettingsView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.MenuItem;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
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
        createTrayIcon(primaryStage);

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

    private void createTrayIcon(Stage stage){
        if (SystemTray.isSupported()) {
            stage.getIcons().add(new Image("/resources/controller2.png"));

            PopupMenu popMenu = new PopupMenu();
            java.awt.MenuItem item1 = new java.awt.MenuItem("Exit");
            java.awt.MenuItem item2 = new java.awt.MenuItem("Config");
            popMenu.add(item2);
            popMenu.add(item1);
            item1.addActionListener(e -> {
                main.log("User exit(tray)");
                System.exit(0);
            });

            item2.addActionListener(e -> showConfig());

            SystemTray tray = SystemTray.getSystemTray();
            File file = new File(GuiManager.class.getResource("/resources/icon.ico").getFile());
            Icon ico = FileSystemView.getFileSystemView().getSystemIcon(file);
            java.awt.Image image = ((ImageIcon) ico).getImage();
            TrayIcon trayIcon = new TrayIcon(image, "Nostalgia", popMenu);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                main.log(e.toString());
                e.printStackTrace();
            }
        }
    }
}
