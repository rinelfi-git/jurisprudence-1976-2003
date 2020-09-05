package mg.jurisprudence.engine;

import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.designPattern.model.dao.msaccess.MSAccess;
import mg.jurisprudence.designPattern.model.dao.postgresql.PostgreSQL;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Constraint {
	public static final int DATE_CONSTRAINT_BEFORE = 0, DATE_CONSTRAINT_EQUAL = 1, DATE_CONSTRAINT_AFTER = 2, DATE_CONSTRAINT_BETWEEN = 3;
	private String numero, nomParties, commentaire, texte;
	private LocalDate dateDebut, dateFin;
	private boolean treatDate, combined;
	private int dateFlag;
	
	public Constraint() {
		setNumero("");
		setNomParties("");
		setCommentaire("");
		setTexte("");
		setDateDebut(null);
		setDateFin(null);
		setTreatDate(false);
		setDateFlag(0);
		setCombined(false);
	}
	
	public String getCompiledConstraint(DaoFactory daoFactory) {
		String output = "";
		if (daoFactory instanceof MSAccess) {
				output += "(numero LIKE ? OR numero LIKE ? OR numero LIKE ?) AND ";
				output += "(nom_partie LIKE ? OR nom_partie LIKE ? OR nom_partie LIKE ?) AND ";
				output += "(commentaire LIKE ? OR commentaire LIKE ? OR commentaire LIKE ?) AND ";
				output += "(texte LIKE ? OR texte LIKE ? OR texte LIKE ?)";
			if(isCombined()) output += " AND ";
			if (isTreatDate()) {
				switch (getDateFlag()) {
					case DATE_CONSTRAINT_BEFORE:
						output += "date_decision < ? ";
						break;
					case DATE_CONSTRAINT_EQUAL:
						output += "date_decision = ? ";
						break;
					case DATE_CONSTRAINT_AFTER:
						output += "date_decision > ? ";
						break;
					case DATE_CONSTRAINT_BETWEEN:
						output += "date_decision < ? AND date_decision > ? ";
						break;
				}
			}
		}
		else if (daoFactory instanceof PostgreSQL) {
			if (!"".equals(getNumero()))
				output += "numero ILIKE '%" + getNumero() + "%' OR numero ILIKE '%" + getNumero().toUpperCase() + "%' OR numero ILIKE '%" + getNumero().toLowerCase() + "%'";
				output += "nom_partie ILIKE '%" + getNomParties() + "%' OR nom_partie ILIKE '%" + getNomParties().toUpperCase() + "%' OR nom_partie ILIKE '%" + getNomParties().toLowerCase() + "%'";
				output += "commentaire ILIKE '%" + getCommentaire() + "%' OR commentaire ILIKE '%" + getCommentaire().toUpperCase() + "%' OR commentaire ILIKE '%" + getCommentaire().toLowerCase() + "%'";
			if (!"".equals(getTexte()))
				output += "texte ILIKE '%" + getTexte() + "%' OR texte ILIKE '%" + getTexte().toUpperCase() + "%' OR texte ILIKE '%" + getTexte().toLowerCase() + "%'";
			if (isTreatDate()) {
				Instant instant = null;
				Date date = null;
				String dateFormat = "yyyy-MM-dd";
				switch (getDateFlag()) {
					case DATE_CONSTRAINT_BEFORE:
						instant = Instant.from(getDateDebut().atStartOfDay(ZoneId.systemDefault()));
						date = Date.from(instant);
						output += "date_decision<'" + new SimpleDateFormat(dateFormat).format(date) + "'";
						break;
					case DATE_CONSTRAINT_EQUAL:
						instant = Instant.from(getDateDebut().atStartOfDay(ZoneId.systemDefault()));
						date = Date.from(instant);
						output += "date_decision='" + new SimpleDateFormat(dateFormat).format(date) + "'";
						break;
					case DATE_CONSTRAINT_AFTER:
						instant = Instant.from(getDateDebut().atStartOfDay(ZoneId.systemDefault()));
						date = Date.from(instant);
						output += "date_decision>'" + new SimpleDateFormat(dateFormat).format(date) + "'";
						break;
					case DATE_CONSTRAINT_BETWEEN:
						instant = Instant.from(getDateDebut().atStartOfDay(ZoneId.systemDefault()));
						date = Date.from(instant);
						output += "date_decision BETWEEN '" + new SimpleDateFormat(dateFormat).format(date) + "'";
						instant = Instant.from(getDateFin().atStartOfDay(ZoneId.systemDefault()));
						date = Date.from(instant);
						output += " AND '" + new SimpleDateFormat(dateFormat).format(date) + "'";
						break;
				}
			}
		}
		return output;
	}
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getNomParties() {
		return nomParties;
	}
	
	public void setNomParties(String nomParties) {
		this.nomParties = nomParties;
	}
	
	public LocalDate getDateDebut() {
		return dateDebut;
	}
	
	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}
	
	public LocalDate getDateFin() {
		return dateFin;
	}
	
	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}
	
	public boolean isTreatDate() {
		return treatDate;
	}
	
	public void setTreatDate(boolean treatDate) {
		this.treatDate = treatDate;
	}
	
	public int getDateFlag() {
		return dateFlag;
	}
	
	public void setDateFlag(int dateFlag) {
		this.dateFlag = dateFlag;
	}
	
	public String getCommentaire() {
		return commentaire;
	}
	
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
	public String getTexte() {
		return texte;
	}
	
	public void setTexte(String texte) {
		this.texte = texte;
	}
	
	public boolean isCombined() {
		return combined;
	}
	
	public void setCombined(boolean combined) {
		this.combined = combined;
	}
}
