package mathml.api;

import java.io.Serializable;
import java.util.List;

public interface MathematicalOperation extends Serializable {
	
	public List<Double> getArguments();
	
	public void setArgument(Double arg);
	
	public int getType();
	
	public List<Integer> getArgsDistanceFromMathOperation();

	public Double getResult() throws IllegalArgumentException, ArithmeticException;
}
