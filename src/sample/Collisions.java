package sample;

import javafx.scene.image.ImageView;

public class Collisions {
	// events
	private EventListener listener;
	public static final String WALL = "wall";
	
	public void collision(SnakeBody snake, ImageView[] hitObject) {
		if (snake.isLive()) {
			for (int i = 0; i < hitObject.length; i++) {
				if (snake.nextX() == hitObject[i].getX() && snake.nextY() == hitObject[i].getY()) {
					System.out.println("1 snake + wall -- [HIT]");
					listener.dispatch(WALL);
					hitObject[i].setRotate(hitObject[i].getRotate() + 25);
				}
			}
		}
	}
	
	public void addEventListener(EventListener el) {
		this.listener = el;
	}
}
