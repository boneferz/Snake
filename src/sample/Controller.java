package sample;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.enums.StatesOfGame;
import sample.events.EventListener;
import sample.gameplayObjects.Apples;
import sample.gameplayObjects.SnakeBody;
import sample.gameplayObjects.Walls;
import sample.utils.Shake;

import static sample.enums.StatesOfGame.*;

public final class Controller {
	
	@FXML
	private AnchorPane root;
	
	@FXML
	private Label textScore;
	
	@FXML
	private Label textDeath;
	
	@FXML
	private Label textRecord;
	
	@FXML
	public Pane gamePane;
	
	@FXML
	private Label textLength;
	
	// screen update
	private int fps = 225;
	private Timeline timeline = new Timeline();
	
	// data
	private int record = 0;
	private int score  = 0;
	private int death  = 0;
	private int length = 1;
	
	// objects
	private SnakeBody snake;
	private Apples appleLoot;
	private Walls walls;
	
	// states
	private StatesOfGame state;
	private static Pane parentPane;
	
	
	@FXML
	public void initialize () {
		parentPane = gamePane;
		
		// objects instance
		walls = new Walls();
		snake = new SnakeBody(2, 1);
		appleLoot = new Apples();
		
		// set data
		setRecord(record);
		setScore(0);
		setDeath(death);
		setLength(1);
		
		// enter frame loop
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setOnFinished(this::onUpdate);
		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(fps), timeline.getOnFinished()));
		timeline.play();
		
		// event dispatch
		EventListener snakeListener = this::handler_snake;
		EventListener wallListener = this::handler_wall;
		EventListener lootListener = this::handler_loot;
		snake.addEventListener(snakeListener);
		walls.addEventListener(wallListener);
		appleLoot.addEventListener(lootListener);
		
		// keys listener
		Main.stage.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyListener);

		reset();
	}
	
	private void reset() {
		setRecord(record);
		setScore(0);
		setDeath(death);
		setLength(1);
		
		setState(GAMEPLAY);
	}
	
	private void onUpdate(ActionEvent e) {
		if (state.equals(GAMEPLAY)) {
			snake.deathChecking();
			walls.hitSnake(snake);
			snake.move();
			appleLoot.onCollectLoot(snake);
		}
	}
	
	private void handler_snake() {
		snakeDie();
	}
	
	private void handler_wall() {
		snake.die();
	}
	
	private void handler_loot() {
		snake.addPart();
		setScore(score += 15);
		setLength(++length);
	}
	
	private void snakeDie() {
		setState(DEATH);
		Shake.toShake(root);
		
		if (record < score)
			setRecord(score);
		setDeath(++death);
		setScore(0);
	}
	
	private void onKeyListener(KeyEvent e) {
		switch (e.getCode()) {
			case ESCAPE:
				snake.destroy();
				snake.reset();
				this.reset();
				break;
			
			case SHIFT:
				snake.addPart();
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
	
	public static Pane getGamePane() {
		return parentPane;
	}
	public void setState(StatesOfGame state) {
		this.state = state;
	}
	
}

