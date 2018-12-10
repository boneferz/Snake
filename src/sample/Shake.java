package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {
	private static Node shakeObject;
	private static Timeline shakeTimeline = new Timeline();
	private final int SHAKE_DURATION = 4;
	private int shakeDuration = SHAKE_DURATION;
	private int shakeSpeedDuration = 100;
	private int shakePower = 2;
	
	public Shake() {
		shakeTimeline.setCycleCount(Timeline.INDEFINITE);
		shakeTimeline.setOnFinished(this::onShake);
		shakeTimeline.getKeyFrames().add(new KeyFrame(
				Duration.millis(shakeSpeedDuration),
				shakeTimeline.getOnFinished()));
	}
	
	public static void toShake(Node node) {
		shakeObject = node;
		shakeTimeline.play();
	}
	
	private void onShake(ActionEvent a) {
		shakeDuration--;
		System.out.println("  |shake|" + shakeDuration);
		
		if (shakeDuration % 2 == 0)
			shakeObject.setLayoutX(shakeObject.getLayoutX() + shakePower);
		else
			shakeObject.setLayoutX(shakeObject.getLayoutX() - shakePower);
		
		if (shakeDuration == 0) {
			shakeTimeline.stop();
			
			shakeDuration = SHAKE_DURATION;
			shakeObject.setLayoutX(0);
			
			System.out.println("");
			return;
		}
	}
}
