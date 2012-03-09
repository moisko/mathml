package mathml.transform;

import java.io.FileNotFoundException;
import java.io.Writer;

import javax.xml.transform.TransformerException;

import mathml.api.IMathMLExpression;

public abstract class MathMLTransformer {

	public abstract void transform(IMathMLExpression expression, Writer out) throws FileNotFoundException, TransformerException,
					MalformedMathMLExpressionException;

}
