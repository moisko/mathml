package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Operation;

public class Plus extends AbstractOperationType {
	private static final long serialVersionUID = 1L;

	public Plus(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public Plus(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	public int getType() {
		return Operation.PLUS;
	}

	public Double getResult() throws IllegalArgumentException {
		Double sum = 0.0;
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No arguments specified for operation: " + this.toString());
		} else {
			for (Double argument : arguments) {
				sum += argument;
			}

			return sum;
		}
	}
}
