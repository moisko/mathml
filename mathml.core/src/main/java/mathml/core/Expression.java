package mathml.core;

import java.util.ArrayList;
import java.util.List;

import mathml.api.IMathMLExpression;
import mathml.api.MathematicalOperation;

public class Expression implements IMathMLExpression {
	private static final long serialVersionUID = 1L;

	private final List<MathematicalOperation> expressionOperands;

	public Expression() {
		this(new ArrayList<MathematicalOperation>());
	}

	public Expression(List<MathematicalOperation> operands) {
		this.expressionOperands = operands;
	}

	public List<MathematicalOperation> getExpressionOperands() {
		return expressionOperands;
	}

	public boolean isEmpty() {
		return expressionOperands.isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expressionOperands == null) ? 0 : expressionOperands.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Expression other = (Expression) obj;
		if (expressionOperands == null) {
			if (other.expressionOperands != null) return false;
		} else if (!expressionOperands.equals(other.expressionOperands)) return false;
		return true;
	}
}
