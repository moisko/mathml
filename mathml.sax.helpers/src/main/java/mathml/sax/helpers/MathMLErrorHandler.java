package mathml.sax.helpers;

import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class MathMLErrorHandler extends DefaultHandler {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private boolean validationError;

	private SAXParseException saxParseException;

	public void error(SAXParseException e) {
		this.validationError = true;

		this.saxParseException = e;
	}

	public void warning(SAXParseException e) {
		this.validationError = false;

		this.saxParseException = e;
	}

	public void fatalError(SAXParseException e) {
		this.validationError = true;

		this.saxParseException = e;
	}

	public boolean isValidMathMLFile() {
		if (validationError) {
			return false;
		} else {
			return true;
		}
	}

	public SAXParseException getSAXParseException() {
		return saxParseException;
	}

	public String getExceptionStackTrace() {
		StringBuilder sb = new StringBuilder();
		if (saxParseException != null) {
			sb.append("line number: " + saxParseException.getLineNumber()).append(LINE_SEPARATOR);
			sb.append("column number: " + saxParseException.getColumnNumber()).append(LINE_SEPARATOR);
			sb.append("localized message: " + saxParseException.getLocalizedMessage()).append(LINE_SEPARATOR);
			sb.append("message: " + saxParseException.getMessage()).append(LINE_SEPARATOR);
			sb.append("stack trace: " + saxParseException.toString());
		}

		return sb.toString();
	}
}
