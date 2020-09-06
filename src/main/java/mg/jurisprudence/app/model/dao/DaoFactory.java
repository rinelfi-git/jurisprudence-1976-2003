package mg.jurisprudence.app.model.dao;

import mg.jurisprudence.app.model.interfaces.JurisprudenceDao;

import java.sql.Connection;

public interface DaoFactory {
	Connection getConnection() throws Exception;
	JurisprudenceDao getJurisprudenceDao();
}
