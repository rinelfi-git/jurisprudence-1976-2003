package mg.jurisprudence.designPattern.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
}
