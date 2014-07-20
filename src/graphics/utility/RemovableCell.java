package graphics.utility;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class RemovableCell<E> extends ListCell<E> {
	BorderPane bp = new BorderPane();
	Label label = new Label();
	Pane pane = new Pane();
    ImageView addImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/delete.png")));

	public RemovableCell(final ObservableList<E> items, double labelMaxWidth) {
		super();
		label.setMaxWidth(labelMaxWidth);
		bp.setLeft(label);
		bp.setRight(addImage);
		HBox.setHgrow(pane, Priority.ALWAYS);
        addImage.setFitHeight(15);
        addImage.setFitWidth(15);

        addImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                items.remove(getIndex());
            }
        });
	}

	@Override
	protected void updateItem(E item, boolean empty) {
		super.updateItem(item, empty);
		setText(null);
		if (empty) {
			setGraphic(null);
		}
		else {
			label.setText(item != null ? item.toString() : "<null>");
			setGraphic(bp);
		}
	}
}
