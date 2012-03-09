package mathml.transform;

import java.io.FileNotFoundException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import mathml.api.Function;
import mathml.api.IMathMLExpression;
import mathml.api.MathMLConstants;
import mathml.api.MathematicalOperation;
import mathml.api.Operation;
import mathml.api.State;
import mathml.function.Cos;
import mathml.function.Cosh;
import mathml.function.Power;
import mathml.function.Sin;
import mathml.function.Sqrt;
import mathml.function.Tan;
import mathml.function.Tanh;
import mathml.operation.Division;
import mathml.operation.Minus;
import mathml.operation.Multiplication;
import mathml.operation.Plus;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MathMLTransformerImpl extends MathMLTransformer {
	private static final String ENCODING = "utf-8";

	private final Document mathMLDoc;

	private final Element mathMLRootElement;

	private List<MathematicalOperation> operands;

	public MathMLTransformerImpl() throws ParserConfigurationException {
		// create xml DOM document
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		mathMLDoc = impl.createDocument("", MathMLConstants.MML_MATH, null);

		// add attributes to mathMLRootElement
		mathMLRootElement = mathMLDoc.getDocumentElement();
		mathMLRootElement.setAttribute("xmlns:mml", MathMLConstants.MATHML_NAMESPACE_URI);
		mathMLRootElement.setAttribute("xmlns:xsi", MathMLConstants.XSI_NAMESPACE_URI);
		mathMLRootElement.setAttribute("xsi:schemaLocation", MathMLConstants.SCHEMA_LOCATION_URI);
	}

	@Override
	public void transform(IMathMLExpression expression, Writer out) throws FileNotFoundException, TransformerException,
					MalformedMathMLExpressionException {
		operands = expression.getExpressionOperands();

		MathematicalOperation parentOperand = operands.get(operands.size() - 1);
		int parentOperandPosition = operands.size() - 1;

		Element parentApplyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);

		if (parentOperand instanceof Operation) {
			Operation parentOperation = (Operation) parentOperand;
			List<Integer> argsPositionList = parentOperation.getArgsDistanceFromMathOperation();
			if (argsPositionList.isEmpty()) {
				// do nothing
			} else {
				// reverse
				Collections.reverse(argsPositionList);
				for (int i = 0; i < argsPositionList.size(); i++) {
					int position = parentOperandPosition - argsPositionList.get(i);
					MathematicalOperation operand = operands.get(position);
					if (operand instanceof Operation) {
						Operation operation = (Operation) operand;
						visitOperation(parentApplyElement, parentOperation, operation, position);
					} else if (operand instanceof Function) {
						Function function = (Function) operand;
						visitFunction(parentApplyElement, parentOperation, function, position);
					}
				}
			}

			int parentOperationType = parentOperation.getType();
			switch (parentOperationType) {
			case Operation.PLUS: {
				Plus plus = (Plus) parentOperation;
				Element plusElement = createParentOperandElement(plus);
				List<Double> plusArgs = plus.getArguments();
				int plusArgsNumber = plusArgs.size();

				parentApplyElement.insertBefore(plusElement, parentApplyElement.getFirstChild());

				for (int i = 0; i < plusArgsNumber; i++) {
					Double plusArg = plusArgs.get(i);
					Element tokenElement = createTokenElement(String.valueOf(plusArg));
					tokenElement.setTextContent(String.valueOf(plusArg));

					parentApplyElement.appendChild(tokenElement);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			case Operation.MINUS: {
				Minus minus = (Minus) parentOperation;
				Element minusElement = createParentOperandElement(minus);
				State minusState = minus.getState();
				List<Double> minusArgs = minus.getArguments();
				int minusArgsNumber = minusArgs.size();
				if (minusArgsNumber == 0) {
					if (!minus.getArgsDistanceFromMathOperation().isEmpty()) {
						parentApplyElement.insertBefore(minusElement, parentApplyElement.getFirstChild());
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				} else if (minusArgsNumber == 1) {
					parentApplyElement.insertBefore(minusElement, parentApplyElement.getFirstChild());

					Double minusArg = minusArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(minusArg));
					tokenElement.setTextContent(String.valueOf(minusArg));

					if (minusState == State.FIRST_OPERAND) {
						parentApplyElement.insertBefore(tokenElement, parentApplyElement.getFirstChild().getNextSibling());
					} else if (minusState == State.SECOND_OPERAND) {
						parentApplyElement.appendChild(tokenElement);
					}
				} else if (minusArgsNumber == 2) {
					parentApplyElement.appendChild(minusElement);

					for (int i = 0; i < minusArgsNumber; i++) {
						Double minusArg = minusArgs.get(i);
						Element tokenElement = createTokenElement(String.valueOf(minusArg));
						tokenElement.setTextContent(String.valueOf(minusArg));

						parentApplyElement.appendChild(tokenElement);
					}
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			case Operation.DIVISION: {
				Division div = (Division) parentOperation;
				Element divElement = createParentOperandElement(div);
				State divState = div.getState();
				List<Double> divArguments = div.getArguments();
				int divArgsNumber = divArguments.size();
				if (divArgsNumber == 0) {
					if (!div.getArgsDistanceFromMathOperation().isEmpty()) {
						parentApplyElement.insertBefore(divElement, parentApplyElement.getFirstChild());
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				} else if (divArgsNumber == 1) {
					parentApplyElement.insertBefore(divElement, parentApplyElement.getFirstChild());

					Double divArg = divArguments.get(0);
					Element tokenElement = createTokenElement(String.valueOf(divArg));
					tokenElement.setTextContent(String.valueOf(divArg));

					if (divState == State.FIRST_OPERAND) {
						parentApplyElement.insertBefore(tokenElement, parentApplyElement.getFirstChild().getNextSibling());
					} else if (divState == State.SECOND_OPERAND) {
						parentApplyElement.appendChild(tokenElement);
					}
				} else if (divArgsNumber == 2) {
					parentApplyElement.appendChild(divElement);

					for (int i = 0; i < divArgsNumber; i++) {
						Double divArg = divArguments.get(i);
						Element tokenElement = createTokenElement(String.valueOf(divArg));
						tokenElement.setTextContent(String.valueOf(divArg));

						parentApplyElement.appendChild(tokenElement);
					}
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			case Operation.MULTIPLICATION: {
				Multiplication times = (Multiplication) parentOperation;
				List<Double> timesArgs = times.getArguments();
				int timesArgsNumber = timesArgs.size();
				Element timesElement = createParentOperandElement(times);

				parentApplyElement.insertBefore(timesElement, parentApplyElement.getFirstChild());

				for (int i = 0; i < timesArgsNumber; i++) {
					Double timesArg = timesArgs.get(i);
					Element tokenElement = createTokenElement(String.valueOf(timesArg));
					tokenElement.setTextContent(String.valueOf(timesArg));

					parentApplyElement.appendChild(tokenElement);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			}
		} else if (parentOperand instanceof Function) {
			Function parentFunction = (Function) parentOperand;
			List<Integer> argsPositionList = parentFunction.getArgsDistanceFromMathOperation();
			if (argsPositionList.isEmpty()) {
				// do nothing
			} else {
				// reverse
				Collections.reverse(argsPositionList);
				for (int i = 0; i < argsPositionList.size(); i++) {
					int position = parentOperandPosition - argsPositionList.get(i);
					Object operand = operands.get(position);
					if (operand instanceof Operation) {
						Operation operation = (Operation) operand;
						visitOperation(parentApplyElement, parentFunction, operation, position);
					} else if (operand instanceof Function) {
						Function function = (Function) operand;
						visitFunction(parentApplyElement, parentFunction, function, position);
					}
				}
			}

			int parentFunctionType = parentFunction.getType();
			switch (parentFunctionType) {
			case Function.SIN: {
				Sin sin = (Sin) parentFunction;
				Element sinElement = createParentOperandElement(sin);
				List<Double> sinArgs = sin.getArguments();
				int sinArgsNumber = sinArgs.size();
				if (sinArgsNumber == 0) {
					if (!sin.getArgsDistanceFromMathOperation().isEmpty()) {
						parentApplyElement.insertBefore(sinElement, parentApplyElement.getFirstChild());
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				} else if (sinArgsNumber == 1) {
					parentApplyElement.insertBefore(sinElement, parentApplyElement.getFirstChild());

					Double sinArg = sinArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(sinArg));
					tokenElement.setTextContent(String.valueOf(sinArg));

					parentApplyElement.appendChild(tokenElement);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			case Function.COS: {
				Cos cos = (Cos) parentFunction;
				Element cosElement = createParentOperandElement(cos);
				List<Double> cosArgs = cos.getArguments();
				int cosArgsNumber = cosArgs.size();
				if (cosArgsNumber == 0) {
					if (!cos.getArgsDistanceFromMathOperation().isEmpty()) {
						parentApplyElement.insertBefore(cosElement, parentApplyElement.getFirstChild());
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}
				} else if (cosArgsNumber == 1) {
					parentApplyElement.insertBefore(cosElement, parentApplyElement.getFirstChild());

					Double cosArg = cosArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(cosArg));
					tokenElement.setTextContent(String.valueOf(cosArg));
					parentApplyElement.appendChild(tokenElement);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			case Function.TAN: {
				Tan tan = (Tan) parentFunction;
				Element tanElement = createParentOperandElement(tan);
				List<Double> tanArgs = tan.getArguments();
				int tanArgsNumber = tanArgs.size();
				if (tanArgsNumber == 0) {
					if (!tan.getArgsDistanceFromMathOperation().isEmpty()) {
						parentApplyElement.appendChild(tanElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				} else if (tanArgsNumber == 1) {
					parentApplyElement.appendChild(tanElement);

					Double tanArg = tanArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(tanArg));
					tokenElement.setTextContent(String.valueOf(tanArg));

					parentApplyElement.appendChild(tokenElement);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			case Function.SQRT: {
				Sqrt sqrt = (Sqrt) parentFunction;
				Element sqrtElement = createParentOperandElement(sqrt);
				List<Double> sqrtArgs = sqrt.getArguments();
				int sqrtArgsNumber = sqrtArgs.size();
				if (sqrtArgsNumber == 0) {
					if (!sqrt.getArgsDistanceFromMathOperation().isEmpty()) {
						parentApplyElement.insertBefore(sqrtElement, parentApplyElement.getFirstChild());
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				} else if (sqrtArgsNumber == 1) {
					parentApplyElement.insertBefore(sqrtElement, parentApplyElement.getFirstChild());

					Double sqrtArg = sqrtArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
					tokenElement.setTextContent(String.valueOf(sqrtArg));

					parentApplyElement.appendChild(tokenElement);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			case Function.POWER: {
				Power power = (Power) parentFunction;
				Element powerElement = createParentOperandElement(power);
				State powerState = power.getState();
				List<Double> powerArgs = power.getArguments();
				int powerArgsNumber = powerArgs.size();
				if (powerArgsNumber == 0) {
					if (!power.getArgsDistanceFromMathOperation().isEmpty()) {
						parentApplyElement.insertBefore(powerElement, parentApplyElement.getFirstChild());
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				} else if (powerArgsNumber == 1) {
					parentApplyElement.insertBefore(powerElement, parentApplyElement.getFirstChild());

					Double powerArg = powerArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(powerArg));
					tokenElement.setTextContent(String.valueOf(powerArg));

					if (powerState == State.FIRST_OPERAND) {
						parentApplyElement.insertBefore(tokenElement, parentApplyElement.getFirstChild().getNextSibling());
					} else if (powerState == State.SECOND_OPERAND) {
						parentApplyElement.appendChild(tokenElement);
					}
				} else if (powerArgsNumber == 2) {
					parentApplyElement.appendChild(powerElement);

					for (int i = 0; i < powerArgsNumber; i++) {
						Double powerArg = powerArgs.get(i);
						Element tokenElement = createTokenElement(String.valueOf(powerArg));
						tokenElement.setTextContent(String.valueOf(powerArg));

						parentApplyElement.appendChild(tokenElement);
					}
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
				}

				mathMLRootElement.appendChild(parentApplyElement);
			}
				break;
			}
		}

		// Serialization through Transform
		DOMSource domSource = new DOMSource(mathMLDoc);
		StreamResult streamResult = new StreamResult(out);
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setAttribute("indent-number", 4);
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, ENCODING);
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.transform(domSource, streamResult);
	}// end of transform

	private void visitOperation(Element parentApplyElement, MathematicalOperation parentOperand, Operation operation, int position)
					throws MalformedMathMLExpressionException {
		Element operationApplyElement = appendOperation(parentApplyElement, parentOperand, operation);

		// check if this operation has children arguments
		List<Integer> operationArgsPositionList = operation.getArgsDistanceFromMathOperation();
		if (!operationArgsPositionList.isEmpty()) {
			Collections.reverse(operationArgsPositionList);
			for (int i = 0; i < operationArgsPositionList.size(); i++) {
				int absoluteOperationArgPosition = position - operationArgsPositionList.get(i);
				MathematicalOperation argument = operands.get(absoluteOperationArgPosition);
				if (argument instanceof Operation) {
					Operation operationArg = (Operation) argument;
					visitOperation(operationApplyElement, operation, operationArg, absoluteOperationArgPosition);
				} else if (argument instanceof Function) {
					Function functionArg = (Function) argument;
					visitFunction(operationApplyElement, operation, functionArg, absoluteOperationArgPosition);
				}
			}
		}
	}

	private Element appendOperation(Element parentApplyElement, MathematicalOperation parentOperand, Operation operation)
					throws MalformedMathMLExpressionException {
		Element operationApplyElement = null;
		int operationType = operation.getType();
		switch (operationType) {
		case Operation.PLUS: {
			Plus plus = (Plus) operation;
			operationApplyElement = appendPlusOperation(parentApplyElement, parentOperand, plus);
		}
			break;
		case Operation.MINUS: {
			Minus minus = (Minus) operation;
			operationApplyElement = appendMinusOperation(parentApplyElement, parentOperand, minus);
		}
			break;
		case Operation.DIVISION: {
			Division division = (Division) operation;
			operationApplyElement = appendDivisionOperation(parentApplyElement, parentOperand, division);
		}
			break;
		case Operation.MULTIPLICATION: {
			Multiplication multiplication = (Multiplication) operation;
			operationApplyElement = appendMultiplicationOperation(parentApplyElement, parentOperand, multiplication);
		}
			break;
		}

		return operationApplyElement;
	}

	private Element appendPlusOperation(Element parentApplyElement, MathematicalOperation parentOperand, Plus plus)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element plusElement = mathMLDoc.createElement(MathMLConstants.MML_PLUS);
		applyElement.appendChild(plusElement);

		List<Double> plusArguments = plus.getArguments();

		if (isDirectChildOperand(plus)) {
			for (int i = 0; i < plusArguments.size(); i++) {
				Double plusArg = plusArguments.get(i);
				Element tokenElement = createTokenElement(String.valueOf(plusArg));
				tokenElement.setTextContent(String.valueOf(plusArg));

				applyElement.appendChild(tokenElement);
			}

			parentApplyElement.appendChild(applyElement);
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentoperationType = parentOperation.getType();
				switch (parentoperationType) {
				case Operation.PLUS: {
					for (int i = 0; i < plusArguments.size(); i++) {
						Double plusArg = plusArguments.get(i);
						Element tokenElement = createTokenElement(String.valueOf(plusArg));
						tokenElement.setTextContent(String.valueOf(plusArg));

						applyElement.appendChild(tokenElement);
					}

					parentApplyElement.appendChild(applyElement);
				}
					break;
				case Operation.MINUS: {
					Minus minus = (Minus) parentOperation;
					State minusState = minus.getState();
					List<Double> minusArgs = minus.getArguments();
					int minusArgsNumber = minusArgs.size();
					if (minusArgsNumber == 0) {
						for (int i = 0; i < plusArguments.size(); i++) {
							Double plusArg = plusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(plusArg));
							tokenElement.setTextContent(String.valueOf(plusArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							for (int i = 0; i < plusArguments.size(); i++) {
								Double plusArg = plusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(plusArg));
								tokenElement.setTextContent(String.valueOf(plusArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else if (minusState == State.SECOND_OPERAND) {
							for (int i = 0; i < plusArguments.size(); i++) {
								Double plusArg = plusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(plusArg));
								tokenElement.setTextContent(String.valueOf(plusArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);

							Double minusArg = minusArgs.get(0);
							Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
							minusTokenElement.setTextContent(String.valueOf(minusArg));

							parentApplyElement.appendChild(minusTokenElement);
						}
					} else if (minusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division division = (Division) parentOperation;
					State divState = division.getState();
					List<Double> divArgs = division.getArguments();
					int divArgsNumber = divArgs.size();
					if (divArgsNumber == 0) {
						for (int i = 0; i < plusArguments.size(); i++) {
							Double plusArg = plusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(plusArg));
							tokenElement.setTextContent(String.valueOf(plusArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							for (int i = 0; i < plusArguments.size(); i++) {
								Double plusArg = plusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(plusArg));
								tokenElement.setTextContent(String.valueOf(plusArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else if (divState == State.SECOND_OPERAND) {
							for (int i = 0; i < plusArguments.size(); i++) {
								Double plusArg = plusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(plusArg));
								tokenElement.setTextContent(String.valueOf(plusArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);

							Double divArg = divArgs.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (divArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					for (int i = 0; i < plusArguments.size(); i++) {
						Double plusArg = plusArguments.get(i);
						Element tokenElement = createTokenElement(String.valueOf(plusArg));
						tokenElement.setTextContent(String.valueOf(plusArg));

						applyElement.appendChild(tokenElement);
					}

					parentApplyElement.appendChild(applyElement);
				}
					break;
				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin sin = (Sin) parentFunction;
					List<Double> sinArgs = sin.getArguments();
					int sinArgsNumber = sinArgs.size();
					if (sinArgsNumber == 0) {
						for (int i = 0; i < plusArguments.size(); i++) {
							Double plusArg = plusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(plusArg));
							tokenElement.setTextContent(String.valueOf(plusArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}

				}
					break;
				case Function.COS: {
					Cos cos = (Cos) parentFunction;
					List<Double> cosArgs = cos.getArguments();
					int cosArgsNumber = cosArgs.size();
					if (cosArgsNumber == 0) {
						for (int i = 0; i < plusArguments.size(); i++) {
							Double plusArg = plusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(plusArg));
							tokenElement.setTextContent(String.valueOf(plusArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}
				}
					break;
				case Function.TAN: {
					Tan tan = (Tan) parentFunction;
					List<Double> tanArgs = tan.getArguments();
					int tanArgsNumber = tanArgs.size();
					if (tanArgsNumber == 0) {
						for (int i = 0; i < plusArguments.size(); i++) {
							Double plusArg = plusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(plusArg));
							tokenElement.setTextContent(String.valueOf(plusArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;
				case Function.SQRT: {
					Sqrt sqrt = (Sqrt) parentFunction;
					List<Double> sqrtArgs = sqrt.getArguments();
					int sqrtArgsNumber = sqrtArgs.size();
					if (sqrtArgsNumber == 0) {
						for (int i = 0; i < plusArguments.size(); i++) {
							Double plusArg = plusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(plusArg));
							tokenElement.setTextContent(String.valueOf(plusArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				case Function.POWER: {
					Power power = (Power) parentFunction;
					State powerState = power.getState();
					List<Double> powerArgs = power.getArguments();
					int powerArgsNumber = powerArgs.size();
					if (powerArgsNumber == 0) {
						for (int i = 0; i < plusArguments.size(); i++) {
							Double plusArg = plusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(plusArg));
							tokenElement.setTextContent(String.valueOf(plusArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							for (int i = 0; i < plusArguments.size(); i++) {
								Double plusArg = plusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(plusArg));
								tokenElement.setTextContent(String.valueOf(plusArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else if (powerState == State.SECOND_OPERAND) {
							for (int i = 0; i < plusArguments.size(); i++) {
								Double plusArg = plusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(plusArg));
								tokenElement.setTextContent(String.valueOf(plusArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);

							Double powerArg = powerArgs.get(0);
							Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
							powerTokenElement.setTextContent(String.valueOf(powerArg));

							parentApplyElement.appendChild(powerTokenElement);
						}
					} else if (powerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power operation " + powerArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power operation " + powerArgsNumber);
					}
				}
					break;
				}
			}
		}

		return applyElement;
	}

	private Element appendMinusOperation(Element parentApplyElement, Object parentOperand, Minus minus)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element minusElement = mathMLDoc.createElement(MathMLConstants.MML_MINUS);
		applyElement.appendChild(minusElement);

		List<Double> minusArguments = minus.getArguments();
		int minusArgsNumber = minusArguments.size();
		State minusState = minus.getState();

		if (isDirectChildOperand(minus)) {
			if (minusArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (minusArgsNumber == 1) {
				if (minusState == State.FIRST_OPERAND) {
					Double minusArg = minusArguments.get(0);
					Element tokenElement = createTokenElement(String.valueOf(minusArg));
					tokenElement.setTextContent(String.valueOf(minusArg));

					applyElement.appendChild(tokenElement);

					parentApplyElement.appendChild(applyElement);
				} else if (minusState == State.SECOND_OPERAND) {
					parentApplyElement.appendChild(applyElement);
				}
			} else if (minusArgsNumber == 2) {
				for (int i = 0; i < minusArgsNumber; i++) {
					Double minusArg = minusArguments.get(i);
					Element tokenElement = createTokenElement(String.valueOf(minusArg));
					tokenElement.setTextContent(String.valueOf(minusArg));

					applyElement.appendChild(tokenElement);
				}
				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
			}
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentOperationType = parentOperation.getType();
				switch (parentOperationType) {
				case Operation.PLUS: {
					if (minusArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							Double minusArg = minusArguments.get(0);
							Element tokenElement = createTokenElement(String.valueOf(minusArg));
							tokenElement.setTextContent(String.valueOf(minusArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else if (minusState == State.SECOND_OPERAND) {
							parentApplyElement.appendChild(applyElement);
						}
					} else if (minusArgsNumber == 2) {
						for (int i = 0; i < minusArgsNumber; i++) {
							Double minusArg = minusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(minusArg));
							tokenElement.setTextContent(String.valueOf(minusArg));

							applyElement.appendChild(tokenElement);
						}
						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.MINUS: {
					Minus parentMinusOp = (Minus) parentOperation;
					State parentMinusState = parentMinusOp.getState();
					List<Double> parentMinusArgs = parentMinusOp.getArguments();
					int parentMinusArgsNumber = parentMinusArgs.size();
					if (parentMinusArgsNumber == 0) {
						if (minusArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (minusArgsNumber == 1) {
							if (minusState == State.FIRST_OPERAND) {
								Double minusArg = minusArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (minusState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (minusArgsNumber == 2) {
							for (int i = 0; i < minusArgsNumber; i++) {
								Double minusArg = minusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);
							}
							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
											+ minusArgsNumber);
						}
					} else if (parentMinusArgsNumber == 1) {
						if (parentMinusState == State.FIRST_OPERAND) {
							if (minusArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (minusArgsNumber == 1) {
								if (minusState == State.FIRST_OPERAND) {
									Double minusArg = minusArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (minusState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (minusArgsNumber == 2) {
								for (int i = 0; i < minusArgsNumber; i++) {
									Double minusArg = minusArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);
								}
								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
												+ minusArgsNumber);
							}
						} else if (parentMinusState == State.SECOND_OPERAND) {
							if (minusArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (minusArgsNumber == 1) {
								if (minusState == State.FIRST_OPERAND) {
									Double minusArg = minusArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (minusState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (minusArgsNumber == 2) {
								for (int i = 0; i < minusArgsNumber; i++) {
									Double minusArg = minusArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);
								}
								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
												+ minusArgsNumber);
							}

							Double parentMinusArg = parentMinusArgs.get(0);
							Element parentMinusTokenElement = createTokenElement(String.valueOf(parentMinusArg));
							parentMinusTokenElement.setTextContent(String.valueOf(parentMinusArg));

							parentApplyElement.appendChild(parentMinusTokenElement);
						}
					} else if (parentMinusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
										+ parentMinusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
										+ parentMinusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division division = (Division) parentOperation;
					State divState = division.getState();
					List<Double> divArgs = division.getArguments();
					int divArgsNumber = divArgs.size();
					if (divArgsNumber == 0) {
						if (minusArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (minusArgsNumber == 1) {
							if (minusState == State.FIRST_OPERAND) {
								Double minusArg = minusArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (minusState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (minusArgsNumber == 2) {
							for (int i = 0; i < minusArgsNumber; i++) {
								Double minusArg = minusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);
							}
							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
											+ minusArgsNumber);
						}
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							if (minusArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (minusArgsNumber == 1) {
								if (minusState == State.FIRST_OPERAND) {
									Double minusArg = minusArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (minusState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (minusArgsNumber == 2) {
								for (int i = 0; i < minusArgsNumber; i++) {
									Double minusArg = minusArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);
								}
								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
												+ minusArgsNumber);
							}
						} else if (divState == State.SECOND_OPERAND) {
							if (minusArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (minusArgsNumber == 1) {
								if (minusState == State.FIRST_OPERAND) {
									Double minusArg = minusArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (minusState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (minusArgsNumber == 2) {
								for (int i = 0; i < minusArgsNumber; i++) {
									Double minusArg = minusArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);
								}
								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
												+ minusArgsNumber);
							}

							Double divArg = divArgs.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (divArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					if (minusArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							Double minusArg = minusArguments.get(0);
							Element tokenElement = createTokenElement(String.valueOf(minusArg));
							tokenElement.setTextContent(String.valueOf(minusArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else if (minusState == State.SECOND_OPERAND) {
							parentApplyElement.appendChild(applyElement);
						}
					} else if (minusArgsNumber == 2) {
						for (int i = 0; i < minusArgsNumber; i++) {
							Double minusArg = minusArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(minusArg));
							tokenElement.setTextContent(String.valueOf(minusArg));

							applyElement.appendChild(tokenElement);
						}
						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin sin = (Sin) parentFunction;
					List<Double> sinArgs = sin.getArguments();
					int sinArgsNumber = sinArgs.size();
					if (sinArgsNumber == 0) {
						if (minusArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (minusArgsNumber == 1) {
							if (minusState == State.FIRST_OPERAND) {
								Double minusArg = minusArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (minusState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (minusArgsNumber == 2) {
							for (int i = 0; i < minusArgsNumber; i++) {
								Double minusArg = minusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);
							}
							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
											+ minusArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				}
					break;
				case Function.COS: {
					Cos cos = (Cos) parentFunction;
					List<Double> cosArgs = cos.getArguments();
					int cosArgsNumber = cosArgs.size();
					if (cosArgsNumber == 0) {
						if (minusArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (minusArgsNumber == 1) {
							if (minusState == State.FIRST_OPERAND) {
								Double minusArg = minusArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (minusState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (minusArgsNumber == 2) {
							for (int i = 0; i < minusArgsNumber; i++) {
								Double minusArg = minusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);
							}
							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
											+ minusArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}

				}
					break;
				case Function.TAN: {
					Tan tan = (Tan) parentFunction;
					List<Double> tanArgs = tan.getArguments();
					int tanArgsNumber = tanArgs.size();
					if (tanArgsNumber == 0) {
						if (minusArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (minusArgsNumber == 1) {
							if (minusState == State.FIRST_OPERAND) {
								Double minusArg = minusArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (minusState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (minusArgsNumber == 2) {
							for (int i = 0; i < minusArgsNumber; i++) {
								Double minusArg = minusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);
							}
							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
											+ minusArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;
				case Function.SQRT: {
					Sqrt sqrt = (Sqrt) parentFunction;
					List<Double> sqrtArgs = sqrt.getArguments();
					int sqrtArgsNumber = sqrtArgs.size();
					if (sqrtArgsNumber == 0) {
						if (minusArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (minusArgsNumber == 1) {
							if (minusState == State.FIRST_OPERAND) {
								Double minusArg = minusArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (minusState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (minusArgsNumber == 2) {
							for (int i = 0; i < minusArgsNumber; i++) {
								Double minusArg = minusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);
							}
							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
											+ minusArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				case Function.POWER: {
					Power power = (Power) parentFunction;
					State powerState = power.getState();
					List<Double> powerArgs = power.getArguments();
					int powerArgsNumber = powerArgs.size();
					if (powerArgsNumber == 0) {
						if (minusArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (minusArgsNumber == 1) {
							if (minusState == State.FIRST_OPERAND) {
								Double minusArg = minusArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (minusState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (minusArgsNumber == 2) {
							for (int i = 0; i < minusArgsNumber; i++) {
								Double minusArg = minusArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(minusArg));
								tokenElement.setTextContent(String.valueOf(minusArg));

								applyElement.appendChild(tokenElement);
							}
							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
											+ minusArgsNumber);
						}
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							if (minusArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (minusArgsNumber == 1) {
								if (minusState == State.FIRST_OPERAND) {
									Double minusArg = minusArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (minusState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (minusArgsNumber == 2) {
								for (int i = 0; i < minusArgsNumber; i++) {
									Double minusArg = minusArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);
								}
								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
												+ minusArgsNumber);
							}
						} else if (powerState == State.SECOND_OPERAND) {
							if (minusArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (minusArgsNumber == 1) {
								if (minusState == State.FIRST_OPERAND) {
									Double minusArg = minusArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (minusState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (minusArgsNumber == 2) {
								for (int i = 0; i < minusArgsNumber; i++) {
									Double minusArg = minusArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(minusArg));
									tokenElement.setTextContent(String.valueOf(minusArg));

									applyElement.appendChild(tokenElement);
								}
								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation "
												+ minusArgsNumber);
							}

							Double powerArg = powerArgs.get(0);
							Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
							powerTokenElement.setTextContent(String.valueOf(powerArg));

							parentApplyElement.appendChild(powerTokenElement);
						}
					} else if (powerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
					break;
				}
			}
		}

		return applyElement;
	}

	private Element appendDivisionOperation(Element parentApplyElement, MathematicalOperation parentOperand, Division division)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element divisionElement = mathMLDoc.createElement(MathMLConstants.MML_DIVIDE);
		applyElement.appendChild(divisionElement);

		List<Double> divArguments = division.getArguments();
		int divArgsNumber = division.getArguments().size();
		State divState = division.getState();

		if (isDirectChildOperand(division)) {
			if (divArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (divArgsNumber == 1) {
				if (divState == State.FIRST_OPERAND) {
					Double divArg = divArguments.get(0);
					Element tokenElement = createTokenElement(String.valueOf(divArg));
					tokenElement.setTextContent(String.valueOf(divArg));

					applyElement.appendChild(tokenElement);

					parentApplyElement.appendChild(applyElement);
				} else if (divState == State.SECOND_OPERAND) {
					parentApplyElement.appendChild(applyElement);
				}
			} else if (divArgsNumber == 2) {
				for (int i = 0; i < divArgsNumber; i++) {
					Double divArg = divArguments.get(i);
					Element tokenElement = createTokenElement(String.valueOf(divArg));
					tokenElement.setTextContent(String.valueOf(divArg));

					applyElement.appendChild(tokenElement);
				}

				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
			}
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentOperationType = parentOperation.getType();
				switch (parentOperationType) {
				case Operation.PLUS: {
					if (divArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							Double divArg = divArguments.get(0);
							Element tokenElement = createTokenElement(String.valueOf(divArg));
							tokenElement.setTextContent(String.valueOf(divArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else if (divState == State.SECOND_OPERAND) {
							parentApplyElement.appendChild(applyElement);
						}
					} else if (divArgsNumber == 2) {
						for (int i = 0; i < divArgsNumber; i++) {
							Double divArg = divArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(divArg));
							tokenElement.setTextContent(String.valueOf(divArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MINUS: {
					Minus minus = (Minus) parentOperation;
					State minusState = minus.getState();
					List<Double> minusArgs = minus.getArguments();
					int minusArgsNumber = minusArgs.size();
					if (minusArgsNumber == 0) {
						if (divArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (divArgsNumber == 1) {
							if (divState == State.FIRST_OPERAND) {
								Double divArg = divArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (divState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (divArgsNumber == 2) {
							for (int i = 0; i < divArgsNumber; i++) {
								Double divArg = divArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
											+ divArgsNumber);
						}
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							if (divArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (divArgsNumber == 1) {
								if (divState == State.FIRST_OPERAND) {
									Double divArg = divArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (divState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (divArgsNumber == 2) {
								for (int i = 0; i < divArgsNumber; i++) {
									Double divArg = divArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
												+ divArgsNumber);
							}
						} else if (minusState == State.SECOND_OPERAND) {
							if (divArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (divArgsNumber == 1) {
								if (divState == State.FIRST_OPERAND) {
									Double divArg = divArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (divState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (divArgsNumber == 2) {
								for (int i = 0; i < divArgsNumber; i++) {
									Double divArg = divArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
												+ divArgsNumber);
							}

							Double minusArg = minusArgs.get(0);
							Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
							minusTokenElement.setTextContent(String.valueOf(minusArg));

							parentApplyElement.appendChild(minusTokenElement);
						}
					} else if (minusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division parentDiv = (Division) parentOperation;
					State parentDivState = parentDiv.getState();
					List<Double> parentDivArgs = parentDiv.getArguments();
					int parentDivArgsNumber = parentDivArgs.size();
					if (parentDivArgsNumber == 0) {
						if (divArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (divArgsNumber == 1) {
							if (divState == State.FIRST_OPERAND) {
								Double divArg = divArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (divState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (divArgsNumber == 2) {
							for (int i = 0; i < divArgsNumber; i++) {
								Double divArg = divArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
											+ divArgsNumber);
						}
					} else if (parentDivArgsNumber == 1) {
						if (parentDivState == State.FIRST_OPERAND) {
							if (divArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (divArgsNumber == 1) {
								if (divState == State.FIRST_OPERAND) {
									Double divArg = divArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (divState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (divArgsNumber == 2) {
								for (int i = 0; i < divArgsNumber; i++) {
									Double divArg = divArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
												+ divArgsNumber);
							}
						} else if (parentDivState == State.SECOND_OPERAND) {
							if (divArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (divArgsNumber == 1) {
								if (divState == State.FIRST_OPERAND) {
									Double divArg = divArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (divState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (divArgsNumber == 2) {
								for (int i = 0; i < divArgsNumber; i++) {
									Double divArg = divArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
												+ divArgsNumber);
							}

							Double divArg = divArguments.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (parentDivArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
										+ parentDivArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
										+ parentDivArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					if (divArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							Double divArg = divArguments.get(0);
							Element tokenElement = createTokenElement(String.valueOf(divArg));
							tokenElement.setTextContent(String.valueOf(divArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else if (divState == State.SECOND_OPERAND) {
							parentApplyElement.appendChild(applyElement);
						}
					} else if (divArgsNumber == 2) {
						for (int i = 0; i < divArgsNumber; i++) {
							Double divArg = divArguments.get(i);
							Element tokenElement = createTokenElement(String.valueOf(divArg));
							tokenElement.setTextContent(String.valueOf(divArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin sin = (Sin) parentFunction;
					List<Double> sinArgs = sin.getArguments();
					int sinArgsNumber = sinArgs.size();
					if (sinArgsNumber == 0) {
						if (divArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (divArgsNumber == 1) {
							if (divState == State.FIRST_OPERAND) {
								Double divArg = divArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (divState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (divArgsNumber == 2) {
							for (int i = 0; i < divArgsNumber; i++) {
								Double divArg = divArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
											+ divArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				}
					break;
				case Function.COS: {
					Cos cos = (Cos) parentFunction;
					List<Double> cosArgs = cos.getArguments();
					int cosArgsNumber = cosArgs.size();
					if (cosArgsNumber == 0) {
						if (divArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (divArgsNumber == 1) {
							if (divState == State.FIRST_OPERAND) {
								Double divArg = divArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (divState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (divArgsNumber == 2) {
							for (int i = 0; i < divArgsNumber; i++) {
								Double divArg = divArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
											+ divArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}

				}
					break;
				case Function.TAN: {
					Tan tan = (Tan) parentFunction;
					List<Double> tanArgs = tan.getArguments();
					int tanArgsNumber = tanArgs.size();
					if (tanArgsNumber == 0) {
						if (divArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (divArgsNumber == 1) {
							if (divState == State.FIRST_OPERAND) {
								Double divArg = divArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (divState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (divArgsNumber == 2) {
							for (int i = 0; i < divArgsNumber; i++) {
								Double divArg = divArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
											+ divArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;
				case Function.SQRT: {
					Sqrt sqrt = (Sqrt) parentFunction;
					List<Double> sqrtArgs = sqrt.getArguments();
					int sqrtArgsNumber = sqrtArgs.size();
					if (sqrtArgsNumber == 0) {
						if (divArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (divArgsNumber == 1) {
							if (divState == State.FIRST_OPERAND) {
								Double divArg = divArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (divState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (divArgsNumber == 2) {
							for (int i = 0; i < divArgsNumber; i++) {
								Double divArg = divArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
											+ divArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				case Function.POWER: {
					Power power = (Power) parentFunction;
					State powerState = power.getState();
					List<Double> powerArgs = power.getArguments();
					int powerArgsNumber = powerArgs.size();
					if (powerArgsNumber == 0) {
						if (divArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (divArgsNumber == 1) {
							if (divState == State.FIRST_OPERAND) {
								Double divArg = divArguments.get(0);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (divState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (divArgsNumber == 2) {
							for (int i = 0; i < divArgsNumber; i++) {
								Double divArg = divArguments.get(i);
								Element tokenElement = createTokenElement(String.valueOf(divArg));
								tokenElement.setTextContent(String.valueOf(divArg));
								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
											+ divArgsNumber);
						}
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							if (divArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (divArgsNumber == 1) {
								if (divState == State.FIRST_OPERAND) {
									Double divArg = divArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (divState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (divArgsNumber == 2) {
								for (int i = 0; i < divArgsNumber; i++) {
									Double divArg = divArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
												+ divArgsNumber);
							}
						} else if (powerState == State.SECOND_OPERAND) {
							if (divArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (divArgsNumber == 1) {
								if (divState == State.FIRST_OPERAND) {
									Double divArg = divArguments.get(0);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (divState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (divArgsNumber == 2) {
								for (int i = 0; i < divArgsNumber; i++) {
									Double divArg = divArguments.get(i);
									Element tokenElement = createTokenElement(String.valueOf(divArg));
									tokenElement.setTextContent(String.valueOf(divArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation "
												+ divArgsNumber);
							}

							Double powerArg = powerArgs.get(0);
							Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
							powerTokenElement.setTextContent(String.valueOf(powerArg));

							parentApplyElement.appendChild(powerTokenElement);
						}
					} else if (powerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
					break;
				}
			}
		}
		return applyElement;
	}

	private Element appendMultiplicationOperation(Element parentApplyElement, MathematicalOperation parentOperand,
					Multiplication multiplication) throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element multiplicationElement = mathMLDoc.createElement(MathMLConstants.MML_TIMES);
		applyElement.appendChild(multiplicationElement);

		List<Double> timesArgs = multiplication.getArguments();
		if (isDirectChildOperand(multiplication)) {
			for (int i = 0; i < timesArgs.size(); i++) {
				Double timesArg = timesArgs.get(i);
				Element tokenElement = createTokenElement(String.valueOf(timesArg));
				tokenElement.setTextContent(String.valueOf(timesArg));

				applyElement.appendChild(tokenElement);
			}

			parentApplyElement.appendChild(applyElement);
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentOperationType = parentOperation.getType();
				switch (parentOperationType) {
				case Operation.PLUS: {
					for (int i = 0; i < timesArgs.size(); i++) {
						Double timesArg = timesArgs.get(i);
						Element tokenElement = createTokenElement(String.valueOf(timesArg));
						tokenElement.setTextContent(String.valueOf(timesArg));

						applyElement.appendChild(tokenElement);
					}

					parentApplyElement.appendChild(applyElement);
				}
					break;
				case Operation.MINUS: {
					Minus minus = (Minus) parentOperation;
					State minusState = minus.getState();
					List<Double> minusArgs = minus.getArguments();
					int minusArgsNumber = minusArgs.size();
					if (minusArgsNumber == 0) {
						for (int i = 0; i < timesArgs.size(); i++) {
							Double timesArg = timesArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(timesArg));
							tokenElement.setTextContent(String.valueOf(timesArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							for (int i = 0; i < timesArgs.size(); i++) {
								Double timesArg = timesArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(timesArg));
								tokenElement.setTextContent(String.valueOf(timesArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else if (minusState == State.SECOND_OPERAND) {
							for (int i = 0; i < timesArgs.size(); i++) {
								Double timesArg = timesArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(timesArg));
								tokenElement.setTextContent(String.valueOf(timesArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);

							Double minusArg = minusArgs.get(0);
							Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
							minusTokenElement.setTextContent(String.valueOf(minusArg));

							parentApplyElement.appendChild(minusTokenElement);
						}
					} else if (minusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division division = (Division) parentOperation;
					State divState = division.getState();
					List<Double> divArgs = division.getArguments();
					int divArgsNumber = divArgs.size();
					if (divArgsNumber == 0) {
						for (int i = 0; i < timesArgs.size(); i++) {
							Double timesArg = timesArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(timesArg));
							tokenElement.setTextContent(String.valueOf(timesArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							for (int i = 0; i < timesArgs.size(); i++) {
								Double timesArg = timesArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(timesArg));
								tokenElement.setTextContent(String.valueOf(timesArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else if (divState == State.SECOND_OPERAND) {
							for (int i = 0; i < timesArgs.size(); i++) {
								Double timesArg = timesArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(timesArg));
								tokenElement.setTextContent(String.valueOf(timesArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);

							Double divArg = divArgs.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (divArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					for (int i = 0; i < timesArgs.size(); i++) {
						Double timesArg = timesArgs.get(i);
						Element tokenElement = createTokenElement(String.valueOf(timesArg));
						tokenElement.setTextContent(String.valueOf(timesArg));

						applyElement.appendChild(tokenElement);
					}

					parentApplyElement.appendChild(applyElement);
				}
					break;
				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin sin = (Sin) parentFunction;
					List<Double> sinArgs = sin.getArguments();
					int sinArgsNumber = sinArgs.size();
					if (sinArgsNumber == 0) {
						for (int i = 0; i < timesArgs.size(); i++) {
							Double timesArg = timesArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(timesArg));
							tokenElement.setTextContent(String.valueOf(timesArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				}
					break;
				case Function.COS: {
					Cos cos = (Cos) parentFunction;
					List<Double> cosArgs = cos.getArguments();
					int cosArgsNumber = cosArgs.size();
					if (cosArgsNumber == 0) {
						for (int i = 0; i < timesArgs.size(); i++) {
							Double timesArg = timesArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(timesArg));
							tokenElement.setTextContent(String.valueOf(timesArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}
				}
					break;
				case Function.TAN: {
					Tan tan = (Tan) parentFunction;
					List<Double> tanArgs = tan.getArguments();
					int tanArgsNumber = tanArgs.size();
					if (tanArgsNumber == 0) {
						for (int i = 0; i < timesArgs.size(); i++) {
							Double timesArg = timesArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(timesArg));
							tokenElement.setTextContent(String.valueOf(timesArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;
				case Function.SQRT: {
					Sqrt sqrt = (Sqrt) parentFunction;
					List<Double> sqrtArgs = sqrt.getArguments();
					int sqrtArgsNumber = sqrtArgs.size();
					if (sqrtArgsNumber == 0) {
						for (int i = 0; i < timesArgs.size(); i++) {
							Double timesArg = timesArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(timesArg));
							tokenElement.setTextContent(String.valueOf(timesArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				case Function.POWER: {
					Power power = (Power) parentFunction;
					State powerState = power.getState();
					List<Double> powerArgs = power.getArguments();
					int powerArgsNumber = powerArgs.size();
					if (powerArgsNumber == 0) {
						for (int i = 0; i < timesArgs.size(); i++) {
							Double timesArg = timesArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(timesArg));
							tokenElement.setTextContent(String.valueOf(timesArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							for (int i = 0; i < timesArgs.size(); i++) {
								Double timesArg = timesArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(timesArg));
								tokenElement.setTextContent(String.valueOf(timesArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else if (powerState == State.SECOND_OPERAND) {
							for (int i = 0; i < timesArgs.size(); i++) {
								Double timesArg = timesArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(timesArg));
								tokenElement.setTextContent(String.valueOf(timesArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);

							Double powerArg = powerArgs.get(0);
							Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
							powerTokenElement.setTextContent(String.valueOf(powerArg));

							parentApplyElement.appendChild(powerTokenElement);
						}
					} else if (powerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power operation " + powerArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power operation " + powerArgsNumber);
					}
				}
					break;
				}
			}
		}

		return applyElement;
	}

	private void visitFunction(Element parentApplyElement, MathematicalOperation parentOperand, Function function, int position)
					throws MalformedMathMLExpressionException {
		Element functionApplyElement = appendFunction(parentApplyElement, parentOperand, function);

		// check if this function has children arguments
		List<Integer> functionArgsPositionList = function.getArgsDistanceFromMathOperation();
		if (!functionArgsPositionList.isEmpty()) {
			Collections.reverse(functionArgsPositionList);
			for (int i = 0; i < functionArgsPositionList.size(); i++) {
				int absoluteFunctionPosition = position - functionArgsPositionList.get(i);
				MathematicalOperation argument = operands.get(absoluteFunctionPosition);
				if (argument instanceof Operation) {
					Operation operationArg = (Operation) argument;
					visitOperation(functionApplyElement, function, operationArg, absoluteFunctionPosition);
				} else if (argument instanceof Function) {
					Function functionArg = (Function) argument;
					visitFunction(functionApplyElement, function, functionArg, absoluteFunctionPosition);
				}
			}
		}
	}

	private Element appendFunction(Element parentApplyElement, MathematicalOperation parentOperand, Function function)
					throws MalformedMathMLExpressionException {
		Element functionApplyElement = null;
		int functionType = function.getType();
		switch (functionType) {
		case Function.SIN: {
			Sin sin = (Sin) function;
			functionApplyElement = appendSinFunction(parentApplyElement, parentOperand, sin);
		}
			break;
		case Function.COS: {
			Cos cos = (Cos) function;
			functionApplyElement = appendCosFunction(parentApplyElement, parentOperand, cos);
		}
			break;
		case Function.COSH: {
			Cosh cosh = (Cosh) function;
			functionApplyElement = appendCoshFunction(parentApplyElement, parentOperand, cosh);
		}
			break;
		case Function.TAN: {
			Tan tan = (Tan) function;
			functionApplyElement = appendTanFunction(parentApplyElement, parentOperand, tan);
		}
			break;
		case Function.TANH: {
			Tanh tanh = (Tanh) function;
			functionApplyElement = appendTanhFunction(parentApplyElement, parentOperand, tanh);
		}
			break;
		case Function.SQRT: {
			Sqrt sqrt = (Sqrt) function;
			functionApplyElement = appendSqrtFunction(parentApplyElement, parentOperand, sqrt);
		}
			break;
		case Function.POWER: {
			Power power = (Power) function;
			functionApplyElement = appendPowerFunction(parentApplyElement, parentOperand, power);
		}
			break;
		}
		return functionApplyElement;
	}

	private Element appendSinFunction(Element parentApplyElement, MathematicalOperation parentOperand, Sin sin)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element sinElement = mathMLDoc.createElement(MathMLConstants.MML_SIN);
		applyElement.appendChild(sinElement);

		List<Double> sinArgs = sin.getArguments();
		int sinArgsNumber = sinArgs.size();

		if (isDirectChildOperand(sin)) {
			if (sinArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (sinArgsNumber == 1) {
				Double sinArg = sinArgs.get(0);
				Element tokenElement = createTokenElement(String.valueOf(sinArg));
				tokenElement.setTextContent(String.valueOf(sinArg));

				applyElement.appendChild(tokenElement);

				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
			}
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentOperationType = parentOperation.getType();
				switch (parentOperationType) {
				case Operation.PLUS: {
					if (sinArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (sinArgsNumber == 1) {
						Double sinArg = sinArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(sinArg));
						tokenElement.setTextContent(String.valueOf(sinArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				}
					break;
				case Operation.MINUS: {
					Minus minus = (Minus) parentOperation;
					State minusState = minus.getState();
					List<Double> minusArgs = minus.getArguments();
					int minusArgsNumber = minusArgs.size();
					if (minusArgsNumber == 0) {
						if (sinArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sinArgsNumber == 1) {
							Double sinArg = sinArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sinArg));
							tokenElement.setTextContent(String.valueOf(sinArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
						}
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							if (sinArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sinArgsNumber == 1) {
								Double sinArg = sinArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sinArg));
								tokenElement.setTextContent(String.valueOf(sinArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function "
												+ sinArgsNumber);
							}
						} else if (minusState == State.SECOND_OPERAND) {
							if (sinArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sinArgsNumber == 1) {
								Double sinArg = sinArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sinArg));
								tokenElement.setTextContent(String.valueOf(sinArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function "
												+ sinArgsNumber);
							}

							Double minusArg = minusArgs.get(0);
							Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
							minusTokenElement.setTextContent(String.valueOf(minusArg));

							parentApplyElement.appendChild(minusTokenElement);
						}
					} else if (minusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division division = (Division) parentOperation;
					State divState = division.getState();
					List<Double> divArgs = division.getArguments();
					int divArgsNumber = divArgs.size();
					if (divArgsNumber == 0) {
						if (sinArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sinArgsNumber == 1) {
							Double sinArg = sinArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sinArg));
							tokenElement.setTextContent(String.valueOf(sinArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
						}
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							if (sinArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sinArgsNumber == 1) {
								Double sinArg = sinArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sinArg));
								tokenElement.setTextContent(String.valueOf(sinArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function "
												+ sinArgsNumber);
							}
						} else if (divState == State.SECOND_OPERAND) {
							if (sinArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sinArgsNumber == 1) {
								Double sinArg = sinArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sinArg));
								tokenElement.setTextContent(String.valueOf(sinArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function "
												+ sinArgsNumber);
							}

							Double divArg = divArgs.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (divArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					if (sinArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (sinArgsNumber == 1) {
						Double sinArg = sinArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(sinArg));
						tokenElement.setTextContent(String.valueOf(sinArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				}
					break;
				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin parentSin = (Sin) parentFunction;
					List<Double> parentSinArgs = parentSin.getArguments();
					int parentSinArgsNumber = parentSinArgs.size();
					if (parentSinArgsNumber == 0) {
						if (sinArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sinArgsNumber == 1) {
							Double sinArg = sinArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sinArg));
							tokenElement.setTextContent(String.valueOf(sinArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for parent sin function "
										+ parentSinArgsNumber);
					}

				}
					break;
				case Function.COS: {
					Cos cos = (Cos) parentFunction;
					List<Double> cosArgs = cos.getArguments();
					int cosArgsNumber = cosArgs.size();
					if (cosArgsNumber == 0) {
						if (sinArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sinArgsNumber == 1) {
							Double sinArg = sinArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sinArg));
							tokenElement.setTextContent(String.valueOf(sinArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}
				}
					break;
				case Function.TAN: {
					Tan tan = (Tan) parentFunction;
					List<Double> tanArgs = tan.getArguments();
					int tanArgsNumber = tanArgs.size();
					if (tanArgsNumber == 0) {
						if (sinArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sinArgsNumber == 1) {
							Double sinArg = sinArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sinArg));
							tokenElement.setTextContent(String.valueOf(sinArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;
				case Function.SQRT: {
					Sqrt sqrt = (Sqrt) parentFunction;
					List<Double> sqrtArgs = sqrt.getArguments();
					int sqrtArgsNumber = sqrtArgs.size();
					if (sqrtArgsNumber == 0) {
						if (sinArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sinArgsNumber == 1) {
							Double sinArg = sinArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sinArg));
							tokenElement.setTextContent(String.valueOf(sinArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				case Function.POWER: {
					Power power = (Power) parentFunction;
					State powerState = power.getState();
					List<Double> powerArgs = power.getArguments();
					int powerArgsNumber = powerArgs.size();
					if (powerArgsNumber == 0) {
						if (sinArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sinArgsNumber == 1) {
							Double sinArg = sinArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sinArg));
							tokenElement.setTextContent(String.valueOf(sinArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
						}
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							if (sinArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sinArgsNumber == 1) {
								Double sinArg = sinArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sinArg));
								tokenElement.setTextContent(String.valueOf(sinArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function "
												+ sinArgsNumber);
							}
						} else if (powerState == State.SECOND_OPERAND) {
							if (sinArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sinArgsNumber == 1) {
								Double sinArg = sinArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sinArg));
								tokenElement.setTextContent(String.valueOf(sinArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function "
												+ sinArgsNumber);
							}

							Double powerArg = powerArgs.get(0);
							Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
							powerTokenElement.setTextContent(String.valueOf(powerArg));

							parentApplyElement.appendChild(powerTokenElement);
						}
					} else if (powerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
					break;
				}
			}
		}

		return applyElement;
	}

	private Element appendCosFunction(Element parentApplyElement, MathematicalOperation parentOperand, Cos cos)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element cosElement = mathMLDoc.createElement(MathMLConstants.MML_COS);
		applyElement.appendChild(cosElement);

		List<Double> cosArgs = cos.getArguments();
		int cosArgsNumber = cosArgs.size();

		if (isDirectChildOperand(cos)) {
			if (cosArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (cosArgsNumber == 1) {
				Double cosArg = cosArgs.get(0);
				Element tokenElement = createTokenElement(String.valueOf(cosArg));
				tokenElement.setTextContent(String.valueOf(cosArg));

				applyElement.appendChild(tokenElement);

				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
			}
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentOperationType = parentOperation.getType();
				switch (parentOperationType) {
				case Operation.PLUS: {
					if (cosArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (cosArgsNumber == 1) {
						Double cosArg = cosArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(cosArg));
						tokenElement.setTextContent(String.valueOf(cosArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}
				}
					break;
				case Operation.MINUS: {
					Minus minus = (Minus) parentOperation;
					State minusState = minus.getState();
					List<Double> minusArgs = minus.getArguments();
					int minusArgsNumber = minusArgs.size();
					if (minusArgsNumber == 0) {
						if (cosArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (cosArgsNumber == 1) {
							Double cosArg = cosArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(cosArg));
							tokenElement.setTextContent(String.valueOf(cosArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
						}
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							if (cosArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (cosArgsNumber == 1) {
								Double cosArg = cosArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(cosArg));
								tokenElement.setTextContent(String.valueOf(cosArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function "
												+ cosArgsNumber);
							}
						} else if (minusState == State.SECOND_OPERAND) {
							if (cosArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (cosArgsNumber == 1) {
								Double cosArg = cosArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(cosArg));
								tokenElement.setTextContent(String.valueOf(cosArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function "
												+ cosArgsNumber);
							}

							Double minusArg = minusArgs.get(0);
							Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
							minusTokenElement.setTextContent(String.valueOf(minusArg));

							parentApplyElement.appendChild(minusTokenElement);
						}
					} else if (minusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division division = (Division) parentOperation;
					State divState = division.getState();
					List<Double> divArgs = division.getArguments();
					int divArgsNumber = divArgs.size();
					if (divArgsNumber == 0) {
						if (cosArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (cosArgsNumber == 1) {
							Double cosArg = cosArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(cosArg));
							tokenElement.setTextContent(String.valueOf(cosArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
						}
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							if (cosArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (cosArgsNumber == 1) {
								Double cosArg = cosArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(cosArg));
								tokenElement.setTextContent(String.valueOf(cosArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function "
												+ cosArgsNumber);
							}
						} else if (divState == State.SECOND_OPERAND) {
							if (cosArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (cosArgsNumber == 1) {
								Double cosArg = cosArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(cosArg));
								tokenElement.setTextContent(String.valueOf(cosArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function "
												+ cosArgsNumber);
							}

							Double divArg = divArgs.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (divArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					if (cosArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (cosArgsNumber == 1) {
						Double cosArg = cosArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(cosArg));
						tokenElement.setTextContent(String.valueOf(cosArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}
				}
					break;

				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin sin = (Sin) parentFunction;
					List<Double> sinArgs = sin.getArguments();
					int sinArgsNumber = sinArgs.size();
					if (sinArgsNumber == 0) {
						if (cosArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (cosArgsNumber == 1) {
							Double cosArg = cosArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(cosArg));
							tokenElement.setTextContent(String.valueOf(cosArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				}
					break;
				case Function.COS: {
					Cos parentCos = (Cos) parentFunction;
					List<Double> parentCosArgs = parentCos.getArguments();
					int parentCosArgsNumber = parentCosArgs.size();
					if (parentCosArgsNumber == 0) {
						if (cosArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (cosArgsNumber == 1) {
							Double cosArg = cosArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(cosArg));
							tokenElement.setTextContent(String.valueOf(cosArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for parent cos function "
										+ parentCosArgsNumber);
					}

				}
					break;
				case Function.TAN: {
					Tan tan = (Tan) parentFunction;
					List<Double> tanArgs = tan.getArguments();
					int tanArgsNumber = tanArgs.size();
					if (tanArgsNumber == 0) {
						if (cosArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (cosArgsNumber == 1) {
							Double cosArg = cosArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(cosArg));
							tokenElement.setTextContent(String.valueOf(cosArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;
				case Function.SQRT: {
					Sqrt sqrt = (Sqrt) parentFunction;
					List<Double> sqrtArgs = sqrt.getArguments();
					int sqrtArgsNumber = sqrtArgs.size();
					if (sqrtArgsNumber == 0) {
						if (cosArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (cosArgsNumber == 1) {
							Double cosArg = cosArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(cosArg));
							tokenElement.setTextContent(String.valueOf(cosArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				case Function.POWER: {
					Power power = (Power) parentFunction;
					State powerState = power.getState();
					List<Double> powerArgs = power.getArguments();
					int powerArgsNumber = powerArgs.size();
					if (powerArgsNumber == 0) {
						if (cosArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (cosArgsNumber == 1) {
							Double cosArg = cosArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(cosArg));
							tokenElement.setTextContent(String.valueOf(cosArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
						}
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							if (cosArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (cosArgsNumber == 1) {
								Double cosArg = cosArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(cosArg));
								tokenElement.setTextContent(String.valueOf(cosArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function "
												+ cosArgsNumber);
							}
						} else if (powerState == State.SECOND_OPERAND) {
							if (cosArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (cosArgsNumber == 1) {
								Double cosArg = cosArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(cosArg));
								tokenElement.setTextContent(String.valueOf(cosArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function "
												+ cosArgsNumber);
							}

							Double powerArg = powerArgs.get(0);
							Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
							powerTokenElement.setTextContent(String.valueOf(powerArg));

							parentApplyElement.appendChild(powerTokenElement);
						}
					} else if (powerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
					break;
				}
			}
		}

		return applyElement;
	}

	private Element appendCoshFunction(Element parentApplyElement, MathematicalOperation parentOperand, Cosh cosh)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element coshElement = mathMLDoc.createElement(MathMLConstants.MML_COSH);
		applyElement.appendChild(coshElement);

		List<Double> coshArgs = cosh.getArguments();
		int coshArgsNumber = coshArgs.size();

		if (isDirectChildOperand(cosh)) {
			if (coshArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (coshArgsNumber == 1) {
				Double coshArg = coshArgs.get(0);
				Element tokenElement = createTokenElement(String.valueOf(coshArg));
				tokenElement.setTextContent(String.valueOf(coshArg));

				applyElement.appendChild(tokenElement);

				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
			}
		} else {
			int parentOperationType = parentOperand.getType();
			switch (parentOperationType) {
			case Operation.PLUS: {
				if (coshArgsNumber == 0) {
					parentApplyElement.appendChild(applyElement);
				} else if (coshArgsNumber == 1) {
					Double cosArg = coshArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(cosArg));
					tokenElement.setTextContent(String.valueOf(cosArg));

					applyElement.appendChild(tokenElement);

					parentApplyElement.appendChild(applyElement);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
				}
			}
				break;
			case Operation.MINUS: {
				Minus minus = (Minus) parentOperand;
				State minusState = minus.getState();
				List<Double> minusArgs = minus.getArguments();
				int minusArgsNumber = minusArgs.size();
				if (minusArgsNumber == 0) {
					if (coshArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (coshArgsNumber == 1) {
						Double coshArg = coshArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(coshArg));
						tokenElement.setTextContent(String.valueOf(coshArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
					}
				} else if (minusArgsNumber == 1) {
					if (minusState == State.FIRST_OPERAND) {
						if (coshArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (coshArgsNumber == 1) {
							Double coshArg = coshArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(coshArg));
							tokenElement.setTextContent(String.valueOf(coshArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
						}
					} else if (minusState == State.SECOND_OPERAND) {
						if (coshArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (coshArgsNumber == 1) {
							Double coshArg = coshArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(coshArg));
							tokenElement.setTextContent(String.valueOf(coshArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
						}

						Double minusArg = minusArgs.get(0);
						Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
						minusTokenElement.setTextContent(String.valueOf(minusArg));

						parentApplyElement.appendChild(minusTokenElement);
					}
				} else if (minusArgsNumber == 2) {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
				}
			}
				break;
			case Operation.DIVISION: {
				Division div = (Division) parentOperand;
				State divState = div.getState();
				List<Double> divArgs = div.getArguments();
				int divArgsNumber = divArgs.size();
				if (divArgsNumber == 0) {
					if (coshArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (coshArgsNumber == 1) {
						Double coshArg = coshArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(coshArg));
						tokenElement.setTextContent(String.valueOf(coshArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
					}
				} else if (divArgsNumber == 1) {
					if (divState == State.FIRST_OPERAND) {
						if (coshArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (coshArgsNumber == 1) {
							Double coshArg = coshArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(coshArg));
							tokenElement.setTextContent(String.valueOf(coshArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
						}
					} else if (divState == State.SECOND_OPERAND) {
						if (coshArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (coshArgsNumber == 1) {
							Double coshArg = coshArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(coshArg));
							tokenElement.setTextContent(String.valueOf(coshArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
						}

						Double divArg = divArgs.get(0);
						Element divTokenElement = createTokenElement(String.valueOf(divArg));
						divTokenElement.setTextContent(String.valueOf(divArg));

						parentApplyElement.appendChild(divTokenElement);
					}
				} else if (divArgsNumber == 2) {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
				}
			}
				break;
			case Operation.MULTIPLICATION: {
				Multiplication times = (Multiplication) parentOperand;
				List<Double> timesArgs = times.getArguments();
				int timesArgsNumber = timesArgs.size();
				if (timesArgsNumber == 0) {
					if (coshArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (coshArgsNumber == 1) {
						Double coshArg = coshArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(coshArg));
						tokenElement.setTextContent(String.valueOf(coshArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
					}
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for times operation " + timesArgsNumber);
				}
			}
				break;
			case Function.ABS: {
				// TODO provide implementation
			}
				break;
			case Function.ACOS: {
				// TODO provide implementation
			}
				break;
			case Function.ASIN: {
				// TODO provide implementation
			}
				break;
			case Function.ATAN: {
				// TODO provide implementation
			}
				break;
			case Function.CBRT: {
				// TODO provide implementation
			}
				break;
			case Function.CEIL: {
				// TODO provide implementation
			}
				break;
			case Function.COS: {
				// TODO provide implementation
			}
				break;
			case Function.COSH: {
				// TODO provide implementation
			}
				break;
			case Function.POWER: {
				Power power = (Power) parentOperand;
				State powerState = power.getState();
				List<Double> powerArgs = power.getArguments();
				int powerArgsNumber = powerArgs.size();
				if (powerArgsNumber == 0) {
					if (coshArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (coshArgsNumber == 1) {
						Double coshArg = coshArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(coshArg));
						tokenElement.setTextContent(String.valueOf(coshArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
					}
				} else if (powerArgsNumber == 1) {
					if (powerState == State.FIRST_OPERAND) {
						if (coshArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (coshArgsNumber == 1) {
							Double coshArg = coshArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(coshArg));
							tokenElement.setTextContent(String.valueOf(coshArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
						}
					} else if (powerState == State.SECOND_OPERAND) {
						if (coshArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (coshArgsNumber == 1) {
							Double coshArg = coshArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(coshArg));
							tokenElement.setTextContent(String.valueOf(coshArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
						}

						Double powerArg = powerArgs.get(0);
						Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
						powerTokenElement.setTextContent(String.valueOf(powerArg));

						parentApplyElement.appendChild(powerTokenElement);
					}
				} else if (powerArgsNumber == 2) {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
				}
			}
				break;
			case Function.SIN: {
				Sin sin = (Sin) parentOperand;
				List<Double> sinArgs = sin.getArguments();
				int sinArgsNumber = sinArgs.size();
				if (sinArgsNumber == 0) {
					if (coshArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (coshArgsNumber == 1) {
						Double coshArg = coshArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(coshArg));
						tokenElement.setTextContent(String.valueOf(coshArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
					}
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
				}
			}
				break;
			case Function.SQRT: {
				Sqrt sqrt = (Sqrt) parentOperand;
				List<Double> sqrtArgs = sqrt.getArguments();
				int sqrtArgsNumber = sqrtArgs.size();
				if (sqrtArgsNumber == 0) {
					if (coshArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (coshArgsNumber == 1) {
						Double coshArg = coshArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(coshArg));
						tokenElement.setTextContent(String.valueOf(coshArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
					}
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
				}
			}
				break;
			case Function.TAN: {
				Tan tan = (Tan) parentOperand;
				List<Double> tanArgs = tan.getArguments();
				int tanArgsNumber = tanArgs.size();
				if (tanArgsNumber == 0) {
					if (coshArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (coshArgsNumber == 1) {
						Double coshArg = coshArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(coshArg));
						tokenElement.setTextContent(String.valueOf(coshArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
					}
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
				}
			}
				break;
			case Function.TANH: {
				Tanh tanh = (Tanh) parentOperand;
				List<Double> tanhArgs = tanh.getArguments();
				int tanhArgsNumber = tanhArgs.size();
				if (tanhArgsNumber == 0) {
					if (coshArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (coshArgsNumber == 1) {
						Double coshArg = coshArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(coshArg));
						tokenElement.setTextContent(String.valueOf(coshArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cosh function " + coshArgsNumber);
					}
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for tanh function " + tanhArgsNumber);
				}
			}
				break;
			}
		}

		return applyElement;
	}

	private Element appendTanFunction(Element parentApplyElement, MathematicalOperation parentOperand, Tan tan)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element tanElement = mathMLDoc.createElement(MathMLConstants.MML_TAN);
		applyElement.appendChild(tanElement);

		List<Double> tanArgs = tan.getArguments();
		int tanArgsNumber = tanArgs.size();

		if (isDirectChildOperand(tan)) {
			if (tanArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (tanArgsNumber == 1) {
				Double tanArg = tanArgs.get(0);
				Element tokenElement = createTokenElement(String.valueOf(tanArg));
				tokenElement.setTextContent(String.valueOf(tanArg));

				applyElement.appendChild(tokenElement);

				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
			}
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentOperationType = parentOperation.getType();
				switch (parentOperationType) {
				case Operation.PLUS: {
					if (tanArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (tanArgsNumber == 1) {
						Double tanArg = tanArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(tanArg));
						tokenElement.setTextContent(String.valueOf(tanArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;
				case Operation.MINUS: {
					Minus minus = (Minus) parentOperation;
					State minusState = minus.getState();
					List<Double> minusArgs = minus.getArguments();
					int minusArgsNumber = minusArgs.size();
					if (minusArgsNumber == 0) {
						if (tanArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (tanArgsNumber == 1) {
							Double tanArg = tanArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(tanArg));
							tokenElement.setTextContent(String.valueOf(tanArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
						}
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							if (tanArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (tanArgsNumber == 1) {
								Double tanArg = tanArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(tanArg));
								tokenElement.setTextContent(String.valueOf(tanArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function "
												+ tanArgsNumber);
							}
						} else if (minusState == State.SECOND_OPERAND) {
							if (tanArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (tanArgsNumber == 1) {
								Double tanArg = tanArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(tanArg));
								tokenElement.setTextContent(String.valueOf(tanArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function "
												+ tanArgsNumber);
							}

							Double minusArg = minusArgs.get(0);
							Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
							minusTokenElement.setTextContent(String.valueOf(minusArg));

							parentApplyElement.appendChild(minusTokenElement);
						}
					} else if (minusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division division = (Division) parentOperation;
					State divState = division.getState();
					List<Double> divArgs = division.getArguments();
					int divArgsNumber = divArgs.size();
					if (divArgsNumber == 0) {
						if (tanArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (tanArgsNumber == 1) {
							Double tanArg = tanArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(tanArg));
							tokenElement.setTextContent(String.valueOf(tanArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
						}
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							if (tanArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (tanArgsNumber == 1) {
								Double tanArg = tanArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(tanArg));
								tokenElement.setTextContent(String.valueOf(tanArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function "
												+ tanArgsNumber);
							}
						} else if (divState == State.SECOND_OPERAND) {
							if (tanArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (tanArgsNumber == 1) {
								Double tanArg = tanArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(tanArg));
								tokenElement.setTextContent(String.valueOf(tanArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function "
												+ tanArgsNumber);
							}

							Double divArg = divArgs.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (divArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					if (tanArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (tanArgsNumber == 1) {
						Double tanArg = tanArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(tanArg));
						tokenElement.setTextContent(String.valueOf(tanArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;

				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin sin = (Sin) parentFunction;
					List<Double> sinArgs = sin.getArguments();
					int sinArgsNumber = sinArgs.size();

					if (sinArgsNumber == 0) {
						if (tanArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (tanArgsNumber == 1) {
							Double tanArg = tanArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(tanArg));
							tokenElement.setTextContent(String.valueOf(tanArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				}
					break;
				case Function.COS: {
					Cos cos = (Cos) parentFunction;
					List<Double> cosArgs = cos.getArguments();
					int cosArgsNumber = cosArgs.size();
					if (cosArgsNumber == 0) {
						if (tanArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (tanArgsNumber == 1) {
							Double tanArg = tanArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(tanArg));
							tokenElement.setTextContent(String.valueOf(tanArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}
				}
					break;
				case Function.TAN: {
					Tan parentTan = (Tan) parentFunction;
					List<Double> parentTanArgs = parentTan.getArguments();
					int parentTanArgsNumber = parentTanArgs.size();
					if (parentTanArgsNumber == 0) {
						if (tanArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (tanArgsNumber == 1) {
							Double tanArg = tanArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(tanArg));
							tokenElement.setTextContent(String.valueOf(tanArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for parentTan function "
										+ parentTanArgsNumber);
					}
				}
					break;
				case Function.SQRT: {
					Sqrt sqrt = (Sqrt) parentFunction;
					List<Double> sqrtArgs = sqrt.getArguments();
					int sqrtArgsNumber = sqrtArgs.size();
					if (sqrtArgsNumber == 0) {
						if (tanArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (tanArgsNumber == 1) {
							Double tanArg = tanArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(tanArg));
							tokenElement.setTextContent(String.valueOf(tanArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				case Function.POWER: {
					Power power = (Power) parentFunction;
					State powerState = power.getState();
					List<Double> powerArgs = power.getArguments();
					int powerArgsNumber = powerArgs.size();
					if (powerArgsNumber == 0) {
						if (tanArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (tanArgsNumber == 1) {
							Double tanArg = tanArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(tanArg));
							tokenElement.setTextContent(String.valueOf(tanArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
						}
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							if (tanArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (tanArgsNumber == 1) {
								Double tanArg = tanArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(tanArg));
								tokenElement.setTextContent(String.valueOf(tanArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function "
												+ tanArgsNumber);
							}
						} else if (powerState == State.SECOND_OPERAND) {
							if (tanArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (tanArgsNumber == 1) {
								Double tanArg = tanArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(tanArg));
								tokenElement.setTextContent(String.valueOf(tanArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function "
												+ tanArgsNumber);
							}

							Double powerArg = powerArgs.get(0);
							Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
							powerTokenElement.setTextContent(String.valueOf(powerArg));

							parentApplyElement.appendChild(powerTokenElement);
						}
					} else if (powerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
					break;
				}
			}
		}

		return applyElement;
	}

	private Element appendTanhFunction(Element parentApplyElement, MathematicalOperation parentOperand, Tanh tanh)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element tanhElement = mathMLDoc.createElement(MathMLConstants.MML_TANH);
		applyElement.appendChild(tanhElement);

		List<Double> tanhArgs = tanh.getArguments();
		int tanhArgsNumber = tanhArgs.size();

		if (isDirectChildOperand(tanh)) {
			if (tanhArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (tanhArgsNumber == 1) {
				Double tanhArgument = tanhArgs.get(0);
				Element tokenElement = createTokenElement(String.valueOf(tanhArgument));
				tokenElement.setTextContent(String.valueOf(tanhArgument));

				applyElement.appendChild(tokenElement);

				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for tanh function " + tanhArgsNumber);
			}
		} else {
			int parentOperationType = parentOperand.getType();
			switch (parentOperationType) {
			case Operation.PLUS: {
				if (tanhArgsNumber == 0) {
					parentApplyElement.appendChild(applyElement);
				} else if (tanhArgsNumber == 1) {
					Double tanhArgument = tanhArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(tanhArgument));
					tokenElement.setTextContent(String.valueOf(tanhArgument));

					applyElement.appendChild(tokenElement);

					parentApplyElement.appendChild(applyElement);
				} else {
					throw new MalformedMathMLExpressionException("Illegal number of arguments for tanh function " + tanhArgsNumber);
				}
			}
				break;
			case Operation.MINUS: {
				// TODO provide implementation
			}
				break;
			case Operation.DIVISION: {
				// TODO provide implementation
			}
				break;
			case Operation.MULTIPLICATION: {
				// TODO provide implementation
			}
				break;
			case Function.ABS: {
				// TODO provide implementation
			}
				break;
			case Function.ACOS: {
				// TODO provide implementation
			}
				break;
			case Function.ASIN: {
				// TODO provide implementation
			}
				break;
			case Function.ATAN: {
				// TODO provide implementation
			}
				break;
			case Function.CBRT: {
				// TODO provide implementation
			}
				break;
			case Function.CEIL: {
				// TODO provide implementation
			}
				break;
			case Function.COS: {
				// TODO provide implementation
			}
				break;
			case Function.COSH: {
				// TODO provide implementation
			}
				break;
			case Function.POWER: {
				// TODO provide implementation
			}
				break;
			case Function.SIN: {
				// TODO provide implementation
			}
				break;
			case Function.SQRT: {
				// TODO provide implementation
			}
				break;
			case Function.TAN: {
				// TODO provide implementation
			}
				break;
			case Function.TANH: {
				// TODO provide implementation
			}
				break;
			}
		}

		return applyElement;
	}

	private Element appendSqrtFunction(Element parentApplyElement, MathematicalOperation parentOperand, Sqrt sqrt)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element sqrtElement = mathMLDoc.createElement(MathMLConstants.MML_SQRT);
		applyElement.appendChild(sqrtElement);

		List<Double> sqrtArgs = sqrt.getArguments();
		int sqrtArgsNumber = sqrtArgs.size();

		if (isDirectChildOperand(sqrt)) {
			if (sqrtArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (sqrtArgsNumber == 1) {
				Double sqrtArg = sqrtArgs.get(0);
				Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
				tokenElement.setTextContent(String.valueOf(sqrtArg));

				applyElement.appendChild(tokenElement);

				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
			}
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentOperationType = parentOperation.getType();
				switch (parentOperationType) {
				case Operation.PLUS: {
					if (sqrtArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (sqrtArgsNumber == 1) {
						Double sqrtArg = sqrtArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
						tokenElement.setTextContent(String.valueOf(sqrtArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				case Operation.MINUS: {
					Minus minus = (Minus) parentOperation;
					State minusState = minus.getState();
					List<Double> minusArgs = minus.getArguments();
					int minusArgsNumber = minusArgs.size();
					if (minusArgsNumber == 0) {
						if (sqrtArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sqrtArgsNumber == 1) {
							Double sqrtArg = sqrtArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
							tokenElement.setTextContent(String.valueOf(sqrtArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
						}
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							if (sqrtArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sqrtArgsNumber == 1) {
								Double sqrtArg = sqrtArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
								tokenElement.setTextContent(String.valueOf(sqrtArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function "
												+ sqrtArgsNumber);
							}
						} else if (minusState == State.SECOND_OPERAND) {
							if (sqrtArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sqrtArgsNumber == 1) {
								Double sqrtArg = sqrtArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
								tokenElement.setTextContent(String.valueOf(sqrtArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function "
												+ sqrtArgsNumber);
							}

							Double minusArg = minusArgs.get(0);
							Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
							minusTokenElement.setTextContent(String.valueOf(minusArg));

							parentApplyElement.appendChild(minusTokenElement);
						}
					} else if (minusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division division = (Division) parentOperation;
					State divState = division.getState();
					List<Double> divArgs = division.getArguments();
					int divArgsNumber = divArgs.size();
					if (divArgsNumber == 0) {
						if (sqrtArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sqrtArgsNumber == 1) {
							Double sqrtArg = sqrtArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
							tokenElement.setTextContent(String.valueOf(sqrtArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
						}
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							if (sqrtArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sqrtArgsNumber == 1) {
								Double sqrtArg = sqrtArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
								tokenElement.setTextContent(String.valueOf(sqrtArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function "
												+ sqrtArgsNumber);
							}
						} else if (divState == State.SECOND_OPERAND) {
							if (sqrtArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sqrtArgsNumber == 1) {
								Double sqrtArg = sqrtArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
								tokenElement.setTextContent(String.valueOf(sqrtArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function "
												+ sqrtArgsNumber);
							}

							Double divArg = divArgs.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (divArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					if (sqrtArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (sqrtArgsNumber == 1) {
						Double sqrtArg = sqrtArgs.get(0);
						Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
						tokenElement.setTextContent(String.valueOf(sqrtArg));

						applyElement.appendChild(tokenElement);

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}
				}
					break;
				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin sin = (Sin) parentFunction;
					List<Double> sinArgs = sin.getArguments();
					int sinArgsNumber = sinArgs.size();
					if (sinArgsNumber == 0) {
						if (sqrtArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sqrtArgsNumber == 1) {
							Double sqrtArg = sqrtArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
							tokenElement.setTextContent(String.valueOf(sqrtArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}

				}
					break;
				case Function.COS: {
					Cos cos = (Cos) parentFunction;
					List<Double> cosArgs = cos.getArguments();
					int cosArgsNumber = cosArgs.size();
					if (cosArgsNumber == 0) {
						if (sqrtArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sqrtArgsNumber == 1) {
							Double sqrtArg = sqrtArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
							tokenElement.setTextContent(String.valueOf(sqrtArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}

				}
					break;
				case Function.TAN: {
					Tan tan = (Tan) parentFunction;
					List<Double> tanArgs = tan.getArguments();
					int tanArgsNumber = tanArgs.size();
					if (tanArgsNumber == 0) {
						if (sqrtArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sqrtArgsNumber == 1) {
							Double sqrtArg = sqrtArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
							tokenElement.setTextContent(String.valueOf(sqrtArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}

				}
					break;
				case Function.SQRT: {
					Sqrt parentSqrt = (Sqrt) parentFunction;
					List<Double> parentSqrtArgs = parentSqrt.getArguments();
					int parentSqrtArgsNumber = parentSqrtArgs.size();
					if (parentSqrtArgsNumber == 0) {
						if (sqrtArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sqrtArgsNumber == 1) {
							Double sqrtArg = sqrtArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
							tokenElement.setTextContent(String.valueOf(sqrtArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for parentSqrt function "
										+ parentSqrtArgsNumber);
					}
				}
					break;
				case Function.POWER: {
					Power power = (Power) parentFunction;
					State powerState = power.getState();
					List<Double> powerArgs = power.getArguments();
					int powerArgsNumber = powerArgs.size();
					if (powerArgsNumber == 0) {
						if (sqrtArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (sqrtArgsNumber == 1) {
							Double sqrtArg = sqrtArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
							tokenElement.setTextContent(String.valueOf(sqrtArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
						}
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							if (sqrtArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sqrtArgsNumber == 1) {
								Double sqrtArg = sqrtArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
								tokenElement.setTextContent(String.valueOf(sqrtArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function "
												+ sqrtArgsNumber);
							}
						} else if (powerState == State.SECOND_OPERAND) {
							if (sqrtArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (sqrtArgsNumber == 1) {
								Double sqrtArg = sqrtArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(sqrtArg));
								tokenElement.setTextContent(String.valueOf(sqrtArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function "
												+ sqrtArgsNumber);
							}

							Double powerArg = powerArgs.get(0);
							Element powerTokenElement = createTokenElement(String.valueOf(powerArg));
							powerTokenElement.setTextContent(String.valueOf(powerArg));

							parentApplyElement.appendChild(powerTokenElement);
						}
					} else if (powerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
					break;
				}
			}
		}

		return applyElement;
	}

	private Element appendPowerFunction(Element parentApplyElement, Object parentOperand, Power power)
					throws MalformedMathMLExpressionException {
		Element applyElement = mathMLDoc.createElement(MathMLConstants.MML_APPLY);
		Element powerElement = mathMLDoc.createElement(MathMLConstants.MML_POWER);
		applyElement.appendChild(powerElement);

		List<Double> powerArgs = power.getArguments();
		int powerArgsNumber = powerArgs.size();
		State powerState = power.getState();

		if (isDirectChildOperand(power)) {
			if (powerArgsNumber == 0) {
				parentApplyElement.appendChild(applyElement);
			} else if (powerArgsNumber == 1) {
				if (powerState == State.FIRST_OPERAND) {
					Double powerArg = powerArgs.get(0);
					Element tokenElement = createTokenElement(String.valueOf(powerArg));
					tokenElement.setTextContent(String.valueOf(powerArg));

					applyElement.appendChild(tokenElement);

					parentApplyElement.appendChild(applyElement);
				} else if (powerState == State.SECOND_OPERAND) {
					parentApplyElement.appendChild(applyElement);
				}
			} else if (powerArgsNumber == 2) {
				for (int i = 0; i < powerArgsNumber; i++) {
					Double powerArg = powerArgs.get(i);
					Element tokenElement = createTokenElement(String.valueOf(powerArg));
					tokenElement.setTextContent(String.valueOf(powerArg));

					applyElement.appendChild(tokenElement);
				}

				parentApplyElement.appendChild(applyElement);
			} else {
				throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
			}
		} else {
			if (parentOperand instanceof Operation) {
				Operation parentOperation = (Operation) parentOperand;
				int parentOperationType = parentOperation.getType();
				switch (parentOperationType) {
				case Operation.PLUS: {
					if (powerArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							Double powerArg = powerArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(powerArg));
							tokenElement.setTextContent(String.valueOf(powerArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else if (powerState == State.SECOND_OPERAND) {
							parentApplyElement.appendChild(applyElement);
						}
					} else if (powerArgsNumber == 2) {
						for (int i = 0; i < powerArgsNumber; i++) {
							Double powerArg = powerArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(powerArg));
							tokenElement.setTextContent(String.valueOf(powerArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
					break;
				case Operation.MINUS: {
					Minus minus = (Minus) parentOperation;
					State minusState = minus.getState();
					List<Double> minusArgs = minus.getArguments();
					int minusArgsNumber = minusArgs.size();
					if (minusArgsNumber == 0) {
						if (powerArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (powerArgsNumber == 1) {
							if (powerState == State.FIRST_OPERAND) {
								Double powerArg = powerArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (powerState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (powerArgsNumber == 2) {
							for (int i = 0; i < powerArgsNumber; i++) {
								Double powerArg = powerArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
											+ powerArgsNumber);
						}
					} else if (minusArgsNumber == 1) {
						if (minusState == State.FIRST_OPERAND) {
							if (powerArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (powerArgsNumber == 1) {
								if (powerState == State.FIRST_OPERAND) {
									Double powerArg = powerArgs.get(0);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (powerState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (powerArgsNumber == 2) {
								for (int i = 0; i < powerArgsNumber; i++) {
									Double powerArg = powerArgs.get(i);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
												+ powerArgsNumber);
							}
						} else if (minusState == State.SECOND_OPERAND) {
							if (powerArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (powerArgsNumber == 1) {
								if (powerState == State.FIRST_OPERAND) {
									Double powerArg = powerArgs.get(0);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (powerState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (powerArgsNumber == 2) {
								for (int i = 0; i < powerArgsNumber; i++) {
									Double powerArg = powerArgs.get(i);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
												+ powerArgsNumber);
							}

							Double minusArg = minusArgs.get(0);
							Element minusTokenElement = createTokenElement(String.valueOf(minusArg));
							minusTokenElement.setTextContent(String.valueOf(minusArg));

							parentApplyElement.appendChild(minusTokenElement);
						}
					} else if (minusArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for minus operation " + minusArgsNumber);
					}
				}
					break;
				case Operation.DIVISION: {
					Division division = (Division) parentOperation;
					State divState = division.getState();
					List<Double> divArgs = division.getArguments();
					int divArgsNumber = divArgs.size();
					if (divArgsNumber == 0) {
						if (powerArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (powerArgsNumber == 1) {
							if (powerState == State.FIRST_OPERAND) {
								Double powerArg = powerArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (powerState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (powerArgsNumber == 2) {
							for (int i = 0; i < powerArgsNumber; i++) {
								Double powerArg = powerArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
											+ powerArgsNumber);
						}
					} else if (divArgsNumber == 1) {
						if (divState == State.FIRST_OPERAND) {
							if (powerArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (powerArgsNumber == 1) {
								if (powerState == State.FIRST_OPERAND) {
									Double powerArg = powerArgs.get(0);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (powerState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (powerArgsNumber == 2) {
								for (int i = 0; i < powerArgsNumber; i++) {
									Double powerArg = powerArgs.get(i);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
												+ powerArgsNumber);
							}
						} else if (divState == State.SECOND_OPERAND) {
							if (powerArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (powerArgsNumber == 1) {
								if (powerState == State.FIRST_OPERAND) {
									Double powerArg = powerArgs.get(0);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (powerState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (powerArgsNumber == 2) {
								for (int i = 0; i < powerArgsNumber; i++) {
									Double powerArg = powerArgs.get(i);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
												+ powerArgsNumber);
							}

							Double divArg = divArgs.get(0);
							Element divTokenElement = createTokenElement(String.valueOf(divArg));
							divTokenElement.setTextContent(String.valueOf(divArg));

							parentApplyElement.appendChild(divTokenElement);
						}
					} else if (divArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for divide operation " + divArgsNumber);
					}
				}
					break;
				case Operation.MULTIPLICATION: {
					if (powerArgsNumber == 0) {
						parentApplyElement.appendChild(applyElement);
					} else if (powerArgsNumber == 1) {
						if (powerState == State.FIRST_OPERAND) {
							Double powerArg = powerArgs.get(0);
							Element tokenElement = createTokenElement(String.valueOf(powerArg));
							tokenElement.setTextContent(String.valueOf(powerArg));

							applyElement.appendChild(tokenElement);

							parentApplyElement.appendChild(applyElement);
						} else if (powerState == State.SECOND_OPERAND) {
							parentApplyElement.appendChild(applyElement);
						}
					} else if (powerArgsNumber == 2) {
						for (int i = 0; i < powerArgsNumber; i++) {
							Double powerArg = powerArgs.get(i);
							Element tokenElement = createTokenElement(String.valueOf(powerArg));
							tokenElement.setTextContent(String.valueOf(powerArg));

							applyElement.appendChild(tokenElement);
						}

						parentApplyElement.appendChild(applyElement);
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
					break;
				}
			} else if (parentOperand instanceof Function) {
				Function parentFunction = (Function) parentOperand;
				int parentFunctionType = parentFunction.getType();
				switch (parentFunctionType) {
				case Function.SIN: {
					Sin sin = (Sin) parentFunction;
					List<Double> sinArgs = sin.getArguments();
					int sinArgsNumber = sinArgs.size();
					if (sinArgsNumber == 0) {
						if (powerArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (powerArgsNumber == 1) {
							if (powerState == State.FIRST_OPERAND) {
								Double powerArg = powerArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (powerState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (powerArgsNumber == 2) {
							for (int i = 0; i < powerArgsNumber; i++) {
								Double powerArg = powerArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
											+ powerArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sin function " + sinArgsNumber);
					}
				}
					break;
				case Function.COS: {
					Cos cos = (Cos) parentFunction;
					List<Double> cosArgs = cos.getArguments();
					int cosArgsNumber = cosArgs.size();
					if (cosArgsNumber == 0) {
						if (powerArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (powerArgsNumber == 1) {
							if (powerState == State.FIRST_OPERAND) {
								Double powerArg = powerArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (powerState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (powerArgsNumber == 2) {
							for (int i = 0; i < powerArgsNumber; i++) {
								Double powerArg = powerArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
											+ powerArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for cos function " + cosArgsNumber);
					}
				}
					break;
				case Function.TAN: {
					Tan tan = (Tan) parentFunction;
					List<Double> tanArgs = tan.getArguments();
					int tanArgsNumber = tanArgs.size();
					if (tanArgsNumber == 0) {
						if (powerArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (powerArgsNumber == 1) {
							if (powerState == State.FIRST_OPERAND) {
								Double powerArg = powerArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (powerState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (powerArgsNumber == 2) {
							for (int i = 0; i < powerArgsNumber; i++) {
								Double powerArg = powerArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
											+ powerArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for tan function " + tanArgsNumber);
					}
				}
					break;
				case Function.SQRT: {
					Sqrt sqrt = (Sqrt) parentFunction;
					List<Double> sqrtArgs = sqrt.getArguments();
					int sqrtArgsNumber = sqrtArgs.size();
					if (sqrtArgsNumber == 0) {
						if (powerArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (powerArgsNumber == 1) {
							if (powerState == State.FIRST_OPERAND) {
								Double powerArg = powerArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (powerState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (powerArgsNumber == 2) {
							for (int i = 0; i < powerArgsNumber; i++) {
								Double powerArg = powerArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
											+ powerArgsNumber);
						}
					} else {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for sqrt function " + sqrtArgsNumber);
					}

				}
					break;
				case Function.POWER: {
					Power parentPowerFn = (Power) parentFunction;
					State parentPowerState = parentPowerFn.getState();
					List<Double> parentPowerArgs = parentPowerFn.getArguments();
					int parentPowerArgsNumber = parentPowerArgs.size();
					if (parentPowerArgsNumber == 0) {
						if (powerArgsNumber == 0) {
							parentApplyElement.appendChild(applyElement);
						} else if (powerArgsNumber == 1) {
							if (powerState == State.FIRST_OPERAND) {
								Double powerArg = powerArgs.get(0);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);

								parentApplyElement.appendChild(applyElement);
							} else if (powerState == State.SECOND_OPERAND) {
								parentApplyElement.appendChild(applyElement);
							}
						} else if (powerArgsNumber == 2) {
							for (int i = 0; i < powerArgsNumber; i++) {
								Double powerArg = powerArgs.get(i);
								Element tokenElement = createTokenElement(String.valueOf(powerArg));
								tokenElement.setTextContent(String.valueOf(powerArg));

								applyElement.appendChild(tokenElement);
							}

							parentApplyElement.appendChild(applyElement);
						} else {
							throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
											+ powerArgsNumber);
						}
					} else if (parentPowerArgsNumber == 1) {
						if (parentPowerState == State.FIRST_OPERAND) {
							if (powerArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (powerArgsNumber == 1) {
								if (powerState == State.FIRST_OPERAND) {
									Double powerArg = powerArgs.get(0);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (powerState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (powerArgsNumber == 2) {
								for (int i = 0; i < powerArgsNumber; i++) {
									Double powerArg = powerArgs.get(i);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
												+ powerArgsNumber);
							}
						} else if (parentPowerState == State.SECOND_OPERAND) {
							if (powerArgsNumber == 0) {
								parentApplyElement.appendChild(applyElement);
							} else if (powerArgsNumber == 1) {
								if (powerState == State.FIRST_OPERAND) {
									Double powerArg = powerArgs.get(0);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);

									parentApplyElement.appendChild(applyElement);
								} else if (powerState == State.SECOND_OPERAND) {
									parentApplyElement.appendChild(applyElement);
								}
							} else if (powerArgsNumber == 2) {
								for (int i = 0; i < powerArgsNumber; i++) {
									Double powerArg = powerArgs.get(i);
									Element tokenElement = createTokenElement(String.valueOf(powerArg));
									tokenElement.setTextContent(String.valueOf(powerArg));

									applyElement.appendChild(tokenElement);
								}

								parentApplyElement.appendChild(applyElement);
							} else {
								throw new MalformedMathMLExpressionException("Illegal number of arguments for power function "
												+ powerArgsNumber);
							}

							Double parentPowerArg = parentPowerArgs.get(0);
							Element parentPowerTokenelement = createTokenElement(String.valueOf(parentPowerArg));
							parentPowerTokenelement.setTextContent(String.valueOf(parentPowerArg));

							parentApplyElement.appendChild(parentPowerTokenelement);
						}
					} else if (parentPowerArgsNumber == 2) {
						throw new MalformedMathMLExpressionException("Illegal number of arguments for power function " + powerArgsNumber);
					}
				}
				}
			}
		}

		return applyElement;
	}

	private Element createTokenElement(String operandArgument) {
		Element tokenElement = null;
		try {
			new Double(operandArgument);

			tokenElement = mathMLDoc.createElement(MathMLConstants.MML_CN);
		} catch (NumberFormatException e) {
			tokenElement = mathMLDoc.createElement(MathMLConstants.MML_CI);
		}

		return tokenElement;
	}

	private Element createParentOperandElement(MathematicalOperation operand) {
		Element parentElement = null;
		if (operand instanceof Operation) {
			Operation operation = (Operation) operand;
			int operationType = operation.getType();
			switch (operationType) {
			case Operation.PLUS: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_PLUS);
			}
				break;
			case Operation.MINUS: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_MINUS);
			}
				break;
			case Operation.DIVISION: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_DIVIDE);
			}
				break;
			case Operation.MULTIPLICATION: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_TIMES);
			}
				break;

			}
		} else if (operand instanceof Function) {
			Function function = (Function) operand;
			int functionType = function.getType();
			switch (functionType) {
			case Function.SIN: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_SIN);
			}
				break;
			case Function.COS: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_COS);
			}
				break;
			case Function.TAN: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_TAN);
			}
				break;
			case Function.SQRT: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_SQRT);
			}
				break;
			case Function.POWER: {
				parentElement = mathMLDoc.createElement(MathMLConstants.MML_POWER);
			}
				break;
			}
		}

		return parentElement;
	}

	private boolean isDirectChildOperand(MathematicalOperation operand) {
		Object parentOperand = operands.get(operands.size() - 1);

		if (parentOperand instanceof Operation) {
			Operation parentOperation = (Operation) parentOperand;
			List<Integer> relativeParentArgPositionList = parentOperation.getArgsDistanceFromMathOperation();

			int absoluteOperationPosition = operands.indexOf(operand);
			int relativeOpetaionPosition = operands.indexOf(parentOperation) - absoluteOperationPosition;

			if (relativeParentArgPositionList.contains(Integer.valueOf(relativeOpetaionPosition))) {
				return true;
			}
		} else if (parentOperand instanceof Function) {
			Function parentFunction = (Function) parentOperand;
			List<Integer> relativeParentArgPositionList = parentFunction.getArgsDistanceFromMathOperation();

			int absoluteFunctionPosition = operands.indexOf(operand);
			int relativeFunctionPosition = operands.indexOf(parentFunction) - absoluteFunctionPosition;

			if (relativeParentArgPositionList.contains(Integer.valueOf(relativeFunctionPosition))) {
				return true;
			}
		}

		return false;
	}
}
