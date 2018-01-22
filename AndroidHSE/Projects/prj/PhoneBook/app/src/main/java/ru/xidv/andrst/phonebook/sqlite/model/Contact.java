package ru.xidv.andrst.phonebook.sqlite.model;

/**
 * Created by Александр on 14.12.2017.
 */

public class Contact {
    private int id;
    private String contactName;
    private String phoneNumber;
    private String note;

    public Contact() {

    }

    public Contact(String name, String number, String note) {
        this.contactName = name;
        this.phoneNumber = number;
        this.note = note;
    }

    public Contact(int id, String name, String number, String note) {
        this.id = id;
        this.contactName = name;
        this.phoneNumber = number;
        this.note = note;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }
    public void setContactName(String name) { this.contactName = name; }
    public void setPhoneNumber(String number) { this.phoneNumber = number; }
    public void setNote(String note) { this.note = note; }

    //getters
    public long getId() {
        return this.id;
    }
    public String getContactName() { return this.contactName; }
    public String getPhoneNumber() { return this.phoneNumber; }
    public String getNote() { return this.note; }
}
