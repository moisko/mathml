package mathml.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import mathml.api.Function;
import mathml.api.MathematicalOperation;
import mathml.api.Operation;
import mathml.api.State;
import mathml.function.ACos;
import mathml.function.ASin;
import mathml.function.ATan;
import mathml.function.Abs;
import mathml.function.Ceil;
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
import mathml.sax.helpers.MathMLErrorHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class MathMLUtilities {

	private final static MathOperationTypes MATH_OPERATION_TYPES = MathOperationTypes.newInstance();

	public static int getMathematicalOperationType(String nodeName) {
		int operationType = MATH_OPERATION_TYPES.getMathOperationTypeFor(nodeName);

		return operationType;
	}

	public static MathematicalOperation createMathOperation(String operationName, List<Double> arguments, List<Integer> nestedNodesNumber) {
		MathematicalOperation mathematicalOperation = null;

		int operationType = MATH_OPERATION_TYPES.getMathOperationTypeFor(operationName);

		switch (operationType) {
		case Function.COS:
			mathematicalOperation = new Cos(arguments, nestedNodesNumber);
			break;
		case Function.COSH:
			mathematicalOperation = new Cosh(arguments, nestedNodesNumber);
			break;
		case Function.SIN:
			mathematicalOperation = new Sin(arguments, nestedNodesNumber);
			break;
		case Function.SQRT:
			mathematicalOperation = new Sqrt(arguments, nestedNodesNumber);
			break;
		case Function.TAN:
			mathematicalOperation = new Tan(arguments, nestedNodesNumber);
			break;
		case Function.POWER:
			mathematicalOperation = new Power(arguments, State.FIRST_OPERAND, nestedNodesNumber);
			break;
		case Function.ABS:
			mathematicalOperation = new Abs(arguments, nestedNodesNumber);
			break;
		case Function.ACOS:
			mathematicalOperation = new ACos(arguments, nestedNodesNumber);
			break;
		case Function.ASIN:
			mathematicalOperation = new ASin(arguments, nestedNodesNumber);
			break;
		case Function.ATAN:
			mathematicalOperation = new ATan(arguments, nestedNodesNumber);
			break;
		case Function.CEIL:
			mathematicalOperation = new Ceil(arguments, nestedNodesNumber);
			break;
		case Function.TANH:
			mathematicalOperation = new Tanh(arguments, nestedNodesNumber);
			break;

		case Function.MROOT:
			// TODO provide implementation

			break;
		case Function.MSQRT:
			mathematicalOperation = new Sqrt(arguments, nestedNodesNumber);
			break;

		case Operation.PLUS:
			mathematicalOperation = new Plus(arguments, nestedNodesNumber);
			break;
		case Operation.MINUS:
			mathematicalOperation = new Minus(arguments, State.FIRST_OPERAND, nestedNodesNumber);
			break;
		case Operation.DIVISION:
			mathematicalOperation = new Division(arguments, State.FIRST_OPERAND, nestedNodesNumber);
			break;
		case Operation.MULTIPLICATION:
			mathematicalOperation = new Multiplication(arguments, nestedNodesNumber);
			break;
		}

		return mathematicalOperation;
	}

	public static MathematicalOperation createMathOperation(String operationName, List<Double> arguments, State state,
					List<Integer> nestedNodesNumber) {
		MathematicalOperation mathematicOperation = null;

		int operationType = MATH_OPERATION_TYPES.getMathOperationTypeFor(operationName);
		// TODO check if arythmeticType == -1 then throw an exception

		switch (operationType) {
		// Operation
		case Operation.MINUS:
			mathematicOperation = new Minus(arguments, state, nestedNodesNumber);
			break;
		case Operation.DIVISION:
			mathematicOperation = new Division(arguments, state, nestedNodesNumber);
			break;
		case Function.POWER:
			mathematicOperation = new Power(arguments, state, nestedNodesNumber);
			break;
		}
		return mathematicOperation;
	}

	public static void validateMathMLFile(InputStream mathMLContent, Schema schema, MathMLErrorHandler mathMLErrorHandler)
					throws SAXException, IOException {
		Validator validator = schema.newValidator();
		validator.setErrorHandler(mathMLErrorHandler);
		validator.validate(new StreamSource(mathMLContent));
	}

	public static void removeWhitespaceFromMathMLDocument(Document mathMLDocument) {
		NodeList nodeList = mathMLDocument.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element element = (Element) nodeList.item(i);
			removeWhitespaceFromMathMLElement(element);
		}
	}

	private static void removeWhitespaceFromMathMLElement(Element e) {
		NodeList children = e.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
			Node child = children.item(i);
			if (child instanceof Text && ((Text) child).getData().trim().length() == 0) {
				e.removeChild(child);
			} else if (child instanceof Element) {
				removeWhitespaceFromMathMLElement((Element) child);
			}
		}
	}
}
