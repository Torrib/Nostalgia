package graphics.settings;

import graphics.utility.EditList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.MenuItem;
import models.SubMenu;


public class SubMenuView {

    private EditList<MenuItem> menuEditList;
    private Stage stage;

    public SubMenuView(Stage parent, SubMenu subMenu, Runnable onSave){

        stage = new Stage();
        stage.setTitle("Sub menu");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);

        Label nameLabel = new Label("Name");
        TextField nameField = new TextField(subMenu.getName());

        HBox nameBox = new HBox(10);
        nameBox.getChildren().addAll(nameLabel, nameField);

        menuEditList = new EditList<>(subMenu.getMenuItems(), true);
        menuEditList.getAddButton().setOnAction(event -> openMenuItemView(new MenuItem(), true));
        menuEditList.getEditButton().setOnAction(event -> openMenuItemView(menuEditList.getList().getSelectionModel().getSelectedItem(), false));


        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            if(nameField.getText().isEmpty()){
                nameField.setStyle("-fx-border-color: red;-fx-border-style: round|outside");
                return;
            }
            subMenu.setName(nameField.getText());
            subMenu.setMenuItems(menuEditList.getItems());
            onSave.run();

            stage.close();
        });
        cancelButton.setOnAction(event -> stage.close());

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(saveButton, cancelButton);

        VBox vBox = new VBox(15);
        vBox.getChildren().addAll(nameBox, menuEditList, buttons);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    private void openMenuItemView(MenuItem menuItem, boolean newItem){

        Runnable onSave = () -> {
            if(newItem)
                menuEditList.getItems().add(menuItem);
            else
                menuEditList.update();
        };

        if(menuItem != null)
            new MenuItemView(stage, menuItem, onSave, true);
    }
}
