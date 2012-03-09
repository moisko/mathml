package mathml.parser;

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
import mathml.api.UnsupportedMathMLElementException;
import mathml.core.Expression;
import mathml.utils.MathMLUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

public class PresentationMarkupParser implements IMathMLParser {

	private final List<MathematicalOperation> expressionOperands = new ArrayList<MathematicalOperation>();

	private final XPath xPath;

	public PresentationMarkupParser() {
		XPathFactory xpathFactory = XPathFactory.newInstance();

		xPath = xpathFactory.newXPath();
	}

	@Override
	public IMathMLExpression parse(InputStream mathMLContent) throws ParserConfigurationException, SAXException, IOException,
					UnsupportedMathMLElementException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document mathMLDocument = builder.parse(mathMLContent);

		DocumentTraversal traversible = (DocumentTraversal) mathMLDocument;
		TreeWalker walker = traversible.createTreeWalker(mathMLDocument.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);

		MathMLUtilities.removeWhitespaceFromMathMLDocument(mathMLDocument);

		NodeList mRowElementList = mathMLDocument.getElementsByTagName(MathMLConstants.MROW_ELEMENT);
		for (int i = mRowElementList.getLength() - 1; i >= 0; i--) {
			Node mRowNode = mRowElementList.item(i);
			parseMRowNode(walker, mRowNode);
		}

		IMathMLExpression expression = new Expression(expressionOperands);

		return expression;
	}

	private void parseMRowNode(TreeWalker walker, Node mRowNode) throws UnsupportedMathMLElementException {
		final List<Double> arguments = new ArrayList<Double>();

		final List<Integer> nestedMRowNodesNumber = new ArrayList<Integer>();

		walker.setCurrentNode(mRowNode);

		// TODO get the parent <mrow> node and check its type

		while (mRowNode != null) {
			Node firstChild = walker.firstChild();
			PresentationMarkupElementCategory operationCategory = getCategoryType(firstChild);
			switch (operationCategory) {
			case TOKEN: {
				// TODO provide implementation
				Node token = null;
				while(getCategoryType(walker.nextNode()) == PresentationMarkupElementCategory.TOKEN){
					
				}
			}
				break;
			case LAYOUT_SCHEMATA: {
				Node token = null;
				while ((token = walker.nextNode()) != null) {
					String tokenName = token.getNodeName();
					if (tokenName.equals(MathMLConstants.MROW_ELEMENT)) {
						// TODO provide implementation
					}
					String argument = token.getTextContent();
					arguments.add(Double.valueOf(argument));
				}// end of while

				addMathOperationToExpression(firstChild, arguments, nestedMRowNodesNumber);
			}
				break;
			}// end of switch

			mRowNode = walker.nextNode();
		}
	}

	private void addMathOperationToExpression(Node operation, List<Double> arguments, List<Integer> nestedMRowNodesNumber) {
		String operationName = operation.getLocalName();

		MathematicalOperation mathOperation = createMathOperation(operationName, arguments, nestedMRowNodesNumber);

		expressionOperands.add(mathOperation);
	}

	private MathematicalOperation createMathOperation(String operationName, List<Double> arguments, List<Integer> nestedMRowNodesNumber) {
		MathematicalOperation mathOperation = MathMLUtilities.createMathOperation(operationName, arguments, nestedMRowNodesNumber);

		return mathOperation;
	}

	private int countDirectMRowNodes(Node mRowNode) {
		int mRowNodesNumber = 0;
		try {
			XPathExpression xPathExpression = xPath.compile("count(child::*[name()='" + MathMLConstants.MROW_ELEMENT + "'])");
			Object result = xPathExpression.evaluate(mRowNode, XPathConstants.NUMBER);
			if (result instanceof Double) {
				Double d = (Double) result;

				mRowNodesNumber = d.intValue();
			} else {
				throw new ClassCastException("cannot convert " + result + " to Double");
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return mRowNodesNumber;
	}

	private int countDescendantMRowNodes(Node mRowNode) {
		int mRowNodesNumber = 0;
		try {
			XPathExpression xPathExpression = xPath.compile("count(descendant::*[name()='" + MathMLConstants.MROOT_ELEMENT + "'])");

			Object result = xPathExpression.evaluate(mRowNode, XPathConstants.NUMBER);
			if (result instanceof Double) {
				Double d = (Double) result;

				mRowNodesNumber = d.intValue();
			} else {
				throw new ClassCastException("cannot convert " + result + " to Double");
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return mRowNodesNumber;
	}

	private PresentationMarkupElementCategory getCategoryType(Node presentationMarkupNode) throws UnsupportedMathMLElementException {
		String presentationMarkupNodeName = presentationMarkupNode.getNodeName();
		if (presentationMarkupNodeName.equals(MathMLConstants.MFENCED_ELEMENT)) {
			return PresentationMarkupElementCategory.LAYOUT_SCHEMATA;
		} else if (presentationMarkupNodeName.equals(MathMLConstants.MFRAC_ELEMENT)) {
			return PresentationMarkupElementCategory.LAYOUT_SCHEMATA;
		} else if (presentationMarkupNodeName.equals(MathMLConstants.MI_ELEMENT)) {
			return PresentationMarkupElementCategory.TOKEN;
		} else if (presentationMarkupNodeName.equals(MathMLConstants.MN_ELEMENT)) {
			return PresentationMarkupElementCategory.TOKEN;
		} else if (presentationMarkupNodeName.equals(MathMLConstants.MO_ELEMENT)) {
			return PresentationMarkupElementCategory.TOKEN;
		} else if (presentationMarkupNodeName.equals(MathMLConstants.MROOT_ELEMENT)) {
			return PresentationMarkupElementCategory.LAYOUT_SCHEMATA;
		} else if (presentationMarkupNodeName.equals(MathMLConstants.MROW_ELEMENT)) {
			return PresentationMarkupElementCategory.LAYOUT_SCHEMATA;
		} else if (presentationMarkupNodeName.equals(MathMLConstants.MSQRT_ELEMENT)) {
			return PresentationMarkupElementCategory.LAYOUT_SCHEMATA;
		} else if (presentationMarkupNodeName.equals(MathMLConstants.MSUP_ELEMENT)) {
			return PresentationMarkupElementCategory.LAYOUT_SCHEMATA;
		} else {
			String message = "NOT supported " + presentationMarkupNodeName + " element.";
			throw new UnsupportedMathMLElementException(message);
		}
	}

}
