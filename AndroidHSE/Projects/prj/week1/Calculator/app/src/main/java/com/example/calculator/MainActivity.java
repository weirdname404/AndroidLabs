package com.example.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onButtonClicked(View view) {

        retreiveData();
    }


    public void retreiveData() {

        try {

            EditText ruEditText = (EditText) findViewById(R.id.rubEditText);
            EditText usEditText = (EditText) findViewById(R.id.usdEditText);
            EditText rsEditText = (EditText) findViewById(R.id.rubUsdEditText);

            EditText[] userInputs = new EditText[]{ruEditText, usEditText, rsEditText};
            double[] requiredValues = new double[]{Double.NaN, Double.NaN, Double.NaN};

            int missingValues = 0;

            // iterates to gather numbers
            for (int i = 0; i < 3; ++i) {

                if (!userInputs[i].getText().toString().isEmpty()) {
                    requiredValues[i] = Double.parseDouble(userInputs[i].getText().toString());
                } else {
                    ++missingValues;
                }
            }

            double res = 0;

            if (missingValues == 1) {

                // iterates to gather missing values
                for (int i = 0; i < 3; ++i) {
                    if (userInputs[i].getText().toString().isEmpty()) {
                        switch (i) {
                            case 0:
                                res = requiredValues[1] * requiredValues[2];
                                break;
                            case 1:
                                res = requiredValues[0] / requiredValues[2];
                                break;
                            case 2:
                                res = requiredValues[0] / requiredValues[1];
                                break;
//                          default:
//                              throw new IllegalArgumentException();
                        }

                        userInputs[i].setText(Double.toString(res));
                    }

                }

            } else {
                throw new IllegalArgumentException();
            }

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "2 int or double inputs are required",
                    Toast.LENGTH_SHORT).show();
        }
    }
}