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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;

public class Controller {
	
	@FXML
	Pane gameField;
	
	@FXML
	private Label textScore;
	
	@FXML
	private Label textDeath;
	
	@FXML
	private AnchorPane root;
	
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
	
	private int fps = 200;
	private Timeline timeline = new Timeline();
	
	private String direction;
	private int vectorX = 1;
	private int vectorY = 1;
	private final String UP = "up";
	private final String DOWN = "down";
	private final String LEFT = "left";
	private final String RIGHT = "right";
	private boolean isWent = true;
	
	final int pixel = 15;
	final int widthGameField = 19;
	final int heightGameField = 16;
	final int step = pixel + 1;
	
	private final int borderTop = 0;
	private final int borderLeft = 0;
	private final int borderBottom = heightGameField * step;
	private final int borderRight = widthGameField * step;
	
	private int record = 0;
	int score = 0;
	private int death = 0;
	int length = 1;
	
	SnakeBody snake;

	private ImageView wallBlocks[];
	private int wallCount = 0;
	ArrayList<Point> wallPositionsMap = new ArrayList<>();
	private int[][] level =
			{{3, 2}, {2, 2}, {2, 3}, {2, 12}, {2, 13}, {3, 13}, {15, 13},
					{16, 13}, {16, 12}, {16, 3}, {16, 2}, {15, 2}, {9, 2},
					{9, 3}, {9, 12}, {9, 13}, {2, 8}, {2, 7}, {16, 7},
					{16, 8}, {9, 8}, {9, 7}, {9, 9}, {9, 6}, {6, 2},
					{12, 2}, {6, 13}, {12, 13}};

	
	private boolean isLive = false;
	ColorAdjust filterForApple = new ColorAdjust(
			0, 0, -1, 0);
	private boolean isShaking = false;
	
	private Apples apples;
	
	private void init() {
		// enter frame loop
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setOnFinished( e -> onUpdate());
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(fps), timeline.getOnFinished()));
		timeline.play();
		
		// keys listener
		Main.stage.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyListener);
		
		// snake
		snake = new SnakeBody();
		
		// wall
		wallBlocks = new ImageView[level.length];
		for (int i = 0; i < level.length; i++) {
			wallBlocks[wallCount] = addWall();
			gameField.getChildren().add(wallBlocks[wallCount]);
			wallBlocks[wallCount].setX((level[i][0]) * step + 1);
			wallBlocks[wallCount].setY((level[i][1]) * step + 1);
			wallCount++;
		}
		
		//
		apples = new Apples(this);
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
	}
	
	@FXML
	public void initialize () {
		
		init();
		reset();
		
		apples.addLoot(40);
		
		levelRedactor();
	}
	
	private void levelRedactor() {
		gameField.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onGameFieldListener);
	}
	
	private void onGameFieldListener(MouseEvent e) {
		Point pointOfClick = new Point();
		pointOfClick.x = ( ((int) e.getX()) - ((int) e.getX()) % step ) / step;
		pointOfClick.y = ( ((int) e.getY()) - ((int) e.getY()) % step ) / step;
		
		wallPositionsMap.add(pointOfClick);
		
		
		System.out.println("point: " + pointOfClick);
		System.out.println("");
		
		Image w = new Image("sample/res/apple.png");
		ImageView wView = new ImageView(w);
		wView.setOpacity(0.5);
		wView.setX(pointOfClick.x * step + 1);
		wView.setY(pointOfClick.y * step + 1);
		gameField.getChildren().add(wView);
	}
	
	private void parseLevelArr() {
		String levelTxt = "{";
		for (int i = 0; i < wallPositionsMap.size(); i++) {
			levelTxt += "{" + wallPositionsMap.get(i).x + ", "+ wallPositionsMap.get(i).y + "}";
			if (i != wallPositionsMap.size() - 1) levelTxt += ", ";
			if (i != 0 && i % 6 == 0) levelTxt += "\n";
		}
		levelTxt += "}";
		// output level data
		System.out.println(levelTxt);
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
				
				if (wallBlocks[i] == null) {
					System.out.println("wall|i:" + i + "||" + wallBlocks[i]);
				} else {
					if (snake.getX() + (step * vectorX) == wallBlocks[i].getX()
							&& snake.getY() + (step * vectorY) == wallBlocks[i].getY()) {
						System.out.println("death >> wall");
						snakeDie();
						return;
					}
				}
			}
			
			// move
			snake.move( vectorX * step,  vectorY * step);
			
			// went to setting
			if (direction.equals(UP)
					|| direction.equals(DOWN)
					|| direction.equals(LEFT)
					|| direction.equals(RIGHT)) isWent = true;
			
			// apples
			apples.onCollectLoot();
		}
	}
	
	/*===================================================
	*                   GAMEPLAY
	*==================================================*/
	
	private void snakeDie() {
		isLive = false;
		
		snake.die();
		Shake.toShake(root);

		setDeath(++death);
		if (record < score) setRecord(score);
		setScore(0);
	}
	
	private ImageView addWall() {
		Image wallImg = new Image("sample/res/wallBlock.png");
		return new ImageView(wallImg);
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
				gameField.getChildren().add(snake.addPart());
				break;
				
			case CONTROL:
				parseLevelArr();
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
