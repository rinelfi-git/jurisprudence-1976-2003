package mg.jurisprudence.designPattern.model.interfaces;

import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.engine.Constraint;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface JurisprudenceDao {
	ArrayList<Jurisprudence> selectWithoutDate(Constraint constraint);
	ArrayList<Jurisprudence> selectWithDate(Constraint constraint);
	ArrayList<Jurisprudence> selectWithDateOnly(Constraint constraint);
	ArrayList<Jurisprudence> select();
	Jurisprudence select(int id);
}
