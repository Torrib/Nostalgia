package graphics.utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;

/**
 * Created by thb on 08.07.2014.
 */
public class EditList<T> extends BorderPane {
    private ListView<T> list;
    private Button addButton;
    private Button removeButton;
    private Button editButton;
    private ObservableList<T> oItems;

    public EditList(List<T> items){
        super();

        setup(items);
    }

    public EditList(List<T> items, double width, double height){
        super();

        setup(items);
        this.setMaxHeight(height);
        this.setMaxWidth(width);
    }

    private void setup(List<T> items){
        list = new ListView<>();
        oItems = FXCollections.observableArrayList(items);
        list.setItems(oItems);

        ImageView addImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/add.png")));
        ImageView removeImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/remove.png")));
        ImageView editImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/edit.png")));

        addButton = new Button();
        addButton.setGraphic(addImage);
        addImage.setFitWidth(10);
        addImage.setFitHeight(10);

        removeButton = new Button();
        removeImage.setFitWidth(10);
        removeImage.setFitHeight(10);
        removeButton.setGraphic(removeImage);

        editButton = new Button();
        editImage.setFitWidth(10);
        editImage.setFitHeight(10);
        editButton.setGraphic(editImage);

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(list.getSelectionModel().getSelectedItem() != null)
                    list.getItems().remove(list.getSelectionModel().getSelectedItem());
            }
        });

        VBox buttons = new VBox(5);
        buttons.getChildren().addAll(addButton, removeButton, editButton);

        this.setCenter(list);
        this.setRight(buttons);
        this.setMargin(list, new Insets(0, 10, 0, 0));
    }

    public Button getAddButton(){
        return addButton;
    }

    public Button getEditButton(){
        return editButton;
    }

    public ObservableList<T> getItems(){
        return oItems;
    }

    public ListView<T> getList(){
        return list;
    }

    public void update(){
        list.setItems(null);
        list.setItems(getItems());
    }

    public T getSelected(){
        return list.getSelectionModel().getSelectedItem();
    }
}
