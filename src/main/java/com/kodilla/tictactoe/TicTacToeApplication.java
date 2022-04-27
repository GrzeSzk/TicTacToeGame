package com.kodilla.tictactoe;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


import java.awt.*;
import java.util.ArrayList;

@SpringBootApplication //do usunięcia
public class TicTacToeApplication extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    private boolean playable = true;
    private boolean turnX = true; //odpowiada za wpisanie x, a później o, bez możliwości wpisywania ciągle tej samej wartości
    private Tile[][] board = new Tile[3][3];
    private ArrayList<Combo> combos = new ArrayList<Combo>();
    private  Pane root = new Pane(); //tworzę planszę, pustą

    private Parent createContent(){

        root.setPrefSize(600,600);

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                Tile tile = new Tile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);

                root.getChildren().add(tile);

                board[j][i] = tile;
            }
        }
        //poziome linie
        for (int y=0; y<3; y++){
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }

        //pionowe linie
        for (int x=0; x<3; x++){
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        //ukośne linie
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return root;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }
    private void checkState() { //sprawdzam czy ktoś wygrał
        for (Combo combo: combos) {
            if (combo.isComplete()){
                playable = false;
                playWinAnimation (combo);
                break;
            }
        }
    }
    private void playWinAnimation(Combo combo) { //tworze animacje, ktora pokazuje za pomoca linii wygraną
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[0].getCenterX());
        line.setEndY(combo.tiles[0].getCenterY());

        root.getChildren().add(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
                new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
        timeline.play();
    }

    private class Combo { //klasa, w której sprawdzam czy ktoś wygrał
        private Tile[] tiles;
        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }
        public boolean isComplete(){
            if (tiles[0].getValue().isEmpty())
                return false;
            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    private class Tile extends StackPane {
        private Text text = new Text();

        public Tile () {
            Rectangle border = new Rectangle(200,200); //ustawiam kwadraty
            border.setFill(null); //ustawwiam kolor wypełnienia
            border.setStroke(Color.BLACK); //ustawiam kolor ramki kwadratów

            text.setFont(Font.font(77)); //ustawiam wielkość wyświetlanych liter X i O

            setAlignment(Pos.CENTER); //ustawiam położenie/wyrównanie kwadratów
            getChildren().addAll(border, (Node) text); //dodaję obiekt border do listy obiektów

            setOnMouseClicked(event -> {
                if (!playable)
                    return;

                if (event.getButton() == MouseButton.PRIMARY) { //ustawiam kliknięcie myszką, które inicjuje wpisanie X
                    if (!turnX)
                        return;

                    drawX();
                    turnX = false;
                    checkState();
                }
                else if (event.getButton() == MouseButton.SECONDARY) { //ustawiam kliknięcie myszką, które inicjuje wpisanie O
                    if (turnX)
                        return;

                    drawO();
                    turnX = true;
                    checkState();
                }
            });
        }

        public double getCenterX(){
            return getTranslateX() + 100;
        }
        public double getCenterY(){
            return getTranslateY() + 100;
        }

        public String getValue(){
            return text.getText();
        }
        private void drawX() {
            text.setText("X");
        }
        private void drawO(){
            text.setText("O");
        }
    }




}
