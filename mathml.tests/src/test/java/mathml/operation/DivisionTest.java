package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Operation;
import mathml.api.State;
import mathml.operation.Division;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DivisionTest {
	private List<Double> arguments;
	private List<Integer> nestedApplyElementsNumber;
	private State divState;
	
	private Division div_1;
	private Division div_2;
	
	@Before
	public void setUp(){
		arguments = new ArrayList<Double>();
		arguments.add(1.0);
		arguments.add(2.0);
		divState = State.FIRST_OPERAND;
		
		nestedApplyElementsNumber = new ArrayList<Integer>();
		
		div_1 = new Division(arguments, divState, nestedApplyElementsNumber);
		div_2 = new Division(arguments, divState, nestedApplyElementsNumber);
	}
	
	@Test
	public void testReferenceEquality() throws Exception {
		Assert.assertEquals(div_1, div_2);
	}
	
	@Test
	public void testGetResult_1() throws Exception {
		Double expectedResult = 0.5;
		Double actualResult = div_1.getResult();
		
		Double res_1 = div_1.getResult();
		Double res_2 = div_2.getResult();
		
		Assert.assertEquals(expectedResult, actualResult);
		Assert.assertEquals(res_1, res_2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_2() throws Exception {
		arguments = new ArrayList<Double>();
		div_1 = new Division(arguments, divState, new ArrayList<Integer>());
		
		div_1.getResult();
	}
	
	@Test(expected=ArithmeticException.class)
	public void testGetResult_3() throws Exception {
		arguments = new ArrayList<Double>();
		arguments.add(1.0);
		arguments.add(0.0);
		div_1 = new Division(arguments, divState, new ArrayList<Integer>());
		
		div_1.getResult();
	}
	
	@Test
	public void testGetOperationType() throws Exception {
		int expectedOperationType = Operation.DIVISION;;
		int actualOperationType = div_1.getType();
		
		Assert.assertEquals(expectedOperationType, actualOperationType);
	}
	
	@Test
	public void testHashCodeEquality() throws Exception {
		int result = 31 + arguments.hashCode();
		result = 31 * result + nestedApplyElementsNumber.hashCode();
		result = 31 * result + State.FIRST_OPERAND.hashCode();
		
		int expectedHashCodeValue = result;
		int actualHashCodeValue = div_1.hashCode();
		
		Assert.assertEquals(expectedHashCodeValue, actualHashCodeValue);
	}
}
