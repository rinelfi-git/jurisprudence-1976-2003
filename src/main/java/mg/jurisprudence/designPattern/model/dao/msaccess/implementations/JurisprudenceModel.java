package mg.jurisprudence.designPattern.model.dao.msaccess.implementations;

import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.designPattern.model.dao.msaccess.MSAccess;
import mg.jurisprudence.designPattern.model.interfaces.JurisprudenceDao;
import mg.jurisprudence.engine.Constraint;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class JurisprudenceModel implements JurisprudenceDao {
	MSAccess msAccess;
	
	public JurisprudenceModel(MSAccess msAccess) {
		this.msAccess = msAccess;
	}
	
	@Override
	public ArrayList<Jurisprudence> selectWithoutDate(Constraint constraint) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<Jurisprudence> jurisprudences = new ArrayList<>();
		String query = new String("SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE ");
		query += constraint.getCompiledConstraint(msAccess);
		try {
			connection = msAccess.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + constraint.getNumero() + "%");
			preparedStatement.setString(2, "%" + constraint.getNumero().toLowerCase() + "%");
			preparedStatement.setString(3, "%" + constraint.getNumero().toUpperCase() + "%");
			preparedStatement.setString(4, "%" + constraint.getNomParties() + "%");
			preparedStatement.setString(5, "%" + constraint.getNomParties().toLowerCase() + "%");
			preparedStatement.setString(6, "%" + constraint.getNomParties().toUpperCase() + "%");
			preparedStatement.setString(7, "%" + constraint.getCommentaire() + "%");
			preparedStatement.setString(8, "%" + constraint.getCommentaire().toLowerCase() + "%");
			preparedStatement.setString(9, "%" + constraint.getCommentaire().toUpperCase() + "%");
			preparedStatement.setString(10, "%" + constraint.getTexte() + "%");
			preparedStatement.setString(11, "%" + constraint.getTexte().toLowerCase() + "%");
			preparedStatement.setString(12, "%" + constraint.getTexte().toUpperCase() + "%");
			System.out.println(preparedStatement.toString());
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
		String query = new String("SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE ");
		constraint.setCombined(true);
		query += constraint.getCompiledConstraint(msAccess);
		Instant instant = Instant.from(constraint.getDateDebut().atStartOfDay(ZoneId.systemDefault()));
		Date date = Date.from(instant);
		try {
			connection = msAccess.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + constraint.getNumero() + "%");
			preparedStatement.setString(2, "%" + constraint.getNumero().toLowerCase() + "%");
			preparedStatement.setString(3, "%" + constraint.getNumero().toUpperCase() + "%");
			preparedStatement.setString(4, "%" + constraint.getNomParties() + "%");
			preparedStatement.setString(5, "%" + constraint.getNomParties().toLowerCase() + "%");
			preparedStatement.setString(6, "%" + constraint.getNomParties().toUpperCase() + "%");
			preparedStatement.setString(7, "%" + constraint.getCommentaire() + "%");
			preparedStatement.setString(8, "%" + constraint.getCommentaire().toLowerCase() + "%");
			preparedStatement.setString(9, "%" + constraint.getCommentaire().toUpperCase() + "%");
			preparedStatement.setString(10, "%" + constraint.getTexte() + "%");
			preparedStatement.setString(11, "%" + constraint.getTexte().toLowerCase() + "%");
			preparedStatement.setString(12, "%" + constraint.getTexte().toUpperCase() + "%");
			preparedStatement.setTimestamp(13, new Timestamp(date.getTime()));
			if (constraint.getDateFlag() == Constraint.DATE_CONSTRAINT_BETWEEN) {
				instant = Instant.from(constraint.getDateFin().atStartOfDay(ZoneId.systemDefault()));
				date = Date.from(instant);
				preparedStatement.setTimestamp(14, new Timestamp(date.getTime()));
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
		String query = new String("SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE ");
		query += constraint.getCompiledConstraint(msAccess);
		Instant instant = Instant.from(constraint.getDateDebut().atStartOfDay(ZoneId.systemDefault()));
		Date date = Date.from(instant);
		try {
			connection = msAccess.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setTimestamp(1, new Timestamp(date.getTime()));
			if (constraint.getDateFlag() == Constraint.DATE_CONSTRAINT_BETWEEN) {
				instant = Instant.from(constraint.getDateFin().atStartOfDay(ZoneId.systemDefault()));
				date = Date.from(instant);
				preparedStatement.setTimestamp(2, new Timestamp(date.getTime()));
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
		String query = new String("SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE TRUE");
		try {
			connection = msAccess.getConnection();
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
		String query = new String("SELECT * FROM arrete WHERE id=?");
		Jurisprudence jurisprudence = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = msAccess.getConnection();
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


//	@Override
//	public ArrayList<Jurisprudence> select(ArrayList<Constraint> constraints) {
//		String query = new String("SELECT id, numero, nom_partie, date_decision, commentaire FROM arrete WHERE");
//		if (constraints.size() > 0) {
//			for (Constraint constraint : constraints) {
//				query += " (" + constraint.getCompiledMsAccessConstraint() + ") AND";
//			}
//			query = query.substring(0, query.length() - 4);
//		} else query += " TRUE";
//		ArrayList<Jurisprudence> jurisprudences = new ArrayList<>();
//		Connection connection = null;
//		Statement statement = null;
//		ResultSet resultSet = null;
//		try {
//			connection = factory.getConnection();
//			statement = connection.createStatement();
//			resultSet = statement.executeQuery(query);
//			while (resultSet.next()) {
//				Jurisprudence jurisprudence = new Jurisprudence();
//				jurisprudence.setCommentaire(resultSet.getString("commentaire"));
//				jurisprudence.setDateDecision(resultSet.getDate("date_decision"));
//				jurisprudence.setId(resultSet.getInt("id"));
//				jurisprudence.setNomPartie(resultSet.getString("nom_partie").replaceAll("c/", "[CONTRE]"));
//				jurisprudence.setNumero(resultSet.getString("numero"));
//				jurisprudences.add(jurisprudence);
//			}
//		} catch (SQLException throwables) {
//			throwables.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (connection != null) connection.close();
//				if (statement != null) statement.close();
//				if (resultSet != null) resultSet.close();
//			} catch (SQLException throwables) {
//				throwables.printStackTrace();
//			}
//		}
//		return jurisprudences;
//	}
//
//	@Override
//	public Jurisprudence select(int id) {
//		String query = new String("SELECT * FROM arrete WHERE id=" + id);
//		Jurisprudence jurisprudence = null;
//		Connection connection = null;
//		Statement statement = null;
//		ResultSet resultSet = null;
//		try {
//			connection = factory.getConnection();
//			statement = connection.createStatement();
//			resultSet = statement.executeQuery(query);
//			if (resultSet.next()) {
//				jurisprudence = new Jurisprudence();
//				jurisprudence.setCommentaire(resultSet.getString("commentaire"));
//				jurisprudence.setDateDecision(resultSet.getDate("date_decision"));
//				jurisprudence.setId(resultSet.getInt("id"));
//				jurisprudence.setNomPartie(resultSet.getString("nom_partie"));
//				jurisprudence.setNumero(resultSet.getString("numero"));
//				jurisprudence.setTexte(resultSet.getString("texte"));
//			}
//		} catch (SQLException throwables) {
//			throwables.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (connection != null) connection.close();
//				if (statement != null) statement.close();
//				if (resultSet != null) resultSet.close();
//			} catch (SQLException throwables) {
//				throwables.printStackTrace();
//			}
//		}
//		return jurisprudence;
//	}
}
