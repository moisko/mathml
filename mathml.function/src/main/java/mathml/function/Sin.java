package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

public class Sin extends AbstractFunctionType {
	private static final long serialVersionUID = 1L;

	public Sin(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public Sin(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	public int getType() {
		return Function.SIN;
	}

	public Double getResult() throws IllegalArgumentException {
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No argument specified for function Function.SIN");
		} else {
			Double argument = arguments.get(0);

			return Math.sin(argument);
		}
	}
}
