package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Operation;

public class Multiplication extends AbstractOperationType {
	private static final long serialVersionUID = 1L;

	public Multiplication(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public Multiplication(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	public int getType() {
		return Operation.MULTIPLICATION;
	}

	public Double getResult() throws IllegalArgumentException {
		Double result = 1.0;
		if (!arguments.isEmpty()) {
			for (Double argument : arguments) {
				result *= argument;
			}
			return result;
		} else {
			throw new IllegalArgumentException("No arguments specified for operation: " + this.toString());
		}
	}
}
