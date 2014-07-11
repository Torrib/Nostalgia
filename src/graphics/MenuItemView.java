package graphics;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import settings.MenuItem;

/**
 * Created by thb on 08.07.2014.
 */
public class MenuItemView {

    private MenuItem menuItem;

    public MenuItemView(ApplicationView applicationView, MenuItem menuItem, boolean newItem){
        Stage stage = new Stage();
        stage.setTitle("Menu item");

        Label nameLabel = new Label("Name");
        TextField nameField = new TextField(menuItem.getDisplayName());
        Label messageLabel = new Label("Message");
        TextField messageField = new TextField(menuItem.getMessage());
        Label commandLabel = new Label("Command");
        TextField commandField = new TextField(menuItem.getCommand());

        Button saveButton = new Button("Save");

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                menuItem.setDisplayName(nameField.getText());
                menuItem.setMessage(messageField.getText());
                menuItem.setCommand(commandField.getText());

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
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(messageLabel, 0, 1);
        grid.add(messageField, 1, 1);
        grid.add(commandLabel, 0, 2);
        grid.add(commandField, 1, 2);
        grid.add(saveButton, 0, 3);
        grid.add(cancelButton, 1, 3);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }
}
