package sample.gameplayObjects;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import sample.Controller;
import sample.events.EventListener;
import sample.data.GameField;

public class Apples {
	private Pane parent;
	private GameField gameField;
	private EventListener listener;
	
	private ImageView apples[];
	private final int MAX = 15;
	private int countApples = 0;
	ColorAdjust filterForApple = new ColorAdjust(0, 0, -1, 0);
	private boolean thereIs = false;
	
	
	
	public Apples() {
		this.parent = Controller.getGamePane();
		this.gameField = GameField.getInstance();
		
		init();
	}
	
	private void init() {
		countApples = MAX;
		addLoot(MAX);
	}
	
	void addLoot(int amount) {
		int randomX = 0;
		int randomY = 0;
		countApples = amount;
		apples = new ImageView[amount];
		for (int i = 0; i < apples.length; i++) {
			apples[i] = addPart();
			do {
				randomX = (int) (Math.random() * gameField.widthGameField);
				randomY = (int) (Math.random() * gameField.heightGameField);
			} while (!gameField.gameEmptyPlaceMap[randomY][randomX].equals("-"));
			
			apples[i].setX((randomX) * gameField.step + 1);
			apples[i].setY((randomY) * gameField.step + 1);
			
			parent.getChildren().add(apples[i]);
			apples[i].setEffect(rainbowColor());
			
			gameField.gameEmptyPlaceMap[randomY][randomX] = "O";
		}
		
		gameField.print();
	}
	
	ImageView addPart() {
		Image bodyImg = new Image("sample/_res/apple.png");
		return new ImageView(bodyImg);
	}
	
	private Effect rainbowColor() {
		int rand = (int) (Math.random() * 7);
		float hue = 0;
		
		switch (rand) {
			case 1: hue = -0.7f;
				break;
			case 2: hue = -0.5f;
				break;
			case 3: hue = -0.3f;
				break;
			case 4: hue = -0.1f;
				break;
			case 5: hue =  0.15f;
				break;
			case 6: hue =  0.4f;
				break;
			case 7: hue =  0.8f;
				break;
		}
		
		return new ColorAdjust(hue,0.15,  0, 0);
	}
	
	public void onCollectLoot(SnakeBody snake) {
		if (countApples != 0) {
			for (int i = 0; i < apples.length; i++) {
				if (apples[i] != null) {
					if (snake.getX() == apples[i].getX()
					 && snake.getY() == apples[i].getY()) {
						apples[i].setOpacity(0.1);
						apples[i].setEffect(filterForApple);
						apples[i] = null;
						countApples--;
						
						listener.dispatch();
					}
				}
			}
		} else {
			init();
		}
	}
	
	public boolean isThereIs() {
		return thereIs;
	}
	
	public int getCountApples() {
		return countApples;
	}
	
	public void addEventListener(EventListener el) {
		this.listener = el;
	}
	
	
}
