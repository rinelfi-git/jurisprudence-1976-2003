package mg.jurisprudence.app.controller;

import javafx.application.Platform;
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
import mg.jurisprudence.app.model.dao.DaoFactory;
import mg.jurisprudence.app.model.dao.MSAccess;
import mg.jurisprudence.app.model.interfaces.JurisprudenceDao;
import mg.jurisprudence.beans.Jurisprudence;
import mg.jurisprudence.engine.Constraint;
import mg.jurisprudence.engine.MSAccessConstraint;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
	private TableColumn<Jurisprudence, Date> jurDate;
	
	@FXML
	private TableColumn<Jurisprudence, String> jurNumero;
	
	@FXML
	private TableColumn<Jurisprudence, String> jurNomPartie;
	
	@FXML
	private TableColumn<Jurisprudence, String> jurCommentaire;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.selectionDateElements = new String[]{"", "Du", "Avant le", "Après le", "Entre le"};
		constraints = new ArrayList<>();
		initSelectionDate();
		showRecord.setDisable(true);
		dateFin.setDisable(true);
		dateDebut.setDisable(true);
		initDates();
		new Thread(() -> {
			Platform.runLater(() -> {
				daoFactory = MSAccess.getInstance();
				jurisprudenceDao = daoFactory.getJurisprudenceDao();
				try {
					daoFactory.getConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}).start();
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
		jurDate.setCellValueFactory(cell -> cell.getValue().tableViewDate());
		jurDate.setCellFactory(column -> {
			TableCell<Jurisprudence, Date> cell = new TableCell<Jurisprudence, Date>() {
				private SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
				
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						if (item != null)
							this.setText(format.format(item));
					}
				}
			};
			return cell;
		});
		jurNumero.setCellValueFactory(new PropertyValueFactory<Jurisprudence, String>("numero"));
		jurNomPartie.setCellValueFactory(new PropertyValueFactory<Jurisprudence, String>("nom_partie"));
		jurCommentaire.setCellValueFactory(new PropertyValueFactory<Jurisprudence, String>("commentaire"));
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
			URL location = getClass().getResource("/mg/jurisprudence/app/view/ViewerView.fxml");
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
		Constraint constraint = new MSAccessConstraint();
		if (!numeroArret.getText().equals("")) constraint.setNumero(numeroArret.getText());
		if (!nomPartie.getText().equals("")) constraint.setNomParties(nomPartie.getText());
		if (!commentaire.getText().equals("")) constraint.setCommentaire(commentaire.getText());
		if (!texte.getText().equals("")) constraint.setTexte(texte.getText());
		if (selectionDate.getSelectionModel().getSelectedIndex() != 0) {
			constraint.setTreatDate(true);
			switch (selectionDate.getSelectionModel().getSelectedIndex()) {
				case 1:
					constraint.setDateDebut(dateDebut.getValue());
					constraint.setDateFlag(Constraint.DATE_CONSTRAINT_EQUAL);
					break;
				case 2:
					constraint.setDateDebut(dateDebut.getValue());
					constraint.setDateFlag(Constraint.DATE_CONSTRAINT_BEFORE);
					break;
				case 3:
					constraint.setDateDebut(dateDebut.getValue());
					constraint.setDateFlag(Constraint.DATE_CONSTRAINT_AFTER);
					break;
				case 4:
					constraint.setDateDebut(dateDebut.getValue());
					constraint.setDateFin(dateFin.getValue());
					constraint.setDateFlag(Constraint.DATE_CONSTRAINT_BETWEEN);
					break;
			}
		}
		initTable();
		ArrayList<Jurisprudence> jurisprudences = null;
		boolean textConstraintExists = !constraint.getNumero().equals("") || !constraint.getNomParties().equals("") || !constraint.getCommentaire().equals("") || !constraint.getTexte().equals("");
		if (constraint.isTreatDate() && textConstraintExists) jurisprudences = this.jurisprudenceDao.selectWithDate(constraint);
		else if (constraint.isTreatDate() && !textConstraintExists) jurisprudences = this.jurisprudenceDao.selectWithDateOnly(constraint);
		else if (!constraint.isTreatDate() && textConstraintExists) jurisprudences = this.jurisprudenceDao.selectWithoutDate(constraint);
		else if (!constraint.isTreatDate() && !textConstraintExists) jurisprudences = this.jurisprudenceDao.select();
		tableView.setItems(FXCollections.observableArrayList(jurisprudences));
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
