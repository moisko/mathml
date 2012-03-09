package mathml.api;

public interface ICalculate {

	public Double calculate(IMathMLExpression expression) throws IllegalArgumentException, ArithmeticException;
}
