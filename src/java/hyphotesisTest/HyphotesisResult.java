package hyphotesisTest;

public class HyphotesisResult {

	private final double pValue;
	private final boolean firstPeriodHigher;
	private Double averageBefore = null;
	private Double averageAfter = null;
	
	
	public HyphotesisResult(boolean firstPeriodHigher, double pValue ){
		this.pValue = pValue;
		this.firstPeriodHigher = firstPeriodHigher;
	}
	
	public HyphotesisResult(boolean firstPeriodHigher, double pValue, double averageBefore, double averageAfter ){
		this.pValue = pValue;
		this.firstPeriodHigher = firstPeriodHigher;
		this.averageBefore = averageBefore;
		this.averageAfter = averageAfter;
	}
	
	@Override
	public String toString(){
		StringBuffer out = new StringBuffer("");
		if (firstPeriodHigher){
			out.append("the first perdiod is higher with a p value of "+pValue);
		}else{
			out.append("the second perdiod is higher with a p value of "+pValue);
		}
		if (averageBefore != null){
			out.append("\tAverage of first period is: "+averageBefore+" Average of second period is: "+averageAfter);
		}
		return out.toString();
	}
}
