package hyphotesisTest;

import java.util.List;

public interface HyphotesisTester {

	HyphotesisResult test(List<TimePoint> series);
	
	
}
