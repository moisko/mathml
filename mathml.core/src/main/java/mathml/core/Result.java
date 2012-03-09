package mathml.core;

import java.util.LinkedList;
import java.util.List;

public class Result {

	private final LinkedList<Double> results;

	public Result(LinkedList<Double> results) {
		this.results = results;
	}

	public List<Double> getResults() {
		return results;
	}

	public Double getFirstResult() {
		return results.getFirst();
	}

	public Double getLastResult() {
		return results.getLast();
	}

	public Double getResult(int index) {
		return results.get(index);
	}

	public boolean isEmpty() {
		return results.isEmpty();
	}
}
