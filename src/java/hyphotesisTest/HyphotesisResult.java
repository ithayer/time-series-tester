package hyphotesisTest;

public class HyphotesisResult {

	private final double pValue;
	private final boolean firstPeriodHigher;
	
	public HyphotesisResult(boolean firstPeriodHigher, double pValue ){
		this.pValue = pValue;
		this.firstPeriodHigher = firstPeriodHigher;
	}
	
	@Override
	public String toString(){
		if (firstPeriodHigher){
			return "the first perdiod is higher with a p value of "+pValue;
		}else{
			return "the second perdiod is higher with a p value of "+pValue;
		}
	}
}
