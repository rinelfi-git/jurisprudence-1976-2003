package mg.jurisprudence.designPattern.model.dao;

import mg.jurisprudence.designPattern.model.dao.interfaces.InJurisprudence;

import java.sql.Connection;
import java.sql.SQLException;

public interface DaoFactory {
	public Connection getConnection() throws SQLException;
	public InJurisprudence getJurisprudenceDao();
}
