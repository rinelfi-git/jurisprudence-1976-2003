package mg.jurisprudence.designPattern.controller;

import javafx.application.Platform;
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
import mg.jurisprudence.designPattern.model.dao.msaccess.MSAccess;
import mg.jurisprudence.engine.Constraint;
import mg.jurisprudence.designPattern.model.dao.DaoFactory;
import mg.jurisprudence.designPattern.model.dao.postgresql.PostgreSQL;
import mg.jurisprudence.designPattern.model.interfaces.JurisprudenceDao;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class StarterController implements Initializable {
	private DaoFactory daoFactory;
	private JurisprudenceDao jurisprudenceDao;
	private String[] selectionDateElements;
	ArrayList<Constraint> constraints;
	
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
	private TextArea query;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.selectionDateElements = new String[]{"", "Du", "Avant le", "Après le", "Entre le"};
		constraints = new ArrayList<>();
		initSelectionDate();
		showRecord.setDisable(true);
		dateFin.setDisable(true);
		dateDebut.setDisable(true);
		initDates();
		Platform.runLater(() -> {
			daoFactory = MSAccess.getInstance();
			jurisprudenceDao = daoFactory.getJurisprudenceDao();
		});
	}
	
	private void initSelectionDate() {
		ArrayList<String> dynamicSelectionDateElements = new ArrayList<>();
		for (String selectionDateElement : selectionDateElements) {
			dynamicSelectionDateElements.add(selectionDateElement);
		}
		// convert arraylist to observablelist
		ObservableList<String> observableSelectionDateElements = FXCollections.observableArrayList(dynamicSelectionDateElements);
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
	void selectRecord(ActionEvent event) {
		int index = tableView.getSelectionModel().getSelectedItem().getId();
		try {
			FXMLLoader loader = new FXMLLoader();
			URL location = getClass().getResource("/mg/jurisprudence/designPattern/view/ViewerView.fxml");
			loader.load(location.openStream());
			Scene scene = new Scene(loader.getRoot());
			Stage stage = new Stage();
			final Jurisprudence jurisprudences[] = new Jurisprudence[]{new Jurisprudence()};
			// Execute the request on parallel
			Platform.runLater(() -> {
				jurisprudences[0] = jurisprudenceDao.select(index);
				ViewerController controller = loader.getController();
				loader.setController(controller);
				controller.initElements(jurisprudences[0]);
				stage.setTitle("Arrêté N°" + jurisprudences[0].getNumero());
				stage.setScene(scene);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.show();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void applyFilters(ActionEvent event) {
		if (!numeroArret.getText().equals("")) constraints.add(new Constraint("numero", numeroArret.getText()));
		if (!nomPartie.getText().equals("")) constraints.add(new Constraint("nom_partie", nomPartie.getText()));
		if (!commentaire.getText().equals("")) constraints.add(new Constraint("commentaire", commentaire.getText()));
		if (!texte.getText().equals("")) constraints.add(new Constraint("texte", texte.getText()));
		if (selectionDate.getSelectionModel().getSelectedIndex() != 0) {
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
		ArrayList<Jurisprudence> jurisprudences = this.jurisprudenceDao.select(constraints);
		tableView.setItems(FXCollections.observableArrayList(jurisprudences));
		String queryString = "INSERT INTO arrete(id, numero, date_decision, nom_partie, texte, commentaire) VALUES\n";
		for (Jurisprudence jurisprudence : jurisprudences) {
			queryString += "(" + jurisprudence.getId() + "," +
				               "'" + (jurisprudence.getNumero() != null ? jurisprudence.getNumero().replace("'", "''") : "") + "'," +
				               "'" + (jurisprudence.getDateDecision() != null ? jurisprudence.getDateDecision().toString() : "") + "'," +
				               "'" + (jurisprudence.getNomPartie() != null ? jurisprudence.getNomPartie().replace("'", "''") : "") + "'," +
				               "'" + (jurisprudence.getTexte() != null ? jurisprudence.getTexte().replace("'", "''") : "") + "'," +
				               "'" + (jurisprudence.getCommentaire() != null ? jurisprudence.getCommentaire().replace("'", "''") : "") + "'" +
				               "),\n";
		}
		query.setText(queryString);
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
		} else if (selectionDate.getSelectionModel().getSelectedIndex() == 0) {
			dateFin.setDisable(true);
			dateDebut.setDisable(true);
		} else {
			dateFin.setDisable(true);
			dateDebut.setDisable(false);
		}
	}
}
