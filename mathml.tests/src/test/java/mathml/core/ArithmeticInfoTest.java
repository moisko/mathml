package mathml.core;

import static org.junit.Assert.*;

import mathml.utils.MathOperationTypes;

import org.junit.Test;


public class ArithmeticInfoTest {

	@Test
	public void testReferenceEquality() throws Exception {
		MathOperationTypes info_1 = MathOperationTypes.newInstance();
		MathOperationTypes info_2 = MathOperationTypes.newInstance();
		
		assertEquals(info_1.hashCode(), info_2.hashCode());
	}
}
