package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class SinTest {
	private List<Double> arguments;
	private List<Integer> nestedApplyElementsNumber;
	
	private Sin sinus_1;
	private Sin sinus_2;
	
	@Before
	public void setUp(){
		arguments = new ArrayList<Double>();
		arguments.add(20.0);
		
		nestedApplyElementsNumber = new ArrayList<Integer>();
		
		sinus_1 = new Sin(arguments, nestedApplyElementsNumber);
		sinus_2 = new Sin(arguments, nestedApplyElementsNumber);
	}
	
	@Test
	public void testReferenceEquality() throws Exception {
		Assert.assertEquals(sinus_1, sinus_2);
	}
	
	@Test
	public void testGetResult_1() throws Exception {
		Double result_1 = sinus_1.getResult();
		Double result_2 = sinus_2.getResult();
		
		Assert.assertEquals(result_1, result_2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_2() throws Exception {
		arguments = new ArrayList<Double>();
		sinus_1 = new Sin(arguments);
		
		sinus_1.getResult();
	}
	
	@Test
	public void testHashCodeEquality() throws Exception {
		int expectedHashCodeValue = (31 + arguments.hashCode()) * 31 + nestedApplyElementsNumber.hashCode();
		int actualHashCodValue = sinus_1.hashCode();
		
		Assert.assertEquals(expectedHashCodeValue, actualHashCodValue);
	}
	
	@Test
	public void testGetFunctionType() throws Exception {
		int expectedFunctionType = Function.SIN;
		int actualFunctionType = sinus_1.getType();
		
		Assert.assertEquals(expectedFunctionType, actualFunctionType);
	}
	
}
