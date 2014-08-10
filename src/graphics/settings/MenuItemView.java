package graphics.settings;

import graphics.utility.CommandBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;
import models.MenuItem;
import models.SubMenu;

public class MenuItemView{

    public MenuItemView(Stage parent, MenuItem menuItem, Runnable onSave, boolean disableType){

        Stage stage = new Stage();
        stage.setTitle("Menu item");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);

        Label typeLabel = new Label("Type");
        ComboBox<String> typeCB = new ComboBox<>(FXCollections.observableArrayList("Commands", "Sub menu"));
        typeCB.getSelectionModel().select(0);

        if(disableType)
            typeCB.setDisable(true);

        HBox typeBox = new HBox(15);
        typeBox.getChildren().addAll(typeLabel, typeCB);

        Label nameLabel = new Label("Name");
        TextField nameField = new TextField(menuItem.getDisplayName());
        Label messageLabel = new Label("Message");
        TextField messageField = new TextField(menuItem.getMessage());
        Label confirmationLabel = new Label("Confirmation");
        CheckBox confirmationCB = new CheckBox();
        confirmationCB.setSelected(menuItem.isConfirmation());
        confirmationLabel.setTooltip(new Tooltip("Require confirmation before performing the command"));
        confirmationCB.setTooltip(new Tooltip("Require confirmation before performing the command"));

        CommandBox commandBox = new CommandBox(menuItem.getCommands(), stage);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(messageLabel, 0, 1);
        grid.add(messageField, 1, 1);
        grid.add(confirmationLabel, 0, 2);
        grid.add(confirmationCB, 1, 2);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        cancelButton.setOnAction(event ->stage.close());

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox commandView = new VBox(20);
        commandView.getChildren().addAll(grid, commandBox);

        Label subMenuLabel = new Label("Sub menu");
        subMenuLabel.setTooltip(new Tooltip("Opens the sub menu on select"));

        ComboBox<SubMenu> subMenuComboBox = new ComboBox(FXCollections.observableArrayList(Main.SETTINGS.getSubMenus()));
        subMenuComboBox.getSelectionModel().select(menuItem.getType());

        HBox subMenuBox = new HBox(20);
        subMenuBox.getChildren().addAll(subMenuLabel, subMenuComboBox);

        saveButton.setOnAction(event -> {

            switch(typeCB.getSelectionModel().getSelectedIndex()) {
                case MenuItem.COMMAND:
                    if (commandBox.isIgnoreCloseRequest()) {
                        event.consume();
                        commandBox.setIgnoreCloseRequest(false);
                    } else {
                        if (nameField.getText().isEmpty()) {
                            nameField.setStyle("-fx-border-color: red;-fx-border-style: round|outside");
                            return;
                        }
                        menuItem.setDisplayName(nameField.getText());
                        menuItem.setMessage(messageField.getText());
                        menuItem.setCommands(commandBox.getItems());
                        menuItem.setConfirmation(confirmationCB.isSelected());

                        onSave.run();
                        stage.close();
                    }
                    break;
                case MenuItem.SUBMENU:
                    menuItem.setSubMenu(subMenuComboBox.getSelectionModel().getSelectedItem());
                    onSave.run();
                    stage.close();
                    break;
            }
        });

        Pane swapPane = new Pane();
        swapPane.getChildren().add(commandView);

        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(typeBox, swapPane, buttonBox);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);

        typeCB.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            swapPane.getChildren().removeAll(swapPane.getChildren());
            switch(typeCB.getSelectionModel().getSelectedIndex()){
                case MenuItem.COMMAND:
                    swapPane.getChildren().add(commandView);
                    scene.getWindow().setHeight(scene.getWindow().getHeight() + 330);
                    break;
                case MenuItem.SUBMENU:
                    swapPane.getChildren().add(subMenuBox);
                    scene.getWindow().setHeight(scene.getWindow().getHeight() - 330);
                    break;
            }

        });

        stage.show();
    }
}
