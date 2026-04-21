package view;

public enum DataAnalysisTimeWindow {
	ALL("All", Integer.MIN_VALUE),
	LAST_YEAR("Last year", 365),
	LAST_WEEK("Last week", 7);
	
	private String humanReadableName;
	private int days;
	
	private DataAnalysisTimeWindow(String humanReadableName, int days) {
		this.humanReadableName = humanReadableName;
		this.days = days;
	}
	
	public String getHumanReadableName() {
		return this.humanReadableName;
	}
	
	public int getDays() {
		return this.days;
	}
	
	public String toString() {
		return this.getHumanReadableName();
	}
}
