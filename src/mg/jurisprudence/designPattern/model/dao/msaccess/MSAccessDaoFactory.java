package mg.jurisprudence.designPattern.model.dao.msaccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.designPattern.model.dao.implementations.ImJurisprudence;
import mg.jurisprudence.designPattern.model.dao.interfaces.InJurisprudence;

public class MSAccessDaoFactory implements DaoFactory {
	private String databaseUrl;
	
	public MSAccessDaoFactory(String databaseUrl) {
		setDatabaseUrl(databaseUrl);
	}
	
	public static MSAccessDaoFactory getInstance() {
		MSAccessDaoFactory self = null;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			File databaseFile = new File("jurisprudence.accdb");
			if (!databaseFile.exists()) {
				System.out.println("Extraction of database");
				// export database from package to relative path
				Files.copy(MSAccessDaoFactory.class.getResourceAsStream("/mg/jurisprudence/designPattern/model/dao/msaccess/jurisprudence.accdb"), databaseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				if (databaseFile.isFile())
					System.out.println("Extraction successfull");
				else
					System.out.println("An error occured during extraction");
			}
			System.out.println(databaseFile.getAbsolutePath());
			self = new MSAccessDaoFactory(databaseFile.getAbsolutePath());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
