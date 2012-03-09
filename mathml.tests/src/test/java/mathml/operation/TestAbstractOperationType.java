package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.operation.AbstractOperationType;
import mathml.operation.Plus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestAbstractOperationType {
	private List<Integer> nestedApplyElementsNumberList;
	
	private AbstractOperationType abstractOperationType;
	
	@Before
	public void setUp(){
		nestedApplyElementsNumberList = new ArrayList<Integer>();
		nestedApplyElementsNumberList.add(1);
		nestedApplyElementsNumberList.add(0);
		
		abstractOperationType = new Plus(new ArrayList<Double>(), nestedApplyElementsNumberList);
	}
	
	@Test
	public void testGetArgsDistanceFromOperation_1() throws Exception {
		List<Integer> expectedArgsPositionList = new ArrayList<Integer>();
		expectedArgsPositionList.add(3);
		expectedArgsPositionList.add(1);
		
		List<Integer> actualArgsPositionList = abstractOperationType.getArgsDistanceFromMathOperation();
		
		Assert.assertEquals(expectedArgsPositionList, actualArgsPositionList);
	}
	
	@Test
	public void testGetArgsDistanceFromOperation_2() throws Exception {
		List<Integer> expectedArgsPositionList = new ArrayList<Integer>();
		expectedArgsPositionList.add(3);
		expectedArgsPositionList.add(1);
		
		// first invocation
		abstractOperationType.getArgsDistanceFromMathOperation();
		// second invocation
		List<Integer> argsPositionListAfterReverse = abstractOperationType.getArgsDistanceFromMathOperation();

		Assert.assertEquals(expectedArgsPositionList, argsPositionListAfterReverse);
	}
}
