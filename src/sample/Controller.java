package sample;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import sample.datamodel.Contact;
import sample.datamodel.ContactData;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    private FXMLLoader fxmlLoader;
    private Dialog<ButtonType> dialog;
    private DialogController controller;
    
    @FXML
    private VBox mainVBox;
    
    @FXML
    private TableView<Contact> tableView;
    
    @FXML
    private TableColumn<Contact,SimpleStringProperty> firstName;
    
    @FXML
    private TableColumn<Contact,SimpleStringProperty> lastName;
    
    @FXML
    private TableColumn<Contact,SimpleStringProperty> phoneNumber;
    
    @FXML
    private TableColumn<Contact,SimpleStringProperty> notes;
    
    @FXML
    private ContextMenu tableContextMenu;

    public void initialize(){
        firstName.setCellValueFactory(new PropertyValueFactory<Contact, SimpleStringProperty>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Contact,SimpleStringProperty>("lastName"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<Contact,SimpleStringProperty>("phoneNumber"));
        notes.setCellValueFactory(new PropertyValueFactory<Contact,SimpleStringProperty>("notes"));
        tableView.setItems(ContactData.getInstance().getContacts());
        tableView.getSelectionModel().selectFirst();
        
        initializeContextMenu();
        setupRowFactory();
    }
    
    private void initializeContextMenu(){
        tableContextMenu = new ContextMenu();
        
        MenuItem deleteContactMenu = new MenuItem("Delete Contact");
        deleteContactMenu.setOnAction(actionEvent -> deleteContact(tableView.getSelectionModel().getSelectedItem()));
    
        MenuItem editContactMenu = new MenuItem("Edit Contact");
        editContactMenu.setOnAction(actionEvent -> showEditContactDialog());
        
        tableContextMenu.getItems().add(editContactMenu);
        tableContextMenu.getItems().add(deleteContactMenu);
    }
    
    private void setupRowFactory(){
        tableView.setRowFactory(new Callback<>() {
            @Override
            public TableRow<Contact> call(TableView<Contact> contactTableView) {
                TableRow<Contact> row = new TableRow<>() {
                    @Override
                    protected void updateItem(Contact contact, boolean empty) {
                        super.updateItem(contact, empty);
                    }
                };
                row.emptyProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasEmpty, Boolean isEmpty) {
                        if (isEmpty)
                            row.setContextMenu(null);
                        else
                            row.setContextMenu(tableContextMenu);
                    }
                });
                return row;
            }
        });
    }
    
    @FXML
    private void showNewContactDialog(){
        dialog = new Dialog<>();
        dialog.initOwner(mainVBox.getScene().getWindow());  //main.fxml--->VBox owns dialog.fxml
        dialog.setTitle("Create New Contact");
        dialog.setHeaderText("Use this dialog to create a new contact");
        setFxmlLoader();  //initialized fxml and controller
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            Contact newContact = controller.getDialogInput();
            if(newContact == null){
                System.out.println("Invalid input provided");
            }else {
                ContactData.getInstance().addContact(newContact);
                tableView.getSelectionModel().select(newContact);
            }
        }
    }
    
    @FXML
    public void showEditContactDialog(){
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
        dialog = new Dialog<>();
        dialog.initOwner(mainVBox.getScene().getWindow());  //main.fxml--->VBox owns dialog.fxml
        dialog.setTitle("Edit Contact");
        dialog.setHeaderText("Edit: " + selectedContact.getFirstName() + " " + selectedContact.getLastName());
        setFxmlLoader();
        controller.setFields(selectedContact);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            controller.updateContact(selectedContact);
            tableView.getSelectionModel().select(selectedContact);
//            Contact updatedContact = controller.getDialogInput();
//            if(updatedContact == null){
//                System.out.println("invalid input");
//            }else{
//                ContactData.getInstance().replaceContact(selectedContact,updatedContact);
//                //controller.updateContact(selectedContact);
//                tableView.getSelectionModel().select(updatedContact);
//            }
        }
    }
    
    @FXML
    private  void deleteContact(){
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
        deleteContact(selectedContact);
    }
    
    private void deleteContact(Contact contact){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText("Delete: " + contact.getFirstName() + " " + contact.getLastName());
        alert.setContentText("Warning!! Your Contact will be deleted.");
        
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            ContactData.getInstance().deleteContact(contact);
        }
    }
    
    @FXML
    private void exitApplication(){
        Platform.exit();
    }
    
    private void setFxmlLoader(){
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialog.fxml"));
    
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        controller = fxmlLoader.getController();
        controller.initialize(dialog);
    }
    
    @FXML
    private void handleKeyPressed(KeyEvent pressedKey){
        Contact selectedContact = tableView.getSelectionModel().getSelectedItem();
        if(selectedContact != null && pressedKey.getCode().equals(KeyCode.DELETE)){
            deleteContact(selectedContact);
        }
    }
}
