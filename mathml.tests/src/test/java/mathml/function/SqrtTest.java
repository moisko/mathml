package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SqrtTest {
	private List<Double> arguments;
	private List<Integer> nestedApplyElementsNumber;
	
	private Sqrt sqrt_1;
	private Sqrt sqrt_2;
	
	@Before
	public void setUp(){
		arguments = new ArrayList<Double>();
		arguments.add(4.0);
		
		nestedApplyElementsNumber = new ArrayList<Integer>();
		
		sqrt_1 = new Sqrt(arguments, nestedApplyElementsNumber);
		sqrt_2 = new Sqrt(arguments, nestedApplyElementsNumber);
	}
	
	@Test
	public void testReferenceEquality() throws Exception {
		Assert.assertEquals(sqrt_1, sqrt_2);
	}
	
	@Test
	public void testGetResult_1() throws Exception {
		Double res_1 = sqrt_1.getResult();
		Double res_2 = sqrt_2.getResult();
		
		Assert.assertEquals(res_1, res_2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_2() throws Exception {
		arguments = new ArrayList<Double>();
		sqrt_1 = new Sqrt(arguments);
		
		sqrt_1.getResult();
	}
	
	@Test
	public void testHashCodeEquality() throws Exception {
		int expectedHashCodeValue = (31 + arguments.hashCode()) * 31 + nestedApplyElementsNumber.hashCode();
		int actualHashCodeValue = sqrt_1.hashCode();
		
		Assert.assertEquals(expectedHashCodeValue, actualHashCodeValue);
	}
	
	@Test
	public void testGetFunctionType() throws Exception {
		int expectedFunctionType = Function.SQRT;;
		int actualFunctionType = sqrt_1.getType();
		
		Assert.assertEquals(expectedFunctionType, actualFunctionType);
	}
	
	@Test(expected=ArithmeticException.class)
	public void testGetResult_3() throws Exception {
		arguments = new ArrayList<Double>();
		arguments.add(-4.0);
		sqrt_1 = new Sqrt(arguments);
		
		sqrt_1.getResult();
	}
}
