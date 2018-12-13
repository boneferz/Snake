package sample.data;

public final class GameField {
	private static final GameField instance = new GameField();
	
	public final int pixel = 15;
	public final int widthGameField = 19;
	public final int heightGameField = 16;
	public final int step = pixel + 1;
	
	public final int borderTop = 0;
	public final int borderLeft = 0;
	public final int borderBottom = heightGameField * step;
	public final int borderRight = widthGameField * step;
	
	public String[][] gameEmptyPlaceMap = new String[heightGameField][widthGameField];
	
	
	
	public GameField() {
		for (int i = 0; i < gameEmptyPlaceMap.length; i++) {
			for (int j = 0; j < gameEmptyPlaceMap[0].length; j++) {
				gameEmptyPlaceMap[i][j] = "-";
			}
		}
	}
	
	public static GameField getInstance() {
		return instance;
	}
	
	public void print() {
		for (int i = 0; i < gameEmptyPlaceMap.length; i++) {
			for (int j = 0; j < gameEmptyPlaceMap[0].length; j++) {
				System.out.print(gameEmptyPlaceMap[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println("");
		
	}
}
