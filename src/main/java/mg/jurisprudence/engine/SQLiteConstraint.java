package mg.jurisprudence.engine;

import java.util.ArrayList;

public class SQLiteConstraint extends Constraint {
	@Override
	public String getCompiledConstraint() {
		ArrayList<String> compiledRequest = new ArrayList<>();
		String output = "";
		if (!"".equals(super.getNumero())) compiledRequest.add("numero LIKE ?");
		if (!"".equals(super.getNomParties())) compiledRequest.add("nom_partie LIKE ?");
		if (!"".equals(super.getCommentaire())) compiledRequest.add("commentaire LIKE ?");
		if (!"".equals(super.getTexte())) compiledRequest.add("texte LIKE ?");
		if (super.isTreatDate()) {
			switch (super.getDateFlag()) {
				case DATE_CONSTRAINT_BEFORE:
					compiledRequest.add("date_decision < ?");
					break;
				case DATE_CONSTRAINT_EQUAL:
					compiledRequest.add("date_decision = ?");
					break;
				case DATE_CONSTRAINT_AFTER:
					compiledRequest.add("date_decision > ?");
					break;
				case DATE_CONSTRAINT_BETWEEN:
					compiledRequest.add("date_decision BETWEEN ? AND ?");
					break;
			}
		}
		for (String request : compiledRequest) {
			output += request + " AND ";
		}
		output = output.substring(0, output.length() - 5);
		return output;
	}
}
