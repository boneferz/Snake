package sample;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Apples {
	
	private Pane parent;
	private GameField gameField;
	
	public ImageView apples[];
	private int countApples = 0;
	ColorAdjust filterForApple = new ColorAdjust(0, 0, -1, 0);
	private boolean thereIs = false;
	
	public Apples(Node rootPane, GameField gameFieldData) {
		this.parent = parent;
	}
	
	void addLoot(int amount) {
		countApples = amount;
		apples = new ImageView[amount];
		for (int i = 0; i < apples.length; i++) {
			apples[i] = addApple();
			apples[i].setX(((int) (Math.random() * gameField.widthGameField)) * gameField.step + 1);
			apples[i].setY(((int) (Math.random() * gameField.heightGameField)) * gameField.step + 1);
			parent.getChildren().add(apples[i]);
		}
	}
	
	ImageView addApple() {
		Image bodyImg = new Image("sample/res/apple.png");
		return new ImageView(bodyImg);
	}
	
	public boolean isThereIs() {
		return thereIs;
	}
	
	/*void onCollectLoot() {
		int index = 0;
		if (countApples != 0) {
			for (int i = 0; i < apples.length; i++) {
				if (apples[i] != null) {
					if (root.snake.getX() == apples[i].getX() && root.snake.getY() == apples[i].getY()) {
						index = i;
						countApples--;
						
						apples[i].setOpacity(0.1);
						apples[i].setEffect(root.filterForApple);
						apples[i] = null;
						
						root.gameField.getChildren().add(root.snake.addPart());
						root.setScore(root.score += 15);
						
						root.setLength(++root.length);
					}
				}
			}
		} else {
			countApples = 20;
			addLoot(20);
		}
		
	}*/
	
}
