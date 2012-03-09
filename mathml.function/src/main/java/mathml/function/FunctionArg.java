package mathml.function;

class FunctionArg {
	private final String name;
	private final int argNumber;

	public FunctionArg(String name, int argNumber) {
		this.name = name;
		
		this.argNumber = argNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + argNumber;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FunctionArg other = (FunctionArg) obj;
		if (argNumber != other.argNumber) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}

	public String getArgName() {
		return name;
	}

	public int getArgNumber() {
		return argNumber;
	}

	public String toString() {
		return name + argNumber;
	}
}
