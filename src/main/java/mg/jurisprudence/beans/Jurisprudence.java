package mg.jurisprudence.beans;

import java.util.Date;

import javafx.beans.property.*;

public class Jurisprudence {
	private int id;
	private String numero;
	private String nomPartie;
	private Date dateDecision;
	private String commentaire;
	private String texte;
	
	public Jurisprudence() {
		setId(0);
		setNumero("");
		setNomPartie("");
		setDateDecision(new Date());
		setCommentaire("");
		setTexte("");
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNumero() {
		return numero != null ? numero : "";
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getNomPartie() {
		return nomPartie != null ? nomPartie : "";
	}
	
	public void setNomPartie(String nomPartie) {
		this.nomPartie = nomPartie;
	}
	
	public Date getDateDecision() {
		return dateDecision;
	}
	
	public void setDateDecision(Date dateDecision) {
		this.dateDecision = dateDecision;
	}
	
	public String getCommentaire() {
		return commentaire != null ? commentaire : "";
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
	
	public Integer tableViewId() {
		IntegerProperty _id = new SimpleIntegerProperty(this, "id");
		_id.set(getId());
		return _id.get();
	}
	
	public ObjectProperty<Date> tableViewDate() {
		ObjectProperty<Date> _date_decision = new SimpleObjectProperty<>();
		_date_decision.set(getDateDecision());
		return _date_decision;
	}
	
	public String tableViewNumero() {
		StringProperty _numero = new SimpleStringProperty(this, "numero");
		_numero.set(getNumero());
		return _numero.get();
	}
	
	public String tableViewNomPartie() {
		StringProperty _nomPartie = new SimpleStringProperty(this, "nom_partie");
		_nomPartie.set(getNumero());
		return _nomPartie.get();
	}
	
	public String tableViewCommetaire() {
		StringProperty _commentaire = new SimpleStringProperty(this, "commentaire");
		_commentaire.set(getNumero());
		return _commentaire.get();
	}
	
	public String toString() {
		return "[" + getId() + "] => {\n" +
			       "\t[numero]        => \"" + getNumero() + "\"\n" +
			       "\t[parties]       => \"" + getNomPartie() + "\"\n" +
			       "\t[date_decision] => \"" + getDateDecision() + "\"\n" +
			       "\t[commentaire]   => \"" + getCommentaire() + "\"\n" +
			       "}\n";
	}
}
