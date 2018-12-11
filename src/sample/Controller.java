package sample;

import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Controller {
	
	@FXML
	private Pane gamePane;
	
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
	
	// screen update
	private int fps = 200;
	private Timeline timeline = new Timeline();
	
	// data
	private int record = 0;
	private int score  = 0;
	private int death  = 0;
	private int length = 1;
	
	// objects
	private GameField gameFieldData;
	private SnakeBody snake;
	private Apples apples;
	private Walls walls;
	private LevelMap level;
	
	
	@FXML
	public void initialize () {
		// objects instance
		gameFieldData = new GameField();
		level = new LevelMap();
//		walls = new Walls(gamePane, gameFieldData);
//		apples = new Apples(gamePane, gameFieldData);
		snake = new SnakeBody(gamePane, gameFieldData, 1, 2);
		
		// set data
		setRecord(record);
		setScore(0);
		setDeath(death);
		setLength(1);
		
		// enter frame loop
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setOnFinished( e -> onUpdate());
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(fps), timeline.getOnFinished()));
		timeline.play();
		
		
		// event dispatch
		EventListener snakeListener = this::snakeHandler;
		snake.addEventListener(snakeListener);
		
		// keys listener
		//Main.stage.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyListener);
		
		// create game objects
//		apples.addLoot(40);

//		levelRedactor();
		
		reset();
	}
	
	private void snakeHandler(String s) {
		switch (s) {
			case SnakeBody.DEATH:
				System.out.println("snakeDie();");
				snakeDie();
				break;
		}
	}
	
	private void reset() {
		setRecord(record);
		setScore(0);
		setDeath(death);
		setLength(1);
	}
	
	/*private void levelRedactor() {
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
	}*/
	
	private void onUpdate() {
		
		snake.snakeMove();
		
		if (snake.isLive()) {
		
		
//			apples.onCollectLoot(); <<<<<<
			
			// death > wall <<<<<<
			/*for (int i = 0; i < wallBlocks.length; i++) {
				if (wallBlocks[i] == null) {
//					System.out.println("wall|i:" + i + "||" + wallBlocks[i]);
				} else {
					if (snake.getX() + (step * vectorX) == wallBlocks[i].getX()
							&& snake.getY() + (step * vectorY) == wallBlocks[i].getY()) {
//						System.out.println("death >> wall");
						snakeDie();
						return;
					}
				}
			}*/
			
		}
	}
	
	private void snakeDie() {
		Shake.toShake(root);
		// data
		if (record < score) setRecord(score);
		setDeath(++death);
		setScore(0);
	}
	
	/*private ImageView addWall() {
		Image wallImg = new Image("sample/res/wallBlock.png");
		return new ImageView(wallImg);
	}*/
	
	private void onKeyListener(KeyEvent e) {
		switch (e.getCode()) {
			case CONTROL:
				//parseLevelArr();
				break;
		}
	}
	
	
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
