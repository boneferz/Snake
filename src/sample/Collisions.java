package sample;

import javafx.scene.image.ImageView;

public class Collisions {
	
	public boolean collision(SnakeBody snake, ImageView[] hitObject) {
		for (int i = 0; i < hitObject.length; i++) {
			if (snake.nextX() == hitObject[i].getX()
					&& snake.nextY() == hitObject[i].getY()) {
//				snakeDie();
				System.out.println("snake + wall : HIT!");
				return true;
			}
		}
		return false;
	}
	
	/*
	
	public boolean collision(ImageView snakeHead, ImageView[] hitObject) {
		// death > wall <<<<<<
		for (int i = 0; i < wallBlocks.length; i++) {
			if (snake.getX() + (step * vectorX) == wallBlocks[i].getX()
					&& snake.getY() + (step * vectorY) == wallBlocks[i].getY()) {
//						System.out.println("death >> wall");
//				snakeDie();
				return true;
			}
		}
	}*/
}
