/*
 * @Datei           ReportController.java
 * @Autor           Muhammednur Şehebi
 * @Matrikelnummer  170503112
 * @Date            6/20/2020
 */

package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.handlers.DatabaseHandler;
import sample.handlers.FileHandler;
import sample.model.*;
import sample.model.InspectionResult;
import sample.model.Report;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ReportController {

    private static final String ERORRTEXTFILESTYLE = "-fx-border-color: red;";
    private static final String PDF = "PDF";
    private static final String EXCEL= "EXCEL";
    private Equipment equipment;
    private String reportNo;
    private LocalDate reportDate;
    private User operator;
    private User evaluator;
    private User conformer;
    private Customer customer;
    private User user;
    private ObservableList<Customer> customers;
    private ObservableList<User> operators;
    private ObservableList<User> evaluators;
    private ObservableList<User> conformers;


    @FXML
    private AnchorPane generalPane;

    @FXML
    private Pane equipmentPane;

    @FXML
    private Pane personnelPane;

    @FXML
    private Pane inspectionPane;

    @FXML
    private Pane customerAndReportPane;

    @FXML
    private TextField MPCarrierMediumField;

    @FXML
    private TextField poleDistanceField;

    @FXML
    private TextField equipmentField;

    @FXML
    private TextField UVLightIntensityField;

    @FXML
    private TextField magTechField;

    @FXML
    private TextField distanceOfLightField;

    @FXML
    private TextField examinationAreaField;

    @FXML
    private ComboBox<String> currentTypeComboBox;

    @FXML
    private TextField luxmeterField;

    @FXML
    private TextField demagnetizationField;

    @FXML
    private TextField testMediumField;

    @FXML
    private TextField heatTreatmentField;

    @FXML
    private TextField surfaceTemperatureField;

    @FXML
    private TextField gaussFieldStrengthField;

    @FXML
    private TextField equipmentSurfaceConditionField;

    @FXML
    private TextField liftingTestDateNumberField;

    @FXML
    private TextField identificationOfLightEquipField;

    @FXML
    private TextField standardDeviationsField;

    @FXML
    private CheckBox buttWeldCheckBox;

    @FXML
    private CheckBox filerWeldCheckBox;

    @FXML
    private TextField descriptionAndAttachmentsField;

    @FXML
    private DatePicker inspectionDatesDatePicker;

    @FXML
    private ComboBox<User> operatorComboBox;

    @FXML
    private TextField operatorLevelField;

    @FXML
    private ComboBox<User> evaluatorComboBox;

    @FXML
    private TextField evaluatorLevelField;

    @FXML
    private ComboBox<User> conformerComboBox;

    @FXML
    private TextField conformerLevelField;

    @FXML
    private DatePicker operatorDateDatePicker;

    @FXML
    private DatePicker evaluatorDateDatePicker;

    @FXML
    private DatePicker conformerDateDatePicker;

    @FXML
    private ComboBox<Customer> costumerComboBox;

    @FXML
    private ComboBox<ProjectName> projectNameComboBox;

    @FXML
    private TextField testPlaceField;

    @FXML
    private TextField inspectionStandardField;

    @FXML
    private TextField evaluationStandardField;

    @FXML
    private TextField inspectionProcedureField;

    @FXML
    private TextField drawingNoField;

    @FXML
    private TextField reportNoField;

    @FXML
    private ComboBox<Integer> inspectionScopeComboBox;

    @FXML
    private ComboBox<Integer> numberOfPagesComboBox;

    @FXML
    private ComboBox<JobOrderNo> jobOrderNoComboBox;

    @FXML
    private ComboBox<OfferNo> offerNoComboBox;

    @FXML
    private DatePicker reportDateDatePicker;

    @FXML
    private TableView<InspectionResult> inspectionResultTableView;

    @FXML
    private TableColumn<InspectionResult, Integer> serialNoColumn;

    @FXML
    private TableColumn<InspectionResult, String> weldPieceNoColumn;

    @FXML
    private TableColumn<InspectionResult, Double> testLengthColumn;

    @FXML
    private TableColumn<InspectionResult, String> weldingProcessColumn;

    @FXML
    private TableColumn<InspectionResult, Double> thicknessColumn;

    @FXML
    private TableColumn<InspectionResult, String> diameterColumn;

    @FXML
    private TableColumn<InspectionResult, String> defectTypeColumn;

    @FXML
    private TableColumn<InspectionResult, String> defectLocColumn;

    @FXML
    private TableColumn<InspectionResult, Boolean> resultColumn;

    @FXML
    private TextField weldPieceNoField;

    @FXML
    private TextField testLengthField;

    @FXML
    private TextField weldingProcessField;

    @FXML
    private TextField thicknessField;

    @FXML
    private TextField diameterField;

    @FXML
    private TextField defectTypeField;

    @FXML
    private TextField defectLocField;

    @FXML
    private ComboBox<String> resultComboBox; // RED or OK

    @FXML
    private Button pdfButton;

    @FXML
    private Button excdlButton;

    @FXML
    private ComboBox<SurfaceCondition> surfaceConditionComboBox;

    @FXML
    private ComboBox<StageOfExamination> stageOfExaminationComboBox;

    @FXML
    private ToggleButton inspectionResultButton;

    @FXML
    private ToggleButton customerButton;

    @FXML
    private ToggleButton equipmentButton;

    @FXML
    private ToggleButton personnelButton;

    @FXML
    void initialize() throws SQLException {
        // Init inspectionScopes
        ObservableList<Integer> inspectionScopes = FXCollections.observableArrayList();
        for(int i = Report.MINSCOPE; i <= Report.MAXSCOPE; i++){
            inspectionScopes.add(i);
        }
        inspectionScopeComboBox.setItems(inspectionScopes);
        inspectionScopeComboBox.setValue(Report.MAXSCOPE); // 100 is the default

        // Init SurfaceCondition
        surfaceConditionComboBox.setItems(DatabaseHandler.getAllSurfaceConditions());

        // Init StageOfExamination
        stageOfExaminationComboBox.setItems(DatabaseHandler.getAllStageOfExaminations());

        // Init numberOfPagesComboBox
        numberOfPagesComboBox.setItems(inspectionScopes);
        numberOfPagesComboBox.setValue(1); // 1 is the default

        // Init reportNo
        reportNoField.setText(reportNo);

        // Init reportDate
        reportDateDatePicker.setValue(reportDate);

        // Init currentTypeComboBox
        currentTypeComboBox.getItems().addAll(Report.AC, Report.DC);
        currentTypeComboBox.setValue(Report.AC);

        // Init resultComboBox
        resultComboBox.getItems().addAll(InspectionResult.OK, InspectionResult.RED);

        // Init InspectionResults Columns
        serialNoColumn.setCellValueFactory(new PropertyValueFactory<>("serialNo"));
        weldPieceNoColumn.setCellValueFactory(new PropertyValueFactory<>("weldPieceNo"));
        testLengthColumn.setCellValueFactory(new PropertyValueFactory<>("testLength"));
        weldingProcessColumn.setCellValueFactory(new PropertyValueFactory<>("weldingProcess"));
        thicknessColumn.setCellValueFactory(new PropertyValueFactory<>("thickness"));
        diameterColumn.setCellValueFactory(new PropertyValueFactory<>("diameter"));
        defectTypeColumn.setCellValueFactory(new PropertyValueFactory<>("defectType"));
        defectLocColumn.setCellValueFactory(new PropertyValueFactory<>("defectLoc"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        // Init other data
        Report.MAXDATE = LocalDate.now();
        inspectionDatesDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(Report.MAXDATE) || item.isBefore(Report.MINDATE));
                    }});
        operatorDateDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(Report.MAXDATE) || item.isBefore(Report.MINDATE));
                    }});
        evaluatorDateDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(Report.MAXDATE) || item.isBefore(Report.MINDATE));
                    }});
        conformerDateDatePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(Report.MAXDATE) || item.isBefore(Report.MINDATE));
                    }});
    }

    // ====== On Action ==============================

    @FXML
    void customerAndReportOnAction(ActionEvent event) {
        equipmentButton.setSelected(false);
        personnelButton.setSelected(false);
        inspectionResultButton.setSelected(false);
        customerAndReportPane.toFront();
        if(!customerButton.isSelected()){
            customerButton.setSelected(true);
        }
    }

    @FXML
    void equipmentOnAuction(ActionEvent event) {
        personnelButton.setSelected(false);
        inspectionResultButton.setSelected(false);
        customerButton.setSelected(false);
        equipmentPane.toFront();
        if(!equipmentButton.isSelected()){
            equipmentButton.setSelected(true);
        }
    }

    @FXML
    void inspectionOnAction(ActionEvent event) {
        equipmentButton.setSelected(false);
        personnelButton.setSelected(false);
        customerButton.setSelected(false);
        inspectionPane.toFront();
        if(!inspectionResultButton.isSelected()){
            inspectionResultButton.setSelected(true);
        }
    }

    @FXML
    void personnelOnAction(ActionEvent event) {
        equipmentButton.setSelected(false);
        inspectionResultButton.setSelected(false);
        customerButton.setSelected(false);
        personnelPane.toFront();
        if(!personnelButton.isSelected()){
            personnelButton.setSelected(true);
        }
    }

    @FXML
    void cancelOnAction(ActionEvent event) throws IOException {
        if(cancelAlert()){
            generalPane.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/main.fxml"));
            Parent root = fxmlLoader.load();
            MainController mainController = fxmlLoader.getController();
            // showing settings if the user is an admin
            if(user.getLevel() >= User.LEVEL3)
                mainController.showSettingsButton();
            mainController.setUser(user);
            mainController.setHelloLabel();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setTitle("Ana Ekran");
            stage.show();
        }
    }

    @FXML
    void excelButtonOnAction(ActionEvent event) {
        Report report = getReport();
        if(report != null){
            String path = getPath(EXCEL);
            if(path != null)
                try {
                    FileHandler.getFile(report, path, FileHandler.EXCELMODE);
                }catch (Exception e){
                    cantSaveFileAlert(e);
                }
        }
    }

    @FXML
    void pdfButtonOnAction(ActionEvent event)  {
        Report report = getReport();
        if(report != null){
            String path = getPath(PDF);
            if(path != null)
                try {
                    FileHandler.getFile(report, path, FileHandler.PDFMODE);
                }catch (Exception e){
                    cantSaveFileAlert(e);
                }
        }
    }

    @FXML
    void InspectionResultAddButtonOnAction(ActionEvent event) {
        if(inspectionResultTableView.getItems().size() >= InspectionResult.MAXSERIALNO){
            outOfBoundaryAlert();
        }else{
            if(!areInsResFieldsLegal()){
                InspectionResult inspectionResult = new InspectionResult(
                        inspectionResultTableView.getItems().size() + 1,
                        weldPieceNoField.getText(),
                        Double.parseDouble(testLengthField.getText()),
                        weldingProcessField.getText(),
                        Double.parseDouble(thicknessField.getText()),
                        diameterField.getText(),
                        defectTypeField.getText(),
                        defectLocField.getText(),
                        resultComboBox.getValue()

                );
                inspectionResultTableView.getItems().add(inspectionResult);
                inspectionResultTableView.setStyle(null);
                inspectionResultButton.setStyle(null);
            }
        }
    }

    @FXML
    void InspectionResultDeleteButtonOnAction(ActionEvent event) {
        InspectionResult item =  inspectionResultTableView.getSelectionModel().getSelectedItem();
        if(item != null){
            inspectionResultTableView.getItems().remove(item);
            // update serialNo
            for(int i = 1; i <= inspectionResultTableView.getItems().size(); i++){
                inspectionResultTableView.getItems().get(i-1).setSerialNo(i);
            }
        }else {
            selectItemAlert();
        }
    }

    @FXML
    void costumerComboBoxOnAction(ActionEvent event) {
        Customer selectedCustomer = costumerComboBox.getValue();
        testPlaceField.setText(selectedCustomer.getTestPlace());
        jobOrderNoComboBox.setItems(selectedCustomer.getJobOrderNos());
        projectNameComboBox.setItems(selectedCustomer.getProjectNames());
        offerNoComboBox.setItems(selectedCustomer.getOfferNos());
    }

    @FXML
    void operatorComboBoxOnAction(ActionEvent event) {
        operatorLevelField.setText(((Integer) operatorComboBox.getValue().getLevel()).toString());
    }

    @FXML
    void evaluatorComboBoxOnAction(ActionEvent event) {
        evaluatorLevelField.setText(((Integer) evaluatorComboBox.getValue().getLevel()).toString());
    }

    @FXML
    void conformerComboBoxOnAction(ActionEvent event){
        conformerLevelField.setText(((Integer) conformerComboBox.getValue().getLevel()).toString());
    }

    // ====== On Key ==============================

    // these functions are made to sense any change in text fields and report errors immediately

    // Customer on key

    @FXML
    void evaluationStandardFieldOnKey(KeyEvent event) {
        evaluationStandardField.setStyle(null);
        if(evaluationStandardField.getText().equals("")|| Main.isStringNotLegal(evaluationStandardField.getText())){
            evaluationStandardField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void inspectionProcedureFieldOnKey(KeyEvent event) {
        inspectionProcedureField.setStyle(null);
        if(inspectionProcedureField.getText().equals("")|| Main.isStringNotLegal(inspectionProcedureField.getText())){
            inspectionProcedureField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void inspectionStandardFieldOnKey(KeyEvent event) {
        inspectionStandardField.setStyle(null);
        if(inspectionStandardField.getText().equals("")|| Main.isStringNotLegal(inspectionStandardField.getText())){
            inspectionStandardField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void testPlaceFieldOnKey(KeyEvent event) {
        testPlaceField.setStyle(null);
        if(testPlaceField.getText().equals("") || Main.isStringNotLegal(testPlaceField.getText())){
            testPlaceField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    // Equipment on key

    @FXML
    void MPCarrierMediumField(KeyEvent event) {
        equipmentButton.setStyle(null);
        MPCarrierMediumField.setStyle(null);
        if(MPCarrierMediumField.getText().equals("")|| Main.isStringNotLegal(MPCarrierMediumField.getText())){
            MPCarrierMediumField.setStyle(ERORRTEXTFILESTYLE);
            equipmentButton.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void UVLightIntensityField(KeyEvent event) {
        UVLightIntensityField.setStyle(null);
        if(UVLightIntensityField.getText().equals("")|| Main.isStringNotLegal(UVLightIntensityField.getText())){
            UVLightIntensityField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void distanceOfLightField(KeyEvent event) {
        distanceOfLightField.setStyle(null);
        if(distanceOfLightField.getText().equals("")|| Main.isStringNotLegal(distanceOfLightField.getText())){
            distanceOfLightField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void equipmentField(KeyEvent event) {
        equipmentField.setStyle(null);
        if (equipmentField.getText().equals("") || Main.isStringNotLegal(equipmentField.getText())) {
            equipmentField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void equipmentSurfaceConditionField(KeyEvent event) {
        equipmentSurfaceConditionField.setStyle(null);
        if(equipmentSurfaceConditionField.getText().equals("")|| Main.isStringNotLegal(equipmentSurfaceConditionField.getText())){
            equipmentSurfaceConditionField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void examinationAreaField(KeyEvent event) {
        examinationAreaField.setStyle(null);
        if(examinationAreaField.getText().equals("")|| Main.isStringNotLegal(examinationAreaField.getText())){
            examinationAreaField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void gaussFieldStrengthField(KeyEvent event) {
        gaussFieldStrengthField.setStyle(null);
        if(gaussFieldStrengthField.getText().equals("")|| Main.isStringNotLegal(gaussFieldStrengthField.getText())){
            gaussFieldStrengthField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void identificationOfLightEquipField(KeyEvent event) {
        identificationOfLightEquipField.setStyle(null);
        if(identificationOfLightEquipField.getText().equals("")|| Main.isStringNotLegal(identificationOfLightEquipField.getText())){
            identificationOfLightEquipField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void liftingTestDateNumberField(KeyEvent event) {
        liftingTestDateNumberField.setStyle(null);
        if(liftingTestDateNumberField.getText().equals("")|| Main.isStringNotLegal(liftingTestDateNumberField.getText())){
            liftingTestDateNumberField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void luxmeterField(KeyEvent event) {
        luxmeterField.setStyle(null);
        if(luxmeterField.getText().equals("")|| Main.isStringNotLegal(luxmeterField.getText())){
            luxmeterField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void magTechField(KeyEvent event) {
        magTechField.setStyle(null);
        if(magTechField.getText().equals("")|| Main.isStringNotLegal(magTechField.getText())){
            magTechField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void poleDistanceField(KeyEvent event) {
        poleDistanceField.setStyle(null);
        if(poleDistanceField.getText().equals("") ||
                Main.isNotDouble(poleDistanceField.getText())){
            poleDistanceField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void standardDeviationsField(KeyEvent event) {
        standardDeviationsField.setStyle(null);
        if(standardDeviationsField.getText().equals("")|| Main.isStringNotLegal(standardDeviationsField.getText())){
            standardDeviationsField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void surfaceTemperatureField(KeyEvent event) {
        surfaceTemperatureField.setStyle(null);
        if(surfaceTemperatureField.getText().equals("") ||
                Main.isNotDouble(surfaceTemperatureField.getText())){
            surfaceTemperatureField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    // Inspection result on key

    @FXML
    void testLengthField(KeyEvent event) {
        testLengthField.setStyle(null);
        if(testLengthField.getText().equals("") ||
                Main.isNotDouble(testLengthField.getText())){
            testLengthField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void thicknessField(KeyEvent event) {
        thicknessField.setStyle(null);
        if(thicknessField.getText().equals("") ||
                Main.isNotDouble(thicknessField.getText())){
            thicknessField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void weldPieceNoField(KeyEvent event){
        weldPieceNoField.setStyle(null);
        if(weldPieceNoField.getText().equals("")|| Main.isStringNotLegal(weldPieceNoField.getText())){
            weldPieceNoField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void weldingProcessField(KeyEvent event){
        weldingProcessField.setStyle(null);
        if(weldingProcessField.getText().equals("")|| Main.isStringNotLegal(weldingProcessField.getText())){
            weldingProcessField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    @FXML
    void reportNoField(KeyEvent event){
        reportNoField.setStyle(null);
        if(reportNoField.getText().equals("") || !reportNoField.getText().matches("(\\p{Digit}+)")|| Main.isStringNotLegal(reportNoField.getText())){
            reportNoField.setStyle(ERORRTEXTFILESTYLE);
        }
    }

    // ====== Checking Functions =====================

    private boolean areInsResFieldsLegal(){
        // reset style of fields
        weldPieceNoField.setStyle(null);
        testLengthField.setStyle(null);
        weldingProcessField.setStyle(null);
        thicknessField.setStyle(null);
        defectTypeField.setStyle(null);
        defectLocField.setStyle(null);
        resultComboBox.setStyle(null);


        boolean falseness = false;
        if(weldPieceNoField.getText().equals("")  || Main.isStringNotLegal(weldPieceNoField.getText())){
            weldPieceNoField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(testLengthField.getText().equals("") ||
                Main.isNotDouble(testLengthField.getText())){
            testLengthField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(weldingProcessField.getText().equals("")|| Main.isStringNotLegal(weldingProcessField.getText())){
            weldingProcessField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(thicknessField.getText().equals("") ||
                Main.isNotDouble(thicknessField.getText())){
            thicknessField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(resultComboBox.getValue() != null && resultComboBox.getValue().equals(InspectionResult.RED) &&
                (defectTypeField.getText().equals("") || Main.isStringNotLegal(defectTypeField.getText()))){
            defectTypeField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(resultComboBox.getValue() != null && resultComboBox.getValue().equals(InspectionResult.RED) &&
                (defectLocField.getText().equals("")|| Main.isStringNotLegal(defectLocField.getText()))){
            defectLocField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(resultComboBox.getValue() == null){
            resultComboBox.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }

        return falseness;
    }

    private boolean areEquipmentFieldsEmpty(){
        // reset styles
        poleDistanceField.setStyle(null);
        equipmentField.setStyle(null);
        MPCarrierMediumField.setStyle(null);
        magTechField.setStyle(null);
        UVLightIntensityField.setStyle(null);
        distanceOfLightField.setStyle(null);
        examinationAreaField.setStyle(null);
        luxmeterField.setStyle(null);
        surfaceTemperatureField.setStyle(null);
        gaussFieldStrengthField.setStyle(null);
        equipmentSurfaceConditionField.setStyle(null);
        identificationOfLightEquipField.setStyle(null);
        liftingTestDateNumberField.setStyle(null);
        standardDeviationsField.setStyle(null);
        equipmentButton.setStyle(null);

        boolean falseness = false;
        if(poleDistanceField.getText().equals("") ||
                Main.isNotDouble(poleDistanceField.getText())){
            poleDistanceField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(equipmentField.getText().equals("")|| Main.isStringNotLegal(equipmentField.getText())){
            equipmentField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(MPCarrierMediumField.getText().equals("")|| Main.isStringNotLegal(MPCarrierMediumField.getText())){
            MPCarrierMediumField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(magTechField.getText().equals("")|| Main.isStringNotLegal(magTechField.getText())){
            magTechField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(UVLightIntensityField.getText().equals("")|| Main.isStringNotLegal(UVLightIntensityField.getText())){
            UVLightIntensityField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(distanceOfLightField.getText().equals("")|| Main.isStringNotLegal(distanceOfLightField.getText())){
            distanceOfLightField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(examinationAreaField.getText().equals("")|| Main.isStringNotLegal(examinationAreaField.getText())){
            examinationAreaField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(luxmeterField.getText().equals("")|| Main.isStringNotLegal(luxmeterField.getText())){
            luxmeterField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(surfaceTemperatureField.getText().equals("") ||
                Main.isNotDouble(poleDistanceField.getText())){
            surfaceTemperatureField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(gaussFieldStrengthField.getText().equals("")|| Main.isStringNotLegal(gaussFieldStrengthField.getText())){
            gaussFieldStrengthField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(equipmentSurfaceConditionField.getText().equals("")|| Main.isStringNotLegal(equipmentSurfaceConditionField.getText())){
            equipmentSurfaceConditionField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(identificationOfLightEquipField.getText().equals("")|| Main.isStringNotLegal(identificationOfLightEquipField.getText())){
            identificationOfLightEquipField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(liftingTestDateNumberField.getText().equals("")|| Main.isStringNotLegal(liftingTestDateNumberField.getText())){
            liftingTestDateNumberField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(standardDeviationsField.getText().equals("")|| Main.isStringNotLegal(standardDeviationsField.getText())){
            standardDeviationsField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }

        if(falseness){
            equipmentButton.setStyle(ERORRTEXTFILESTYLE);
        }

        return falseness;
    }

    private boolean areCostumerFieldsEmpty(){
        jobOrderNoComboBox.setStyle(null);
        projectNameComboBox.setStyle(null);
        offerNoComboBox.setStyle(null);
        testPlaceField.setStyle(null);
        inspectionStandardField.setStyle(null);
        evaluationStandardField.setStyle(null);
        inspectionProcedureField.setStyle(null);
        stageOfExaminationComboBox.setStyle(null);
        surfaceConditionComboBox.setStyle(null);
        customerButton.setStyle(null);
        reportNoField.setStyle(null);

        boolean falseness = false;

        if(jobOrderNoComboBox.getValue() == null){
            jobOrderNoComboBox.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(projectNameComboBox.getValue() == null){
            projectNameComboBox.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(offerNoComboBox.getValue() == null){
            offerNoComboBox.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(testPlaceField.getText().equals("") || Main.isStringNotLegal(testPlaceField.getText())){
            testPlaceField.setStyle(ERORRTEXTFILESTYLE);
            falseness =true;
        }

        if(inspectionStandardField.getText().equals("")|| Main.isStringNotLegal(inspectionStandardField.getText())){
            inspectionStandardField.setStyle(ERORRTEXTFILESTYLE);
            falseness =true;
        }
        if(evaluationStandardField.getText().equals("")|| Main.isStringNotLegal(evaluationStandardField.getText())){
            evaluationStandardField.setStyle(ERORRTEXTFILESTYLE);
            falseness =true;
        }
        if(inspectionProcedureField.getText().equals("")|| Main.isStringNotLegal(inspectionProcedureField.getText())){
            inspectionProcedureField.setStyle(ERORRTEXTFILESTYLE);
            falseness =true;
        }

        if(stageOfExaminationComboBox.getValue() == null){
            stageOfExaminationComboBox.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(surfaceConditionComboBox.getValue() == null){
            surfaceConditionComboBox.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(reportNoField.getText().equals("") || !reportNoField.getText().matches("(\\p{Digit}+)")
                || Main.isStringNotLegal(reportNoField.getText())){
            reportNoField.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if(falseness){
            customerButton.setStyle(ERORRTEXTFILESTYLE);
        }
        return falseness;
    }

    private boolean isInsResTableViewEmpty(){
        inspectionResultTableView.setStyle(null);
        inspectionResultButton.setStyle(null);
        boolean falseness = false;
        if(inspectionResultTableView.getItems().size() == 0){
            inspectionResultTableView.setStyle(ERORRTEXTFILESTYLE);
            falseness = true;
        }
        if (falseness){
            inspectionResultButton.setStyle(ERORRTEXTFILESTYLE);
        }
        return falseness;
    }

    // ====== Helper Functions =========================

    public void setData() {
        // Users Data
        operatorComboBox.setItems(operators);
        operatorComboBox.setValue(operator);
        operatorLevelField.setText(String.valueOf(operator.getLevel()));
        operatorDateDatePicker.setValue(reportDate);

        evaluatorComboBox.setItems(evaluators);
        evaluatorComboBox.setValue(evaluator);
        evaluatorLevelField.setText(String.valueOf(evaluator.getLevel()));
        evaluatorDateDatePicker.setValue(reportDate);

        conformerComboBox.setItems(conformers);
        conformerComboBox.setValue(conformer);
        conformerLevelField.setText(String.valueOf(conformer.getLevel()));
        conformerDateDatePicker.setValue(reportDate);

        // Equipment Data
        poleDistanceField.setText(String.valueOf(equipment.getPoleDistance()));
        equipmentField.setText(equipment.getEquipment());
        MPCarrierMediumField.setText(equipment.getMPCarrierMedium());
        magTechField.setText(equipment.getMagTech());
        UVLightIntensityField.setText(equipment.getUVLightIntensity());
        distanceOfLightField.setText(equipment.getDistanceOfLight());

        // Costumer Data
        costumerComboBox.setItems(customers);
        costumerComboBox.setValue(customer);
        testPlaceField.setText(customer.getTestPlace());
        jobOrderNoComboBox.setItems(customer.getJobOrderNos());
        projectNameComboBox.setItems(customer.getProjectNames());
        offerNoComboBox.setItems(customer.getOfferNos());

        // Other Data
        reportDateDatePicker.setValue(reportDate);
        reportNoField.setText(reportNo);
        inspectionDatesDatePicker.setValue(reportDate);
    }

    private Report getReport(){
        Report report = null;
        boolean check = !areEquipmentFieldsEmpty();
        check = !isInsResTableViewEmpty() && check;
        check = !areCostumerFieldsEmpty() && check;
        if (check){
            customer = costumerComboBox.getValue();
            customer.setTestPlace(testPlaceField.getText());
            equipment = new Equipment(
                    Double.parseDouble(poleDistanceField.getText()),
                    equipmentField.getText(),
                    MPCarrierMediumField.getText(),
                    magTechField.getText(),
                    UVLightIntensityField.getText(),
                    distanceOfLightField.getText()
            );
            report = new Report(
                    operator,
                    evaluator,
                    conformer,
                    customer,
                    stageOfExaminationComboBox.getValue(),
                    surfaceConditionComboBox.getValue(),
                    equipment,
                    inspectionResultTableView.getItems(),
                    inspectionStandardField.getText(),
                    evaluationStandardField.getText(),
                    inspectionProcedureField.getText(),
                    inspectionScopeComboBox.getValue(),
                    drawingNoField.getText(),
                    numberOfPagesComboBox.getValue(),
                    reportNo,
                    reportDateDatePicker.getValue(),
                    examinationAreaField.getText(),
                    currentTypeComboBox.getValue(),
                    luxmeterField.getText(),
                    testMediumField.getText(),
                    demagnetizationField.getText(),
                    heatTreatmentField.getText(),
                    Double.parseDouble(surfaceTemperatureField.getText()),
                    gaussFieldStrengthField.getText(),
                    equipmentSurfaceConditionField.getText(),
                    identificationOfLightEquipField.getText(),
                    liftingTestDateNumberField.getText(),
                    filerWeldCheckBox.isSelected(),
                    buttWeldCheckBox.isSelected(),
                    standardDeviationsField.getText(),
                    inspectionDatesDatePicker.getValue(),
                    descriptionAndAttachmentsField.getText(),
                    jobOrderNoComboBox.getValue(),
                    offerNoComboBox.getValue(),
                    projectNameComboBox.getValue(),
                    operatorDateDatePicker.getValue(),
                    evaluatorDateDatePicker.getValue(),
                    conformerDateDatePicker.getValue()
            );
        }
        return report;
    }

    private String getPath(String str){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter;
        if(str.equals(EXCEL))
            extFilter = new FileChooser.ExtensionFilter("Excel (*.xlsx)", "*.xlsx");
        else
            extFilter = new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().addAll(extFilter);
        fileChooser.setInitialFileName("Report");
        File file = fileChooser.showSaveDialog(generalPane.getScene().getWindow());
        if(file == null)
            return null;
        System.out.println(file.getPath());
        return file.getPath();
    }

    // ====== Alerts ====================================

    public static boolean cancelAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Uyarı");
        alert.setHeaderText(null);
        alert.setContentText("Girdiğiniz tüm bilgiler kaydedilmeyecek. İptal etmek istediğinden emin misiniz?");
        ButtonType buttonYes = new ButtonType("Evet");
        ButtonType buttonNo = new ButtonType("Hayır");
        alert.getButtonTypes().setAll(buttonYes, buttonNo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonYes;// if user exit without clicking anything or if user clicked cancel do nothing
    }

    private void outOfBoundaryAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("sınır dışı");
        alert.setHeaderText(null);
        alert.setContentText("14'ten fazla muayene sonuçu ekleyemezsiniz.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(generalPane.getScene().getWindow());
        ButtonType buttonYes = new ButtonType("Tamam");
        alert.getButtonTypes().setAll(buttonYes);
        alert.showAndWait();
    }

    private void selectItemAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hiçbir şey seçilmedi");
        alert.setHeaderText(null);
        alert.setContentText("Lütfen bir satır seçin");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(generalPane.getScene().getWindow());
        ButtonType buttonYes = new ButtonType("Tamam");
        alert.getButtonTypes().setAll(buttonYes);
        alert.show();
    }

    private void cantSaveFileAlert(Exception e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dosya kaydedilemedi");
        alert.setHeaderText(null);
        alert.setContentText(e.toString());
        ButtonType buttonYes = new ButtonType("Tamam");
        alert.getButtonTypes().setAll(buttonYes);
        alert.show();
    }

    // ======= Setters and Getters ======================

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public User getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(User evaluator) {
        this.evaluator = evaluator;
    }

    public User getConformer() {
        return conformer;
    }

    public void setConformer(User conformer) {
        this.conformer = conformer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ObservableList<Customer> customers) {
        this.customers = customers;
    }

    public ObservableList<User> getOperators() {
        return operators;
    }

    public void setOperators(ObservableList<User> operators) {
        this.operators = operators;
    }

    public ObservableList<User> getEvaluators() {
        return evaluators;
    }

    public void setEvaluators(ObservableList<User> evaluators) {
        this.evaluators = evaluators;
    }

    public ObservableList<User> getConformers() {
        return conformers;
    }

    public void setConformers(ObservableList<User> conformers) {
        this.conformers = conformers;
    }
}