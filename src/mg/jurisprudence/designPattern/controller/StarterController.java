package mg.jurisprudence.designPattern.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.designPattern.model.dao.Constraint;
import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.designPattern.model.dao.MSAccessDaoFactory;
import mg.jurisprudence.designPattern.model.dao.interfaces.InJurisprudence;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class StarterController implements Initializable {
	private DaoFactory daoFactory;
	private InJurisprudence inJurisprudence;
	private String[] selectionDateElements;
	ArrayList<Constraint> constraints;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.daoFactory = MSAccessDaoFactory.getInstance();
		this.inJurisprudence = daoFactory.getJurisprudenceDao();
		this.selectionDateElements = new String[]{"", "Du", "Avant le", "Après le", "Entre le"};
		constraints = new ArrayList<>();
		initSelectionDate();
		showRecord.setDisable(true);
		dateFin.setDisable(true);
		dateDebut.setDisable(true);
		initDates();
	}
	
	private void initSelectionDate() {
		ArrayList<String> dynamicSelectionDateElements = new ArrayList<>();
		for (String selectionDateElement : selectionDateElements) {
			dynamicSelectionDateElements.add(selectionDateElement);
		}
		// convert arraylist to observablelist
		ObservableList observableSelectionDateElements = FXCollections.observableArrayList(dynamicSelectionDateElements);
		selectionDate.setItems(observableSelectionDateElements);
		selectionDate.getSelectionModel().select(0);
	}
	
	private void initTable() {
		jurId.setCellValueFactory(new PropertyValueFactory<Jurisprudence, Integer>("id"));
		jurDate.setCellValueFactory(cell -> (new SimpleStringProperty(String.valueOf(cell.getValue().getDateDecision()))));
		jurNumero.setCellValueFactory(cell -> (new SimpleStringProperty(String.valueOf(cell.getValue().getNumero()))));
		jurNomPartie.setCellValueFactory(cell -> (new SimpleStringProperty(String.valueOf(cell.getValue().getNomPartie()))));
		jurCommentaire.setCellValueFactory(cell -> (new SimpleStringProperty(String.valueOf(cell.getValue().getCommentaire()))));
	}
	
	private void initDates() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate min = LocalDate.parse("1976-02-05", formatter);
		LocalDate max = LocalDate.parse("2003-12-01", formatter);
		//set defaults values
		dateDebut.setValue(min);
		dateFin.setValue(max);
		// define min and max selectables values
		dateDebut.setDayCellFactory(d -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				setDisable(item.isAfter(max) || item.isBefore(min));
			}
		});
		dateFin.setDayCellFactory(d -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				setDisable(item.isAfter(max) || item.isBefore(min));
			}
		});
	}
	
	@FXML
	private Button showRecord;
	
	@FXML
	private TextField numeroArret;
	
	@FXML
	private TextField nomPartie;
	
	@FXML
	private ComboBox<String> selectionDate;
	
	@FXML
	private DatePicker dateDebut;
	
	@FXML
	private DatePicker dateFin;
	
	@FXML
	private TextField commentaire;
	
	@FXML
	private TextField texte;
	
	@FXML
	private TableView<Jurisprudence> tableView;
	@FXML
	private TableColumn<Jurisprudence, Integer> jurId;
	
	@FXML
	private TableColumn<Jurisprudence, String> jurDate;
	
	@FXML
	private TableColumn<Jurisprudence, String> jurNumero;
	
	@FXML
	private TableColumn<Jurisprudence, String> jurNomPartie;
	
	@FXML
	private TableColumn<Jurisprudence, String> jurCommentaire;
	
	@FXML
	void afficher(ActionEvent event) {
		int index = tableView.getSelectionModel().getSelectedItem().getId();
		try {
			FXMLLoader loader = new FXMLLoader();
			URL location = getClass().getResource("/mg/jurisprudence/designPattern/view/ViewerView.fxml");
			loader.load(location.openStream());
			Jurisprudence jurisprudence = inJurisprudence.select(index);
			ViewerController controller = loader.getController();
			loader.setController(controller);
			controller.initElements(jurisprudence);
			Scene scene = new Scene(loader.getRoot());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Arrêté N°" + jurisprudence.getNumero());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void appliquerFiltre(ActionEvent event) {
		if (!numeroArret.getText().equals("")) constraints.add(new Constraint("numero", numeroArret.getText()));
		if (!nomPartie.getText().equals("")) constraints.add(new Constraint("nom_partie", nomPartie.getText()));
		if (!commentaire.getText().equals("")) constraints.add(new Constraint("commentaire", commentaire.getText()));
		if (!texte.getText().equals("")) constraints.add(new Constraint("nom_partie", texte.getText()));
		if(selectionDate.getSelectionModel().getSelectedIndex() != 0) {
			LocalDate debut = dateDebut.getValue();
			Instant instant = Instant.from(debut.atStartOfDay(ZoneId.systemDefault()));
			Date date = Date.from(instant);
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			switch (selectionDate.getSelectionModel().getSelectedIndex()) {
				case 1:
					constraints.add(new Constraint("date_decision=#" + dateFormat.format(date) + "#"));
					break;
				case 2:
					constraints.add(new Constraint("date_decision<#" + dateFormat.format(date) + "#"));
					break;
				case 3:
					constraints.add(new Constraint("date_decision>#" + dateFormat.format(date) + "#"));
					break;
				case 4:
					constraints.add(new Constraint("date_decision>#" + dateFormat.format(date) + "#"));
					debut = dateFin.getValue();
					instant = Instant.from(debut.atStartOfDay(ZoneId.systemDefault()));
					date = Date.from(instant);
					constraints.add(new Constraint("date_decision<#" + dateFormat.format(date) + "#"));
					break;
			}
		}
		initTable();
		tableView.setItems(FXCollections.observableArrayList(this.inJurisprudence.select(constraints)));
		constraints = new ArrayList<>();
		showRecord.setDisable(true);
	}
	
	@FXML
	void selectedTable(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (!tableView.getSelectionModel().isEmpty())
				showRecord.setDisable(false);
		}
	}
	
	@FXML
	void changeSelectionType(ActionEvent event) {
		if (selectionDate.getSelectionModel().getSelectedIndex() == 4) {
			dateFin.setDisable(false);
			dateDebut.setDisable(false);
		}
		else if(selectionDate.getSelectionModel().getSelectedIndex() == 0) {
			dateFin.setDisable(true);
			dateDebut.setDisable(true);
		} else {
			dateFin.setDisable(true);
			dateDebut.setDisable(false);
		}
	}
}