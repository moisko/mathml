package mathml.transform;

import javax.xml.parsers.ParserConfigurationException;

public class MathMLTransformerFactory {

	private MathMLTransformerFactory() {
	}

	public static MathMLTransformerFactory getDefault() {
		return new MathMLTransformerFactory();
	}

	public MathMLTransformer createMathMLTransformer() throws ParserConfigurationException {
		return new MathMLTransformerImpl();
	}
}
