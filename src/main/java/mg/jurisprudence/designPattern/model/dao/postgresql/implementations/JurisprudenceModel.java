package mg.jurisprudence.designPattern.model.dao.postgresql.implementations;

import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.engine.Constraint;
import mg.jurisprudence.designPattern.model.interfaces.JurisprudenceDao;

import java.sql.*;
import java.util.ArrayList;

public class JurisprudenceModel implements JurisprudenceDao {
	DaoFactory daoFactory;
	
	public JurisprudenceModel(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public ArrayList<Jurisprudence> select(ArrayList<Constraint> constraints) {
		String query = new String("SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE");
		if (constraints.size() > 0) {
			for (Constraint constraint : constraints) {
				query += " (" + constraint.getCompiledPostgresqlConstraint() + ") AND";
			}
			query = query.substring(0, query.length() - 4);
		} else query += " TRUE";
		ArrayList<Jurisprudence> jurisprudences = new ArrayList<>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				Jurisprudence jurisprudence = new Jurisprudence();
				jurisprudence.setCommentaire(resultSet.getString("commentaire"));
				jurisprudence.setDateDecision(resultSet.getDate("date_decision"));
				jurisprudence.setId(resultSet.getInt("id"));
				jurisprudence.setNomPartie(resultSet.getString("nom_partie").replaceAll("c/", "[CONTRE]"));
				jurisprudence.setNumero(resultSet.getString("numero"));
				jurisprudences.add(jurisprudence);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) connection.close();
				if (statement != null) statement.close();
				if (resultSet != null) resultSet.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}
		return jurisprudences;
	}
	
	@Override
	public Jurisprudence select(int id) {
		String query = new String("SELECT * FROM arrete WHERE id=?");
		Jurisprudence jurisprudence = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = daoFactory.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				jurisprudence = new Jurisprudence();
				jurisprudence.setCommentaire(resultSet.getString("commentaire"));
				jurisprudence.setDateDecision(resultSet.getDate("date_decision"));
				jurisprudence.setId(resultSet.getInt("id"));
				jurisprudence.setNomPartie(resultSet.getString("nom_partie"));
				jurisprudence.setNumero(resultSet.getString("numero"));
				jurisprudence.setTexte(resultSet.getString("texte"));
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) connection.close();
				if (preparedStatement != null) preparedStatement.close();
				if (resultSet != null) resultSet.close();
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		}
		return jurisprudence;
	}
}
