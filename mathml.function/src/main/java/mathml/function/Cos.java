package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

public class Cos extends AbstractFunctionType {
	private static final long serialVersionUID = 1L;

	public Cos(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public Cos(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	public int getType() {
		return Function.COS;
	}

	public Double getResult() throws IllegalArgumentException {
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No argument specified for function Function.COS");
		} else {
			Double argument = arguments.get(0);

			return Math.cos(argument);
		}
	}
}
