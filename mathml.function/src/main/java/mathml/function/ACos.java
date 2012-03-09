package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

public class ACos extends AbstractFunctionType {
	private static final long serialVersionUID = 1L;

	public ACos(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public ACos(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	@Override
	public Double getResult() throws IllegalArgumentException {
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No argument specified for function Function.ACOS");
		} else {
			Double argument = arguments.get(0);

			return Math.acos(argument);
		}
	}

	@Override
	public int getType() {
		return Function.ACOS;
	}
}
