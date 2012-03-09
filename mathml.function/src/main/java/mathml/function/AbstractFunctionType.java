package mathml.function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mathml.api.Function;

public abstract class AbstractFunctionType implements Function {

	private static final long serialVersionUID = 1L;

	protected final List<Integer> nestedApplyElementsNumberList;

	protected final List<Double> arguments;

	public AbstractFunctionType(List<Double> arguments) {
		this(arguments, new ArrayList<Integer>());
	}

	public AbstractFunctionType(List<Double> arguments, List<Integer> nestedApplyElementsNumberList) {
		this.arguments = arguments;

		this.nestedApplyElementsNumberList = nestedApplyElementsNumberList;
	}

	public final List<Integer> getArgsDistanceFromMathOperation() {
		List<Integer> argsPositionList = new ArrayList<Integer>();
		// check if nestedApplyElementsNumberList is empty -> if isEmpty() == true => return an empty argsPositionList
		if (nestedApplyElementsNumberList.isEmpty()) {
			return argsPositionList;
		} else {
			List<Object> argList = new ArrayList<Object>();
			// get the number of nested <apply> tags
			int argsNumber = nestedApplyElementsNumberList.size() - 1;
			for (int i = argsNumber; i >= 0; i--) {
				argList.add(new FunctionArg("Arg", i));
			}
			// reverse the elements of nestedApplyElementsNumberList
			Collections.reverse(nestedApplyElementsNumberList);
			int lastArgPosition = 0;
			for (int i = 0; i < nestedApplyElementsNumberList.size(); i++) {
				int applyElementsNumber = nestedApplyElementsNumberList.get(i);
				// check if number is != or == to 0
				if (applyElementsNumber == 0) {
					if (i == 0) {
						// no right shift from the original position
						argsPositionList.add(i);
						lastArgPosition = i;
						continue;
					} else {
						// right shift with one position
						lastArgPosition++;
						argsPositionList.add(lastArgPosition);
					}
				} else {
					if (i == 0) {
						for (int j = 0; j < applyElementsNumber; j++) {
							argList.add(j, -1);
						}
						lastArgPosition = applyElementsNumber++;
						// add the position of this argument to argPositionList
						argsPositionList.add(lastArgPosition);
					} else {
						int beginIndex = lastArgPosition + 1;
						int lastIndex = beginIndex + applyElementsNumber;
						for (int j = beginIndex; j < lastIndex; j++) {
							argList.add(j, -1);
						}
						lastArgPosition = lastIndex;
						// add the position of this argument to argPositionList
						argsPositionList.add(lastArgPosition);
					}
				}
			}
			// calculate the distance from the parent function
			int mainOperationPosition = argList.size();
			// get the position of each argument in reverse order
			for (int i = 0; i < argsPositionList.size(); i++) {
				int argPosition = argsPositionList.get(i);
				int argDistance = mainOperationPosition - argPosition;
				argsPositionList.set(i, argDistance);
			}
		}

		return argsPositionList;
	}

	public List<Double> getArguments() {
		return arguments;
	}

	public List<Integer> getNestedApplyElementsNumberList() {
		return nestedApplyElementsNumberList;
	}

	public void setArgument(Double arg) {
		arguments.add(arg);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((nestedApplyElementsNumberList == null) ? 0 : nestedApplyElementsNumberList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AbstractFunctionType other = (AbstractFunctionType) obj;
		if (arguments == null) {
			if (other.arguments != null) return false;
		} else if (!arguments.equals(other.arguments)) return false;
		if (nestedApplyElementsNumberList == null) {
			if (other.nestedApplyElementsNumberList != null) return false;
		} else if (!nestedApplyElementsNumberList.equals(other.nestedApplyElementsNumberList)) return false;
		return true;
	}

}
