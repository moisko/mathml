package mathml.core;

import mathml.function.CosTest;
import mathml.function.PowerTest;
import mathml.function.SinTest;
import mathml.function.SqrtTest;
import mathml.function.TanTest;
import mathml.operation.DivisionTest;
import mathml.operation.MinusTest;
import mathml.operation.MultiplicationTest;
import mathml.operation.PlusTest;
import mathml.operation.TestAbstractOperationType;
import mathml.utils.MathMLUtilitiesTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	MathMLParserTest.class,
	// PresentationMarkupParserImplTest.class,
	CalculatorTest.class,
	MathMLUtilitiesTest.class,
	ArithmeticInfoTest.class,
	
	CosTest.class,
	PowerTest.class,
	SinTest.class,
	SqrtTest.class,
	TanTest.class,
	
	DivisionTest.class,
	MinusTest.class,
	MultiplicationTest.class,
	PlusTest.class,
	TestAbstractOperationType.class
})
public class MathMLCoreTestSuite {
	
}
