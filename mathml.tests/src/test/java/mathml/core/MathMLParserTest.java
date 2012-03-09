package mathml.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.Assert;
import mathml.api.ICalculate;
import mathml.api.IMathMLExpression;
import mathml.api.IMathMLParser;
import mathml.parser.ContentMarkupParser;

import org.junit.Test;

public class MathMLParserTest extends MathMLBaseTest {

    @Test
    public void testParseMathFile_2() throws Exception {
        File mathFile = new File(mathFile_2);
        InputStream is = null;
        try {
            is = new FileInputStream(mathFile);

            IMathMLParser parser = new ContentMarkupParser();

            IMathMLExpression expression = parser.parse(is);
            ICalculate calculator = new Calculator();

            Double actualResult = calculator.calculate(expression);
            Double expectedResult = -1.0;

            Assert.assertEquals(expectedResult, actualResult);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    @Test
    public void testParseMathFile_3() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_3));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 8.0;

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseMathFile_4() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_4));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 1.5;

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseMathFile_5() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_5));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 14.15;

        Assert.assertEquals(expectedResult, actualResult, 0.005);
    }

    @Test
    public void testParseMathFile_6() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_6));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 0.733;

        Assert.assertEquals(expectedResult, actualResult, 0.0004);
    }

    @Test
    public void testParseMathFile_7() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_7));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 0.711;

        Assert.assertEquals(expectedResult, actualResult, 0.0005);
    }

    @Test
    public void testParseMathFile_9() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_9));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 10.0;

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseMathFile_10() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_10));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 20.0;

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseMathFile_11() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_11));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 4.421;

        Assert.assertEquals(expectedResult, actualResult, 0.0009);
    }

    @Test
    public void testParseMathFile_13() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_13));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 1.23;

        Assert.assertEquals(expectedResult, actualResult, 0.004);
    }

    @Test
    public void testParseMathFile_14() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_14));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 9.0;

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseMathFile_21() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_21));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = 2.0;

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testParseMathFile_22() throws Exception {
        InputStream is = new FileInputStream(new File(mathFile_22));

        IMathMLParser parser = new ContentMarkupParser();

        IMathMLExpression expression = parser.parse(is);

        ICalculate calculator = new Calculator();

        Double actualResult = calculator.calculate(expression);
        Double expectedResult = Double.NaN;

        Assert.assertEquals(expectedResult, actualResult);
    }

}
