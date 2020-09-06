package mg.jurisprudence.app.model.dao.msaccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mg.jurisprudence.app.model.dao.DaoFactory;
import mg.jurisprudence.app.model.dao.msaccess.implementations.JurisprudenceModel;
import mg.jurisprudence.app.model.interfaces.JurisprudenceDao;

public class MSAccess implements DaoFactory {
	private String database;
	
	public MSAccess(String database) {
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
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			MSAccess msAccess = new MSAccess("jurisprudence.accdb");
			return msAccess;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private final String getDatabase() {
		return database;
	}
	
	private void setDatabase(String database) {
		this.database = database;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:ucanaccess://" + getDatabase() + ";memory=false");
	}
	
	@Override
	public JurisprudenceDao getJurisprudenceDao() {
		return new JurisprudenceModel(this);
	}
}
