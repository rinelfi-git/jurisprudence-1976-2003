package mg.jurisprudence.designPattern.model.dao;

import mg.jurisprudence.designPattern.model.interfaces.JurisprudenceDao;

import java.sql.Connection;

public interface DaoFactory {
	Connection getConnection() throws Exception;
	JurisprudenceDao getJurisprudenceDao();
}
