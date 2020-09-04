package mg.jurisprudence.designPattern.model.dao.postgresql;

import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.designPattern.model.dao.postgresql.implementations.JurisprudenceModel;
import mg.jurisprudence.designPattern.model.interfaces.JurisprudenceDao;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSQL implements DaoFactory {
	private String host, username, password, database;
	
	private PostgreSQL(String host, String username, String password, String database) {
		setHost(host);
		setUsername(username);
		setPassword(password);
		setDatabase(database);
	}
	
	public static DaoFactory getInstance() {
		try {
			Class.forName("org.postgresql.Driver");
			DaoFactory factory = new PostgreSQL("localhost", "postgres", "postgres", "jurisprudence");
			return factory;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDatabase() {
		return database;
	}
	
	public void setDatabase(String database) {
		this.database = database;
	}
	
	@Override
	public Connection getConnection() throws Exception {
		return DriverManager.getConnection("jdbc:postgresql://" + getHost() + "/" + getDatabase(), getUsername(), getPassword());
	}
	
	@Override
	public JurisprudenceDao getJurisprudenceDao() {
		return new JurisprudenceModel(this);
	}
}
