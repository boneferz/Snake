package sample;

import javafx.animation.AnimationTimer;
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
	private Label textRecord;
	
	private Image bodyImg = new Image("sample/res/partOfBody.png");
	private ImageView body = new ImageView(bodyImg);
	
	private Image appleImg = new Image("sample/res/apple.png");
	private ImageView apple = new ImageView(appleImg);
	
	private Image wallImg = new Image("sample/res/wallBlock.png");
	private ImageView wall = new ImageView(wallImg);
	
	private int fps = 100;
	private final Timeline timeline = new Timeline();
	
	private String keyVector;
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
	
	private SnakeBody snake;
	private ImageView apples[];
	
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
		
		keyVector = RIGHT;

		// snake
		gameField.getChildren().add(snake.getHead());
		snake.init(3 * step + 1, 3 * step + 1);
	}
	
	@FXML
	public void initialize () {
		
		init();
		reset();
		
		addLoot();
		
	}

	/*===================================================
	*                   TIMELINE
	*==================================================*/
	
	private void onUpdate() {
		if (isLive) {
			// death
			if (snake.getY() < borderTop + step && keyVector.equals(UP)
					|| snake.getY() > borderBottom - step && keyVector.equals(DOWN)
					|| snake.getX() > borderRight - step && keyVector.equals(RIGHT)
					|| snake.getX() < borderLeft + step && keyVector.equals(LEFT)) {
				snakeDie();
				System.out.println("    (-life)");
			} else {
				// move
				switch (keyVector) {
					case UP:
						snake.move(snake.getX(), snake.getY() - step);
						break;
					case DOWN:
						snake.move(snake.getX(), snake.getY() + step);
						break;
					case LEFT:
						snake.move(snake.getX() - step, snake.getY());
						break;
					case RIGHT:
						snake.move(snake.getX() + step, snake.getY());
						break;
				}
				
				if (keyVector.equals(UP)
						|| keyVector.equals(DOWN)
						|| keyVector.equals(LEFT)
						|| keyVector.equals(RIGHT)) isWent = true;
				
				if (snake.suicide()) snakeDie();
			}
			
			// collision with apples
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
		
		System.out.println("--die--");
	}
	
	private void addLoot() {
		apples = new ImageView[25];
		for (int i = 0; i < apples.length; i++) {
			apples[i] = addApple();
			gameField.getChildren().add(apples[i]);
			apples[i].setX(((int) (Math.random() * widthGameField)) * step + 1);
			apples[i].setY(((int) (Math.random() * heightGameField)) * step + 1);
		}
	}
	
	private ImageView addApple() {
		Image bodyImg = new Image("sample/res/apple.png");
		return new ImageView(bodyImg);
	}
	
	private void onCollectLoot() {
		for (int i = 0; i < apples.length; i++) {
			if (snake.getX() == apples[i].getX() && snake.getY() == apples[i].getY()) {
				apples[i].setOpacity(0.05);
				apples[i].setEffect(filterForApple);
				
//				addLoot();
				addSnakePart();
				
				setScore(score += 15);
			}
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
					if (!keyVector.equals(DOWN)) keyVector = UP;
					break;
				case DOWN:
					if (!keyVector.equals(UP)) keyVector = DOWN;
					break;
				case LEFT:
					if (!keyVector.equals(RIGHT)) keyVector = LEFT;
					break;
				case RIGHT:
					if (!keyVector.equals(LEFT)) keyVector = RIGHT;
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
	
}
