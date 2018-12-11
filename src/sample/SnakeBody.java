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
	private boolean isWent = true;
	
	private int initX = 1;
	private int initY = 1;
	
	// events
	private EventListener listener;
	public static final String DEATH = "death";
	
	
	
	// Consctructor() >> разовая инициализация
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
		
		init();
	}
	// init() >> значения по умолчанию
	// (повторно используется - при сбросе и при инициализации)
	private void init() {
		isLive = true;
		isWent = false;
		direction = RIGHT;
		body[0].setX(initX);
		body[0].setY(initY);
	}
	// reset() >> сброс
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
	
	public void snakeMove() {
		if (isLive) {
			// death > borders
			if (getY() < gameField.borderTop + gameField.step && direction.equals(UP)
					|| getY() > gameField.borderBottom - gameField.step && direction.equals(DOWN)
					|| getX() > gameField.borderRight - gameField.step && direction.equals(RIGHT)
					|| getX() < gameField.borderLeft + gameField.step && direction.equals(LEFT)) {
				System.out.println("die();");
				die();
				return;
			}
			
			// death > suicide
			if (suicide(vectorX * gameField.step, vectorY * gameField.step)) {
				die();
				return;
			}
			
			// switch vector direction
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
			
			// went to setting
			if (direction.equals(UP)
					|| direction.equals(DOWN)
					|| direction.equals(LEFT)
					|| direction.equals(RIGHT)) isWent = true;
			
			// move
			move( vectorX * gameField.step,  vectorY * gameField.step);
		}
	}
	
	private void move(double x, double y) {
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
				body[0].setX(body[0].getX() + x);
				body[0].setY(body[0].getY() + y);
			} else {
				body[i].setX(localTempPreviousX);
				body[i].setY(localTempPreviousY);
			}
			localTempPreviousX = localTempCurrentX;
			localTempPreviousY = localTempCurrentY;
		}
	}
	
	private void onKeyListener(KeyEvent e) {
		switch (e.getCode()) {
			case ESCAPE:
				destroy();
				reset();
				break;
			
			case SHIFT:
				addPart();
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
		System.out.println(":" + direction);
	}
	
	private boolean suicide(int vectorX, int vectorY) {
		for (int i = 1; i < body.length; i++) {
			if (body[0].getX() + vectorX == body[i].getX()
					&& body[0].getY() + vectorY == body[i].getY()) {
				return true;
			}
		}
		return false;
	}
	
	private void die() {
		isLive = false;
		
		for (int i = 0; i < body.length; i++) {
			body[i].setOpacity(0.65);
		}
		
		listener.handler(DEATH);
	}
	
	public void destroy() {
		for (int i = 0; i < body.length; i++) {
			parent.getChildren().remove(body[i]);
		}
		ImageView[] newArr = new ImageView[1];
		newArr[0] = body[0];
		body = newArr;
		length = 2;
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
