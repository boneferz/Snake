package sample;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	
	private Image bodyImg = new Image("sample/res/partBody.png");
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
	
	private ImageView[] snake = new ImageView[1];
	private double tempX = 0;
	private double tempY = 0;
	
	private boolean isLive = false;
	
	
	
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
		
		record = 0;
		death = 0;
		
		// snake
		gameField.getChildren().add(apple);
		snake[0] = apple;
//		snake[0].setX(5 * step);
//		snake[0].setY(5 * step);
	}
	
	private void reset() {
		isLive = true;
		
		score = 0;
		
		setRecord(record);
		setScore(score);
		setDeath(death);
		
		snake[0].setX(1);
		snake[0].setY(1);
		snake[0].setOpacity(1);
		
		keyVector = RIGHT;
	}
	
	@FXML
	public void initialize () {
		
		init();
		reset();
		
		/*for (ImageView key:snake) {
			System.out.println("snake body:" + key);
		}*/
	}

	/*===================================================
	*                   TIMELINE
	*==================================================*/
	
	private void onUpdate() {
		if (isLive) {
			// death
			if (snake[0].getY() < borderTop + step && keyVector.equals(UP)
					|| snake[0].getY() > borderBottom - step && keyVector.equals(DOWN)
					|| snake[0].getX() > borderRight - step && keyVector.equals(RIGHT)
					|| snake[0].getX() < borderLeft + step && keyVector.equals(LEFT)) {
				System.out.println("    (-life)");
				setDeath(++death);
				
				die();
				
				//  borders
				if (snake[0].getY() > borderBottom - step && keyVector.equals(DOWN)) { // bottom border
					keyVector = UP;
					System.out.println("|border|");
				} else if (snake[0].getY() < borderTop + step && keyVector.equals(UP)) { // top border
					keyVector = DOWN;
					System.out.println("|border|");
					
				}
				else if (snake[0].getX() > borderRight - step && keyVector.equals(RIGHT)) { // right border
					keyVector = LEFT;
				} else if (snake[0].getX() < borderLeft + step && keyVector.equals(LEFT)) { // left border
					keyVector = RIGHT;
				}
				
			} else {
				// move
				switch (keyVector) {
					case UP:
						moveSnake(snake[0].getX(), snake[0].getY() - step);
						break;
					case DOWN:
						moveSnake(snake[0].getX(), snake[0].getY() + step);
						break;
					case LEFT:
						moveSnake(snake[0].getX() - step, snake[0].getY());
						break;
					case RIGHT:
						moveSnake(snake[0].getX() + step, snake[0].getY());
						break;
				}
			}
		}
	}
	
	private void moveSnake(double x, double y) {
		snake[0].setX(x);
		snake[0].setY(y);
		/*for (int i = 0; i < snake.length; i++) {
			if (snake[i] == null) return;
			if (i == 0) {
				// move
				snake[i].setX(x);
				snake[i].setY(y);
			} else {
				// move
				snake[i].setX(tempX + i);
				snake[i].setY(tempY + i);
			}
			
			tempX = snake[i].getX();
			tempY = snake[i].getY();
		}*/
		
	}
	
	/*===================================================
	*                   GAMEPLAY
	*==================================================*/
	
	private ImageView addPart(int index) {
		Image bodyImg = new Image("sample/res/apple.png");
		ImageView imageView = new ImageView(bodyImg);
		gameField.getChildren().add(imageView);
		return imageView;
	}
	
	private void die() {
		isLive = false;
		snake[0].setOpacity(0.5);
	}
	
	/*===================================================
	*                   LISTENERS
	*==================================================*/
	
	private void onKeyListener(KeyEvent e) {
		switch (e.getCode()) {
			case UP:
				keyVector = UP;
				break;
			case DOWN:
				keyVector = DOWN;
				break;
			case LEFT:
				keyVector = LEFT;
				break;
			case RIGHT:
				keyVector = RIGHT;
				break;
			case ESCAPE:
				reset();
				break;
		}
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
