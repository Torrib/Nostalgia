package graphics.utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class EditList<T> extends BorderPane {
    private ListView<T> list;
    private Button addButton;
    private Button editButton;
    private ObservableList<T> oItems;

    public EditList(List<T> items, boolean draggable){
        super();
        setup(items, draggable);
    }

    private void setup(List<T> items, boolean draggable){
        list = new ListView<>();
        oItems = FXCollections.observableArrayList(items);
        list.setItems(oItems);

        if(draggable) {
            list.setCellFactory(param -> {
                DraggableCell cell = new DraggableCell<T>();
                cell.init(oItems);
                return cell;
            });
        }

        ImageView addImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/add.png")));
        ImageView removeImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/remove.png")));
        ImageView editImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/edit.png")));

        addButton = new Button();
        addButton.setGraphic(addImage);
        addImage.setFitWidth(10);
        addImage.setFitHeight(10);

        Button removeButton = new Button();
        removeImage.setFitWidth(10);
        removeImage.setFitHeight(10);
        removeButton.setGraphic(removeImage);

        editButton = new Button();
        editImage.setFitWidth(10);
        editImage.setFitHeight(10);
        editButton.setGraphic(editImage);

        removeButton.setOnAction(event -> {
            if(list.getSelectionModel().getSelectedItem() != null)
                list.getItems().remove(list.getSelectionModel().getSelectedItem());
        });

        VBox buttons = new VBox(5);
        buttons.getChildren().addAll(addButton, removeButton, editButton);

        this.setCenter(list);
        this.setRight(buttons);
        setMargin(list, new Insets(0, 10, 0, 0));
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
        list.getSelectionModel().select(-1);
    }

    public T getSelected(){
        return list.getSelectionModel().getSelectedItem();
    }
}
