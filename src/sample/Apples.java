package sample;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Apples {
	private Pane parent;
	private GameField gameField;
	
	public ImageView apples[];
	
	private final int MAX = 15;
	private int countApples = 0;
	ColorAdjust filterForApple = new ColorAdjust(0, 0, -1, 0);
	private boolean thereIs = false;
	
	private EventListener listener;
	public static final String LOOT = "loot";
	
	
	public Apples(Pane rootPane, GameField gameFieldData) {
		this.parent = rootPane;
		this.gameField = gameFieldData;
		
		init();
	}
	
	private void init() {
		countApples = MAX;
		addLoot(MAX);
	}
	
	void addLoot(int amount) {
		int randomX = 0;
		int randomY = 0;
		boolean emptyPlace = true;
		
		countApples = amount;
		apples = new ImageView[amount];
		for (int i = 0; i < apples.length; i++) {
			apples[i] = addPart();
			
			do {
				randomX = (int) (Math.random() * gameField.widthGameField);
				randomY = (int) (Math.random() * gameField.heightGameField);
				if (gameField.gameEmptyPlaceMap[randomY][randomX].equals("-")) {
					emptyPlace = false;
				} else {
					System.out.println("repeat");
				}
			} while (!gameField.gameEmptyPlaceMap[randomY][randomX].equals("-"));
			
			apples[i].setX((randomX) * gameField.step + 1);
			apples[i].setY((randomY) * gameField.step + 1);
			
			parent.getChildren().add(apples[i]);
			apples[i].setEffect(rainbowColor());
			
			gameField.gameEmptyPlaceMap[randomY][randomX] = "S";
		}
		
		gameField.print();
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
	
	ImageView addPart() {
		Image bodyImg = new Image("sample/res/apple.png");
		return new ImageView(bodyImg);
	}
	
	public boolean isThereIs() {
		return thereIs;
	}
	
	void onCollectLoot(SnakeBody snake) {
		if (countApples != 0) {
			for (int i = 0; i < apples.length; i++) {
				if (apples[i] != null) {
					if (snake.getX() == apples[i].getX()
					 && snake.getY() == apples[i].getY()) {
						apples[i].setOpacity(0.1);
						apples[i].setEffect(filterForApple);
						apples[i] = null;
						countApples--;
						
						listener.dispatch(LOOT);
					}
				}
			}
		} else {
			init();
		}
	}
	
	public void addEventListener(EventListener el) {
		this.listener = el;
	}
	
	public int getCountApples() {
		return countApples;
	}
}
