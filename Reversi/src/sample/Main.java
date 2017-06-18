package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public static final int TITLE_SIZE = 8;
    boolean fillFields[][] = new boolean[WIDTH][HEIGHT];
    @Override
    public void start(Stage stage) throws Exception{
        viewApp view = new viewApp();
        Controller controller = new Controller();
        controller.setView(view);
        view.setController(controller);
        view.createView();

        Scene scene = new Scene(view.bp, 800, 800);
        stage.setTitle("OTHELLO");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
