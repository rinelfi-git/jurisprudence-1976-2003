package mg.jurisprudence.app.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
	private JurisprudenceDao jurisprudenceDao;
	private String[] selectionDateElements;
	private Stage stage;
	
	@FXML
	private Button showRecord;
	@FXML
	private TextField numeroArret, nomPartie, commentaire, texte;
	@FXML
	private ComboBox<String> selectionDate;
	@FXML
	private DatePicker dateDebut, dateFin;
	@FXML
	private TableView<Jurisprudence> tableView;
	@FXML
	private TableColumn<Jurisprudence, Integer> jurId;
	@FXML
	private TableColumn<Jurisprudence, Date> jurDate;
	@FXML
	private TableColumn<Jurisprudence, String> jurNumero, jurNomPartie, jurCommentaire;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialize DAO Objects
		DaoFactory daoFactory = MSAccess.getInstance();
		jurisprudenceDao = daoFactory.getJurisprudenceDao();
		this.selectionDateElements = new String[]{"", "Du", "Avant le", "Après le", "Entre le"};
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
		ObservableList<String> observableSelectionDateElements = FXCollections.observableArrayList(dynamicSelectionDateElements);
		selectionDate.setItems(observableSelectionDateElements);
		selectionDate.getSelectionModel().select(0);
	}
	
	private void initTable() {
		jurId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());
		jurDate.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getDateDecision()));
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
							setText(format.format(item));
					}
				}
			};
			return cell;
		});
		jurNumero.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNumero()));
		jurNomPartie.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNomPartie()));
		jurCommentaire.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCommentaire()));
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
			final FXMLLoader loader = new FXMLLoader();
			loader.setLocation(StarterController.class.getResource("/mg/jurisprudence/app/view/ViewerView.fxml"));
			Scene scene = new Scene(loader.load());
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
				stage.setResizable(false);
				stage.show();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	void applyFilters(ActionEvent event) {
		final Scene scene = getStage().getScene();
		final Cursor oldCursor = scene.getCursor();
		Cursor waitingCursor = Cursor.WAIT;
		scene.setCursor(waitingCursor);
		final Service<Void> dataLoadService = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
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
						ArrayList<Jurisprudence>[] jurisprudences = new ArrayList[]{null};
						boolean textConstraintExists = !constraint.getNumero().equals("") || !constraint.getNomParties().equals("") || !constraint.getCommentaire().equals("") || !constraint.getTexte().equals("");
						if (constraint.isTreatDate() && textConstraintExists) jurisprudences[0] = jurisprudenceDao.selectWithDate(constraint);
						else if (constraint.isTreatDate() && !textConstraintExists) jurisprudences[0] = jurisprudenceDao.selectWithDateOnly(constraint);
						else if (!constraint.isTreatDate() && textConstraintExists) jurisprudences[0] = jurisprudenceDao.selectWithoutDate(constraint);
						else if (!constraint.isTreatDate() && !textConstraintExists) jurisprudences[0] = jurisprudenceDao.select();
						tableView.setItems(FXCollections.observableArrayList(jurisprudences[0]));
						return null;
					}
				};
			}
		};
		dataLoadService.stateProperty().addListener((observable, oldValue, newValue) -> {
			switch (newValue) {
				case FAILED:
				case CANCELLED:
				case SUCCEEDED:
					scene.setCursor(oldCursor);
					break;
			}
		});
		dataLoadService.start();
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
	
	public Stage getStage() {
		return stage;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
