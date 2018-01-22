package ru.xidv.andrst.persfinassistantdatamanip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {


    //----<Auxiliary Types>----

    // Class that creates the AlertDialog
    public static class AboutDialogFragment extends DialogFragment {

        public static AboutDialogFragment newInstance() {
            return new AboutDialogFragment();
        }

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("xi Personal Finance Assistant")

                    // User can dismiss dialog by hitting back button
                    .setCancelable(true)

//                    // Set up No Button
//                    .setNegativeButton("No",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int id) {
//                                    ((MainActivity) getActivity())
//                                            .doSomething();
//                                }
//                            })

                    // Set up Yes Button
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                    // just do nothing...
//                                    ((MainActivity) getActivity())
//                                            .doSomething();
                                }
                            }).create();
        }
    } // class AboutDialogFragment


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // Creates a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Get a reference to the MenuInflater
        MenuInflater inflater = getMenuInflater();

        // Inflate the main_menu using activity_menu.xml
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }


    // When a menu item is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_about:
                showAboutDlg();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        } // switch

    }

    // shows About dialog
    private void showAboutDlg() {
        if(_aboutDialog == null)
            _aboutDialog = AboutDialogFragment.newInstance();

        _aboutDialog.show(getFragmentManager(), "About Personal Finance Assistant");
    }


    //--------<Fields>----------

    AboutDialogFragment _aboutDialog = null;    // about site dialog

}
