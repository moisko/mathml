package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.api.ICalculate;
import mathml.api.MathematicalOperation;
import mathml.api.Operation;
import mathml.api.State;
import mathml.core.Calculator;
import mathml.core.Expression;
import mathml.operation.Minus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MinusTest {
	private List<Double> arguments;
	private List<Integer> nestedApplyElementsNumber;
	private State minusState;
	
	private Minus minus_1;
	private Minus minus_2;
	
	@Before
	public void setUp(){
		arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(1.0);
		minusState = State.FIRST_OPERAND;
		
		nestedApplyElementsNumber = new ArrayList<Integer>();
		
		minus_1 = new Minus(arguments, minusState, nestedApplyElementsNumber);
		minus_2 = new Minus(arguments, minusState, nestedApplyElementsNumber);
	}
	
	@Test
	public void testReferenceEquality_1() throws Exception {
		Assert.assertEquals(minus_1, minus_2);
	}
	
	@Test
	public void testMinusCalculation() throws Exception {
		arguments = new ArrayList<Double>();
		arguments.add(-1.0);
		arguments.add(-2.0);
		
		Minus minus = new Minus(arguments, State.SECOND_OPERAND, new ArrayList<Integer>());
		
		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(minus);
		Expression expression = new Expression(operands);
		ICalculate calc = new Calculator();
		Double actualResult = calc.calculate(expression);
		Double expectedResult = new Double("-1");
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReferenceEquality_2() throws Exception {
		Minus minus_1 = new Minus(arguments);
		List<MathematicalOperation> mathExpression_1 = new ArrayList<MathematicalOperation>();
		mathExpression_1.add(minus_1);
		Expression expr_1 = new Expression(mathExpression_1);
		
		Minus minus_2 = new Minus(arguments);
		List<MathematicalOperation> mathExpression_2 = new ArrayList<MathematicalOperation>();
		mathExpression_2.add(minus_2);
		Expression expr_2 = new Expression(mathExpression_2);
		
		Assert.assertEquals("These two Expression objects are not equal", expr_1, expr_2);
		Assert.assertEquals("These two Plus objects are not equal", minus_1, minus_2);
		Assert.assertEquals(minus_1, minus_2);
		Assert.assertEquals(expr_1.getExpressionOperands(), expr_2.getExpressionOperands());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_1() throws Exception {
		arguments = new ArrayList<Double>();
		minus_1 = new Minus(arguments, minusState, new ArrayList<Integer>());
		
		minus_1.getResult();
	}
	
	/**
	 * This method should throw an IllegalArgumentException, because for the minus
	 * operation, there is a limitation for the number of arguments - max 2 arguments
	 * 
	 * @throws IllegalArgumentException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_2() throws Exception {
		arguments = new ArrayList<Double>();
		arguments.add(1.0);
		arguments.add(1.0);
		arguments.add(1.0);
		minus_1 = new Minus(arguments);
		
		Double expectedResult = - 3.0;
		Double actualResult = minus_1.getResult();
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testGetResult_3() throws Exception {
		arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(5.0);
		minus_1 = new Minus(arguments, State.FIRST_OPERAND, new ArrayList<Integer>());
		
		Double expectedResult = - 3.0;
		Double actualResult = minus_1.getResult();
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testHashCodeEquality() throws Exception {
		int result = 31 + arguments.hashCode();
		result = 31 * result + nestedApplyElementsNumber.hashCode();
		result = 31 * result + State.FIRST_OPERAND.hashCode();
		int expectedHashCodeValue = result;
		int actualHashCodeValue = minus_1.hashCode();
		
		Assert.assertEquals(expectedHashCodeValue, actualHashCodeValue);
	}
	
	@Test
	public void testGetOperationType() throws Exception {
		int expectedOperationType = Operation.MINUS;
		int actualOperationType = minus_1.getType();
		
		Assert.assertEquals(expectedOperationType, actualOperationType);
	}
	
}
