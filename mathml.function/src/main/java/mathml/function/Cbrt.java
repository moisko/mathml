package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

public class Cbrt extends AbstractFunctionType {

	private static final long serialVersionUID = 1L;

	public Cbrt(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public Cbrt(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	@Override
	public Double getResult() throws IllegalArgumentException, ArithmeticException {
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No argument specified for function Function.CBRT");
		} else {
			Double argument = arguments.get(0);

			return Math.cbrt(argument);
		}
	}

	@Override
	public int getType() {
		return Function.CBRT;
	}
}
