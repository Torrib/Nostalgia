package graphics;

import graphics.settings.WindowSettingsView;
import graphics.utility.CommandBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Program;
import settings.MenuItem;
import settings.Settings;

import java.util.List;

public class MenuItemView{

    public MenuItemView(WindowSettingsView applicationView, MenuItem menuItem, boolean newItem, List<Program> programs){

        Stage stage = new Stage();
        stage.setTitle("Menu item");
        stage.initOwner(applicationView.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        Label nameLabel = new Label("Name");
        TextField nameField = new TextField(menuItem.getDisplayName());
        Label messageLabel = new Label("Message");
        TextField messageField = new TextField(menuItem.getMessage());

        CommandBox commandBox = new CommandBox(menuItem.getCommands(), programs);

        Button saveButton = new Button("Save");

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                menuItem.setDisplayName(nameField.getText());
                menuItem.setMessage(messageField.getText());
                menuItem.setCommands(commandBox.getItems());

                if(newItem)
                    applicationView.addMenuItem(menuItem);
                else
                    applicationView.updateMenuItemList();
                stage.close();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(messageLabel, 0, 1);
        grid.add(messageField, 1, 1);

        HBox buttons = new HBox(30);
        buttons.getChildren().addAll(saveButton, cancelButton);


        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(grid, commandBox, buttons);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
