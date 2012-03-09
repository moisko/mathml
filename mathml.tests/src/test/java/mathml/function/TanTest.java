package mathml.function;

import java.util.ArrayList;
import java.util.List;

import mathml.api.Function;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TanTest {
	private List<Double> arguments;
	private List<Integer> nestedApplyElementsNumber;
	private Tan tan_1;
	private Tan tan_2;
	
	@Before
	public void setUp(){
		arguments = new ArrayList<Double>();
		arguments.add(45.0);
		
		nestedApplyElementsNumber = new ArrayList<Integer>();
		
		tan_1 = new Tan(arguments, nestedApplyElementsNumber);
		tan_2 = new Tan(arguments, nestedApplyElementsNumber);
	}
	
	@Test
	public void testReferenceEquality() throws Exception {
		Assert.assertEquals(tan_1, tan_2);
	}
	
	@Test
	public void testGetResult_1() throws Exception {
		Double result_1 = tan_1.getResult();
		Double result_2 = tan_2.getResult();
		
		Assert.assertEquals(result_1, result_2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetResult_2() throws Exception {
		arguments = new ArrayList<Double>();
		tan_1 = new Tan(arguments);
		
		tan_1.getResult();
	}
	
	@Test
	public void testHashCodeEquality() throws Exception {
		int expectedHashCode = (31 + arguments.hashCode()) * 31 + nestedApplyElementsNumber.hashCode();
		int actualHashCode = tan_1.hashCode();
		
		Assert.assertEquals(expectedHashCode, actualHashCode);
	}
	
	@Test
	public void testGetFunctionType() throws Exception {
		int expectedFunctionType = Function.TAN;
		int actualFunctionType = tan_1.getType();;
		
		Assert.assertEquals(expectedFunctionType, actualFunctionType);
	}
	
}
