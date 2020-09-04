package mg.jurisprudence.engine;

public class Constraint {
	private String key;
	private String value;
	private String compiledConstraint;
	
	public Constraint(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public Constraint(String compiledConstraint) {
		this.compiledConstraint = compiledConstraint;
	}
	
	private String getCompiledConstraint() {
		if (key != null) {
			if (key.contains(" ")) return key + value.toLowerCase();
			else return key + " LIKE \"%" + value + "%\" OR " + key + " LIKE \"%" + value.toUpperCase() + "%\" OR " + key + " LIKE \"%" + value.toLowerCase() + "%\"";
		} else if (compiledConstraint != null) return compiledConstraint;
		return "";
	}
	
	@Override
	public String toString() {
		return getCompiledConstraint();
	}
}
