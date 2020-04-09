package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import sample.datamodel.Contact;

public class DialogController {
    @FXML
    private TextField firstName;
    
    @FXML
    private TextField lastName;
    
    @FXML
    private TextField phoneNumber;
    
    @FXML
    private TextField address;
    
    private Dialog<ButtonType> dialog;
    
    public void initialize(Dialog<ButtonType> dialog){
        this.dialog = dialog;
        if(hasEmptyField()){
            dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        }
        TextFieldListener textFieldListener = new TextFieldListener();
        firstName.textProperty().addListener(textFieldListener);
        lastName.textProperty().addListener(textFieldListener);
        phoneNumber.textProperty().addListener(textFieldListener);
        address.textProperty().addListener(textFieldListener);
    }
    
    private class TextFieldListener implements  ChangeListener<String>{
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
            if(hasEmptyField()) {
                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
            }else{
                dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
            }
        }
    }
    
    private boolean hasEmptyField(){
        if(firstName.getText().equals("") || lastName.getText().equals("") ||
            phoneNumber.getText().equals("") || address.getText().equals("")){
            return true;
        }
        return false;
    }
    
    public Contact getDialogInput(){
        String firstName = this.firstName.getText().trim();
        String lastName = this.lastName.getText().trim();
        String phoneNumber = this.phoneNumber.getText().trim();
        String address = this.address.getText().trim();
        
        if(firstName.equals("") || lastName.equals("") || phoneNumber.equals("") || address.equals(""))
            return null;
        else{
            Contact newContact = new Contact();
            newContact.setFirstName(firstName);
            newContact.setLastName(lastName);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setNotes(address);
            return newContact;
        }
    }
    
    public void setFields(Contact contact){
        this.firstName.setText(contact.getFirstName());
        this.lastName.setText(contact.getLastName());
        this.address.setText(contact.getNotes());
        this.phoneNumber.setText(contact.getPhoneNumber());
    }
    
    public void updateContact(Contact contact){
        contact.setFirstName(this.firstName.getText());
        contact.setLastName(this.lastName.getText());
        contact.setPhoneNumber(this.phoneNumber.getText());
        contact.setNotes(this.address.getText());
    }
    
}
