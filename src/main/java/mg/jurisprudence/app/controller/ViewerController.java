package mg.jurisprudence.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import mg.jurisprudence.beans.Jurisprudence;

import java.net.URL;
import java.text.DateFormat;
import java.util.ResourceBundle;

public class ViewerController implements Initializable {
	@FXML
	private TextField numeroArretView;
	
	@FXML
	private TextField dateArretView;
	
	@FXML
	private TextField nomPartieView;
	
	@FXML
	private TextField commentaireView;
	
	@FXML
	private TextArea texteView;
	
	
	// Methods field
	public void initElements(Jurisprudence jurisprudence) {
		numeroArretView.setText(jurisprudence.getNumero());
		dateArretView.setText(DateFormat.getDateInstance().format(jurisprudence.getDateDecision()));
		nomPartieView.setText(jurisprudence.getNomPartie().replaceAll("c/", "CONTRE"));
		commentaireView.setText(jurisprudence.getCommentaire());
		texteView.setText(jurisprudence.getTexte());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		texteView.setWrapText(true);
	}
	
	@FXML
	void copyToClipboard(ActionEvent event) {
		String formattedCopyFields = new String(texteView.getText());
		ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(formattedCopyFields);
		Clipboard clipboard = Clipboard.getSystemClipboard();
		clipboard.setContent(clipboardContent);
	}
	
	public TextField getNumeroArretView() {
		return numeroArretView;
	}
	
	public void setNumeroArretView(TextField numeroArretView) {
		this.numeroArretView = numeroArretView;
	}
	
	public TextField getDateArretView() {
		return dateArretView;
	}
	
	public void setDateArretView(TextField dateArretView) {
		this.dateArretView = dateArretView;
	}
	
	public TextField getNomPartieView() {
		return nomPartieView;
	}
	
	public void setNomPartieView(TextField nomPartieView) {
		this.nomPartieView = nomPartieView;
	}
	
	public TextField getCommentaireView() {
		return commentaireView;
	}
	
	public void setCommentaireView(TextField commentaireView) {
		this.commentaireView = commentaireView;
	}
	
	public TextArea getTexteView() {
		return texteView;
	}
	
	public void setTexteView(TextArea texteView) {
		this.texteView = texteView;
	}
}
