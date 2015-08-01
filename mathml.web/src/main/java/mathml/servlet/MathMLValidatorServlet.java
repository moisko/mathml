package mathml.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import mathml.sax.helpers.MathMLErrorHandler;
import mathml.utils.MathMLUtilities;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.xml.sax.SAXException;

public class MathMLValidatorServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final long MAX_SIZE_LIMIT = 50000;

	private static final String CONTENT_TYPE = "text/plain";

	private static final String SCHEMA_RELATIVE_PATH = "/WEB-INF/schema/mathml2/mathml2.xsd";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletFileUpload fileUpload = new ServletFileUpload();
		// set the file size limit
		fileUpload.setSizeMax(MAX_SIZE_LIMIT);

		response.setContentType(CONTENT_TYPE);

		PrintWriter out = response.getWriter();

		String fileName = null;
		MathMLErrorHandler errorHandler = new MathMLErrorHandler();
		try {
			FileItemIterator iterator = fileUpload.getItemIterator(request);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream mathMLContent = item.openStream();
				if (item.isFormField()) {
					out.println("Got a form field: " + item.getFieldName());
				} else {
					fileName = item.getName();
					// validate MathML content
					validateMathMLContent(mathMLContent, errorHandler);
					out.println("---------------------------------------");
					out.println(fileName + " is valid");
					out.println("---------------------------------------");
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace(out);
		} catch (SAXException e) {
			if (fileName != null) {
				out.println("---------------------------------------");
				out.println(fileName + " is NOT valid with respect to mathml2.xsd schema");
				out.println("---------------------------------------");
			}
			
			out.println(errorHandler.getExceptionStackTrace());
			
			errorHandler.getSAXParseException().printStackTrace(out);
		} catch (IllegalArgumentException e) {
			e.printStackTrace(out);
		} catch (ArithmeticException e) {
			e.printStackTrace(out);
		}
	}

	private void validateMathMLContent(InputStream mathMLContent, MathMLErrorHandler errorHandler) throws SAXException, IOException {
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		ServletContext servletContext = getServletContext();

		Source schemaSource = new StreamSource(new File(servletContext.getRealPath(SCHEMA_RELATIVE_PATH)));

		Schema schema = schemaFactory.newSchema(schemaSource);

		MathMLUtilities.validateMathMLFile(mathMLContent, schema, errorHandler);
	}
}
