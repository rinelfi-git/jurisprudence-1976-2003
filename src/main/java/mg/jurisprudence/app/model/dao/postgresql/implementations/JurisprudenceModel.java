package mg.jurisprudence.app.model.dao.postgresql.implementations;

import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.app.model.dao.DaoFactory;
import mg.jurisprudence.app.model.dao.postgresql.PostgreSQL;
import mg.jurisprudence.engine.Constraint;
import mg.jurisprudence.app.model.interfaces.JurisprudenceDao;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class JurisprudenceModel implements JurisprudenceDao {
	PostgreSQL postgreSQL;
	
	public JurisprudenceModel(DaoFactory daoFactory) {
		this.postgreSQL = (PostgreSQL) daoFactory;
	}
	
	@Override
	public ArrayList<Jurisprudence> selectWithoutDate(Constraint constraint) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Jurisprudence> jurisprudences = new ArrayList<>();
		String query = "SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE ";
		query += constraint.getCompiledConstraint();
		try {
			connection = postgreSQL.getConnection();
			preparedStatement = connection.prepareStatement(query);
			int index = 0;
			if (!"".equals(constraint.getNumero())) preparedStatement.setString(++index, "%" + constraint.getNumero() + "%");
			if (!"".equals(constraint.getNomParties())) preparedStatement.setString(++index, "%" + constraint.getNomParties() + "%");
			if (!"".equals(constraint.getCommentaire())) preparedStatement.setString(++index, "%" + constraint.getCommentaire() + "%");
			if (!"".equals(constraint.getTexte())) preparedStatement.setString(++index, "%" + constraint.getTexte() + "%");
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Jurisprudence jurisprudence = new Jurisprudence();
				jurisprudence.setCommentaire(resultSet.getString("commentaire"));
				jurisprudence.setDateDecision(resultSet.getDate("date_decision"));
				jurisprudence.setId(resultSet.getInt("id"));
				jurisprudence.setNomPartie(resultSet.getString("nom_partie").replaceAll("c/", "[CONTRE]"));
				jurisprudence.setNumero(resultSet.getString("numero"));
				jurisprudences.add(jurisprudence);
			}
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
		return jurisprudences;
	}
	
	@Override
	public ArrayList<Jurisprudence> selectWithDate(Constraint constraint) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Jurisprudence> jurisprudences = new ArrayList<>();
		String query = "SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE ";
		constraint.setCombined(true);
		query += constraint.getCompiledConstraint();
		Instant instant = Instant.from(constraint.getDateDebut().atStartOfDay(ZoneId.systemDefault()));
		java.util.Date date = java.util.Date.from(instant);
		try {
			connection = postgreSQL.getConnection();
			preparedStatement = connection.prepareStatement(query);
			int index = 0;
			if (!"".equals(constraint.getNumero())) preparedStatement.setString(++index, "%" + constraint.getNumero() + "%");
			if (!"".equals(constraint.getNomParties())) preparedStatement.setString(++index, "%" + constraint.getNomParties() + "%");
			if (!"".equals(constraint.getCommentaire())) preparedStatement.setString(++index, "%" + constraint.getCommentaire() + "%");
			if (!"".equals(constraint.getTexte())) preparedStatement.setString(++index, "%" + constraint.getTexte() + "%");
			preparedStatement.setTimestamp(++index, new Timestamp(date.getTime()));
			if (constraint.getDateFlag() == Constraint.DATE_CONSTRAINT_BETWEEN) {
				instant = Instant.from(constraint.getDateFin().atStartOfDay(ZoneId.systemDefault()));
				date = Date.from(instant);
				preparedStatement.setTimestamp(++index, new Timestamp(date.getTime()));
			}
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Jurisprudence jurisprudence = new Jurisprudence();
				jurisprudence.setCommentaire(resultSet.getString("commentaire"));
				jurisprudence.setDateDecision(resultSet.getDate("date_decision"));
				jurisprudence.setId(resultSet.getInt("id"));
				jurisprudence.setNomPartie(resultSet.getString("nom_partie").replaceAll("c/", "[CONTRE]"));
				jurisprudence.setNumero(resultSet.getString("numero"));
				jurisprudences.add(jurisprudence);
			}
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
		return jurisprudences;
	}
	
	@Override
	public ArrayList<Jurisprudence> selectWithDateOnly(Constraint constraint) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Jurisprudence> jurisprudences = new ArrayList<>();
		String query = "SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE ";
		query += constraint.getCompiledConstraint();
		Instant instant = Instant.from(constraint.getDateDebut().atStartOfDay(ZoneId.systemDefault()));
		Date date = Date.from(instant);
		try {
			connection = postgreSQL.getConnection();
			preparedStatement = connection.prepareStatement(query);
			int index = 0;
			preparedStatement.setTimestamp(++index, new Timestamp(date.getTime()));
			if (constraint.getDateFlag() == Constraint.DATE_CONSTRAINT_BETWEEN) {
				instant = Instant.from(constraint.getDateFin().atStartOfDay(ZoneId.systemDefault()));
				date = Date.from(instant);
				preparedStatement.setTimestamp(++index, new Timestamp(date.getTime()));
			}
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Jurisprudence jurisprudence = new Jurisprudence();
				jurisprudence.setCommentaire(resultSet.getString("commentaire"));
				jurisprudence.setDateDecision(resultSet.getDate("date_decision"));
				jurisprudence.setId(resultSet.getInt("id"));
				jurisprudence.setNomPartie(resultSet.getString("nom_partie").replaceAll("c/", "[CONTRE]"));
				jurisprudence.setNumero(resultSet.getString("numero"));
				jurisprudences.add(jurisprudence);
			}
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
		return jurisprudences;
	}
	
	@Override
	public ArrayList<Jurisprudence> select() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<Jurisprudence> jurisprudences = new ArrayList<>();
		String query = "SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete";
		try {
			connection = postgreSQL.getConnection();
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
		String query = "SELECT * FROM arrete WHERE id=?";
		Jurisprudence jurisprudence = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = postgreSQL.getConnection();
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
