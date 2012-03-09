package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

public class Sqrt extends AbstractFunctionType {
	private static final long serialVersionUID = 1L;

	public Sqrt(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public Sqrt(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	public int getType() {
		return Function.SQRT;
	}

	public Double getResult() throws IllegalArgumentException {
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No argument specified for function Function.SQRT");
		} else if (arguments.size() == 1) {
			Double argument = arguments.get(0);
			// check for negative arguments
			if (argument < 0) {
				throw new ArithmeticException("Argument must be greater than 0");
			} else {
				return Math.sqrt(argument);
			}
		} else {
			throw new IllegalArgumentException("No argument specified for operation Function.SQRT");
		}
	}
}
