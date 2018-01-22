package ru.xidv.andrst.phonebook.Activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.xidv.andrst.phonebook.R;
import ru.xidv.andrst.phonebook.sqlite.helper.DataHelper;
import ru.xidv.andrst.phonebook.sqlite.model.Contact;

public class MainActivity extends AppCompatActivity {

    DataHelper db;
    int SELECTED_ITEM;
    final List<String> CONTACTS_INFO = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DataHelper(getApplicationContext());

        final ListView contactListView = findViewById(R.id.contactListView);
        registerForContextMenu(contactListView);


        final List<Contact> contactsArray = db.getAllContacts();

        if (contactsArray.size() > 0) {
            for ( int i = 0; i < contactsArray.size(); i++ ) {

                if (contactsArray.get(i).getContactName() != null) {
                    CONTACTS_INFO.add(contactsArray.get(i).getContactName()
                            + " " + contactsArray.get(i).getPhoneNumber());
                }

                if (contactsArray.get(i).getContactName() == null) {
                    CONTACTS_INFO.add(contactsArray.get(i).getPhoneNumber());
                }
            }
        }

        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, CONTACTS_INFO);

        contactListView.setAdapter(listAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showContactEditorDialog(true);
                listAdapter.notifyDataSetChanged();
            }

        });

        db.closeDB();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);

        // Get the info on which item was selected
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        SELECTED_ITEM = info.position;

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        List<String> contacts_info = CONTACTS_INFO;

        switch (item.getItemId()) {
            case R.id.edit:
                showContactEditorDialog(false);
                return true;

            case R.id.delete:
                deleteContact();
                return true;

            case R.id.call:
                makeCall();
                return true;

            case R.id.sms:
                sendSms();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                System.out.println("success");
                this.recreate();
            }
        }
    }


    public void showContactEditorDialog(final boolean add) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);


        final EditText editName = dialogView.findViewById(R.id.edit1);
        final EditText editNumber = dialogView.findViewById(R.id.edit2);
        final EditText editNote = dialogView.findViewById(R.id.edit3);

        if (!add) {
            List<Contact> allContacts = db.getAllContacts();
            Contact selectedContact = allContacts.get(SELECTED_ITEM);

            editName.setText(selectedContact.getContactName());
            editNumber.setText(selectedContact.getPhoneNumber());
            editNote.setText(selectedContact.getNote());
        }

        dialogBuilder.setTitle("Contact info");
        dialogBuilder.setMessage("Enter info below");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();


                // adding contact to DB
                if (editNumber.getText().toString().length() >= 6) {

                    String contactName = editName.getText().toString();
                    String contactPhoneNumber = editNumber.getText().toString();
                    String contactNote = editNote.getText().toString();

                    contactPhoneNumber = contactPhoneNumber.charAt(0) == '+' ||
                            contactPhoneNumber.charAt(0) ==  '8' ? contactPhoneNumber : '+' + contactPhoneNumber;

                    if (add) {
                        Contact contact = new Contact(contactName, contactPhoneNumber, contactNote);
                        long contact_id = db.createContact(contact);
                    }

                    if (!add) {
                        List<Contact> allContacts = db.getAllContacts();
                        Contact selectedContact = allContacts.get(SELECTED_ITEM);

                        selectedContact.setContactName(contactName);
                        selectedContact.setPhoneNumber(contactPhoneNumber);
                        selectedContact.setNote(contactNote);
                        db.updateContactInfo(selectedContact);
                    }

                    recreate();
//                    db.deleteAll("CONTACTS");

                } else {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(MainActivity.this);
                    }
                    builder.setTitle("Attention")
                            .setMessage("You should give at least 6 digit phone number.")
                            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void deleteContact() {
        List<Contact> allContacts = db.getAllContacts();
        Contact selectedContact = allContacts.get(SELECTED_ITEM);
        db.deleteContact(selectedContact);
        recreate();
    }

    public void sendSms() {
        List<Contact> allContacts = db.getAllContacts();
        Contact selectedContact = allContacts.get(SELECTED_ITEM);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(selectedContact.getPhoneNumber(), null, "Test SMS from PhoneBook Application by Alexander Lebedev.", null, null);
        Toast.makeText(getApplicationContext(), "Test Message sent", Toast.LENGTH_SHORT).show();
    }

    public void makeCall() {
        List<Contact> allContacts = db.getAllContacts();
        Contact selectedContact = allContacts.get(SELECTED_ITEM);

        Uri call = Uri.parse("tel:" + selectedContact.getPhoneNumber());
        Intent surf = new Intent(Intent.ACTION_CALL, call);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(surf);

    }

}
