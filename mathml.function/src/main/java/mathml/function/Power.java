package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;
import mathml.api.State;

public class Power extends AbstractFunctionType {
	private static final long serialVersionUID = 1L;

	private final State powerState;

	public Power(List<Double> arguments) {
		this(arguments, State.FIRST_OPERAND, new ArrayList<Integer>());
	}

	public Power(List<Double> arguments, State powerState, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);

		this.powerState = powerState;
	}

	public int getType() {
		return Function.POWER;
	}

	public Double getResult() throws IllegalArgumentException {
		Double arg1 = 0.0;
		Double arg2 = 0.0;
		Double result = 0.0;
		if (arguments.size() == 2) {
			if (powerState == State.FIRST_OPERAND) {
				arg1 = arguments.get(0);
				arg2 = arguments.get(1);
			} else if (powerState == State.SECOND_OPERAND) {
				arg1 = arguments.get(1);
				arg2 = arguments.get(0);
			}
			result = Math.pow(arg1, arg2);
			return result;
		} else {
			throw new IllegalArgumentException("Illegal number of arguments: " + arguments.size() + ". Two args allowed for this function.");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((powerState == null) ? 0 : powerState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Power other = (Power) obj;
		if (powerState == null) {
			if (other.powerState != null) return false;
		} else if (!powerState.equals(other.powerState)) return false;
		return true;
	}

	public State getState() {
		return powerState;
	}
}
