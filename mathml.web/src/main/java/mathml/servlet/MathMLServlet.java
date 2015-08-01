package mathml.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import mathml.api.ICalculate;
import mathml.api.IMathMLExpression;
import mathml.api.IMathMLParser;
import mathml.api.UnsupportedMathMLElementException;
import mathml.core.Calculator;
import mathml.parser.ContentMarkupParser;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class MathMLServlet extends HttpServlet {
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
					String fieldName = item.getFieldName();
					String contentType = item.getContentType();

					// calculate the result from the uploaded MathML file
					IMathMLParser parser = new ContentMarkupParser();
					IMathMLExpression expression = parser.parse(mathMLContent);
					ICalculate calculator = new Calculator();
					Double result = calculator.calculate(expression);

					// send the result to the client
					out.println("----------------------------------------");
					out.println("fileName = " + fileName);
					out.println("field name = " + fieldName);
					out.println("content type = " + contentType);

					out.println("----------------------------------------");
					out.println("result = " + result);
					out.println("----------------------------------------");
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
		} catch (UnsupportedMathMLElementException e) {
			e.printStackTrace();
		}
	}
}
