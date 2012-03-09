package mathml.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import junit.framework.Assert;
import mathml.core.MathMLBaseTest;
import mathml.sax.helpers.MathMLErrorHandler;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class MathMLUtilitiesTest extends MathMLBaseTest {
	private static final String MATHML2_XSD_RELATIVE_PATH = "src/test/resources/mathml/schema/mathml2/mathml2.xsd";

	private Schema schema;

	@Before
	public void setUp() throws FileNotFoundException, SAXException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		Source schemaSource = new StreamSource(new File(MATHML2_XSD_RELATIVE_PATH));

		schema = schemaFactory.newSchema(schemaSource);
	}

	@Test
	public void testValidateMathFile_1() throws Exception {

		MathMLErrorHandler errorHandler = new MathMLErrorHandler();

		File mathfile_1 = new File(mathFile_1);

		MathMLUtilities.validateMathMLFile(new FileInputStream(mathfile_1), schema, errorHandler);

		Assert.assertTrue(errorHandler.getExceptionStackTrace(), errorHandler.isValidMathMLFile());
	}

	@Test
	public void testValidateMathFile_2() throws Exception {

		MathMLErrorHandler errorHandler = new MathMLErrorHandler();

		File mathfile_2 = new File(mathFile_2);

		MathMLUtilities.validateMathMLFile(new FileInputStream(mathfile_2), schema, errorHandler);

		Assert.assertTrue(errorHandler.getExceptionStackTrace(), errorHandler.isValidMathMLFile());
	}
}
