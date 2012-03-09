package mathml.api;

public class UnsupportedMathMLElementException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnsupportedMathMLElementException() {
		super();
	}

	public UnsupportedMathMLElementException(String message) {
		super(message);
	}

	public UnsupportedMathMLElementException(String message, Exception exception) {
		super(message, exception);
	}
}
