package mathml.api;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Boris Borisov
 *         <p>
 *         Basic interface representing the tree structure of the parsed MathML file.
 *         </p>
 */
public interface IMathMLExpression extends Serializable {

	/**
	 * Return list of all expression operands.
	 * 
	 * @return list of all expression operands
	 */
	public List<MathematicalOperation> getExpressionOperands();

	/**
	 * Check if the expression contains any operands.
	 * 
	 * @return true if the specified expression is empty, false otherwise
	 */
	public boolean isEmpty();
}
