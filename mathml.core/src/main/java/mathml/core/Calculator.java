package mathml.core;

import java.util.ArrayList;
import java.util.List;

import mathml.api.ICalculate;
import mathml.api.IMathMLExpression;
import mathml.api.MathematicalOperation;

public class Calculator implements ICalculate {
	
	@Override
	public Double calculate(IMathMLExpression expression) throws IllegalArgumentException, ArithmeticException {
		List<Double> buffer = new ArrayList<Double>();
		
		List<MathematicalOperation> operands = expression.getExpressionOperands();

		for (int i = 0; i < operands.size(); i++) {
			MathematicalOperation mathematicalOperation = operands.get(i);
			List<Integer> argsPosition = mathematicalOperation.getArgsDistanceFromMathOperation();
			if (!argsPosition.isEmpty()) {
				for (Integer relativePosition : argsPosition) {
					int argPosition = i - relativePosition;

					Double arg = buffer.get(argPosition);

					mathematicalOperation.setArgument(arg);
				}
			}
			
			// Calculate the result
			Double result = mathematicalOperation.getResult();
			// add this result to buffer list
			buffer.add(result);
		}

		Double finalResult = new Double(buffer.get(buffer.size() - 1));

		return finalResult;
	}

}
