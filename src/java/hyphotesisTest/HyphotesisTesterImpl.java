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

		return 0;
	}

	private static Groups shuffle(Groups groups, int timeIndexTransition) {

		return null;
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
