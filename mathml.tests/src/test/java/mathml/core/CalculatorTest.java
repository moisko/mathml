package mathml.core;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import mathml.api.ICalculate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class CalculatorTest extends MathMLBaseTest {
	private ICalculate calculator;
	
	public CalculatorTest() throws ParserConfigurationException, SAXException, IOException {
		super();
	}

	@Before
	public void setUp() throws Exception {
		calculator = new Calculator();
	}

	@Test
	public void testCalculateExpression_2() throws Exception {
		Expression expression = new Expression(expectedExpression_2.getExpressionOperands());
		
		Double actualResult = calculator.calculate(expression);
		
		Double expectedResult = - 1.0;
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCalculateExpression_3() throws Exception {
		Expression expression = new Expression(expectedExpression_3.getExpressionOperands());
		
		Double actualResult = calculator.calculate(expression);
		
		Double expectedResult = 8.0;
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCalculateExpression_4() throws Exception {
		Expression expression = new Expression(expectedExpression_4.getExpressionOperands());
		
		Double actualResult = calculator.calculate(expression);
		
		Double expectedResult = 1.5;
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCalculateExpression_16() throws Exception {
		Expression expression = new Expression(expectedExpression_16.getExpressionOperands());
		
		Double actualResult = calculator.calculate(expression);
		
		Double expectedResult = pow(1, 1 + sin(30));
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCalculatePlusExpression_18() throws Exception {
		Expression expression = new Expression(expectedExpression_18.getExpressionOperands());
		
		Double actualResult = calculator.calculate(expression);
		
		Double expectedResult = cos(sin(45)) + sin(pow(tan(45), 30)) + 1 - 1/3 + 1 + 1/3 + 1 - 3;
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCalculateExpression() throws Exception {
		ICalculate calc = new Calculator();
		
		Double actualResult = calc.calculate(new Expression(expectedExpression_1.getExpressionOperands()));
		
		Double expectedResult = new Double("2");

		Assert.assertEquals(expectedResult, actualResult);
	}
	
}
