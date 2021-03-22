package org.dut.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Récupère les champs
        EditText birthDate = (EditText)findViewById(R.id.birthDateEditText);
        ImageButton birthDatePickerButton = (ImageButton)findViewById(R.id.birthDatePickerImageButton);

        // Ajoute les listeners
        birthDatePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);

                        birthDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                    }
                });
                datePickerDialog.show();
            }
        });
    }
}