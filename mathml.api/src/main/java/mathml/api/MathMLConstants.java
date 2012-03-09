package mathml.api;

public interface MathMLConstants {
	
	// ========================= math attributes ================================
	
	public static final String MATHML_NAMESPACE_URI = "http://www.w3.org/1998/Math/MathML";
	
	public static final String XSI_NAMESPACE_URI = "http://www.w3.org/2001/XMLSchema-instance";
	
	public static final String SCHEMA_LOCATION_URI = "http://www.w3.org/1998/Math/MathML http://www.w3.org/Math/XMLSchema/mathml2/mathml2.xsd";
	
	
	// ========================= content markup tags ============================
	
	public static final String MML_MATH = "mml:math";
	
	public static final String MML_APPLY = "mml:apply";
	
	public static final String APPLY = "apply";
	
	public static final String MML_CN = "mml:cn";
	
	public static final String MML_CI = "mml:ci";
	
	
	// ========================== operation tags ================================
	
	public static final String MML_PLUS = "mml:plus";
	
	public static final String MML_MINUS = "mml:minus";
	
	public static final String MINUS = "minus";
	
	public static final String MML_DIVIDE = "mml:divide";
	
	public static final String DIVIDE = "divide";
	
	public static final String MML_TIMES = "mml:times";
	
	
	// ========================== function tags =================================
	
	public static final String MML_SIN = "mml:sin";
	
	public static final String MML_COS = "mml:cos";
	
	public static final String MML_TAN = "mml:tan";
	
	public static final String MML_TANH = "mml:tanh";
	
	public static final String MML_SQRT = "mml:root";
	
	public static final String MML_POWER = "mml:power";
	
	public static final String MML_COSH = "mml:cosh";
	
	public static final String MML_ARC_COSH = "mml:arccosh";
	
	public static final String POWER = "power";
	
	
	// ========================== presentation markup tags ======================
	
	// Token

	public static final String MI_ELEMENT = "mml:mi";

	public static final String MO_ELEMENT = "mml:mo";

	public static final String MN_ELEMENT = "mml:mn";
	
	// LayoutSchemata 
	
	public static final String MROW_ELEMENT = "mml:mrow";

	public static final String MFENCED_ELEMENT = "mml:mfenced";

	public static final String MFRAC_ELEMENT = "mml:mfrac";

	public static final String MROOT_ELEMENT = "mml:mroot";

	public static final String MSQRT_ELEMENT = "mml:msqrt";

	public static final String MSUP_ELEMENT = "mml:msup";
}
