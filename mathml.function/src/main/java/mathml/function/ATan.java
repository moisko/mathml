package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

public class ATan extends AbstractFunctionType {
	private static final long serialVersionUID = 1L;

	public ATan(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public ATan(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);
	}

	@Override
	public Double getResult() throws IllegalArgumentException, ArithmeticException {
		if (arguments.isEmpty()) {
			throw new IllegalArgumentException("No argument specified for function Function.ATAN");
		} else {
			Double argument = arguments.get(0);

			return Math.atan(argument);
		}
	}

	@Override
	public int getType() {
		return Function.ATAN;
	}
}
