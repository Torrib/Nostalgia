package graphics.utility;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class DraggableRemovableCell<T> extends ListCell<T> implements ChangeListener<Number>{

    public static DataFormat dataFormat =  new DataFormat("mydeletecell");
    private static IntegerProperty ind = new SimpleIntegerProperty(-1);
    private static Object temp = null;
    private ObservableList items;
    private boolean draggable =true;
    private static int toBeDeleted = -1;
    private String styleclass = "list-cellx";

    private BorderPane bp = new BorderPane();
    private Label label = new Label();
    private Pane pane = new Pane();
    private ImageView removeImage = new ImageView(new Image(getClass().getResourceAsStream("/resources/delete.png")));

    public DraggableRemovableCell(final ObservableList<T> items, double labelMaxWidth){
        super();
        label.setMaxWidth(labelMaxWidth);
        bp.setLeft(label);
        bp.setRight(removeImage);
        HBox.setHgrow(pane, Priority.ALWAYS);
        removeImage.setFitHeight(15);
        removeImage.setFitWidth(15);

        removeImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                items.remove(getIndex());
            }
        });
    }

    private void setDraggable(boolean b){
        draggable = b;
    }

    public boolean isDraggable(){
        return draggable;
    }

    public void init(ObservableList itms){
        items = itms;

        this.indexProperty().addListener(this);
        this.getStyleClass().add(styleclass);
    }

    @Override
    public void updateItem(T item,boolean empty){
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

    @Override
    public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

        if(isDraggable() && getIndex() < items.size()){
            setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent arg0) {
                    getListView().scrollTo(getIndex());
                }

            });

            setOnDragEntered(new EventHandler<DragEvent>(){
                @Override
                public void handle(DragEvent arg0) {
                    if(arg0.getTransferMode() == TransferMode.MOVE){
                        String cellS = (String)arg0.getDragboard().getContent(dataFormat);

                        Object o = arg0.getDragboard().getContent(dataFormat);
                        if(toBeDeleted == getIndex()){
                            return;
                        }
                        if(toBeDeleted != -1){
                            items.remove(toBeDeleted);
                            toBeDeleted = -1;
                        }
                        if(o != null && temp != null ){
                            if(getIndex() < items.size())
                                items.add(getIndex(),(T)temp);
                            else if(getIndex() == items.size())
                                items.add((T)temp);

                        }

                        ind.set(getIndex());
                    }
                }

            });
            ind.addListener(new InvalidationListener(){

                @Override
                public void invalidated(Observable observable) {
                    if(getIndex() == ind.get()){
                        InnerShadow is = new InnerShadow();
                        is.setOffsetX(1.0);
                        is.setColor(Color.web("#666666"));
                        is.setOffsetY(1.0);
                        setEffect(is);
                    }else{
                        setEffect(null);
                    }
                }

            });
            setOnDragExited(new EventHandler<DragEvent>(){

                @Override
                public void handle(DragEvent arg0) {
                    if(arg0.getTransferMode() == TransferMode.MOVE){
                        Object o = arg0.getDragboard().getContent(dataFormat);
                        if(o != null){
                            setEffect(null);
                            if(getIndex()<items.size())
                                toBeDeleted = getIndex();

                        }
                    }
                }

            });

            pressedProperty().addListener(new ChangeListener<Boolean>(){

                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                    InnerShadow is = new InnerShadow();
                    is.setOffsetX(1.0);
                    is.setColor(Color.web("#666666"));
                    is.setOffsetY(1.0);
                    if(arg2){


                    }
                    else
                        setEffect(null);
                }

            });

            setOnDragOver(new EventHandler<DragEvent>(){
                @Override
                public void handle(DragEvent event) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

            });

            setOnDragDetected(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    Dragboard db = getListView().startDragAndDrop(TransferMode.MOVE);
                    temp = items.get(getIndex());
                    toBeDeleted = getIndex();
                    Object item = items.get(getIndex());
                    ClipboardContent content = new ClipboardContent();
                    if(item != null)
                        content.put(dataFormat,item.toString());
                    else
                        content.put(dataFormat,"XData");
                    db.setContent(content);
                    event.consume();
                }
            });
        }
    }
}
