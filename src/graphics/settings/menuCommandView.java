package graphics.settings;


import graphics.utility.CommandBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Command;
import models.Program;

import java.util.List;

public class MenuCommandView {

    public MenuCommandView(WindowSettingsView applicationView, List<Command> commands, boolean preMenuCommand){
        Stage stage = new Stage();
        stage.setTitle(preMenuCommand ? "Pre-menu Command" : "Post-menu Command");
        stage.initOwner(applicationView.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        CommandBox commandBox = new CommandBox(commands, stage);

        commandBox.setPadding(new Insets(15));

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            if (commandBox.isIgnoreCloseRequest()) {
                event.consume();
                commandBox.setIgnoreCloseRequest(false);
            } else {
                if (preMenuCommand)
                    applicationView.setPreMenuCommands(commandBox.getItems());
                else
                    applicationView.setPostMenuCommands(commandBox.getItems());

                stage.close();
            }
        });

        cancelButton.setOnAction(event -> stage.close());

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(0,0, 10,0));
        buttonBox.getChildren().addAll(saveButton, cancelButton);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(commandBox, buttonBox);


        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
