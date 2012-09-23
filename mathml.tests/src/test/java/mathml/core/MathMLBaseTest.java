package mathml.core;

import java.util.ArrayList;
import java.util.List;

import mathml.api.IMathMLExpression;
import mathml.api.MathematicalOperation;
import mathml.api.State;
import mathml.function.Cos;
import mathml.function.Cosh;
import mathml.function.Power;
import mathml.function.Sin;
import mathml.function.Sqrt;
import mathml.function.Tan;
import mathml.function.Tanh;
import mathml.operation.Division;
import mathml.operation.Minus;
import mathml.operation.Multiplication;
import mathml.operation.Plus;

public class MathMLBaseTest {
	public static final String MATHML_REPOSITORY_DIR = "src/test/resources/mathml/mathMLRepository";

	protected static final String mathFile_1 = MATHML_REPOSITORY_DIR + "/content/mathFile_1.math";
	protected static final String mathFile_2 = MATHML_REPOSITORY_DIR + "/content/mathFile_2.math";
	protected static final String mathFile_3 = MATHML_REPOSITORY_DIR + "/content/mathFile_3.math";
	protected static final String mathFile_4 = MATHML_REPOSITORY_DIR + "/content/mathFile_4.math";
	protected static final String mathFile_5 = MATHML_REPOSITORY_DIR + "/content/mathFile_5.math";
	protected static final String mathFile_6 = MATHML_REPOSITORY_DIR + "/content/mathFile_6.math";
	protected static final String mathFile_7 = MATHML_REPOSITORY_DIR + "/content/mathFile_7.math";
	protected static final String mathFile_9 = MATHML_REPOSITORY_DIR + "/content/mathFile_9.math";
	protected static final String mathFile_10 = MATHML_REPOSITORY_DIR + "/content/mathFile_10.math";
	protected static final String mathFile_11 = MATHML_REPOSITORY_DIR + "/content/mathFile_11.math";
	protected static final String mathFile_13 = MATHML_REPOSITORY_DIR + "/content/mathFile_13.math";
	protected static final String mathFile_14 = MATHML_REPOSITORY_DIR + "/content/mathFile_14.math";
	protected static final String mathFile_15 = MATHML_REPOSITORY_DIR + "/content/mathFile_15.math";
	protected static final String mathFile_16 = MATHML_REPOSITORY_DIR + "/content/mathFile_16.math";
	protected static final String mathFile_17 = MATHML_REPOSITORY_DIR + "/content/mathFile_17.math";
	protected static final String mathFile_18 = MATHML_REPOSITORY_DIR + "/content/mathFile_18.math";
	protected static final String mathFile_19 = MATHML_REPOSITORY_DIR + "/content/mathFile_19.math";
	protected static final String mathFile_20 = MATHML_REPOSITORY_DIR + "/content/mathFile_20.math";
	protected static final String mathFile_21 = MATHML_REPOSITORY_DIR + "/content/mathFile_21.math";
	protected static final String mathFile_22 = MATHML_REPOSITORY_DIR + "/content/mathFile_22.math";

	protected IMathMLExpression expectedExpression_1 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> sqrtArgs = new ArrayList<Double>();
			sqrtArgs.add(4.0);
			Sqrt sqrt = new Sqrt(sqrtArgs);

			operands.add(sqrt);

