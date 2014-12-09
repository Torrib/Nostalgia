package graphics.settings;


import graphics.utility.CommandView;
import graphics.utility.EditList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Command;

import java.util.List;

public class MenuCommandView {

    private Stage stage;
    private EditList<Command> commandEditList;

    public MenuCommandView(WindowSettingsView applicationView, List<Command> commands, boolean preMenuCommand){
        stage = new Stage();
        stage.setTitle(preMenuCommand ? "Pre-menu Command" : "Post-menu Command");
        stage.initOwner(applicationView.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        commandEditList = new EditList<>(commands, true);
        commandEditList.setPadding(new Insets(15));
        commandEditList.setPrefHeight(150);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            if (preMenuCommand)
                applicationView.setPreMenuCommands(commandEditList.getItems());
            else
                applicationView.setPostMenuCommands(commandEditList.getItems());

            stage.close();
        });

        cancelButton.setOnAction(event -> stage.close());

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(0,0, 10,0));
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(commandEditList, buttonBox);

        commandEditList.getAddButton().setOnAction(event -> openCommandView(new Command(), true));
        commandEditList.getEditButton().setOnAction(event -> openCommandView(commandEditList.getSelected(), false));


        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }

    private void openCommandView(Command command, boolean newItem){

        Runnable onSave = () -> {
            if(newItem)
                commandEditList.getItems().add(command);
            else
                commandEditList.update();
        };

        if(command != null)
            new CommandView(stage, command, onSave);
    }
}
