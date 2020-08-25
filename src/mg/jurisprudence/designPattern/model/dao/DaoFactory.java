package mg.jurisprudence.designPattern.model.dao;

import mg.jurisprudence.designPattern.model.interfaces.InJurisprudence;

import java.sql.Connection;
import java.sql.SQLException;

public interface DaoFactory {
	public Connection getConnection() throws Exception;
	public InJurisprudence getJurisprudenceDao();
}
