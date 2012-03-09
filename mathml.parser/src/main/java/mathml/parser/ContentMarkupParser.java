package mathml.parser;

import static mathml.api.MathMLConstants.APPLY;
import static mathml.api.MathMLConstants.DIVIDE;
import static mathml.api.MathMLConstants.MINUS;
import static mathml.api.MathMLConstants.POWER;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import mathml.api.IMathMLExpression;
import mathml.api.IMathMLParser;
import mathml.api.MathMLConstants;
import mathml.api.MathematicalOperation;
import mathml.api.State;
import mathml.core.Expression;
import mathml.utils.MathMLUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

public class ContentMarkupParser implements IMathMLParser {
	private final List<MathematicalOperation> expressionOperands = new ArrayList<MathematicalOperation>();

	private final XPath xPath;

	public ContentMarkupParser() {
		XPathFactory xpathFactory = XPathFactory.newInstance();

		xPath = xpathFactory.newXPath();
	}

	public IMathMLExpression parse(InputStream mathMLContent) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document mathMLDocument = builder.parse(mathMLContent);

		DocumentTraversal traversible = (DocumentTraversal) mathMLDocument;
		TreeWalker walker = traversible.createTreeWalker(mathMLDocument.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);

		MathMLUtilities.removeWhitespaceFromMathMLDocument(mathMLDocument);

		NodeList applyElementList = mathMLDocument.getElementsByTagName(MathMLConstants.MML_APPLY);
		for (int i = applyElementList.getLength() - 1; i >= 0; i--) {
			Node applyNode = applyElementList.item(i);
			
			parseMathMLApplyNode(walker, applyNode);
		}

		IMathMLExpression expression = new Expression(expressionOperands);

		return expression;
	}

	private void parseMathMLApplyNode(TreeWalker walker, Node applyNode) throws NumberFormatException {
		List<Double> arguments = new ArrayList<Double>();

		List<Integer> nestedApplyNodesNumber = new ArrayList<Integer>();
		
		walker.setCurrentNode(applyNode);

		int directApplyNodes = countDirectApplyNodes(applyNode);
		
		while (applyNode != null) {
			Node operation = walker.firstChild();
			Node token = null;
			while ((token = walker.nextSibling()) != null) {
				String tokenName = token.getNodeName();
				if (tokenName.equals(MathMLConstants.MML_APPLY)) {
					if (directApplyNodes >= 1) {
						int descendantApplyNodes = countDescendantApplyNodes(token);
						
						nestedApplyNodesNumber.add(descendantApplyNodes);
					}
					continue;
				}

				String tokenValue = token.getTextContent();
				arguments.add(Double.valueOf(tokenValue));
			}// end of inner while
			
			addMathematicalOperationToExpression(operation, arguments, nestedApplyNodesNumber);

			applyNode = walker.nextSibling();
		}// end of while
	}

	private int countDirectApplyNodes(Node applyNode) {
		int applyNodesNumber = 0;
		try {
			XPathExpression xPathExpression = xPath.compile("count(child::*[name()='" + MathMLConstants.MML_APPLY + "'])");
			Object result = xPathExpression.evaluate(applyNode, XPathConstants.NUMBER);
			if (result instanceof Double) {
				Double d = (Double) result;
				applyNodesNumber = d.intValue();
			} else {
				throw new ClassCastException("cannot convert " + result + " to Double");
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return applyNodesNumber;
	}

	private int countDescendantApplyNodes(Node applyNode) {
		int descendantApplyNodesNumber = 0;
		try {
			XPathExpression xPathExpression = xPath.compile("count(descendant::*[name()='" + MathMLConstants.MML_APPLY + "'])");
			Object result = xPathExpression.evaluate(applyNode, XPathConstants.NUMBER);
			if (result instanceof Double) {
				Double d = (Double) result;
				descendantApplyNodesNumber = d.intValue();
			} else {
				throw new ClassCastException("cannot convert " + result + " to Double");
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return descendantApplyNodesNumber;
	}

	private void addMathematicalOperationToExpression(Node operation, List<Double> arguments, List<Integer> nestedApplyNodeNumber) {
		MathematicalOperation mathOperation = null;

		String operationToBeApplied = operation.getLocalName();

		if (operationToBeApplied.equals(DIVIDE) || operationToBeApplied.equals(MINUS) || operationToBeApplied.equals(POWER)) {

			Element nextSibling = (Element) operation.getNextSibling();

			if (nextSibling.getLocalName().equals(APPLY)) {
				mathOperation = MathMLUtilities.createMathOperation(operationToBeApplied, arguments, State.SECOND_OPERAND,
								nestedApplyNodeNumber);
			} else {
				mathOperation = MathMLUtilities.createMathOperation(operationToBeApplied, arguments, State.FIRST_OPERAND,
								nestedApplyNodeNumber);
			}
		} else {
			mathOperation = MathMLUtilities.createMathOperation(operationToBeApplied, arguments, nestedApplyNodeNumber);
		}

		expressionOperands.add(mathOperation);
	}
}
