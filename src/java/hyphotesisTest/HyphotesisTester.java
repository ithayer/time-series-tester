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
	 * checks if there is any significant difference between the 2 group of users in terms of their aggragate value
	 * @param firstGroup
	 * @param secondGroup
	 * @return
	 */
	HyphotesisResult test(List<List<TimePoint>> firstGroup, List<List<TimePoint>> secondGroup);
	
	
	
	/**
	 * checks if there is any difference in between period a and b
	 * of ( (sum_groupa_before) /(sum_groupb_before))/((sum_groupb_after)/( sum_groupa_after))
	 * that cannot be explained by randomization over user and days
	 * NOTE:  all users needs to have the same set of time indeces
	 * @param firstGroup
	 * @param secondGroup
	 * @param timeIndexTransition
	 * @return
	 */
	HyphotesisResult test(List<List<TimePoint>> firstGroup, List<List<TimePoint>> secondGroup, int timeIndexTransition);
}
