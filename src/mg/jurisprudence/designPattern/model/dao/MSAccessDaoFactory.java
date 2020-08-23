package mg.jurisprudence.designPattern.model.dao;

import mg.jurisprudence.designPattern.model.dao.implementations.ImJurisprudence;
import mg.jurisprudence.designPattern.model.dao.interfaces.InJurisprudence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MSAccessDaoFactory implements DaoFactory {
	private String databaseUrl;
	
	public MSAccessDaoFactory(String databaseUrl) {
		setDatabaseUrl(databaseUrl);
	}
	
	public static MSAccessDaoFactory getInstance() {
		MSAccessDaoFactory self = null;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String databaseLocation = "/home/rinelfi/jurisprudence.accdb";/*MSAccessDaoFactory.class.getResource("/mg/jurisprudence/others/embedDatabase/jurisprudence.accdb").toString();*/
			self = new MSAccessDaoFactory(databaseLocation);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return self;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:ucanaccess://" + getDatabaseUrl() + ";memory=false");
	}
	
	@Override
	public InJurisprudence getJurisprudenceDao() {
		return new ImJurisprudence(this);
	}
	
	private final String getDatabaseUrl() {
		return databaseUrl;
	}
	
	private void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}
}
