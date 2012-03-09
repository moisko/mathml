package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CosTest {
	private List<Double> arguments;
	private List<Integer> nestedApplyElementsNumber;
	private Cos cos_1;
	private Cos cos_2;
	
	@Before
	public void setUp(){
		arguments = new ArrayList<Double>();
		arguments.add(30.0);
		
		nestedApplyElementsNumber = new ArrayList<Integer>();
		
		cos_1 = new Cos(arguments, nestedApplyElementsNumber);
		cos_2 = new Cos(arguments, nestedApplyElementsNumber);
	}
	
	@Test
	public void testReferenceEquality() throws Exception {
		Assert.assertEquals(cos_1, cos_2);
	}
	
	@Test
	public void testGetResult_1() throws Exception {
		Double result_1 = cos_1.getResult();
		Double result_2 = cos_2.getResult();
		
		Assert.assertEquals(result_1, result_2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_2() throws Exception {
		arguments = new ArrayList<Double>();
		cos_1 = new Cos(arguments);
		
		cos_1.getResult();
	}
	
	@Test
	public void testHashCodeEquality() throws Exception {
		int expectedHashCode = (31 + arguments.hashCode()) * 31 + nestedApplyElementsNumber.hashCode();
		int actualHashCode = cos_1.hashCode();
		
		Assert.assertEquals(expectedHashCode, actualHashCode);
	}
	
	@Test
	public void testGetFunctionType() throws Exception {
		int expectedFunctionType = Function.COS;
		int actualFunctionType = cos_1.getType();
		
		Assert.assertEquals(expectedFunctionType, actualFunctionType);
	}
}
