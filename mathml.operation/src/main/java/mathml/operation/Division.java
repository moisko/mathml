package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Operation;
import mathml.api.State;

public class Division extends AbstractOperationType {
	private static final long serialVersionUID = 1L;

	private final State divisionState;

	public Division(List<Double> arguments) {
		this(arguments, State.FIRST_OPERAND, new ArrayList<Integer>());
	}

	public Division(List<Double> arguments, State divisionState, List<Integer> nestedApplyElementsNumberList) {
		super(arguments, nestedApplyElementsNumberList);

		this.divisionState = divisionState;
	}

	public int getType() {
		return Operation.DIVISION;
	}

	public Double getResult() throws IllegalArgumentException, ArithmeticException {
		Double nominator = 0.0;
		Double denominator = 0.0;
		Double divResult = 0.0;

		if (arguments.size() == 2) {
			switch (divisionState) {
			case FIRST_OPERAND: {
				nominator = arguments.get(0);
				denominator = arguments.get(1);
			}
				break;
			case SECOND_OPERAND: {
				nominator = arguments.get(1);
				denominator = arguments.get(0);
			}
				break;
			}

			// check the denominator value
			if (denominator != 0.0) {
				divResult = nominator / denominator;
				return divResult;
			} else {
				throw new ArithmeticException("Cannot divide by zero");
			}
		} else {
			throw new IllegalArgumentException("Illegal number of arguments: " + arguments.size()
							+ ". Two args allowed for this operation.");
		}
	}

	public State getState() {
		return divisionState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((divisionState == null) ? 0 : divisionState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Division other = (Division) obj;
		if (divisionState == null) {
			if (other.divisionState != null) return false;
		} else if (!divisionState.equals(other.divisionState)) return false;
		return true;
	}
}
