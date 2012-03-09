package mathml.utils;

import java.util.HashMap;
import java.util.Map;

import mathml.api.Function;
import mathml.api.Operation;

public class MathOperationTypes {
	private static final MathOperationTypes MATH_OPERATION_TYPES = new MathOperationTypes();

	private final Map<String, Integer> mathOperationTypes = new HashMap<String, Integer>();// includes operation and function types

	private MathOperationTypes() {
		this.initMathOperationTypes();
	}

	public static MathOperationTypes newInstance() {
		return MATH_OPERATION_TYPES;
	}

	public int getMathOperationTypeFor(String operationName) {
		Integer mathOperationType = mathOperationTypes.get(operationName);
		if (mathOperationType == null) {
			String message = operationName + " is NOT a supported MathML operation.";

			throw new UnsupportedOperationException(message);// TODO check if this is the correct exception to be thrown
		}
		
		return mathOperationType;
	}

	private void initMathOperationTypes() {
		// ContentMarkup Function types
		mathOperationTypes.put("root", Function.SQRT);
		mathOperationTypes.put("sin", Function.SIN);
		mathOperationTypes.put("cos", Function.COS);
		mathOperationTypes.put("cosh", Function.COSH);
		mathOperationTypes.put("arccosh", Function.COSH);
		mathOperationTypes.put("power", Function.POWER);
		mathOperationTypes.put("tan", Function.TAN);
		mathOperationTypes.put("abs", Function.ABS);
		mathOperationTypes.put("arccos", Function.ACOS);
		mathOperationTypes.put("arcsin", Function.ASIN);
		mathOperationTypes.put("arctan", Function.ATAN);
		mathOperationTypes.put("ceiling", Function.CEIL);
		mathOperationTypes.put("tanh", Function.TANH);
		
		// PresentationMarkup Function types
		mathOperationTypes.put("mroot", Function.MROOT);
		mathOperationTypes.put("msqrt", Function.MSQRT);

		// Operation types
		mathOperationTypes.put("plus", Operation.PLUS);
		mathOperationTypes.put("minus", Operation.MINUS);
		mathOperationTypes.put("divide", Operation.DIVISION);
		mathOperationTypes.put("times", Operation.MULTIPLICATION);
	}
}
