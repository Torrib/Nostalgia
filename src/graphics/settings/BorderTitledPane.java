package graphics.settings;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Created by thb on 11.07.2014.
 */
class BorderTitledPane extends StackPane {

    public BorderTitledPane(String titleString, Node content, double width, double heigth) {
        Label title = new Label(" " + titleString + " ");
        title.getStyleClass().add("bordered-titled-title");
        StackPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane contentPane = new StackPane();
        content.getStyleClass().add("bordered-titled-content");
        contentPane.getChildren().add(content);

        getStyleClass().add("bordered-titled-border");
        getChildren().addAll(title, contentPane);

        setMaxHeight(heigth);
        setMaxWidth(width);
    }
}