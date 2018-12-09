package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SnakeBody {
	private int length = 1;
	public ImageView body[] = new ImageView[length];
	public double tempX;
	public double tempY;
	
	public void init(int x, int y) {
		body[0].setX(x);
		body[0].setY(y);
		body[0].setOpacity(1);
		body[0].setRotate(45);
	}
	
	public SnakeBody() {
		addPart();
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
		length++;
		return imageView;
	}
	
	public void move(double x, double y) {
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
	
	public boolean suicide(int vectorX, int vectorY) {
		for (int i = 1; i < body.length; i++) {
			if (body[0].getX() + vectorX == body[i].getX()
					&& body[0].getY() + vectorY == body[i].getY()) {
				return true;
			}
		}
		return false;
	}
	
	public void die() {
		for (int i = 0; i < body.length; i++) {
			body[i].setOpacity(0.65);
		}
	}
	
	public void destroy() {
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
}
