package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Apples {
	private Controller root;
	private ImageView apples[];
	private int countApples = 0;
	
	public Apples(Controller root) {
		this.root = root;
	}
	
	void addLoot(int amount) {
		countApples = amount;
		apples = new ImageView[amount];
		for (int i = 0; i < apples.length; i++) {
			apples[i] = addApple();
			apples[i].setX(((int) (Math.random() * root.widthGameField)) * root.step + 1);
			apples[i].setY(((int) (Math.random() * root.heightGameField)) * root.step + 1);
			root.gameField.getChildren().add(apples[i]);
		}
	}
	
	ImageView addApple() {
		Image bodyImg = new Image("sample/res/apple.png");
		return new ImageView(bodyImg);
	}
	
	void onCollectLoot() {
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
		
	}
	
}
