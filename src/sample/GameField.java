package sample;

public final class GameField {
	final int pixel = 15;
	final int widthGameField = 19;
	final int heightGameField = 16;
	final int step = pixel + 1;
	
	final int borderTop = 0;
	final int borderLeft = 0;
	final int borderBottom = heightGameField * step;
	final int borderRight = widthGameField * step;
	
	
	public String[][] gameEmptyPlaceMap = new String[heightGameField][widthGameField];
	
	{
		for (int i = 0; i < gameEmptyPlaceMap.length; i++) {
			for (int j = 0; j < gameEmptyPlaceMap[0].length; j++) {
				gameEmptyPlaceMap[i][j] = "-";
			}
		}
	}
	
	public void print() {
		for (int i = 0; i < gameEmptyPlaceMap.length; i++) {
			for (int j = 0; j < gameEmptyPlaceMap[0].length; j++) {
				System.out.print(gameEmptyPlaceMap[i][j] + " ");
			}
			System.out.println("");
		}
	}
}
