package graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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

        addButton = new Button("+");
        removeButton = new Button("-");
        editButton = new Button("edit");

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
        this.setPadding(new Insets(25));
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
