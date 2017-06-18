package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Field {
    int i;
    int j;
    StackPane stackPane;
    boolean fill;
    int color;
    int COLORLESS = 0;
    int BLACK = -1;
    int WHITE = 1;
    public Field(int i, int j){
        this.i = i;
        this.j = j;
        stackPane = new StackPane();
        stackPane.setPrefSize(80, 80);
        setField();
    }
    public Circle drawCircle(){
        Circle circ = new Circle();
        circ.setRadius(35);
        return circ;
    }
    public void setField(){
        stackPane.getChildren().clear();
        if((i==3 && j==3) || (i==4 && j==4)){
            fill = true;
            stackPane.setStyle("-fx-background-color: green;");
            Circle circ = drawCircle();
            circ.setFill(Color.WHITE);
            stackPane.getChildren().addAll(circ);
            color = WHITE;
        } else if((i==3 && j==4) || (i==4 && j==3)) {
            fill = true;
            stackPane.setStyle("-fx-background-color: green;");
            Circle circ = drawCircle();
            circ.setFill(Color.BLACK);
            stackPane.getChildren().addAll(circ);
            color = BLACK;
        } else {
            fill = false;
            color = COLORLESS;
            stackPane.setStyle("-fx-background-color: green;");
        }
    }
}
