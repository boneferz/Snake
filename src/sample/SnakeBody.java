package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SnakeBody {
	private int length = 1;
	public ImageView body[] = new ImageView[length];
	public double tempX;
	public double tempY;
	
	public SnakeBody(double x, double y) {
		tempX = x;
		tempY = y;
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
		imageView.setOpacity(0.75);
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
				body[0].setX(x);
				body[0].setY(y);
			} else {
				body[i].setX(localTempPreviousX);
				body[i].setY(localTempPreviousY);
			}
			localTempPreviousX = localTempCurrentX;
			localTempPreviousY = localTempCurrentY;
		}
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
