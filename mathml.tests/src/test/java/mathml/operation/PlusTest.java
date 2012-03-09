package mathml.operation;

import java.util.ArrayList;
import java.util.List;

import mathml.api.MathematicalOperation;
import mathml.core.Expression;
import mathml.operation.Plus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlusTest {
	private List<Double> arguments;

	@Before
	public void setUp() {
		arguments = new ArrayList<Double>();
		arguments.add(1.0);
		arguments.add(2.0);
		arguments.add(3.0);
	}

	@Test
	public void testReferenceEquality_1() throws Exception {
		Plus plus_1 = new Plus(arguments);
		Plus plus_2 = new Plus(arguments);

		Assert.assertEquals(plus_1, plus_2);
	}

	@Test
	public void testReferenceEquality_2() throws Exception {
		Plus plus_1 = new Plus(arguments);
		List<MathematicalOperation> mathExpression_1 = new ArrayList<MathematicalOperation>();
		mathExpression_1.add(plus_1);
		Expression expr_1 = new Expression(mathExpression_1);

		Plus plus_2 = new Plus(arguments);
		List<MathematicalOperation> mathExpression_2 = new ArrayList<MathematicalOperation>();
		mathExpression_2.add(plus_2);
		Expression expr_2 = new Expression(mathExpression_2);

		Assert.assertEquals("These two Expression objects are not equal", expr_1, expr_2);
		Assert.assertEquals("These two Plus objects are not equal", plus_1, plus_2);
		Assert.assertEquals(plus_1, plus_2);
	}

	@Test
	public void testGetArgsDistanceFromPlusOperation_1() throws Exception {
		List<Integer> nestedApplyElementsNumberList = new ArrayList<Integer>();
		nestedApplyElementsNumberList.add(1);
		nestedApplyElementsNumberList.add(2);
		nestedApplyElementsNumberList.add(1);
		nestedApplyElementsNumberList.add(2);
		List<Double> arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(3.0);
		Plus plus = new Plus(arguments, nestedApplyElementsNumberList);
		List<Integer> actualArgsPositionList = plus.getArgsDistanceFromMathOperation();

		List<Integer> expectedArgsPositionList = new ArrayList<Integer>();
		expectedArgsPositionList.add(8);
		expectedArgsPositionList.add(6);
		expectedArgsPositionList.add(3);
		expectedArgsPositionList.add(1);

		Assert.assertEquals(expectedArgsPositionList, actualArgsPositionList);
	}

	@Test
	public void testGetArgsDistanceFromPlusOperation_2() throws Exception {
		List<Integer> nestedApplyElementsNumberList = new ArrayList<Integer>();
		nestedApplyElementsNumberList.add(0);
		nestedApplyElementsNumberList.add(0);
		List<Double> arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(3.0);
		Plus plus = new Plus(arguments, nestedApplyElementsNumberList);
		List<Integer> actualArgsPositionList = plus.getArgsDistanceFromMathOperation();

		List<Integer> expectedArgsPositionList = new ArrayList<Integer>();
		expectedArgsPositionList.add(2);
		expectedArgsPositionList.add(1);

		Assert.assertEquals(expectedArgsPositionList, actualArgsPositionList);
	}

	@Test
	public void testGetArgsDistanceFromPlusOperation_3() throws Exception {
		List<Integer> nestedApplyElementsNumberList = new ArrayList<Integer>();
		nestedApplyElementsNumberList.add(1);
		nestedApplyElementsNumberList.add(0);
		nestedApplyElementsNumberList.add(1);
		nestedApplyElementsNumberList.add(0);
		List<Double> arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(3.0);
		Plus plus = new Plus(arguments, nestedApplyElementsNumberList);
		List<Integer> actualArgsPositionList = plus.getArgsDistanceFromMathOperation();

		List<Integer> expectedArgsPositionList = new ArrayList<Integer>();
		expectedArgsPositionList.add(6);
		expectedArgsPositionList.add(4);
		expectedArgsPositionList.add(3);
		expectedArgsPositionList.add(1);

		Assert.assertEquals(expectedArgsPositionList, actualArgsPositionList);
	}

	@Test
	public void testGetArgsDistanceFromPlusOperation_4() throws Exception {
		List<Integer> nestedApplyElementsNumberList = new ArrayList<Integer>();
		nestedApplyElementsNumberList.add(0);
		nestedApplyElementsNumberList.add(1);
		nestedApplyElementsNumberList.add(0);
		nestedApplyElementsNumberList.add(2);
		List<Double> arguments = new ArrayList<Double>();
		arguments.add(2.0);
		arguments.add(3.0);
		Plus plus = new Plus(arguments, nestedApplyElementsNumberList);
		List<Integer> actualArgsPositionList = plus.getArgsDistanceFromMathOperation();

		List<Integer> expectedArgsPositionList = new ArrayList<Integer>();
		expectedArgsPositionList.add(5);
		expectedArgsPositionList.add(4);
		expectedArgsPositionList.add(2);
		expectedArgsPositionList.add(1);

		Assert.assertEquals(expectedArgsPositionList, actualArgsPositionList);
	}
}
