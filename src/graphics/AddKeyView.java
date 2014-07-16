package graphics;

import interfaces.CommandView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import settings.Command;

/**
 * Created by thb on 13.07.2014.
 */
public class AddKeyView {

    public AddKeyView(CommandView commandView, Command command, boolean newItem){

        Stage stage = new Stage();
        stage.setTitle("Add Key command");

        Label ctrlLabel = new Label("CTRL");
        CheckBox ctrlCB  = new CheckBox();
        ctrlCB.setSelected(command.isCtrl());

        Label altLabel = new Label("ALT");
        CheckBox altCB = new CheckBox();
        altCB.setSelected(command.isAlt());

        Label shiftLabel = new Label("Shift");
        CheckBox shiftCB = new CheckBox();
        shiftCB.setSelected(command.isShift());

        Label keyLabel = new Label("Key");
//        TextField keyField = new TextField(command.getKey());

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                command.setAlt(altCB.isSelected());
                command.setCtrl(ctrlCB.isSelected());
                command.setShift(shiftCB.isSelected());
//                command.setKey(keyField.getText());

                if(newItem)
                    commandView.addCommand(command);
                else
                    commandView.updateCommandList();

                stage.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

//        keyField.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                System.out.println(event.isControlDown() + " - " + event.getCode().toString());
//            }
//        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(ctrlLabel, 0, 0);
        grid.add(ctrlCB, 1, 0);
        grid.add(altLabel, 0, 1);
        grid.add(altCB, 1, 1);
        grid.add(shiftLabel, 0, 2);
        grid.add(shiftCB, 1, 2);
        grid.add(keyLabel, 0, 3);
//        grid.add(keyField, 1, 3);
        grid.add(saveButton, 0, 4);
        grid.add(cancelButton, 1, 4);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.show();
    }
}
