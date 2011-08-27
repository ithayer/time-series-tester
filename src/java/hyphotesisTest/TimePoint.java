package hyphotesisTest;

public class TimePoint {

	private final double value;
	private final int timeIndex;
	
	public  TimePoint(int timeIndex, double value){
		this.timeIndex = timeIndex;
		this.value =value;
	}
	
	public int getTimeIndex() {
		return timeIndex;
	}
	
	public double getValue() {
		return value;
	}
	
}
