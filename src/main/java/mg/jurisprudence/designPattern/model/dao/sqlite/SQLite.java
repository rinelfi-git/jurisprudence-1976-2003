package mg.jurisprudence.designPattern.model.dao.sqlite;

import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.designPattern.model.dao.msaccess.MSAccess;
import mg.jurisprudence.designPattern.model.dao.postgresql.PostgreSQL;
import mg.jurisprudence.designPattern.model.interfaces.JurisprudenceDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;

public class SQLite implements DaoFactory {
	String database;
	private SQLite(String database) {
		setDatabase(database);
		File databaseFile = new File(getDatabase());
		if (!databaseFile.exists()) {
			try {
				Files.copy(MSAccess.class.getResourceAsStream("/database/" + getDatabase()), databaseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static DaoFactory getInstance() {
		try {
			Class.forName("org.sqlite.JDBC");
			SQLite sqLite = new SQLite("jurisprudence.sqlite");
			return sqLite;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Connection getConnection() throws Exception {
		return DriverManager.getConnection("jdbc:sqlite:" + getDatabase());
	}
	
	@Override
	public JurisprudenceDao getJurisprudenceDao() {
		return null;
	}
	
	public final String getDatabase() {
		return database;
	}
	
	public void setDatabase(String database) {
		this.database = database;
	}
}
