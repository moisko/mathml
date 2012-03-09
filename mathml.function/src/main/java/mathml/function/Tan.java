package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

public class Tan extends AbstractFunctionType {
	private static final long serialVersionUID = 1L;

	public Tan(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public Tan(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	public int getType() {
		return Function.TAN;
	}

	public Double getResult() throws IllegalArgumentException {
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No argument specified for function Function.TAN");
		} else {
			Double argument = new Double(arguments.get(0));
			return Math.tan(argument);
		}
	}
}
