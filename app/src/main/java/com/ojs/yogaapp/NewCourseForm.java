package com.ojs.yogaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NewCourseForm extends AppCompatActivity {
    private TextView durationOfClass, startTimeView, endTimeView;
    private Button starttime, endtime;
//    private boolean[] daysSelected;
//    private ArrayList<Integer> Days = new ArrayList<>();
//    private String[] dayArray = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course_form);

        //add new course button Add_new_course_button
        Button addButton = findViewById(R.id.Add_new_course_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save course data to the database
                saveCourseData();
            }
        });


        Spinner spinner = findViewById(R.id.daysOfWeek);
        String[] dayArray = new String[]{"--select day of class--", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        HintAdapter adapter = new HintAdapter(this, android.R.layout.simple_spinner_item, dayArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner spinner1 = findViewById(R.id.price);
        String[] price = new String[]{"--select price of class--", "£30/session", "£40/session", "£50/session", "£60/session"};

        HintAdapter adapter1 = new HintAdapter(this, android.R.layout.simple_spinner_item, price);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner2 = findViewById(R.id.typeOfClass);
        String[] typeOfClass = new String[]{"--select type of class--", "Flow Yoga", "Aerial Yoga", "Family Yoga"};

        HintAdapter adapter2 = new HintAdapter(this, android.R.layout.simple_spinner_item, typeOfClass);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner spinner3 = findViewById(R.id.capacity);
        String[] capacity = new String[]{"--select capacity of class--", "10 persons", "20 persons", "25 persons", "30 persons"};

        HintAdapter adapter3 = new HintAdapter(this, android.R.layout.simple_spinner_item, capacity);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        durationOfClass = findViewById(R.id.durationOfClass);
        starttime = findViewById(R.id.starttime);
        endtime = findViewById(R.id.endtime);
        startTimeView = findViewById(R.id.startTimeView);
        endTimeView = findViewById(R.id.endTimeView);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateDuration();
            }
        };

        startTimeView.addTextChangedListener(textWatcher);
        endTimeView.addTextChangedListener(textWatcher);

        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(true);
            }
        });

        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(false);
            }
        });
