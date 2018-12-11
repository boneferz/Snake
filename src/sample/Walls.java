package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.awt.Point;
import java.util.ArrayList;

public class Walls {
	private Pane parent;
	private GameField gameField;
	private LevelMap level;
	
	public ImageView wallBlocks[];
	ArrayList<Point> wallPositionsMap = new ArrayList<>();
	
	public Walls(Pane rootPane, GameField gameFieldData) {
		this.parent = rootPane;
		this.gameField = gameFieldData;
		
		level = new LevelMap();
		wallBlocks = new ImageView[level.getLevel().length];
		System.out.println(level.getLevel().length);
		System.out.println(addWall());
		
		
		for (int i = 0; i < level.getLevel().length; i++) {
			wallBlocks[i] = addWall();
			parent.getChildren().add(wallBlocks[i]);
			wallBlocks[i].setX((level.getLevel()[i][0]) * gameFieldData.step + 1);
			wallBlocks[i].setY((level.getLevel()[i][1]) * gameFieldData.step + 1);
		}
	}
	
	private ImageView addWall() {
		Image wallImg = new Image("sample/res/wallBlock.png");
		return new ImageView(wallImg);
	}
}
