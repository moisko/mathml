package mathml.transform;

import javax.xml.parsers.ParserConfigurationException;

public class MathMLTransformerFactory {

	private MathMLTransformerFactory() {

	}

	public static MathMLTransformerFactory newMathMLTransformerFactory() {
		return new MathMLTransformerFactory();
	}

	public MathMLTransformer newMathMLTransformer() throws ParserConfigurationException {
		return new MathMLTransformerImpl();
	}
}
