package client.boards;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.util.Duration;

public class Transition {
	/**
	 * @param
	 * start: 0.0~width
	 * end: 0.0~width
	 */		
	public static void slide(Parent parent, double start, double end) {
		parent.setTranslateX(start);
		Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(parent.translateXProperty(), end);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
	}
	
	public static void slide(Parent parent, double start, double end, EventHandler<ActionEvent> onFinish) {
		parent.setTranslateX(start);
		Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(parent.translateXProperty(), end);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), onFinish, keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
	}

	/**
	 * @param
	 * start: 0.0~1.0
	 * end: 0.0~1.0
	 */	
	public static void fade(Parent parent, double start, double end) {
		parent.setOpacity(start);
		Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(parent.opacityProperty(), end);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
	}
	
	public static void fade(Parent parent, double start, double end, EventHandler<ActionEvent> onFinish) {
		parent.setOpacity(start);
		Timeline timeline = new Timeline();
		KeyValue keyValue = new KeyValue(parent.opacityProperty(), end);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(100), onFinish, keyValue);
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}		
}
