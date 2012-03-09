package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.api.MathematicalOperation;
import mathml.core.Expression;
import mathml.operation.Multiplication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MultiplicationTest {
	private List<Double> arguments;
	private List<Integer> nestedApplyElementsNumber; 
	
	private Multiplication multi_1;
	private Multiplication multi_2;
	
	@Before
	public void setUp(){
		arguments = new ArrayList<Double>();
		arguments.add(1.0);
		arguments.add(2.0);
		
		nestedApplyElementsNumber = new ArrayList<Integer>();
		
		multi_1 = new Multiplication(arguments, nestedApplyElementsNumber);
		multi_2 = new Multiplication(arguments, nestedApplyElementsNumber);
	}
	
	@Test
	public void testReferenceEquality_1() throws Exception {
		Assert.assertEquals(multi_1, multi_2);
	}
	
	@Test
	public void testReferenceEquality_2() throws Exception {
		Multiplication multi_1 = new Multiplication(arguments);
		List<MathematicalOperation> mathExpression_1 = new ArrayList<MathematicalOperation>();
		mathExpression_1.add(multi_1);
		Expression expression_1 = new Expression(mathExpression_1);
		
		Multiplication multi_2 = new Multiplication(arguments);
		List<MathematicalOperation> mathExpression_2 = new ArrayList<MathematicalOperation>();
		mathExpression_2.add(multi_2);
		Expression expression_2 = new Expression(mathExpression_2);
		
		Assert.assertEquals(expression_1, expression_2);
	}
	
	@Test
	public void testGetResult_1() throws Exception {
		arguments = new ArrayList<Double>();
		arguments.add(1.0);
		arguments.add(-5.0);
		multi_1 = new Multiplication(arguments);
		
		Double expectedResult = - 5.0;
		Double actualResult = multi_1.getResult();
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_2() throws Exception {
		arguments = new ArrayList<Double>();
		multi_1 = new Multiplication(arguments);
		
		multi_1.getResult();
	}
	
	@Test
	public void testHashCodeEquality() throws Exception {
		int expectedHashCodeValue = (31 + arguments.hashCode()) * 31 + nestedApplyElementsNumber.hashCode();
		int actualHashCodeValue = multi_1.hashCode();
		
		Assert.assertEquals(expectedHashCodeValue, actualHashCodeValue);
	}
}
