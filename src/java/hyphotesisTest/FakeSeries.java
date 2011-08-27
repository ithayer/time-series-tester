package hyphotesisTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeSeries {
	
	public static int size = 10;
	
	private final static Random RAND = new Random();
	
	public static List<TimePoint> getFakeData(){
		List<TimePoint> series = new ArrayList<TimePoint>();
		
		for (int i =0 ; i < size; i++){
			series.add(new TimePoint(i, RAND.nextDouble()));
		}
		return series;
	}
}
