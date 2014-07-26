package graphics.settings;

import graphics.utility.CommandBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Program;
import models.MenuItem;

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

        CommandBox commandBox = new CommandBox(menuItem.getCommands(), programs, stage);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(messageLabel, 0, 1);
        grid.add(messageField, 1, 1);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(commandBox.isIgnoreCloseRequest()){
                    event.consume();
                    commandBox.setIgnoreCloseRequest(false);
                }
                else {
                    if(nameField.getText().isEmpty()){
                        nameField.setStyle("-fx-border-color: red;-fx-border-style: round|outside");
                        return;
                    }
                    menuItem.setDisplayName(nameField.getText());
                    menuItem.setMessage(messageField.getText());
                    menuItem.setCommands(commandBox.getItems());

                    if (newItem)
                        applicationView.addMenuItem(menuItem);
                    else
                        applicationView.updateMenuItemList();
                    stage.close();
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(grid, commandBox, buttonBox);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