//
//        // Initialize and set up day picker dialog
//        setupDayPickerDialog();


    }

    private void showTimePicker(final boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar1.set(Calendar.MINUTE, minute);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                String time = dateFormat.format(calendar1.getTime());
                if (isStartTime) {
                    startTimeView.setText(time);
                } else {
                    endTimeView.setText(time);
                    calculateDuration();
                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                NewCourseForm.this,
//                R.style.DialogTheme,
                timeSetListener,
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

//    private void setupDayPickerDialog() {
//        TextView textView = findViewById(R.id.daysOfWeek);
//        daysSelected = new boolean[dayArray.length];
//
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Day picker dialog setup...
//            }
//        });
//    }

    private void calculateDuration() {
        String timePickerStartTime = startTimeView.getText().toString();
        String timePickerEndTime = endTimeView.getText().toString();

        if (!timePickerStartTime.isEmpty() && !timePickerEndTime.isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            try {
                Date startTime = simpleDateFormat.parse(timePickerStartTime);
                Date endTime = simpleDateFormat.parse(timePickerEndTime);

                // Calculate the duration in milliseconds
                long durationMillis = endTime.getTime() - startTime.getTime();

                // If the end time is before the start time, adjust the duration to add 24 hours
                if (durationMillis < 0) {
                    durationMillis += 24 * 60 * 60 * 1000; // 24 hours in milliseconds
                }

                // Conversion of milliseconds to minutes
                long durationMinutes = durationMillis / (60 * 1000);

                // Conversion of minutes to hours and minutes
                long hours = durationMinutes / 60;
                long minutes = durationMinutes % 60;

                String durationText;
                if (hours > 0) {
                    durationText = hours + " hr" + (hours > 1 ? "s" : "") + " " + minutes + " min" + (minutes != 1 ? "s" : "");
                } else {
                    durationText = minutes + " minute" + (minutes != 1 ? "s" : "");
                }

                // Update TextView
                durationOfClass.setText(durationText);

            } catch (ParseException e) {
                // Handle the exception, maybe set an error message
                durationOfClass.setText("Invalid time format");
            }
        } else {
            // Handle the case where one or both of the times are not set
            durationOfClass.setText("Start/End time not set");
        }


    }

    private boolean validateFields() {
        EditText courseTitleEditText = findViewById(R.id.courseTitle);
        Spinner daySpinner = findViewById(R.id.daysOfWeek);
        Spinner priceSpinner = findViewById(R.id.price);
        Spinner typeSpinner = findViewById(R.id.typeOfClass);
        Spinner capacitySpinner = findViewById(R.id.capacity);
        TextView startTimeView = findViewById(R.id.startTimeView);
        TextView endTimeView = findViewById(R.id.endTimeView);

        // Validate Course Title
        if (courseTitleEditText.getText().toString().trim().isEmpty()) {
            courseTitleEditText.setError("Please enter a course title");
            courseTitleEditText.requestFocus();
            return false;
        }

        // Validate Day of Week
        if (daySpinner.getSelectedItem().toString().equals("--select day of class--")) {
            Toast.makeText(this, "Please select a day for the class", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Price
        if (priceSpinner.getSelectedItem().toString().equals("--select price of class--")) {
            Toast.makeText(this, "Please select a price for the class", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Type of Class
        if (typeSpinner.getSelectedItem().toString().equals("--select type of class--")) {
            Toast.makeText(this, "Please select a type of class", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Capacity
        if (capacitySpinner.getSelectedItem().toString().equals("--select capacity of class--")) {
            Toast.makeText(this, "Please select a capacity for the class", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate Start Time
        if (startTimeView.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select a start time for the class", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate End Time
        if (endTimeView.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select an end time for the class", Toast.LENGTH_SHORT).show();
            return false;
        }

        // All fields are valid
        return true;
    }


    private void saveCourseData() {
        if (!validateFields()) {
            // Invalid fields, do not proceed with saving data
            return;
        }

        // Get data from input fields
        String courseTitle = ((EditText) findViewById(R.id.courseTitle)).getText().toString().trim();
        String day = ((Spinner) findViewById(R.id.daysOfWeek)).getSelectedItem().toString();
        String price = ((Spinner) findViewById(R.id.price)).getSelectedItem().toString();
        String type = ((Spinner) findViewById(R.id.typeOfClass)).getSelectedItem().toString();
        String capacity = ((Spinner) findViewById(R.id.capacity)).getSelectedItem().toString();
        String startTime = ((TextView) findViewById(R.id.startTimeView)).getText().toString().trim();
        String endTime = ((TextView) findViewById(R.id.endTimeView)).getText().toString().trim();
        String description = ((EditText) findViewById(R.id.description)).getText().toString().trim();

        // Save data to the database
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_COURSE_TITLE, courseTitle);
        values.put(DBHelper.COLUMN_DAY, day);
        values.put(DBHelper.COLUMN_PRICE, price);
        values.put(DBHelper.COLUMN_TYPE, type);
        values.put(DBHelper.COLUMN_CAPACITY, capacity);
        values.put(DBHelper.COLUMN_START_TIME, startTime);
        values.put(DBHelper.COLUMN_END_TIME, endTime);
        values.put(DBHelper.COLUMN_DESCRIPTION, description);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DBHelper.TABLE_COURSE, null, values);

        // Check if data insertion was successful
        if (newRowId == -1) {
            Toast.makeText(this, "Error saving course", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "New course added successfully", Toast.LENGTH_SHORT).show();
        }

        // Close the database helper
        dbHelper.close();

        // Clear the input fields
        resetFormFields();
    }

    private void resetFormFields() {
        ((EditText) findViewById(R.id.courseTitle)).setText("");
        ((Spinner) findViewById(R.id.daysOfWeek)).setSelection(0);
        ((Spinner) findViewById(R.id.price)).setSelection(0);
        ((Spinner) findViewById(R.id.typeOfClass)).setSelection(0);
        ((Spinner) findViewById(R.id.capacity)).setSelection(0);
        ((TextView) findViewById(R.id.startTimeView)).setText("");
        ((TextView) findViewById(R.id.endTimeView)).setText("");
        ((EditText) findViewById(R.id.description)).setText("");
        ((TextView) findViewById(R.id.durationOfClass)).setText(""); // Assuming this is the TextView for duration
    }
}