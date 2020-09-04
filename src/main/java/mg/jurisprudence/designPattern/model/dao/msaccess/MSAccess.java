package mg.jurisprudence.designPattern.model.dao.msaccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.designPattern.model.dao.msaccess.implementations.JurisprudenceModel;
import mg.jurisprudence.designPattern.model.interfaces.JurisprudenceDao;

public class MSAccess implements DaoFactory {
	private String databaseUrl;
	
	public MSAccess(String databaseUrl) {
		setDatabaseUrl(databaseUrl);
	}
	
	public static MSAccess getInstance() {
		MSAccess self = null;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			File databaseFile = new File("jurisprudence.accdb");
			if (!databaseFile.exists())
				Files.copy(MSAccess.class.getResourceAsStream("/database/jurisprudence.accdb"), databaseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			self = new MSAccess(databaseFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return self;
	}
	
	private final String getDatabaseUrl() {
		return databaseUrl;
	}
	
	private void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:ucanaccess://" + getDatabaseUrl() + ";memory=false");
	}
	
	@Override
	public JurisprudenceDao getJurisprudenceDao() {
		return new JurisprudenceModel(this);
	}
}
