package sample;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.util.ArrayList;

public class Walls {
	private Pane parent;
	private GameField gameField;
	
	private ImageView wallBlocks[];
	private int wallCount = 0;
	ArrayList<Point> wallPositionsMap = new ArrayList<>();
	
	
	/*public Walls(Node rootPane, GameField gameFieldData) {
		// wall
		wallBlocks = new ImageView[level.length];
		for (int i = 0; i < level.length; i++) {
			wallBlocks[wallCount] = addWall();
			parent.getChildren().add(wallBlocks[wallCount]);
			wallBlocks[wallCount].setX((level[i][0]) * step + 1);
			wallBlocks[wallCount].setY((level[i][1]) * step + 1);
			wallCount++;
		}
	}*/
}
