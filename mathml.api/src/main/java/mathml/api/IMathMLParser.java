package mathml.api;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public interface IMathMLParser {

	public IMathMLExpression parse(InputStream is) throws SAXException, IOException, ParserConfigurationException,
					UnsupportedMathMLElementException;
}
