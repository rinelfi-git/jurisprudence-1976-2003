package mg.jurisprudence.designPattern.model.interfaces;

import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.designPattern.model.dao.msaccess.Constraint;

import java.util.ArrayList;

public interface InJurisprudence {
	public ArrayList<Jurisprudence> select(ArrayList<Constraint> constraints);
	public Jurisprudence select(int id);
}