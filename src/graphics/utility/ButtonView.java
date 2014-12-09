package graphics.utility;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jgamepad.enums.Button;
import models.ButtonPlaceholder;

public class ButtonView {

    private Stage stage;

    public ButtonView(Stage parent, ButtonPlaceholder buttonPlaceholder, Runnable onSave){
        stage = new Stage();
        stage.setTitle("Add button");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);

        ComboBox<Button> buttonCB = new ComboBox(FXCollections.observableArrayList(jgamepad.enums.Button.values()));
        buttonCB.getSelectionModel().select(buttonPlaceholder.getButton());

        javafx.scene.control.Button saveButton = new javafx.scene.control.Button("Save");
        javafx.scene.control.Button cancelButton = new javafx.scene.control.Button("Cancel");

        saveButton.setOnAction(event ->{
            buttonPlaceholder.setButton(buttonCB.getSelectionModel().getSelectedItem());

            onSave.run();

            stage.close();
        });

        cancelButton.setOnAction(event -> stage.close());

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(buttonCB, buttonBox);
        vBox.setPadding(new Insets(15));

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
