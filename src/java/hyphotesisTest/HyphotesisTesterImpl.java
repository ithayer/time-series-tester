package hyphotesisTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HyphotesisTesterImpl implements HyphotesisTester {

	private final static int DEFAULT_RUNS = 10000;
	private final static Random RAND = new Random();

	private int runs = DEFAULT_RUNS;

	@Override
	public HyphotesisResult test(List<TimePoint> series, int timeIndexTransition) {

		int pointsBefore = 0;
		int pointsAfter = 0;
		double sumBefore = 0;
		double sumAfter = 0;

		for (TimePoint point : series) {
			if (point.getTimeIndex() < timeIndexTransition) {
				pointsBefore++;
				sumBefore += point.getValue();
			} else {
				pointsAfter++;
				sumAfter += point.getValue();
			}
		}
		if (pointsBefore == 0 || pointsAfter == 0) {
			throw new IllegalArgumentException(
					"one of the 2 periods (before/after) is empty");
		}

		double averageBefore = sumBefore / pointsBefore;
		double averageAfter = sumAfter / pointsAfter;

		double diff = averageBefore - averageAfter;
		double total = sumBefore + sumAfter;
		final boolean firstPeriodHigher = averageBefore > averageAfter;

		int moreExtremeSample = 0; // the number of samples that have a
									// difference higher than the one in the
									// given data

		final int dataSize = series.size();
		// we randomly sample a set of point equalt to points before (TODO this
		// can be optimized to pick a set with the smallest numbe of samples)
		for (int run = 0; run < runs; run++) {
			Set<Integer> indecesUsed = new HashSet<Integer>();

			double randomSumBefore = 0;

			while (indecesUsed.size() < pointsBefore) {
				int sampleIndex = RAND.nextInt(dataSize);
				if (!indecesUsed.contains(sampleIndex)) {
					indecesUsed.add(sampleIndex);
					randomSumBefore += series.get(sampleIndex).getValue();
				}
			}

			double randomSumAfter = total - randomSumBefore;

			double randomAverageBefore = randomSumBefore / pointsBefore;
			double randomAverageAfter = randomSumAfter / pointsAfter;

			double randomDiff = randomAverageBefore - randomAverageAfter;

			if (firstPeriodHigher) {
				if (randomDiff > diff) {
					moreExtremeSample++;
				}
			} else {
				if (randomDiff < diff) {
					moreExtremeSample++;
				}
			}
		}
		double pValue = ((double) moreExtremeSample) / ((double) runs);
		return new HyphotesisResult(firstPeriodHigher, pValue, averageBefore,
				averageAfter);
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}

	public int getRuns() {
		return runs;
	}

	public static final void main(String[] args) {
		HyphotesisTester tester = new HyphotesisTesterImpl();

		HyphotesisResult result = tester.test(FakeSeries.getFakeData(),
				FakeSeries.getTransitionPoint());

		System.out.println(result);
	}

	@Override
	public HyphotesisResult test(List<List<TimePoint>> firstGroup,
			List<List<TimePoint>> secondGroup) {

		List<TimePoint> allUsers = new ArrayList<TimePoint>(); // alle theusers
																// group a
																// before and
																// goup b after

		int index = 0;
		for (List<TimePoint> user : firstGroup) {
			allUsers.add(aggregate(user, index));
			index++;
		}
		for (List<TimePoint> user : secondGroup) {
			allUsers.add(aggregate(user, index));
			index++;
		}
		return this.test(allUsers, firstGroup.size());
	}

	private static TimePoint aggregate(List<TimePoint> user, int index) {
		double value = 0.0;
		for (TimePoint point : user) {
			value += point.getValue();
		}
		return new TimePoint(index, value);
	}

	@Override
	public HyphotesisResult test(List<List<TimePoint>> firstGroup,
			List<List<TimePoint>> secondGroup, int timeIndexTransition) {

		Groups initialGroups = new Groups(firstGroup, secondGroup);
		double rr = getRatioOfRatio(initialGroups, timeIndexTransition);
		boolean firstPeriodHigher = rr > 1;

		int moreExtreme = 0;

		for (int run = 0; run < runs; run++) {
			Groups shuffledGroup = shuffle(initialGroups, timeIndexTransition);
			double sampleRR = getRatioOfRatio(shuffledGroup,
					timeIndexTransition);
			if (firstPeriodHigher) {
				if (sampleRR > rr) {
					moreExtreme++;
				}
			} else {
				if (sampleRR < rr) {
					moreExtreme++;
				}
			}
		}

		double pValue = ((double) moreExtreme) / runs;
		return new HyphotesisResult(firstPeriodHigher, pValue);
	}

	private static double getRatioOfRatio(Groups groups, int timeIndexTransition) {
		double sumABefore = 0;
		double sumBBefore = 0;
		double sumAAfter = 0;
		double sumBAfter = 0;
		
		Integer intervalsBefore = null;
		int intervalsAfter = 0;
		
		for (List<TimePoint> groupA : groups.firstGroup){
			int tb = 0;
			int ta = 0;
			for (TimePoint point : groupA){
				if (point.getTimeIndex() < timeIndexTransition){
					sumABefore += point.getValue();
					tb++;
				}else{
					sumAAfter += point.getValue();
					ta++;
				}
			}
			if (intervalsBefore == null){
				intervalsBefore = tb;
				intervalsAfter = ta;
			}else{
				if (intervalsBefore != tb){
					throw new IllegalArgumentException("problem with the data");
				}
				if (intervalsAfter != ta){
					throw new IllegalArgumentException("problem with the data");
				}
			}
		}
		
		for (List<TimePoint> groupB : groups.secondGroup){
			int tb = 0;
			int ta = 0;
			for (TimePoint point : groupB){
				if (point.getTimeIndex() < timeIndexTransition){
					sumBBefore += point.getValue();
					tb++;
				}else{
					sumBAfter += point.getValue();
					ta++;
				}
			}
			if (intervalsBefore != tb){
				throw new IllegalArgumentException("problem with the data");
			}
			if (intervalsAfter != ta){
				throw new IllegalArgumentException("problem with the data");
			}
		}
		
		double out = (sumABefore/sumBBefore)/(sumBAfter/sumAAfter);
		
		return out;
	}

	private static Groups shuffle(Groups groups, int timeIndexTransition) {
		//we shuffle the user and then we shuffle 
		
		List<List<TimePoint>> allUsers = new ArrayList<List<TimePoint>>();
		allUsers.addAll(groups.firstGroup);
		allUsers.addAll(groups.secondGroup);
		
		List<List<TimePoint>> newGroupA = new ArrayList<List<TimePoint>>();
		List<List<TimePoint>> newGroupB = new ArrayList<List<TimePoint>>();
		
		Set<Integer> usedIndeces = new HashSet<Integer>();
		final int size = allUsers.size();
		final int sizeA = groups.firstGroup.size();
		while (newGroupA.size() < sizeA){
			int sampleIndex = RAND.nextInt(size);
			if (!usedIndeces.contains(sampleIndex)){
				usedIndeces.add(sampleIndex);
				newGroupA.add(allUsers.get(sampleIndex));
			}
		}
		newGroupB.addAll(allUsers);
		newGroupB.removeAll(newGroupA);
		
		List<List<TimePoint>> newGroupAShuffledDays = new ArrayList<List<TimePoint>>();
		List<List<TimePoint>> newGroupBShuffledDays = new ArrayList<List<TimePoint>>();
		
		for (List<TimePoint>  userPoint : newGroupA){
			newGroupAShuffledDays.add(shuffle(userPoint));
		}
		for (List<TimePoint>  userPoint : newGroupB){
			newGroupBShuffledDays.add(shuffle(userPoint));
		}
		
		return new Groups(newGroupAShuffledDays, newGroupBShuffledDays);
	}
	
	private static List<TimePoint> shuffle(List<TimePoint> points){
		List<TimePoint> out = new ArrayList<TimePoint>();
		
		List<Double> values = new ArrayList<Double>();
		for (TimePoint point : points){
			values.add(point.getValue());
		}
		
		for (TimePoint point : points){
			int pos = RAND.nextInt(values.size());
			out.add(new TimePoint(point.getTimeIndex(),values.get(pos)));
			values.remove(pos);
		}
		return out;
	}

	private static class Groups {
		public List<List<TimePoint>> firstGroup;
		public List<List<TimePoint>> secondGroup;

		public Groups(List<List<TimePoint>> firstGroup,
				List<List<TimePoint>> secondGroup) {
			this.firstGroup = firstGroup;
			this.secondGroup = secondGroup;
		}

	}

}
