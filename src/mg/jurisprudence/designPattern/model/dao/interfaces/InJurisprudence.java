package mg.jurisprudence.designPattern.model.dao.interfaces;

import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.designPattern.model.dao.Constraint;

import java.util.ArrayList;

public interface InJurisprudence {
	public ArrayList<Jurisprudence> select(ArrayList<Constraint> constraints);
	public Jurisprudence select(int id);
}
