package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class SnakeBody {
	private Pane parent;
	private GameField gameField;
	// body
	private int length = 1;
	private ImageView body[] = new ImageView[length];
	private double tempX;
	private double tempY;
	private ImageView head;
	
	// moving
	private int vectorX = 1;
	private int vectorY = 1;
	private final String UP = "up";
	private final String DOWN = "down";
	private final String LEFT = "left";
	private final String RIGHT = "right";
	private String direction;
	
	private boolean isLive = false;
	private boolean canChangeDirection = true;
	
	private int initX = 1;
	private int initY = 1;
	
	// events
	private EventListener listener;
	public static final String DEATH = "death";
	
	ImageView compas;
	
	
	
	public SnakeBody(Pane rootPane, GameField gameFieldData, int initX, int initY) {
		this.parent = rootPane;
		this.gameField = gameFieldData;
		this.initX = initX * gameField.step + 1;
		this.initY = initY * gameField.step + 1;
		
		// keys listener
		Main.stage.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyListener);
		
		addPart();
		head = body[0];
		body[0].setRotate(45);
		
		// debug
		Image c = new Image("sample/res/apple.png");
		compas = new ImageView(c);
		compas.setScaleX(0.5);
		compas.setScaleY(0.5);
		parent.getChildren().add(compas);
		compas.setTranslateZ(99);
		
		init();
	}
	
	private void init() {
		isLive = true;
		
		direction = RIGHT;
		vectorX = 1;
		vectorY = 0;
		canChangeDirection = false;
		
		body[0].setX(initX);
		body[0].setY(initY);
	}
	
	public void reset() {
		init();
		parent.getChildren().add(body[0]);
		body[0].setOpacity(1);
	}
	
	public void addEventListener(EventListener el) {
		this.listener = el;
	}
	
	public ImageView addPart() {
		// to increase arr
		if (body.length < length) {
			ImageView[] newArr = new ImageView[body.length + 1];
			for (int i = 0; i < body.length; i++) {
				newArr[i] = body[i];
			}
			body = newArr;
		}
		// graphic
		Image bodyImg = new Image("sample/res/partOfBody.png");
		ImageView imageView = new ImageView(bodyImg);
		imageView.setX(tempX);
		imageView.setY(tempY);
		body[body.length - 1] = imageView;
		parent.getChildren().add(imageView);
		length++;
		return imageView;
	}
	
	public void deathChecking() {
		// death -> borders
		if (nextY() < gameField.borderTop
				|| nextY() > gameField.borderBottom
				|| nextX() > gameField.borderRight
				|| nextX() < gameField.borderLeft ) {
			die();
		}
		
		// death -> suicide
		for (int i = 1; i < body.length; i++) {
			if (nextX() == body[i].getX() && nextY() == body[i].getY()) {
				die();
			}
		}
		
		// debug
		compas.setX(nextX());
		compas.setY(nextY());
	}
	
	public void move() {
		if (!isLive) return;
		
		double localTempCurrentX = 0;
		double localTempCurrentY = 0;
		double localTempPreviousX = 0;
		double localTempPreviousY = 0;
		
		// save last
		tempX = body[body.length - 1].getX();
		tempY = body[body.length - 1].getY();
		
		// move middles
		for (int i = 0; i < body.length; i++) {
			localTempCurrentX = body[i].getX();
			localTempCurrentY = body[i].getY();
			if (i == 0) {
				body[0].setX(nextX());
				body[0].setY(nextY());
			} else {
				body[i].setX(localTempPreviousX);
				body[i].setY(localTempPreviousY);
			}
			localTempPreviousX = localTempCurrentX;
			localTempPreviousY = localTempCurrentY;
		}
		
		canChangeDirection = true;
	}
	
	public void die() {
		isLive = false;
		
		for (int i = 0; i < body.length; i++) {
			body[i].setOpacity(0.65);
		}
		
		listener.dispatch(DEATH);
	}
	
	public void destroy() {
		// cut body
		for (int i = 0; i < body.length; i++) {
			parent.getChildren().remove(body[i]);
		}
		ImageView[] newArr = new ImageView[1];
		newArr[0] = body[0];
		body = newArr;
		length = 2;
	}
	
	private void onKeyListener(KeyEvent e) {
		if ((e.getCode() == KeyCode.UP
				|| e.getCode() == KeyCode.DOWN
				|| e.getCode() == KeyCode.LEFT
				|| e.getCode() == KeyCode.RIGHT) && canChangeDirection) {
			canChangeDirection = false;
			
			// switch vector direction
			switch (e.getCode()) {
				case UP:
					if (!direction.equals(DOWN)) {
						direction = UP;
						vectorX = 0;
						vectorY = -1;
					}
					break;
				case DOWN:
					if (!direction.equals(UP)) {
						direction = DOWN;
						vectorX = 0;
						vectorY = 1;
					}
					break;
				case LEFT:
					if (!direction.equals(RIGHT)) {
						direction = LEFT;
						vectorX = -1;
						vectorY = 0;
					}
					break;
				case RIGHT:
					if (!direction.equals(LEFT)) {
						direction = RIGHT;
						vectorX = 1;
						vectorY = 0;
					}
					break;
			}
		}
	}
	
	public double nextX() {
		return body[0].getX() + vectorX * gameField.step;
	}
	public double nextY() {
		return body[0].getY() + vectorY * gameField.step;
	}
	
	public void setX(double x) {
		body[0].setX(x);
	}
	public void setY(double y) {
		body[0].setY(y);
	}
	public double getX() {
		return body[0].getX();
	}
	public double getY() {
		return body[0].getY();
	}
	
	public ImageView getHead() {
		return body[0];
	}
	public boolean isLive() {
		return isLive;
	}
}
