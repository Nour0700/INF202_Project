package sample.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import sample.database.DatabaseHandler;
import sample.model.*;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class CustomerHandlerController {
    private static final String ERORRTEXTFILESTYLE = "-fx-border-color: red;";
    public static final int ADDMODE = 1;
    public static final int EDITMODE = 2;
    private int mode = ADDMODE;
    private Customer selectedCustomer;

    @FXML
    private AnchorPane addCustomerPane;

    @FXML
    private TextField projectNameField;

    @FXML
    private TextField jobOrderNoField;

    @FXML
    private TextField offerNoField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField testPlaceField;

    @FXML
    private Text nameMesg;

    @FXML
    private Text testPlaceMesg;

    @FXML
    private Label projectNameMesg;

    @FXML
    private Label jobOrderNoMesg;

    @FXML
    private Label offerNoMesg;

    @FXML
    private TableView<ProjectName> projectNameTableView;

    @FXML
    private TableColumn<ProjectName, String> projectNameColumn;

    @FXML
    private TableView<JobOrderNo> jobOrderNoTableView;

    @FXML
    private TableColumn<JobOrderNo, String> jobOrderNoColumn;

    @FXML
    private TableView<OfferNo> offerNoTableView;

    @FXML
    private TableColumn<OfferNo, String> offerNoColumn;

    @FXML
    private Button addButton;

    @FXML
    public void initialize() {
        jobOrderNoColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        offerNoColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
    }

    // ====== On Action =======================


    @FXML
    void addButtonOnAction(ActionEvent event) throws SQLException {
        resetStyle();
        if(!areFieldsEmpty()){
            Customer newCustomer = new Customer(
                    nameField.getText(),
                    testPlaceField.getText(),
                    jobOrderNoTableView.getItems(),
                    projectNameTableView.getItems(),
                    offerNoTableView.getItems()
            );
            if(mode == ADDMODE){
                DatabaseHandler.addNewCustomer(newCustomer);
            }else {
                DatabaseHandler.editCustomer(newCustomer, selectedCustomer);
            }
            addCustomerPane.getScene().getWindow().hide();
        }
    }

    @FXML
    void canselButtonOnAction(ActionEvent event) {
        addCustomerPane.getScene().getWindow().hide();
    }

    @FXML
    void jobOrderNoAddButtonOnAction(ActionEvent event) {
        jobOrderNoField.setStyle(null);
        if(jobOrderNoField.getText().equals("")){
            jobOrderNoField.setStyle(ERORRTEXTFILESTYLE);
        }else{
            JobOrderNo newItem = new JobOrderNo(jobOrderNoField.getText());
            jobOrderNoTableView.getItems().add(newItem);
            jobOrderNoField.setText("");
        }
    }

    @FXML
    void jobOrderNoRemoveButtonOnAction(ActionEvent event) {
        ObservableList<JobOrderNo> jobOrderNoSelected;
        jobOrderNoSelected = jobOrderNoTableView.getSelectionModel().getSelectedItems();
        if(!jobOrderNoSelected.isEmpty()){
            // just one item could be selected -> 0
            jobOrderNoTableView.getItems().remove(jobOrderNoSelected.get(0));
        }else{
            selectItemAlert();
        }
    }

    @FXML
    void offerNoAddButtonOnAction(ActionEvent event) {
        offerNoField.setStyle(null);
        if(offerNoField.getText().equals("")){
            offerNoField.setStyle(ERORRTEXTFILESTYLE);
        }else {
            OfferNo newItem = new OfferNo(offerNoField.getText());
            offerNoTableView.getItems().add(newItem);
            offerNoField.setText("");
        }
    }

    @FXML
    void offerNoRemoveButtonOnAction(ActionEvent event) {
        ObservableList<OfferNo> offerNoSelected;
        offerNoSelected = offerNoTableView.getSelectionModel().getSelectedItems();
        if(!offerNoSelected.isEmpty()){
            // just one item could be selected -> 0
            offerNoTableView.getItems().remove(offerNoSelected.get(0));
        }else{
            selectItemAlert();
        }
    }

    @FXML
    void projectNameAddButtonOnAction(ActionEvent event) {
        projectNameField.setStyle(null);
        if(projectNameField.getText().equals("")){
            projectNameField.setStyle(ERORRTEXTFILESTYLE);
        }else {
            ProjectName newItem = new ProjectName(projectNameField.getText());
            projectNameTableView.getItems().add(newItem);
            projectNameField.setText("");
        }
    }

    @FXML
    void projectNameRemoveButtonOnAction(ActionEvent event) {
        ObservableList<ProjectName> projectNameSelected;
        projectNameSelected = projectNameTableView.getSelectionModel().getSelectedItems();
        if(!projectNameSelected.isEmpty()){
            // just one item could be selected -> 0
            projectNameTableView.getItems().remove(projectNameSelected.get(0));
        }else{
            selectItemAlert();
        }
    }


    // ====== Alerts =============================

    private void selectItemAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hiçbir şey seçilmedi");
        alert.setHeaderText(null);
        alert.setContentText("Lütfen bir satır seçin");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(addCustomerPane.getScene().getWindow());
        ButtonType buttonYes = new ButtonType("Tamam");
        alert.getButtonTypes().setAll(buttonYes);
        alert.show();
    }

    // ====== Helper Functions =======================

    public void setEditMode(Customer customer){
        setMode(EDITMODE);
        setSelectedCustomer(customer);
        addButton.setText("Kaydet");
        fillFields();
    }

    public void fillFields(){
        nameField.setText(selectedCustomer.getName());
        testPlaceField.setText(selectedCustomer.getTestPlace());
        jobOrderNoTableView.setItems(selectedCustomer.getJobOrderNos());
        projectNameTableView.setItems(selectedCustomer.getProjectNames());
        offerNoTableView.setItems(selectedCustomer.getOfferNos());
    }

    // ====== Emptiness checking functions =======

    private boolean areFieldsEmpty(){
        boolean emptiness = false;

        if(nameField.getText().equals("")){
            nameMesg.setVisible(true);
            nameField.setStyle(ERORRTEXTFILESTYLE);
            emptiness = true;
        }
        if(testPlaceField.getText().equals("")){
            testPlaceMesg.setVisible(true);
            testPlaceField.setStyle(ERORRTEXTFILESTYLE);
            emptiness = true;
        }
        if(jobOrderNoTableView.getItems().isEmpty()){
            jobOrderNoMesg.setVisible(true);
            emptiness = true;
        }
        if(projectNameTableView.getItems().isEmpty()){
            projectNameMesg.setVisible(true);
            emptiness = true;
        }
        if(offerNoTableView.getItems().isEmpty()){
            offerNoMesg.setVisible(true);
            emptiness = true;
        }
        return emptiness;
    }

    // ====== Setters and Getters ====================

    private void resetStyle(){
        nameMesg.setVisible(false);
        nameField.setStyle(null);
        testPlaceMesg.setVisible(false);
        testPlaceField.setStyle(null);
        jobOrderNoMesg.setVisible(false);
        projectNameMesg.setVisible(false);
        offerNoMesg.setVisible(false);
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
