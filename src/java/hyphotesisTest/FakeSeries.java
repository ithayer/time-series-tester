package hyphotesisTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeSeries {
	
	public static int size = 1000;
	private static int transitionPoint = size /2;
	
	private final static Random RAND = new Random();
	
	public static List<TimePoint> getFakeData(){
		List<TimePoint> series = new ArrayList<TimePoint>();
		
		for (int i =0 ; i < size; i++){
			if (i < transitionPoint){
				series.add(new TimePoint(i, RAND.nextDouble()));
			}else{
				series.add(new TimePoint(i, RAND.nextDouble()+0.02));
			}
		}
		return series;
	}
	
	public static int getTransitionPoint(){
		return transitionPoint; // doesn't have to be in the middle
	}
}
