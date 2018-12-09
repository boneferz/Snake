package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Controller {
	
	@FXML
	private Pane gameField;
	
	@FXML
	private Label textScore;
	
	@FXML
	private Label textDeath;
	
	@FXML
	private Label textLength;
	
	@FXML
	private Label textRecord;
	
	private Image bodyImg = new Image("sample/res/partOfBody.png");
	private ImageView body = new ImageView(bodyImg);
	
	private Image appleImg = new Image("sample/res/apple.png");
	private ImageView apple = new ImageView(appleImg);
	
	private Image wallImg = new Image("sample/res/wallBlock.png");
	private ImageView wall = new ImageView(wallImg);
	
	private int fps = 100;
	private final Timeline timeline = new Timeline();
	
	private String direction;
	private int vectorX = 1;
	private int vectorY = 1;
	private final String UP = "up";
	private final String DOWN = "down";
	private final String LEFT = "left";
	private final String RIGHT = "right";
	private boolean isWent = true;
	private boolean wentUp = false;
	private boolean wentDown = false;
	private boolean wentLeft = false;
	private boolean wentRight = false;
	
	private final int pixel = 15;
	private final int widthGameField = 19;
	private final int heightGameField = 16;
	private final int step = pixel + 1;
	
	private final int borderTop = 0;
	private final int borderLeft = 0;
	private final int borderBottom = heightGameField * step;
	private final int borderRight = widthGameField * step;
	
	private int record = 0;
	private int score = 0;
	private int death = 0;
	private int length = 1;
	
	private SnakeBody snake;
	private ImageView apples[];
	private ImageView wallBlocks[];
	private int countApples = 0;
	
	private boolean isLive = false;
	private ColorAdjust filterForApple = new ColorAdjust(
			0, 0, -1, 0);
	
	
	
	private void init() {
		// enter frame loop
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
		timeline.setOnFinished( e -> onUpdate());
		
		Duration duration = Duration.millis(fps);
		KeyFrame keyFrame = new KeyFrame(duration, timeline.getOnFinished());
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
		
		// keys listener
		Main.stage.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyListener);
		
		// snake
		snake = new SnakeBody();
	}
	
	private void reset() {
		isLive = true;
		
		score = 0;
		setRecord(record);
		setScore(score);
		setDeath(death);
		setLength(length);
		
		direction = RIGHT;

		// snake
		gameField.getChildren().add(snake.getHead());
		snake.init(1, 1);
		
		// wall
		wallBlocks = new ImageView[3];
		wallBlocks[0] = addWall();
		gameField.getChildren().add(wallBlocks[0]);
		wallBlocks[0].setX(3 * step + 1);
		wallBlocks[0].setY(2 * step + 1);
		
		wallBlocks[1] = addWall();
		gameField.getChildren().add(wallBlocks[1]);
		wallBlocks[1].setX(2 * step + 1);
		wallBlocks[1].setY(2 * step + 1);
		
		wallBlocks[2] = addWall();
		gameField.getChildren().add(wallBlocks[2]);
		wallBlocks[2].setX(2 * step + 1);
		wallBlocks[2].setY(3 * step + 1);
	}
	
	@FXML
	public void initialize () {
		
		init();
		reset();
		
		addLoot(40);
		
	}

	/*===================================================
	*                   TIMELINE
	*==================================================*/
	
	private void onUpdate() {
		if (isLive) {
			// switch vectors
			switch (direction) {
				case UP:
					vectorX = 0;
					vectorY = -1;
					break;
				case DOWN:
					vectorX = 0;
					vectorY = 1;
					break;
				case LEFT:
					vectorX = -1;
					vectorY = 0;
					break;
				case RIGHT:
					vectorX = 1;
					vectorY = 0;
					break;
			}
			
			// death > borders
			if (snake.getY() < borderTop + step && direction.equals(UP)
					|| snake.getY() > borderBottom - step && direction.equals(DOWN)
					|| snake.getX() > borderRight - step && direction.equals(RIGHT)
					|| snake.getX() < borderLeft + step && direction.equals(LEFT)) {
				snakeDie();
				return;
			}
			
			// death > suicide
			if (snake.suicide(vectorX * step, vectorY * step)) {
				snakeDie();
				return;
			}
			
			// death > wall
			for (int i = 0; i < wallBlocks.length; i++) {
				if (snake.getX() + (step * vectorX) == wallBlocks[i].getX()
						&& snake.getY() + (step * vectorY) == wallBlocks[i].getY()) {
					System.out.println("death >> wall");
					snakeDie();
					return;
				}// snake.move( vectorX * step,  vectorY * step);
				// body[0].getX() + x
				// snake.getX() + (step * vectorX)
			}
			
			// move
			snake.move( vectorX * step,  vectorY * step);
			
			// went to setting
			if (direction.equals(UP)
					|| direction.equals(DOWN)
					|| direction.equals(LEFT)
					|| direction.equals(RIGHT)) isWent = true;
			
			// apples
			onCollectLoot();
		}
	}
	
	/*===================================================
	*                   GAMEPLAY
	*==================================================*/
	
	private void snakeDie() {
		isLive = false;
		
		snake.die();

		setDeath(++death);
		if (record < score) setRecord(score);
		setScore(0);
	}
	
	private ImageView addWall() {
		Image wallImg = new Image("sample/res/wallBlock.png");
		return new ImageView(wallImg);
	}
	
	private void addLoot(int amount) {
		countApples = amount;
		apples = new ImageView[amount];
		for (int i = 0; i < apples.length; i++) {
			apples[i] = addApple();
			apples[i].setX(((int) (Math.random() * widthGameField)) * step + 1);
			apples[i].setY(((int) (Math.random() * heightGameField)) * step + 1);
			gameField.getChildren().add(apples[i]);
		}
	}
	
	private ImageView addApple() {
		Image bodyImg = new Image("sample/res/apple.png");
		return new ImageView(bodyImg);
	}
	
	private void onCollectLoot() {
		int index = 0;
		if (countApples != 0) {
			for (int i = 0; i < apples.length; i++) {
				if (apples[i] != null) {
					if (snake.getX() == apples[i].getX() && snake.getY() == apples[i].getY()) {
						index = i;
						countApples--;
						
						apples[i].setOpacity(0.1);
						apples[i].setEffect(filterForApple);
						apples[i] = null;
						
						addSnakePart();
						
						setScore(score += 15);
						setLength(++length);
					}
				}
			}
		} else {
			countApples = 20;
			addLoot(20);
		}
		
	}
	
	
	
	/*===================================================
	*                   LISTENERS
	*==================================================*/
	
	private void onKeyListener(KeyEvent e) {
		switch (e.getCode()) {
			case ESCAPE:
				for (int i = 0; i < snake.body.length; i++) {
					gameField.getChildren().remove(snake.body[i]);
				}
				snake.destroy();
				setLength(1);
				reset();
				break;
			case SHIFT:
				addSnakePart();
				break;
		}
		
		if ((e.getCode() == KeyCode.UP
				|| e.getCode() == KeyCode.DOWN
				|| e.getCode() == KeyCode.LEFT
				|| e.getCode() == KeyCode.RIGHT) && isWent) {
			isWent = false;
			
			switch (e.getCode()) {
				case UP:
					if (!direction.equals(DOWN)) direction = UP;
					break;
				case DOWN:
					if (!direction.equals(UP)) direction = DOWN;
					break;
				case LEFT:
					if (!direction.equals(RIGHT)) direction = LEFT;
					break;
				case RIGHT:
					if (!direction.equals(LEFT)) direction = RIGHT;
					break;
			}
		}
	}
	
	private void addSnakePart() {
		gameField.getChildren().add(snake.addPart());
	}
	
	/*===================================================
	*                   SETTERS
	*==================================================*/
	
	public void setRecord(int record) {
		this.record = record;
		textRecord.setText(String.valueOf(record));
	}
	public void setScore(int score) {
		this.score = score;
		textScore.setText(String.valueOf(score));
	}
	public void setDeath(int death) {
		this.death = death;
		textDeath.setText(String.valueOf(death));
	}
	public void setLength(int length) {
		this.length = length;
		textLength.setText(String.valueOf(length));
	}
	
}
