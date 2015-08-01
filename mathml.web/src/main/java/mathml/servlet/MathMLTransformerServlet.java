package mathml.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import mathml.api.IMathMLExpression;
import mathml.api.IMathMLParser;
import mathml.api.UnsupportedMathMLElementException;
import mathml.parser.PresentationMarkupParser;
import mathml.transform.MalformedMathMLExpressionException;
import mathml.transform.MathMLTransformer;
import mathml.transform.MathMLTransformerFactory;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.xml.sax.SAXException;

public class MathMLTransformerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final long MAX_SIZE_LIMIT = 50000;

	private static final String CONTENT_TYPE = "text/plain";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletFileUpload fileUpload = new ServletFileUpload();
		// set the file size limit
		fileUpload.setSizeMax(MAX_SIZE_LIMIT);

		response.setContentType(CONTENT_TYPE);

		PrintWriter out = response.getWriter();
		try {
			FileItemIterator iterator = fileUpload.getItemIterator(request);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream mathMLContent = item.openStream();
				if (item.isFormField()) {
					out.println("Got a form field: " + item.getFieldName());
				} else {
					String fileName = item.getName();

					IMathMLParser parser = new PresentationMarkupParser();
					IMathMLExpression expression = parser.parse(mathMLContent);
					MathMLTransformerFactory factory = MathMLTransformerFactory.getDefault();
					MathMLTransformer transformer = factory.createMathMLTransformer();
					out.println("----------------- " + fileName + " -----------------------");
					transformer.transform(expression, out);
					out.println("----------------------------------------------------------");
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace(out);
		} catch (SAXException e) {
			e.printStackTrace(out);
		} catch (ParserConfigurationException e) {
			e.printStackTrace(out);
		} catch (IllegalArgumentException e) {
			e.printStackTrace(out);
		} catch (ArithmeticException e) {
			e.printStackTrace(out);
		} catch (TransformerException e) {
			e.printStackTrace(out);
		} catch (MalformedMathMLExpressionException e) {
			e.printStackTrace(out);
		} catch (UnsupportedMathMLElementException e) {
			e.printStackTrace();
		}
	}
}
