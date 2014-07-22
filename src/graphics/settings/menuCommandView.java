package graphics.settings;


import graphics.utility.CommandBox;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Command;
import models.Program;

import java.util.List;

public class MenuCommandView {

    public MenuCommandView(WindowSettingsView applicationView, List<Command> commands, List<Program> programs, boolean preMenuCommand){
        Stage stage = new Stage();
        stage.setTitle(preMenuCommand ? "Pre-menu Command" : "Post-menu Command");
        stage.initOwner(applicationView.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        CommandBox commandBox = new CommandBox(commands, programs, stage);

        commandBox.setPadding(new Insets(15));

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(commandBox.isIgnoreCloseRequest()){
                    event.consume();
                    commandBox.setIgnoreCloseRequest(false);
                }
                else {
                    if (preMenuCommand)
                        applicationView.setPreMenuCommands(commandBox.getItems());
                    else
                        applicationView.setPostMenuCommands(commandBox.getItems());

                    stage.close();
                }
            }
        });


        Scene scene = new Scene(commandBox);
        stage.setScene(scene);
        stage.show();
    }
}
