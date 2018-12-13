package sample.data;

public class LevelCreator {

// archive
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
		
		Image w = new Image("sample/_res/apple.png");
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
}