			return operands;
		}
	};

	IMathMLExpression expectedExpression_3 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> plusArgs = new ArrayList<Double>();
			plusArgs.add(4.0);
			plusArgs.add(2.0);
			plusArgs.add(4.0);
			Plus plus = new Plus(plusArgs);

			List<Integer> minusApplyElementsNumberList = new ArrayList<Integer>();
			minusApplyElementsNumberList.add(0);
			List<Double> minusArgs = new ArrayList<Double>();
			minusArgs.add(2.0);
			Minus minus = new Minus(minusArgs, State.SECOND_OPERAND, minusApplyElementsNumberList);

			operands.add(plus);
			operands.add(minus);

			return operands;
		}
	};

	protected IMathMLExpression expectedExpression_2 = new Expression() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> plusArgs = new ArrayList<Double>();
			plusArgs.add(4.0);
			plusArgs.add(2.0);
			Plus plus = new Plus(plusArgs);

			List<Integer> minusApplyElementsNumberList = new ArrayList<Integer>();
			// 1 nested <apply> with 0 nested <apply> tags
			minusApplyElementsNumberList.add(0);
			List<Double> minusArgs = new ArrayList<Double>();
			minusArgs.add(4.0);
			Minus minus = new Minus(minusArgs, State.FIRST_OPERAND, minusApplyElementsNumberList);

			List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
			divApplyElementsNumberList.add(1);
			List<Double> divArgs = new ArrayList<Double>();
			divArgs.add(2.0);
			Division div = new Division(divArgs, State.SECOND_OPERAND, divApplyElementsNumberList);

			operands.add(plus);
			operands.add(minus);
			operands.add(div);

			return operands;
		}
	};

	IMathMLExpression expectedExpression_4 = new Expression() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> sqrtArgs = new ArrayList<Double>();
			sqrtArgs.add(4.0);
			Sqrt sqrt = new Sqrt(sqrtArgs);

			List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
			plusApplyElementsNumberList.add(0);
			List<Double> plusArgs = new ArrayList<Double>();
			plusArgs.add(1.0);
			Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

			List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
			divApplyElementsNumberList.add(1);
			List<Double> divideArgs = new ArrayList<Double>();
			divideArgs.add(2.0);
			Division division = new Division(divideArgs, State.SECOND_OPERAND, divApplyElementsNumberList);

			operands.add(sqrt);
			operands.add(plus);
			operands.add(division);

			return operands;
		}
	};

	IMathMLExpression expectedExpression_13 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> sinArgs = new ArrayList<Double>();
			sinArgs.add(45.0);
			Sin sin = new Sin(sinArgs);

			List<Integer> plusApplyElementsNumber = new ArrayList<Integer>();
			plusApplyElementsNumber.add(0);
			List<Double> plusArgs = new ArrayList<Double>();
			plusArgs.add(1.0);
			Plus plus = new Plus(plusArgs, plusApplyElementsNumber);

			List<Integer> div_1ApplyElementsNumber = new ArrayList<Integer>();
			div_1ApplyElementsNumber.add(1);
			List<Double> div_1Args = new ArrayList<Double>();
			div_1Args.add(3.0);
			Division div_1 = new Division(div_1Args, State.FIRST_OPERAND, div_1ApplyElementsNumber);

			List<Integer> div_2ApplyElementsNumber = new ArrayList<Integer>();
			div_2ApplyElementsNumber.add(2);
			List<Double> div_2Args = new ArrayList<Double>();
			div_2Args.add(2.0);
			Division div_2 = new Division(div_2Args, State.FIRST_OPERAND, div_2ApplyElementsNumber);

			operands.add(sin);
			operands.add(plus);
			operands.add(div_1);
			operands.add(div_2);

			return operands;
		}

	};

	IMathMLExpression expectedExpression_14 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> minusArgs = new ArrayList<Double>();
			minusArgs.add(2.0);
			minusArgs.add(3.0);
			Minus minus = new Minus(minusArgs);

			List<Integer> plusApplyElementsNumber = new ArrayList<Integer>();
			plusApplyElementsNumber.add(0);
			List<Double> plusArgs = new ArrayList<Double>();
			plusArgs.add(1.0);
			plusArgs.add(2.0);
			plusArgs.add(3.0);
			plusArgs.add(4.0);
			Plus plus = new Plus(plusArgs, plusApplyElementsNumber);

			operands.add(minus);
			operands.add(plus);

			return operands;
		}

	};

	IMathMLExpression expectedExpression_15 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> sinArgs = new ArrayList<Double>();
			sinArgs.add(45.0);
			Sin sin = new Sin(sinArgs);

			List<Integer> powerApplyElementsNumber = new ArrayList<Integer>();
			powerApplyElementsNumber.add(0);
			List<Double> powerArgs = new ArrayList<Double>();
			powerArgs.add(1.0);
			Power power = new Power(powerArgs, State.FIRST_OPERAND, powerApplyElementsNumber);

			operands.add(sin);
			operands.add(power);

			return operands;
		}

	};

	IMathMLExpression expectedExpression_16 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> sinArgs = new ArrayList<Double>();
			sinArgs.add(30.0);
			Sin sin = new Sin(sinArgs);

			List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
			plusApplyElementsNumberList.add(0);
			List<Double> plusArgs = new ArrayList<Double>();
			plusArgs.add(1.0);
			Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

			List<Integer> powerApplyElementsNumberList = new ArrayList<Integer>();
			powerApplyElementsNumberList.add(1);
			List<Double> powerArgs = new ArrayList<Double>();
			powerArgs.add(1.0);
			Power power = new Power(powerArgs, State.FIRST_OPERAND, powerApplyElementsNumberList);

			operands.add(sin);
			operands.add(plus);
			operands.add(power);

			return operands;
		}
	};

	IMathMLExpression expectedExpression_17 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> powerArgs = new ArrayList<Double>();
			powerArgs.add(1.0);
			powerArgs.add(2.0);
			Power power = new Power(powerArgs, State.FIRST_OPERAND, new ArrayList<Integer>());

			List<Double> cosArgs = new ArrayList<Double>();
			cosArgs.add(30.0);
			Cos cos = new Cos(cosArgs, new ArrayList<Integer>());

			List<Integer> sinApplyelementsNumber = new ArrayList<Integer>();
			sinApplyelementsNumber.add(0);
			Sin sin = new Sin(new ArrayList<Double>(), sinApplyelementsNumber);

			List<Integer> plusApplyElementsNumber = new ArrayList<Integer>();
			plusApplyElementsNumber.add(1);
			plusApplyElementsNumber.add(0);
			List<Double> plusArgs = new ArrayList<Double>();
			plusArgs.add(1.0);
			Plus plus = new Plus(plusArgs, plusApplyElementsNumber);

			operands.add(power);
			operands.add(cos);
			operands.add(sin);
			operands.add(plus);

			return operands;
		}
	};

	IMathMLExpression expectedExpression_18 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> minus_1Args = new ArrayList<Double>();
			minus_1Args.add(1.0);
			minus_1Args.add(3.0);
			Minus minus_1 = new Minus(minus_1Args, State.FIRST_OPERAND, new ArrayList<Integer>());

			List<Double> div_1Args = new ArrayList<Double>();
			div_1Args.add(1.0);
			div_1Args.add(3.0);
			Division div_1 = new Division(div_1Args, State.FIRST_OPERAND, new ArrayList<Integer>());

			List<Double> plus_1Args = new ArrayList<Double>();
			plus_1Args.add(1.0);
			List<Integer> plus_1ApplyElementsNumber = new ArrayList<Integer>();
			plus_1ApplyElementsNumber.add(0);
			plus_1ApplyElementsNumber.add(0);
			Plus plus_1 = new Plus(plus_1Args, plus_1ApplyElementsNumber);

			List<Double> div_2Args = new ArrayList<Double>();
			div_2Args.add(1.0);
			div_2Args.add(3.0);
			Division div_2 = new Division(div_2Args, State.FIRST_OPERAND, new ArrayList<Integer>());

			List<Double> minus_2Args = new ArrayList<Double>();
			minus_2Args.add(1.0);
			List<Integer> minus_2ApplyElementsNumber = new ArrayList<Integer>();
			minus_2ApplyElementsNumber.add(0);
			Minus minus_2 = new Minus(minus_2Args, State.FIRST_OPERAND, minus_2ApplyElementsNumber);

			List<Double> tanArgs = new ArrayList<Double>();
			tanArgs.add(45.0);
			Tan tan = new Tan(tanArgs, new ArrayList<Integer>());

			List<Double> powerArgs = new ArrayList<Double>();
			powerArgs.add(30.0);
			List<Integer> powerApplyElementsNumber = new ArrayList<Integer>();
			powerApplyElementsNumber.add(0);
			Power power = new Power(powerArgs, State.SECOND_OPERAND, powerApplyElementsNumber);

			List<Integer> sin_1ApplyElementsNumber = new ArrayList<Integer>();
			sin_1ApplyElementsNumber.add(1);
			Sin sin_1 = new Sin(new ArrayList<Double>(), sin_1ApplyElementsNumber);

			List<Double> sin_2Args = new ArrayList<Double>();
			sin_2Args.add(45.0);
			Sin sin_2 = new Sin(sin_2Args, new ArrayList<Integer>());

			List<Integer> cosApplyElementsNumber = new ArrayList<Integer>();
			cosApplyElementsNumber.add(0);
			Cos cos = new Cos(new ArrayList<Double>(), cosApplyElementsNumber);

			List<Integer> plus_2ApplyElementsNumber = new ArrayList<Integer>();
			plus_2ApplyElementsNumber.add(1);
			plus_2ApplyElementsNumber.add(2);
			plus_2ApplyElementsNumber.add(1);
			plus_2ApplyElementsNumber.add(2);
			Plus plus_2 = new Plus(new ArrayList<Double>(), plus_2ApplyElementsNumber);

			operands.add(minus_1);
			operands.add(div_1);
			operands.add(plus_1);
			operands.add(div_2);
			operands.add(minus_2);
			operands.add(tan);
			operands.add(power);
			operands.add(sin_1);
			operands.add(sin_2);
			operands.add(cos);
			operands.add(plus_2);

			return operands;
		}
	};

	IMathMLExpression expectedExpression_19 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
			List<Double> plusArgs = new ArrayList<Double>();
			Plus plus = new Plus(plusArgs);

			operands.add(plus);

			return operands;
		}
	};

	IMathMLExpression expectedExpression_20 = new Expression() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public List<MathematicalOperation> getExpressionOperands() {
			List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();

			List<Double> plus_1Args = new ArrayList<Double>();
			plus_1Args.add(1.0);
			plus_1Args.add(3.0);
			Plus plus_1 = new Plus(plus_1Args, new ArrayList<Integer>());

			List<Double> divArgs = new ArrayList<Double>();
			divArgs.add(4.0);
			List<Integer> divApplyElementsNumber = new ArrayList<Integer>();
			divApplyElementsNumber.add(0);
			Division div = new Division(divArgs, State.SECOND_OPERAND, divApplyElementsNumber);

			List<Double> power_1Args = new ArrayList<Double>();
			power_1Args.add(3.0);
			power_1Args.add(2.0);
			Power power_1 = new Power(power_1Args, State.FIRST_OPERAND, new ArrayList<Integer>());

			List<Double> plus_2Args = new ArrayList<Double>();
			plus_2Args.add(1.0);
			List<Integer> plus_2ApplyElementsNumber = new ArrayList<Integer>();
			plus_2ApplyElementsNumber.add(0);
			Plus plus_2 = new Plus(plus_2Args, plus_2ApplyElementsNumber);

			List<Double> sinArgs = new ArrayList<Double>();
			sinArgs.add(30.0);
			Sin sin = new Sin(sinArgs, new ArrayList<Integer>());

			List<Integer> cosApplyElementsNumber = new ArrayList<Integer>();
			cosApplyElementsNumber.add(0);
			Cos cos = new Cos(new ArrayList<Double>(), cosApplyElementsNumber);

			List<Integer> power_2ApplyElementsNumber = new ArrayList<Integer>();
			power_2ApplyElementsNumber.add(1);
			power_2ApplyElementsNumber.add(1);
			Power power_2 = new Power(new ArrayList<Double>(), State.SECOND_OPERAND, power_2ApplyElementsNumber);

			List<Integer> plus_3ApplyElementsNumberList = new ArrayList<Integer>();
			plus_3ApplyElementsNumberList.add(4);
			plus_3ApplyElementsNumberList.add(1);
			Plus plus_3 = new Plus(new ArrayList<Double>(), plus_3ApplyElementsNumberList);

			operands.add(plus_1);
			operands.add(div);
			operands.add(power_1);
			operands.add(plus_2);
			operands.add(sin);
			operands.add(cos);
			operands.add(power_2);
			operands.add(plus_3);

			return operands;
		}
	};

	protected IMathMLExpression createActualExpression_7() {
		List<Double> minus_1Args = new ArrayList<Double>();
		minus_1Args.add(2.0);
		minus_1Args.add(3.0);
		Minus minus_1 = new Minus(minus_1Args, State.FIRST_OPERAND, new ArrayList<Integer>());

		List<Integer> minus_2ApplyElementsNumberList = new ArrayList<Integer>();
		minus_2ApplyElementsNumberList.add(1);
		List<Double> minus_2Args = new ArrayList<Double>();
		minus_2Args.add(5.0);
		Minus minus_2 = new Minus(minus_2Args, State.FIRST_OPERAND, minus_2ApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(minus_1);
		operands.add(minus_2);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_5() {
		List<Double> plusArgs_1 = new ArrayList<Double>();
		plusArgs_1.add(4.0);
		plusArgs_1.add(2.0);
		Plus plus_1 = new Plus(plusArgs_1);

		List<Integer> minus_1ApplyElementsNumberList = new ArrayList<Integer>();
		minus_1ApplyElementsNumberList.add(0);
		List<Double> minusArgs_1 = new ArrayList<Double>();
		minusArgs_1.add(2.0);
		Minus minus_1 = new Minus(minusArgs_1, State.FIRST_OPERAND, minus_1ApplyElementsNumberList);

		List<Integer> minus_2ApplyElementsNumberList = new ArrayList<Integer>();
		minus_2ApplyElementsNumberList.add(1);
		List<Double> minusArgs_2 = new ArrayList<Double>();
		minusArgs_2.add(4.0);
		Minus minus_2 = new Minus(minusArgs_2, State.FIRST_OPERAND, minus_2ApplyElementsNumberList);

		List<Integer> plus_2ApplyElementsNumberList = new ArrayList<Integer>();
		plus_2ApplyElementsNumberList.add(2);
		List<Double> plusArgs_2 = new ArrayList<Double>();
		plusArgs_2.add(2.0);
		Plus plus_2 = new Plus(plusArgs_2, plus_2ApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(plus_1);
		operands.add(minus_1);
		operands.add(minus_2);
		operands.add(plus_2);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_2() {
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(4.0);
		plusArgs.add(2.0);
		Plus plus = new Plus(plusArgs);

		List<Integer> minusApplyElementsNumberList = new ArrayList<Integer>();
		minusApplyElementsNumberList.add(0);
		List<Double> minusArgs = new ArrayList<Double>();
		minusArgs.add(4.0);
		Minus minus = new Minus(minusArgs, State.FIRST_OPERAND, minusApplyElementsNumberList);

		List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
		divApplyElementsNumberList.add(1);
		List<Double> divArgs = new ArrayList<Double>();
		divArgs.add(2.0);
		Division div = new Division(divArgs, State.SECOND_OPERAND, divApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(plus);
		operands.add(minus);
		operands.add(div);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_1() {
		List<Double> sqrtArgs = new ArrayList<Double>();
		sqrtArgs.add(4.0);
		Sqrt sqrt = new Sqrt(sqrtArgs);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sqrt);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createAcutalExpression_3() {
		List<Double> minusArgs = new ArrayList<Double>();
		minusArgs.add(2.0);
		minusArgs.add(4.0);
		Minus minus = new Minus(minusArgs, State.FIRST_OPERAND, new ArrayList<Integer>());

		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(4.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
		divApplyElementsNumberList.add(1);
		List<Double> divArgs = new ArrayList<Double>();
		divArgs.add(2.0);
		Division div = new Division(divArgs, State.SECOND_OPERAND, divApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(minus);
		operands.add(plus);
		operands.add(div);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_4() {
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(4.0);
		plusArgs.add(2.0);
		Plus plus = new Plus(plusArgs);

		List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
		divApplyElementsNumberList.add(0);
		List<Double> divArgs = new ArrayList<Double>();
		divArgs.add(2.0);
		Division div = new Division(divArgs, State.FIRST_OPERAND, divApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(plus);
		operands.add(div);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_6() {
		List<Double> minusArgs = new ArrayList<Double>();
		minusArgs.add(2.0);
		minusArgs.add(3.0);
		Minus minus = new Minus(minusArgs);

		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(4.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(minus);
		operands.add(plus);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_8() {
		List<Double> minus_1Args = new ArrayList<Double>();
		minus_1Args.add(2.0);
		minus_1Args.add(3.0);
		Minus minus_1 = new Minus(minus_1Args);

		List<Integer> minus_2ApplyElementsnumberList = new ArrayList<Integer>();
		minus_2ApplyElementsnumberList.add(0);
		List<Double> minus_2Args = new ArrayList<Double>();
		minus_2Args.add(2.0);
		Minus minus_2 = new Minus(minus_2Args, State.FIRST_OPERAND, minus_2ApplyElementsnumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(minus_1);
		operands.add(minus_2);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_9() {
		List<Double> minusArgs = new ArrayList<Double>();
		minusArgs.add(2.0);
		minusArgs.add(4.0);
		Minus minus = new Minus(minusArgs, State.FIRST_OPERAND, new ArrayList<Integer>());

		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(4.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
		divApplyElementsNumberList.add(1);
		List<Double> divArgs = new ArrayList<Double>();
		divArgs.add(2.0);
		Division div = new Division(divArgs, State.FIRST_OPERAND, divApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(minus);
		operands.add(plus);
		operands.add(div);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_10() {
		List<Double> div_1Args = new ArrayList<Double>();
		div_1Args.add(2.0);
		div_1Args.add(3.0);
		Division div_1 = new Division(div_1Args, State.FIRST_OPERAND, new ArrayList<Integer>());

		List<Integer> div_2ApplyElementsNumberList = new ArrayList<Integer>();
		div_2ApplyElementsNumberList.add(0);
		List<Double> div_2Args = new ArrayList<Double>();
		div_2Args.add(3.0);
		Division div_2 = new Division(div_2Args, State.FIRST_OPERAND, div_2ApplyElementsNumberList);

		List<Integer> div_3ApplyElementsNumberList = new ArrayList<Integer>();
		div_3ApplyElementsNumberList.add(1);
		List<Double> div_3Args = new ArrayList<Double>();
		div_3Args.add(4.0);
		Division div_3 = new Division(div_3Args, State.FIRST_OPERAND, div_3ApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(div_1);
		operands.add(div_2);
		operands.add(div_3);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_11() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(45.0);
		Sin sin = new Sin(sinArgs);

		List<Integer> plusApplyElementsNumberlist = new ArrayList<Integer>();
		plusApplyElementsNumberlist.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(3.0);
		plusArgs.add(2.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberlist);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(plus);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_12() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(45.0);
		Sin sin = new Sin(sinArgs);

		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(3.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(plus);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_13() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(45.0);
		Sin sin = new Sin(sinArgs);

		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(1.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<Integer> div_1ApplyElementsNumberList = new ArrayList<Integer>();
		div_1ApplyElementsNumberList.add(1);
		List<Double> div_1Args = new ArrayList<Double>();
		div_1Args.add(3.0);
		Division div_1 = new Division(div_1Args, State.FIRST_OPERAND, div_1ApplyElementsNumberList);

		List<Integer> div_2ApplyElementsNumberlist = new ArrayList<Integer>();
		div_2ApplyElementsNumberlist.add(2);
		List<Double> div_2Args = new ArrayList<Double>();
		div_2Args.add(2.0);
		Division div_2 = new Division(div_2Args, State.FIRST_OPERAND, div_2ApplyElementsNumberlist);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(plus);
		operands.add(div_1);
		operands.add(div_2);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_14() {
		List<Double> minusArgs = new ArrayList<Double>();
		minusArgs.add(2.0);
		minusArgs.add(3.0);
		Minus minus = new Minus(minusArgs);

		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(1.0);
		plusArgs.add(2.0);
		plusArgs.add(3.0);
		plusArgs.add(4.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(minus);
		operands.add(plus);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_15() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(45.0);
		Sin sin = new Sin(sinArgs);

		List<Integer> powerApplyElementsNumberList = new ArrayList<Integer>();
		powerApplyElementsNumberList.add(0);
		List<Double> powerArgs = new ArrayList<Double>();
		powerArgs.add(1.0);
		Power power = new Power(powerArgs, State.SECOND_OPERAND, powerApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(power);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_16() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(30.0);
		Sin sin = new Sin(sinArgs);

		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(1.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<Integer> powerApplyElementsNumberList = new ArrayList<Integer>();
		powerApplyElementsNumberList.add(1);
		List<Double> powerArgs = new ArrayList<Double>();
		powerArgs.add(1.0);
		Power power = new Power(powerArgs, State.SECOND_OPERAND, powerApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(plus);
		operands.add(power);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_17() {
		List<Double> powerArgs = new ArrayList<Double>();
		powerArgs.add(1.0);
		powerArgs.add(3.0);
		Power power = new Power(powerArgs);

		List<Double> cosArgs = new ArrayList<Double>();
		cosArgs.add(30.0);
		Cos cos = new Cos(cosArgs);

		List<Integer> sinApplyElementsNumber = new ArrayList<Integer>();
		sinApplyElementsNumber.add(0);
		List<Double> sinArgs = new ArrayList<Double>();
		Sin sin = new Sin(sinArgs, sinApplyElementsNumber);

		List<Integer> plusApplyElementsNumber = new ArrayList<Integer>();
		plusApplyElementsNumber.add(1);
		plusApplyElementsNumber.add(0);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(1.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumber);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(power);
		operands.add(cos);
		operands.add(sin);
		operands.add(plus);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_19() {
		List<Double> tanArgs = new ArrayList<Double>();
		tanArgs.add(45.0);
		Tan tan = new Tan(tanArgs);

		List<Integer> cosApplyElementsNumberList = new ArrayList<Integer>();
		cosApplyElementsNumberList.add(0);
		Cos cos = new Cos(new ArrayList<Double>(), cosApplyElementsNumberList);

		List<Integer> sinApplyElementsNumberList = new ArrayList<Integer>();
		sinApplyElementsNumberList.add(1);
		Sin sin = new Sin(new ArrayList<Double>(), sinApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(tan);
		operands.add(cos);
		operands.add(sin);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_20() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(30.0);
		Sin sin = new Sin(sinArgs);

		List<Integer> minusApplyElementsNumberList = new ArrayList<Integer>();
		minusApplyElementsNumberList.add(0);
		List<Double> minusArgs = new ArrayList<Double>();
		minusArgs.add(1.0);
		Minus minus = new Minus(minusArgs, State.FIRST_OPERAND, minusApplyElementsNumberList);

		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(1);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(1.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(minus);
		operands.add(plus);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_21() {
		List<Double> plus_1Args = new ArrayList<Double>();
		plus_1Args.add(1.0);
		plus_1Args.add(3.0);
		Plus plus_1 = new Plus(plus_1Args);

		List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
		divApplyElementsNumberList.add(0);
		List<Double> div_1Args = new ArrayList<Double>();
		div_1Args.add(4.0);
		Division div_1 = new Division(div_1Args, State.SECOND_OPERAND, divApplyElementsNumberList);

		List<Double> power_1Args = new ArrayList<Double>();
		power_1Args.add(3.0);
		power_1Args.add(2.0);
		Power power_1 = new Power(power_1Args);

		List<Integer> plus_2ApplyElementsNumberList = new ArrayList<Integer>();
		plus_2ApplyElementsNumberList.add(0);
		List<Double> plus_2Args = new ArrayList<Double>();
		plus_2Args.add(1.0);
		Plus plus_2 = new Plus(plus_2Args, plus_2ApplyElementsNumberList);

		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(30.0);
		Sin sin = new Sin(sinArgs);

		List<Integer> cosApplyElementsNumberList = new ArrayList<Integer>();
		cosApplyElementsNumberList.add(0);
		Cos cos = new Cos(new ArrayList<Double>(), cosApplyElementsNumberList);

		List<Integer> power_2ApplyElementsNumberList = new ArrayList<Integer>();
		power_2ApplyElementsNumberList.add(1);
		power_2ApplyElementsNumberList.add(1);
		Power power_2 = new Power(new ArrayList<Double>(), State.FIRST_OPERAND, power_2ApplyElementsNumberList);

		List<Integer> plus_3ApplyElementsNumberList = new ArrayList<Integer>();
		plus_3ApplyElementsNumberList.add(4);
		plus_3ApplyElementsNumberList.add(1);
		Plus plus_3 = new Plus(new ArrayList<Double>(), plus_3ApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(plus_1);
		operands.add(div_1);
		operands.add(power_1);
		operands.add(plus_2);
		operands.add(sin);
		operands.add(cos);
		operands.add(power_2);
		operands.add(plus_3);

		IMathMLExpression e = new Expression(operands);

		return e;
	}

	protected IMathMLExpression createActualExpression_22() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(30.0);
		Sin sin = new Sin(sinArgs);

		List<Double> minus_1Args = new ArrayList<Double>();
		minus_1Args.add(3.0);
		List<Integer> minus_1NestedApplyElementsNumberList = new ArrayList<Integer>();
		minus_1NestedApplyElementsNumberList.add(0);
		Minus minus_1 = new Minus(minus_1Args, State.SECOND_OPERAND, minus_1NestedApplyElementsNumberList);

		List<Double> minus_2Args = new ArrayList<Double>();
		minus_2Args.add(1.0);
		List<Integer> minus_2NestedApplyElementsNumberList = new ArrayList<Integer>();
		minus_2NestedApplyElementsNumberList.add(1);
		Minus minus_2 = new Minus(minus_2Args, State.FIRST_OPERAND, minus_2NestedApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(minus_1);
		operands.add(minus_2);

		IMathMLExpression expression = new Expression(operands);

		return expression;
	}

	protected IMathMLExpression createActualExpression_23() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(30.0);
		Sin sin = new Sin(sinArgs);

		List<Double> minus_1Args = new ArrayList<Double>();
		minus_1Args.add(3.0);
		List<Integer> minus_1NestedApplyElementsNumberList = new ArrayList<Integer>();
		minus_1NestedApplyElementsNumberList.add(0);
		Minus minus_1 = new Minus(minus_1Args, State.SECOND_OPERAND, minus_1NestedApplyElementsNumberList);

		List<Double> minus_2Args = new ArrayList<Double>();
		minus_2Args.add(1.0);
		List<Integer> minus_2NestedApplyElementsNumberList = new ArrayList<Integer>();
		minus_2NestedApplyElementsNumberList.add(1);
		Minus minus_2 = new Minus(minus_2Args, State.FIRST_OPERAND, minus_2NestedApplyElementsNumberList);

		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(2.0);
		List<Integer> plusNestedApplyElementsNumberList = new ArrayList<Integer>();
		plusNestedApplyElementsNumberList.add(2);
		Plus plus = new Plus(plusArgs, plusNestedApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(minus_1);
		operands.add(minus_2);
		operands.add(plus);

		IMathMLExpression expression = new Expression(operands);

		return expression;
	}

	protected IMathMLExpression createActualExpression_24() {
		List<Double> sinArgs = new ArrayList<Double>();
		sinArgs.add(30.0);
		Sin sin = new Sin(sinArgs);

		List<Double> powerArgs = new ArrayList<Double>();
		powerArgs.add(1.0);
		List<Integer> powerNestedApplyElementsNumber = new ArrayList<Integer>();
		powerNestedApplyElementsNumber.add(0);
		Power power = new Power(powerArgs, State.SECOND_OPERAND, powerNestedApplyElementsNumber);

		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(1.0);
		List<Integer> plusNestedApplyElementsNumber = new ArrayList<Integer>();
		plusNestedApplyElementsNumber.add(2);
		Plus plus = new Plus(plusArgs, plusNestedApplyElementsNumber);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(sin);
		operands.add(power);
		operands.add(plus);

		IMathMLExpression expression = new Expression(operands);

		return expression;
	}

	protected IMathMLExpression createActualExpression_25() {
		List<Double> plus_1Args = new ArrayList<Double>();
		plus_1Args.add(3.0);
		plus_1Args.add(4.0);
		Plus plus_1 = new Plus(plus_1Args);

		List<Double> minusArgs = new ArrayList<Double>();
		minusArgs.add(2.0);
		List<Integer> minusApplyElementsNumberList = new ArrayList<Integer>();
		minusApplyElementsNumberList.add(0);
		Minus minus = new Minus(minusArgs, State.FIRST_OPERAND, minusApplyElementsNumberList);

		List<Integer> plus_2ApplyElementsNumberList = new ArrayList<Integer>();
		plus_2ApplyElementsNumberList.add(1);
		List<Double> plus_2Args = new ArrayList<Double>();
		plus_2Args.add(4.0);
		Plus plus_2 = new Plus(plus_2Args, plus_2ApplyElementsNumberList);

		List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
		divApplyElementsNumberList.add(2);
		List<Double> divArgs = new ArrayList<Double>();
		divArgs.add(2.0);
		Division div = new Division(divArgs, State.FIRST_OPERAND, divApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(plus_1);
		operands.add(minus);
		operands.add(plus_2);
		operands.add(div);

		IMathMLExpression expression = new Expression(operands);

		return expression;
	}

	protected IMathMLExpression createActualExpression_26() {
		List<Double> timesArgs = new ArrayList<Double>();
		timesArgs.add(2.0);
		timesArgs.add(3.0);
		Multiplication times = new Multiplication(timesArgs);

		List<Double> divArgs = new ArrayList<Double>();
		divArgs.add(2.0);
		List<Integer> divApplyElementsNumberList = new ArrayList<Integer>();
		divApplyElementsNumberList.add(0);
		Division div = new Division(divArgs, State.SECOND_OPERAND, divApplyElementsNumberList);

		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(3.0);
		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(1);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(times);
		operands.add(div);
		operands.add(plus);

		IMathMLExpression expression = new Expression(operands);

		return expression;
	}

	protected IMathMLExpression createActualExpression_27() {
		List<Double> coshArgs = new ArrayList<Double>();
		coshArgs.add(3.0);
		Cosh cosh = new Cosh(coshArgs);

		List<Double> tanhArgs = new ArrayList<Double>();
		List<Integer> tanhApplyElementsNumberList = new ArrayList<Integer>();
		tanhApplyElementsNumberList.add(0);
		Tanh tanh = new Tanh(tanhArgs, tanhApplyElementsNumberList);
		
		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(1);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(1.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(cosh);
		operands.add(tanh);
		operands.add(plus);

		IMathMLExpression expression = new Expression(operands);

		return expression;
	}
	
	protected IMathMLExpression createActualExpression_28() {
		List<Double> coshArgs = new ArrayList<Double>();
		coshArgs.add(3.0);
		Cosh cosh = new Cosh(coshArgs);

		List<Double> tanArgs = new ArrayList<Double>();
		List<Integer> tanApplyElementsNumberList = new ArrayList<Integer>();
		tanApplyElementsNumberList.add(0);
		Tan tan = new Tan(tanArgs, tanApplyElementsNumberList);
		
		List<Integer> plusApplyElementsNumberList = new ArrayList<Integer>();
		plusApplyElementsNumberList.add(1);
		List<Double> plusArgs = new ArrayList<Double>();
		plusArgs.add(1.0);
		Plus plus = new Plus(plusArgs, plusApplyElementsNumberList);

		List<MathematicalOperation> operands = new ArrayList<MathematicalOperation>();
		operands.add(cosh);
		operands.add(tan);
		operands.add(plus);

		IMathMLExpression expression = new Expression(operands);

		return expression;
	}

}
