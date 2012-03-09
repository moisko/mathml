package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Operation;
import mathml.api.State;

public class Minus extends AbstractOperationType {
	private static final long serialVersionUID = 1L;

	private final State minusState;

	public Minus(List<Double> arguments) {
		this(arguments, State.FIRST_OPERAND, new ArrayList<Integer>());
	}

	public Minus(List<Double> arguments, State minusState, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);

		this.minusState = minusState;
	}

	public int getType() {
		return Operation.MINUS;
	}

	public Double getResult() throws IllegalArgumentException {
		Double arg1 = 0.0;
		Double arg2 = 0.0;
		Double result = 0.0;

		if (arguments.size() == 2) {
			switch (minusState) {
			case FIRST_OPERAND: {
				arg1 = arguments.get(0);
				arg2 = arguments.get(1);
			}
				break;
			case SECOND_OPERAND: {
				arg1 = arguments.get(1);
				arg2 = arguments.get(0);
			}
				break;
			}
			result = arg1 - arg2;

			return result;
		} else {
			throw new IllegalArgumentException("Illegal number of arguments: " + arguments.size()
							+ ". Two args allowed for this operation.");
		}
	}

	public State getState() {
		return minusState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((minusState == null) ? 0 : minusState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Minus other = (Minus) obj;
		if (minusState == null) {
			if (other.minusState != null) return false;
		} else if (!minusState.equals(other.minusState)) return false;
		return true;
	}
}
