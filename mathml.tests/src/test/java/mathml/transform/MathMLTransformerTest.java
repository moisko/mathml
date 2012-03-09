package mathml.transform;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import javax.xml.parsers.ParserConfigurationException;

import mathml.api.ICalculate;
import mathml.api.IMathMLExpression;
import mathml.api.IMathMLParser;
import mathml.core.Calculator;
import mathml.core.MathMLBaseTest;
import mathml.parser.ContentMarkupParser;

import org.junit.Before;
import org.junit.Test;

public class MathMLTransformerTest extends MathMLBaseTest {
	private final File outputDir = new File("mathml/mathMLRepository/transform");

	private IMathMLExpression expression;

	private MathMLTransformer transformer;

	@Before
	public void setUp() throws ParserConfigurationException {
		MathMLTransformerFactory factory = MathMLTransformerFactory.newMathMLTransformerFactory();
		transformer = factory.newMathMLTransformer();

		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
	}

	@Test
	public void testTransformExpression_1() throws Exception {
		expression = createActualExpression_1();
		String fileName = "mathFile_1.math";
		File mathFile_1 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_1));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_1));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);

		Double expectedResult = new Double("2.0");

		assertEquals(expectedResult, actualResult);
	}

	// (4-(4+2))/2
	@Test
	public void testTransformExpression_2() throws Exception {
		expression = createActualExpression_2();
		String fileName = "mathFile_2.math";
		File mathFile_2 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_2));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_2));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("-1.0");

		assertEquals(expectedResult, actualResult);

	}

	// (4+(2-4))/2
	@Test
	public void tesTransformExpression_3() throws Exception {
		expression = createAcutalExpression_3();
		String fileName = "mathFile_3.math";
		File mathFile_3 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_3));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_3));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("1.0");

		assertEquals(expectedResult, actualResult);
	}

	// 2/(4+2)
	@Test
	public void testTransformExpression_4() throws Exception {
		expression = createActualExpression_4();
		String fileName = "mathFile_4.math";
		File mathFile_4 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_4));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_4));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("0.3333333333333333");

		assertEquals(expectedResult, actualResult);
	}

	// 2+4-(2-(4+2))
	@Test
	public void testTransformExpression_5() throws Exception {
		expression = createActualExpression_5();
		String fileName = "mathFile_5.math";
		File mathFile_5 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_5));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_5));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("10.0");

		assertEquals(expectedResult, actualResult);
	}

	// 4+2-3
	@Test
	public void testTransformExpression_6() throws Exception {
		expression = createActualExpression_6();
		String fileName = "mathFile_6.math";
		File mathFile_6 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_6));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_6));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("3.0");

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testTransformExpression_7() throws Exception {
		expression = createActualExpression_7();
		String fileName = "mathFile_7.math";
		File mathFile_7 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_7));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_7));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("6.0");

		assertEquals(expectedResult, actualResult);
	}

	// 2-(2-3)
	@Test
	public void testTransformExpression_8() throws Exception {
		expression = createActualExpression_8();
		String fileName = "mathFile_8.math";
		File mathFile_8 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_8));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_8));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("3.0");

		assertEquals(expectedResult, actualResult);
	}

	// 2/(4+(2-4))
	@Test
	public void tesTransformExpression_9() throws Exception {
		expression = createActualExpression_9();
		String fileName = "mathFile_9.math";
		File mathFile_9 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_9));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_9));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("1.0");

		assertEquals(expectedResult, actualResult);
	}

	// 4/(3/(2/3))
	@Test
	public void testTransformExpression_10() throws Exception {
		expression = createActualExpression_10();
		String fileName = "mathFile_10.math";
		File mathFile_10 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_10));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_10));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		// Double expectedResult = new Double("0.66666667");
		Double expectedResult = new Double("0.8888888888888888");

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testTransformExpression_11() throws Exception {
		expression = createActualExpression_11();
		String fileName = "mathFile_11.math";
		File mathFile_11 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_11));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_11));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("5.850903524534118");

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testTransformExpression_12() throws Exception {
		expression = createActualExpression_12();
		String fileName = "mathFile_12.math";
		File mathFile_12 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_12));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_12));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("3.8509035245341185");

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testTransformExpression_13() throws Exception {
		expression = createActualExpression_13();
		String fileName = "mathFile_13.math";
		File mathFile_13 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_13));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_13));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		// Double expectedResult = new Double("1.2339357");
		Double expectedResult = new Double("1.2339356830227457");

		assertEquals(expectedResult, actualResult);
	}

	// 1+2+3+4+2-3
	@Test
	public void testTransformExpression_14() throws Exception {
		expression = createActualExpression_14();
		String fileName = "mathFile_14.math";
		File mathFile_14 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_14));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_14));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("9.0");

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testTransformExpression_15() throws Exception {
		expression = createActualExpression_15();
		String fileName = "mathFile_15.math";
		File mathFile_15 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_15));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_15));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("0.8509035245341184");

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testTransformExpression_16() throws Exception {
		expression = createActualExpression_16();
		String fileName = "mathFile_16.math";
		File mathFile_16 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_16));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_16));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("0.011968375907138173");

		assertEquals(expectedResult, actualResult);
	}

	// 1+sin(cos(30))+power(1,3)
	@Test
	public void testTransformExpression_17() throws Exception {
		expression = createActualExpression_17();
		String fileName = "mathFile_17.math";
		File mathFile_17 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_17));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_17));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("2.1536404799660938");

		assertEquals(expectedResult, actualResult);
	}

	// sin(cos(tan(45)))
	@Test
	public void testTransformExpression_19() throws Exception {
		expression = createActualExpression_19();
		String fileName = "mathFile_19.math";
		File mathFile_19 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_19));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_19));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("-0.04893972631137305");

		assertEquals(expectedResult, actualResult);
	}

	// 1+1-sin(30)
	@Test
	public void testTransformExpression_20() throws Exception {
		expression = createActualExpression_20();
		String fileName = "mathFile_20.math";
		File mathFile_20 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_20));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_20));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("2.9880316240928617");

		assertEquals(expectedResult, actualResult);
	}

	// power(cos(sin(30)),1+power(3,2))+(1+3)/4
	// [1+3, /4, power(3,2), 1+, sin(30), cos, power(, plus]
	@Test
	public void testTransformExpression_21() throws Exception {
		expression = createActualExpression_21();
		String fileName = "mathFile_21.math";
		File mathFile_21 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_21));

		IMathMLParser parser = new ContentMarkupParser();
		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_21));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);

		Double expectedResult = new Double("1.002548394632125");

		assertEquals(expectedResult, actualResult);
	}

	// 1-(sin(30)-3)
	@Test
	public void testTransformExpression_22() throws Exception {
		expression = createActualExpression_22();
		String fileName = "mathFile_22.math";
		File mathFile_22 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_22));

		IMathMLParser parser = new ContentMarkupParser();
		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_22));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("4.988031624092862");

		assertEquals(expectedResult, actualResult);
	}

	// 2+1-(sin(30)-3)
	@Test
	public void testTransformExpression_23() throws Exception {
		expression = createActualExpression_23();
		String fileName = "mathFile_23.math";
		File mathFile_23 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_23));

		IMathMLParser parser = new ContentMarkupParser();
		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_23));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("6.988031624092862");

		assertEquals(expectedResult, actualResult);
	}

	// 1+power(sin(30),1)
	@Test
	public void testTransformExpression_24() throws Exception {
		expression = createActualExpression_24();
		String fileName = "mathFile_24.math";
		File mathFile_24 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_24));

		IMathMLParser parser = new ContentMarkupParser();
		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_24));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("0.011968375907138173");

		assertEquals(expectedResult, actualResult);
	}

	// 2/(4+2-(3+4))
	@Test
	public void testTransformExpression_25() throws Exception {
		expression = createActualExpression_25();
		String fileName = "mathFile_25.math";
		File mathFile_25 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_25));

		IMathMLParser parser = new ContentMarkupParser();

		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_25));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("-2.0");

		assertEquals(expectedResult, actualResult);
	}

	// 3+(2*3)/2
	@Test
	public void testTransformExpression_26() throws Exception {
		expression = createActualExpression_26();
		String fileName = "mathFile_26.math";
		File mathFile_26 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathFile_26));

		IMathMLParser parser = new ContentMarkupParser();
		IMathMLExpression expression = parser.parse(new FileInputStream(mathFile_26));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = new Double("6.0");

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testTransformExpression_27() throws Exception {
		expression = createActualExpression_27();
		String fileName = "mathFile_27.math";
		File mathfile_27 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathfile_27));

		IMathMLParser parser = new ContentMarkupParser();
		IMathMLExpression expression = parser.parse(new FileInputStream(mathfile_27));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = 2.0;

		assertEquals(expectedResult, actualResult, 0.1);

	}
	
	@Test
	public void testTransformExpression_28() throws Exception {
		expression = createActualExpression_28();
		String fileName = "mathFile_28.math";
		File mathfile_28 = new File(outputDir, fileName);

		transformer.transform(expression, new FileWriter(mathfile_28));

		IMathMLParser parser = new ContentMarkupParser();
		IMathMLExpression expression = parser.parse(new FileInputStream(mathfile_28));

		ICalculate calculator = new Calculator();

		Double actualResult = calculator.calculate(expression);
		Double expectedResult = 1.74903627;

		assertEquals(expectedResult, actualResult, 0.1);

	}
}
