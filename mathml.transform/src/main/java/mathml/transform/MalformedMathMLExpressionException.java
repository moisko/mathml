package mathml.transform;

public class MalformedMathMLExpressionException extends Exception {
	private static final long serialVersionUID = 1L;

	public MalformedMathMLExpressionException() {

	}

	public MalformedMathMLExpressionException(String message) {
		super(message);
	}

	public MalformedMathMLExpressionException(String message, Exception exception) {
		super(message, exception);
	}
	
	public MalformedMathMLExpressionException(Throwable t){
		super(t);
	}
}
