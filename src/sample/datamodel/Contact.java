package sample.datamodel;
import javafx.beans.property.SimpleStringProperty;

public class Contact {
    private SimpleStringProperty firstName;   //updates tableview whenever the contact objects fields are changed.
    private SimpleStringProperty lastName;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty notes;
    
    public Contact(){
        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        phoneNumber = new SimpleStringProperty("");
        notes = new SimpleStringProperty("");
    }
    
    
    public String getFirstName() {
        return firstName.get();
    }
    
    //ALERT!!! even though it says the method is not being used. it is being used magically by tableview to hahaha.
    public SimpleStringProperty firstNameProperty(){ //have to specifically name this property as such or data binding wont work
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }
    
    public String getLastName() {
        return lastName.get();
    }
    
    public SimpleStringProperty lastNameProperty(){
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }
    
    public String getPhoneNumber() {
        return phoneNumber.get();
    }
    
    public SimpleStringProperty phoneNumberProperty(){
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }
    
    public String getNotes() {
        return notes.get();
    }
    
    public SimpleStringProperty notesProperty(){
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes.set(notes);
    }
}
