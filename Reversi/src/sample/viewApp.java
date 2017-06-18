package sample;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

public class viewApp {
    Controller controller;
    BorderPane bp;
    GridPane board;
    HBox hbox;
    Button button;
    Button blackPawns;
    Button whitePawns;

    public viewApp(){
        bp = new BorderPane();
        board = new GridPane();
        hbox = new HBox();
        button = new Button("New Game");
        blackPawns = new Button("Black(Your) Pawns : 2");
        whitePawns = new Button("White Pawns : 2");
    }

    public void setController(Controller controller){
        this.controller = controller;
    }

    public void createView(){
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(50);
        hbox.setStyle("-fx-background-color: #336699;");
        hbox.getChildren().addAll(button, whitePawns, blackPawns);

        button.setPrefSize(100, 50);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> controller.actionOnButton());
        blackPawns.setPrefSize(200, 50);
        whitePawns.setPrefSize(200, 50);

        board.setPrefHeight(640);
        board.setPrefWidth(640);

        controller.createBoard();

        for(int i=0; i<8; i++){
            board.getColumnConstraints().add(new ColumnConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true));
            board.getRowConstraints().add(new RowConstraints(5, Control.USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.CENTER, true));
        }
        board.setStyle("-fx-background-color: white; -fx-grid-lines-visible: true");

        bp.setCenter(board);
        bp.setTop(hbox);
    }

}
