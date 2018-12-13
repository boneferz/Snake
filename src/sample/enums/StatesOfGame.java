package sample.enums;

public enum StatesOfGame {
	GAMEPLAY("gameplay"), DEATH("death");
	
	private String value;
	
	StatesOfGame(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}