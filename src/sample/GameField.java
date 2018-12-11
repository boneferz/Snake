package sample;

public class GameField {
	final int pixel = 15;
	final int widthGameField = 19;
	final int heightGameField = 16;
	final int step = pixel + 1;
	
	final int borderTop = 0;
	final int borderLeft = 0;
	final int borderBottom = heightGameField * step;
	final int borderRight = widthGameField * step;
}
