package graphics.settings;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.PredefinedProgramCommands;
import models.Program;

import java.io.File;

public class ProgramView {

    public ProgramView(Stage parent, Program program, Runnable onSave){
        Stage stage = new Stage();
        stage.setTitle("Program");
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);

        Label nameLabel = new Label("Name");
        TextField nameField = new TextField(program.getName());

        Label preCommandLabel = new Label("Pre-Command");
        TextField preCommandField = new TextField(program.getPreCommand());
        preCommandLabel.setTooltip(new Tooltip("String that will be added before the application (necessary for some types"));
        preCommandField.setTooltip(new Tooltip("String that will be added before the application (necessary for some types"));

        Label postCommandLabel = new Label("Post-Command");
        TextField postCommandField = new TextField(program.getPostCommand());
        postCommandLabel.setTooltip(new Tooltip("String that will be added after the application (necessary for some types"));
        postCommandField.setTooltip(new Tooltip("String that will be added after the application (necessary for some types"));

        Label pathLabel = new Label("Path");
        TextField pathField = new TextField(program.getPath());
        pathLabel.setTooltip(new Tooltip("Path to the program or script to run"));
        pathField.setTooltip(new Tooltip("Path to the program or script to run"));
        Button browsePathButton = new Button("Browse");

        browsePathButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file to run");
            File file = fileChooser.showOpenDialog(stage);
            if(file != null) {
                pathField.setText(file.getAbsolutePath());
                int index = file.getName().lastIndexOf('.');
                String extension = file.getName().substring(index);
                preCommandField.setText(PredefinedProgramCommands.getPreString(extension));
                postCommandField.setText(PredefinedProgramCommands.getPostString(extension));
            }
        });


        HBox pathBox = new HBox(5);
        pathBox.getChildren().addAll(pathField, browsePathButton);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(event -> {
            if(nameField.getText().isEmpty()){
                nameField.setStyle("-fx-border-color: red;");
            }
            else if(pathField.getText().isEmpty()){
                pathField.setStyle("-fx-border-color: red;");
            }
            else {
                program.setName(nameField.getText());
                program.setPreCommand(preCommandField.getText());
                program.setPostCommand(postCommandField.getText());
                program.setPath(pathField.getText());

                onSave.run();

                stage.close();
            }
        });

        cancelButton.setOnAction(event -> stage.close());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(pathLabel, 0, 1);
        grid.add(pathBox, 1, 1);
        grid.add(preCommandLabel, 0, 2);
        grid.add(preCommandField, 1, 2);
        grid.add(postCommandLabel, 0, 3);
        grid.add(postCommandField, 1, 3);
        grid.add(saveButton, 0, 4);
        grid.add(cancelButton, 1, 4);

        grid.setPadding(new Insets(15));

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }
}
