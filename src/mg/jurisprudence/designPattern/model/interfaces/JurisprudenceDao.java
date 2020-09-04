package mg.jurisprudence.designPattern.model.interfaces;

import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.engine.Constraint;

import java.util.ArrayList;

public interface JurisprudenceDao {
	ArrayList<Jurisprudence> select(ArrayList<Constraint> constraints);
	Jurisprudence select(int id);
}
