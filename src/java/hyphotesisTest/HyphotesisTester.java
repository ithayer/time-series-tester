package hyphotesisTest;

import java.util.List;

public interface HyphotesisTester {
	
	/**
	 * test hyphotesis that the firt period ( timeIndex < timeIndexTransition ) is differnt from second
	 * perdiod (timeIndex >= timeIndexTransition)
	 * @param series
	 * @param timeIndex the first day of the second period to be tested
	 * @return the result of the test
	 */
	HyphotesisResult test(List<TimePoint> series, int timeIndexTransition);
	
	/**
	 * 
	 * @param firstGroup
	 * @param secondGroup
	 * @return
	 */
	HyphotesisResult test(List<List<TimePoint>> firstGroup, List<List<TimePoint>> secondGroup);
	
	
}
