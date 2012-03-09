package mathml.presentation.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;

import junit.framework.Assert;
import mathml.api.ICalculate;
import mathml.api.IMathMLExpression;
import mathml.api.IMathMLParser;
import mathml.core.Calculator;
import mathml.core.Expression;
import mathml.core.MathMLBaseTest;
import mathml.parser.PresentationMarkupParser;
import mathml.transform.MathMLTransformer;
import mathml.transform.MathMLTransformerFactory;

import org.junit.Test;

public class PresentationMarkupParserImplTest extends MathMLBaseTest {
	private static final String PRESENTATION_MARKUP_DIR_NAME = "presentation";

	private static final String PRESENTATION_MARKUP_DIR = MathMLBaseTest.MATHML_REPOSITORY_DIR + File.separator
					+ PRESENTATION_MARKUP_DIR_NAME;
	
	@Test
	public void testParseMathFile_1() throws Exception {
		String pMathFileName = "mathFile_1.math";
		File pMathFile_1 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
		
		// check expression equality
		IMathMLParser parser = new PresentationMarkupParser();
		  
		IMathMLExpression actualExpression = parser.parse(new FileInputStream(pMathFile_1));
		IMathMLExpression expectedExpression = new Expression(expectedExpression_1.getExpressionOperands());
		
		Assert.assertEquals(expectedExpression, actualExpression);
		
		// calculate this expression
		ICalculate calculator = new Calculator();
		Double actualResult = calculator.calculate(actualExpression);
		Double expectedResult = Double.valueOf("2.0");

		Assert.assertEquals(expectedResult, actualResult);
		
		// transform to MathML Content  
		MathMLTransformerFactory factory = MathMLTransformerFactory.newMathMLTransformerFactory();
		MathMLTransformer transformer = factory.newMathMLTransformer();
		transformer.transform(actualExpression, new OutputStreamWriter(System.out));
	}
	
 
	@Test
	public void testParseMathFile_2() throws Exception {
		String pMathFileName = "mathFile_2.math";
		File pMathFile_2 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);

		IMathMLParser parser = new PresentationMarkupParser();

		IMathMLExpression actualExpression = parser.parse(new FileInputStream(pMathFile_2));
		IMathMLExpression expectedExpression = new Expression(expectedExpression_2.getExpressionOperands());
		
		Assert.assertEquals(expectedExpression, actualExpression);
		
		// calculate this expression
		ICalculate calculator = new Calculator();
		Double actualResult = calculator.calculate(actualExpression);
		Double expectedResult = Double.valueOf("");
		
		Assert.assertEquals(expectedResult, actualResult);
		
		// transform the MathML Content
		// MathMLTransformerFactory factory = MathMLTransformerFactory.newMathMLTransformerFactory();
		// MathMLTransformer transformer = factory.newMathMLTransformer();
		// transformer.transform(actualExpression, new OutputStreamWriter(System.out));
	}
//
//	@Test
//	public void testParseMathFile_3() throws Exception {
//		String pMathFileName = "mathFile_3.math";
//
//		File pMathFile_3 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_3));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 7;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//
//		// check if the first element is MoElement
//		boolean mnInstance = parsedMathMLElements.get(0) instanceof MnElement;
//		assertTrue(mnInstance);
//
//		// check if the second element is MnElement
//		boolean moInstance = parsedMathMLElements.get(1) instanceof MoElement;
//		assertTrue(moInstance);
//	}
//
//	@Test
//	public void testParseMathFile_4() throws Exception {
//		String pMathFileName = "mathFile_4.math";
//
//		File pMathFile_4 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_4));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 1;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_5() throws Exception {
//		// TODO add MSupElement class for the case described in mathfile_5.math
//		String pMathFileName = "mathFile_5.math";
//
//		File pMathFile_5 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_5));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 1;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_6() throws Exception {
//		String pMathFileName = "mathFile_6.math";
//
//		File pMathFile_6 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_6));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 2;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_7() throws Exception {
//		// TODO check the case when there is no <mrow> tag element after the <math> root tag element
//		String pMathFileName = "mathFile_7.math";
//
//		File pMathFile_7 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_7));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 7;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_8() throws Exception {
//		String pMathFileName = "mathFile_8.math";
//
//		File pMathFile_8 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_8));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 2;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_9() throws Exception {
//		String pMathFileName = "mathFile_9.math";
//
//		File pMathFile_9 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_9));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 5;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_10() throws Exception {
//		String pMathFileName = "mathFile_10.math";
//
//		File pMathFile_10 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_10));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 5;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_13() throws Exception {
//		String pMathFileName = "mathFile_13.math";
//
//		File pMathFile_13 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_13));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 2;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_17() throws Exception {
//		String pMathFileName = "mathFile_17.math";
//
//		File pMathFile_17 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_17));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 4;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_20() throws Exception {
//		String pMathFileName = "mathFile_20.math";
//
//		File pMathFile_20 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_20));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 3;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_21() throws Exception {
//		String pMathFileName = "mathFile_21.math";
//
//		File pMathFile_21 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_21));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 5;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
//
//	@Test
//	public void testParseMathFile_22() throws Exception {
//		String pMathFileName = "mathFile_22.math";
//
//		File pMathFile_22 = new File(PRESENTATION_MARKUP_DIR, pMathFileName);
//
//		IPresentationMarkupParser parser = new PresentationMarkupParserImpl();
//
//		List<PresentationMarkupElement> parsedMathMLElements = parser.parse(new FileInputStream(pMathFile_22));
//
//		int actualParsedMathMLElementsSize = parsedMathMLElements.size();
//
//		int expectedMathMLElementsSize = 5;
//
//		assertEquals(expectedMathMLElementsSize, actualParsedMathMLElementsSize);
//	}
	
}
