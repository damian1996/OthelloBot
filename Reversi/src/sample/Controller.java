package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Controller {
    int move;
    viewApp view;
    int COLORLESS = 0;
    int BLACK = -1;
    int WHITE = 1;
    int BOT = 1;
    int PLAYER = 0;
    double INFTY = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public static final int SIZE = 8;
    public static final int TITLE_SIZE = 8;
    boolean fillFields[][] = new boolean[WIDTH][HEIGHT];
    Field fields[][] = new Field[WIDTH][HEIGHT];
    int nx[] = {-1, 0, 1, 0, -1, -1, 1, 1};
    int ny[] = {0, -1, 0, 1, -1, 1, 1, -1};
    int positions[][] = {{6, 3, 5, 5, 5, 5,	3, 6},
            {3,	1, 2, 2, 2, 2, 1, 3},
            {5, 2, 4, 4, 4, 4, 2, 5},
            {5, 2, 4, 4, 4, 4, 2, 5},
            {5, 2, 4, 4, 4, 4, 2, 5},
            {5, 2, 4, 4, 4, 4, 2, 5},
            {3,	1, 2, 2, 2, 2, 1, 3},
            {6,	3, 5, 5, 5, 5, 3, 6}};

    public Controller(){
        move = -1;
    }
    public void setView(viewApp view){
        this.view = view;
    }

    public void createBoard(){
        for(int i=0; i<WIDTH; i++){
            for(int j=0; j<HEIGHT; j++){
                fields[i][j] = new Field(i, j);
                final int ii = i;
                final int jj = j;
                fields[i][j].stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
                    actionOnField(ii, jj);
                });
                view.board.add(fields[i][j].stackPane, i, j);
            }
        }
    }

    public void actionOnField(int i, int j){
        if(fields[i][j].fill == true || !isAllowed(i, j)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("This field already is occupied or not allowed");
            alert.setContentText("Choose other field!");
            alert.showAndWait();
            return;
        }
        setPawn(i, j);
        reversePawns(i, j);
        if(endOfGame()) {
            output();
            return;
        }

        int black = countBlack();
        int white = countWhite();

        view.blackPawns.setText("Black(Your) Pawns : " + black);
        view.whitePawns.setText("White Pawns : " + white);

        move = 1;
        if(!allowedAnyMove()) {
            move=-1;
            return ;
        } else {
            while(true){
                MinMax();
                if(endOfGame()){
                    output();
                    return;
                }
                move = -1;
                if(!allowedAnyMove()){
                    move=1;
                    continue;
                } else {
                    break;
                }
            }
        }
        return;
    }

    public void output(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of Game!");
        int black = countBlack();int white = countWhite();
        if(black<white) alert.setHeaderText("Winner BOT! You sucks");
        else if(black>white) alert.setHeaderText("Winner PLAYER!");
        else alert.setHeaderText("DRAW!");
        alert.setContentText("You have won " + black + " pawns !");
        alert.showAndWait();
    }

    public boolean allowedAnyMove(){
        for(int i=0; i<WIDTH; i++){
            for(int j=0; j<HEIGHT; j++){
                if(isAllowed(i, j))
                    return true;
            }
        }
        return false;
    }

    public void reversePawns(int i, int j){
        for(int k=0; k<SIZE; k++){
            int x = i, y = j, moves = 0;
            for(int l=1; l<SIZE; l++){
                x += nx[k]; y += ny[k];
                if(endOfBoard(x, y)){
                    moves = 0;
                    break;
                }
                if(move==-1){
                    if(fields[x][y].color==WHITE){
                        moves++;
                        if(!(endOfBoard(x+nx[k], y+ny[k]))){
                            if(fields[x+nx[k]][y+ny[k]].color==BLACK){
                                break;
                            } else if(fields[x+nx[k]][y+ny[k]].color==COLORLESS){
                                moves = 0;
                                break;
                            }
                        }
                    } else{
                        moves = 0;
                        break;
                    }
                } else {
                    if(fields[x][y].color==BLACK){
                        moves++;
                        if(!(endOfBoard(x+nx[k], y+ny[k]))){
                            if(fields[x+nx[k]][y+ny[k]].color==WHITE){
                                break;
                            } else if(fields[x+nx[k]][y+ny[k]].color==COLORLESS){
                                moves = 0;
                                break;
                            }
                        }
                    } else{
                        moves = 0;
                        break;
                    }
                }
            }
            x = i; y = j;
            for(int l=1; l<=moves; l++){
                x += nx[k]; y += ny[k];
                setPawn(x, y);
            }
        }
    }

    public boolean endOfBoard(int x, int y){
        if(x==SIZE || x==-1) return true;
        if(y==SIZE || y==-1) return true;
        return false;
    }

    public boolean isAllowed(int i, int j){
        if(fields[i][j].fill) return false;
        for(int k=0; k<8; k++){
            int x = i, y = j, moves = 0;
            for(int l=1; l<SIZE; l++){
                x += nx[k]; y += ny[k];
                if(endOfBoard(x, y)){
                    moves = 0;
                    break;
                }
                if(move==-1){
                    if(fields[x][y].color==WHITE){
                        moves++;
                        if(!(endOfBoard(x+nx[k], y+ny[k]))){
                            if(fields[x+nx[k]][y+ny[k]].color==BLACK){
                                if(moves>0) return true;
                                break;
                            } else if(fields[x+nx[k]][y+ny[k]].color==COLORLESS){
                                moves = 0;
                                break;
                            }
                        }
                    } else{
                        moves = 0;
                        break;
                    }
                } else {
                    if(fields[x][y].color==BLACK){
                        moves++;
                        if(!(endOfBoard(x+nx[k], y+ny[k]))){
                            if(fields[x+nx[k]][y+ny[k]].color==WHITE){
                                if(moves>0) return true;
                                break;
                            } else if(fields[x+nx[k]][y+ny[k]].color==COLORLESS){
                                moves = 0;
                                break;
                            }
                        }
                    } else{
                        moves = 0;
                        break;
                    }
                }
            }
        }
        return false;
    }

    public void setPawn(int i, int j){
        Circle circ = fields[i][j].drawCircle();
        if(move==-1){
            fields[i][j].color = BLACK;
            circ.setFill(Color.BLACK);
        } else {
            fields[i][j].color = WHITE;
            circ.setFill(Color.WHITE);
        }
        fields[i][j].stackPane.getChildren().clear();
        fields[i][j].stackPane.getChildren().addAll(circ);
        fields[i][j].fill = true;
    }

    public void MinMax(){
        int arr[][] = new int[WIDTH][HEIGHT];
        for(int i=0; i<WIDTH; i++){
            for(int j=0; j<HEIGHT; j++){
                arr[i][j] = fields[i][j].color;
            }
        }
        double val = -INFTY;
        int xx = -1, yy = -1;
        for(int i=0; i<WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if(isAllowed(i, j)){
                    int a[][] = createCopy(arr);
                    setPawnsInArray(a, i, j);
                    double tmp = DoMinMax(PLAYER, a, 1, 5);
                    if(tmp > val){
                        val = tmp;
                        xx = i;
                        yy = j;
                    }
                }
            }
        }
        if(xx==-1 && yy==-1){
            for(int first=0; first<WIDTH; first++){
                for(int sec=0; sec<HEIGHT; sec++){
                    if(isAllowed(first, sec)) {
                        xx = first;
                        yy = sec;
                    }
                }
                if(xx!=-1) return;
            }
        }
        setPawn(xx, yy);
        reversePawns(xx, yy);
        int black = countBlack();
        int white = countWhite();
        view.blackPawns.setText("Black(Your) Pawns : " + black);
        view.whitePawns.setText("White Pawns : " + white);
    }

    public void setPawnsInArray(int[][] arr, int i, int j){
        for(int k=0; k<SIZE; k++){
            int x = i, y = j, moves = 0;
            for(int l=1; l<SIZE; l++){
                x += nx[k]; y += ny[k];
                if(endOfBoard(x, y)){
                    moves = 0;
                    break;
                }
                if(move==-1){
                    if(fields[x][y].color==WHITE){
                        moves++;
                        if(!(endOfBoard(x+nx[k], y+ny[k]))){
                            if(fields[x+nx[k]][y+ny[k]].color==BLACK){
                                break;
                            } else if(fields[x+nx[k]][y+ny[k]].color==COLORLESS){
                                moves = 0;
                                break;
                            }
                        }
                    } else{
                        moves = 0;
                        break;
                    }
                } else {
                    if(fields[x][y].color==BLACK){
                        moves++;
                        if(!(endOfBoard(x+nx[k], y+ny[k]))){
                            if(fields[x+nx[k]][y+ny[k]].color==WHITE){
                                break;
                            } else if(fields[x+nx[k]][y+ny[k]].color==COLORLESS){
                                moves = 0;
                                break;
                            }
                        }
                    } else{
                        moves = 0;
                        break;
                    }
                }
            }
            x = i; y = j;
            for(int l=1; l<=moves; l++){
                x += nx[k]; y += ny[k];
                if(move==-1) arr[x][y] = BLACK;
                else arr[x][y] = WHITE;
            }
        }
    }

    public int[][] createCopy(int[][] arr){
        int[][] ret = new int[WIDTH][HEIGHT];
        for(int i=0; i<WIDTH; i++){
            for(int j=0; j<HEIGHT; j++){
                ret[i][j] = arr[i][j];
            }
        }
        return ret;
    }

    public double DoMinMax(int player, int[][] arr, int curr, int depth){
        return alfaBeta(player, arr, curr, depth, -300, 300);
    }

    public double alfaBeta(int player, int[][] arr, int curr, int depth, double alfa, double beta) {
        double A = alfa, B = beta;
        int children = 0;
        if(curr<=depth){
            double val;
            if(player==BOT) val = -INFTY;
            else val = INFTY;
            for(int i=0; i<WIDTH; i++){
                for(int j=0; j<HEIGHT; j++){
                    if(isAllowed(i, j)){
                        int a[][] = createCopy(arr);
                        setPawnsInArray(a, i, j);
                        double tmp = 0;
                        if(player==BOT) {
                            tmp = alfaBeta(PLAYER, a, curr+1, depth, A, B);
                            //if(tmp > val) val = tmp;
                            if(A<tmp) A = val;
                            if(A>=B) break;

                        }
                        else {
                            tmp = alfaBeta(BOT, a, curr+1, depth, A, B);
                            //if(tmp < val) val = tmp;
                            if(B>tmp) B = val;
                            if(B<=A) break;
                        }
                    }
                }
            }
            if(player==BOT) return A;
            else return B;
            //return val;
        } else {
            int white = countWhite();
            int black = countBlack();
            return (double) (white)/(white+black);
        }
    }

    public void actionOnButton(){
        move = -1;
        for(int i=0; i<WIDTH; i++){
            for(int j=0; j<HEIGHT; j++){
                fields[i][j].setField();
            }
        }
        view.blackPawns.setText("Black(Your) Pawns : 2");
        view.whitePawns.setText("White Pawns : 2");
    }

    public int countBlack(){
        int cnt = 0;
        for(int k=0; k<WIDTH; k++){
            for(int l=0; l<HEIGHT; l++){
                if(fields[k][l].color == BLACK) cnt++;
            }
        }
        return cnt;
    }

    public int countWhite(){
        int cnt = 0;
        for(int k=0; k<WIDTH; k++){
            for(int l=0; l<HEIGHT; l++){
                if(fields[k][l].color == WHITE) cnt++;
            }
        }
        return cnt;
    }

    public boolean endOfGame(){
        int counter = 0;
        for(int i=0; i<WIDTH; i++){
            for(int j=0; j<HEIGHT; j++){
                if(fields[i][j].fill) counter++;
            }
        }
        if(counter==WIDTH*HEIGHT) return true;
        return false;
    }
}

