package sample.gameplayObjects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sample.Controller;
import sample.events.EventListener;
import sample.data.GameField;
import sample.data.LevelMap;

public class Walls {
	private Pane parent;
	private GameField gameField;
	private EventListener listener;
	
	public ImageView wallBlocks[];
	
	
	public Walls() {
		this.parent = Controller.getGamePane();
		this.gameField = GameField.getInstance();
		System.out.println(parent);
		wallBlocks = new ImageView[LevelMap.level.length];
		for (int i = 0; i < LevelMap.level.length; i++) {
			wallBlocks[i] = addWall();
			parent.getChildren().add(wallBlocks[i]);
			
			wallBlocks[i].setX((LevelMap.level[i][0]) * gameField.step + 1);
			wallBlocks[i].setY((LevelMap.level[i][1]) * gameField.step + 1);
			
			gameField.gameEmptyPlaceMap[LevelMap.level[i][1]][LevelMap.level[i][0]] = "#";
		}
	}
	
	private ImageView addWall() {
		Image wallImg = new Image("sample/_res/wallBlock.png");
		return new ImageView(wallImg);
	}
	
	public void hitSnake(SnakeBody snake) {
		for (int i = 0; i < wallBlocks.length; i++) {
			if (snake.nextX() == wallBlocks[i].getX()
			 && snake.nextY() == wallBlocks[i].getY()) {
				listener.dispatch();
			}
		}
	}
	
	public void addEventListener(EventListener el) {
		this.listener = el;
	}
}
