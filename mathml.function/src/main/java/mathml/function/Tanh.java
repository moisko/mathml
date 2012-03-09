package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

public class Tanh extends AbstractFunctionType {

	private static final long serialVersionUID = 1L;

	public Tanh(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public Tanh(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	@Override
	public Double getResult() throws IllegalArgumentException {
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No argument specified for function Function.TANH");
		} else {
			Double argument = arguments.get(0);

			return Math.tanh(argument);
		}
	}

	@Override
	public int getType() {
		return Function.TANH;
	}
}
