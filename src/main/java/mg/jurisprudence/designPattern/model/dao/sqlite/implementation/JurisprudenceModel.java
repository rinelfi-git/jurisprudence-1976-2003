package mg.jurisprudence.designPattern.model.dao.sqlite.implementation;

import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.designPattern.model.dao.sqlite.SQLite;
import mg.jurisprudence.designPattern.model.interfaces.JurisprudenceDao;
import mg.jurisprudence.engine.Constraint;

import java.util.ArrayList;

public class JurisprudenceModel implements JurisprudenceDao {
	private SQLite sqLite;
	
	public JurisprudenceModel(DaoFactory daoFactory) {
		this.sqLite = (SQLite) daoFactory;
	}
	
	@Override
	public ArrayList<Jurisprudence> selectWithoutDate(Constraint constraint) {
		return null;
	}
	
	@Override
	public ArrayList<Jurisprudence> selectWithDate(Constraint constraint) {
		return null;
	}
	
	@Override
	public ArrayList<Jurisprudence> selectWithDateOnly(Constraint constraint) {
		return null;
	}
	
	@Override
	public ArrayList<Jurisprudence> select() {
		return null;
	}
	
	@Override
	public Jurisprudence select(int id) {
		return null;
	}
}
