package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;
import mathml.api.State;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PowerTest {
	private Power power_1;
	private Power power_2;
	private List<Double> arguments;
	private List<Integer> nestedApplyElementsNumber;
	
	@Before
	public void setUp(){
		arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(2.0);
		
		nestedApplyElementsNumber = new ArrayList<Integer>();
		
		power_1 = new Power(arguments, State.FIRST_OPERAND, nestedApplyElementsNumber);
		power_2 = new Power(arguments, State.FIRST_OPERAND, nestedApplyElementsNumber);
	}
	
	@Test
	public void testReferenceEquality() throws Exception {
		Assert.assertEquals(power_1, power_2);
	}
	
	@Test
	public void testGetResult_1() throws Exception {
		Double res_1 = power_1.getResult();
		Double res_2 = power_2.getResult();
		
		Assert.assertEquals(res_1, res_2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_2() throws Exception {
		arguments = new ArrayList<Double>();
		power_1 = new Power(arguments);
		
		power_1.getResult();
	}
	
	@Test
	public void testGetResuult_3() throws Exception {
		arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(0.0);
		power_1 = new Power(arguments);
		
		Double expectedResult = 1.0;
		Double actualResult = power_1.getResult();
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testGetResult_4() throws Exception {
		arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(-1.0);
		power_1 = new Power(arguments);
		
		Double expectedResult = 0.5;
		Double actualResult = power_1.getResult();
		
		Assert.assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testHashCodeEquality() throws Exception {
		int result = 31 + arguments.hashCode();
		result = 31 * result + nestedApplyElementsNumber.hashCode();
		result = 31 * result + State.FIRST_OPERAND.hashCode();
		
		int expectedHashCodeValue = result;
		int actualHashCodeValue = power_1.hashCode();
		
		Assert.assertEquals(expectedHashCodeValue, actualHashCodeValue);
	}
	
	@Test
	public void testFunctionType() throws Exception {
		int expectedFunctionType = Function.POWER;;
		int actualFunctionType = power_1.getType();
		
		Assert.assertEquals(expectedFunctionType, actualFunctionType);
	}
}
