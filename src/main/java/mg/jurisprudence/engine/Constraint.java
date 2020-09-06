package mg.jurisprudence.engine;

import java.time.LocalDate;

public abstract class Constraint {
	public static final int DATE_CONSTRAINT_BEFORE = 0, DATE_CONSTRAINT_EQUAL = 1, DATE_CONSTRAINT_AFTER = 2, DATE_CONSTRAINT_BETWEEN = 3;
	protected String numero, nomParties, commentaire, texte;
	protected LocalDate dateDebut, dateFin;
	protected boolean treatDate, combined;
	protected int dateFlag;
	
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
	
	public abstract String getCompiledConstraint();
	
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
