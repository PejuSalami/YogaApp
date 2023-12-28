package com.ojs.yogaapp;

// Import statements for Android components and Java utilities
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

// Class definition for a new form in a Yoga application
public class NewClassForm extends AppCompatActivity {
    // Declare UI elements and a database helper
    private ListView dateListView;
    private TextView dateView;
    private EditText teacherEditText;
    private EditText commentsEditText;
    private DBHelper dbHelper;
    private String selectedCourseTitle = "";
    private String selectedDate = "";

    // Override the onCreate method to initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for the activity
        setContentView(R.layout.activity_new_class_form);

        // Initialize UI elements by finding them in the layout
        dateView = findViewById(R.id.date_view);
        teacherEditText = findViewById(R.id.select_teacher);
        commentsEditText = findViewById(R.id.comments);
        dateListView = findViewById(R.id.date_list);
        // Initialize database helper
        dbHelper = new DBHelper(this);

        // Populate the course titles in the UI
        populateCourseTitles();

        // Set up a listener for the Save button
        Button saveButton = findViewById(R.id.save_class_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to save class instance when button is clicked
                saveClassInstance();
            }
        });
    }

    // Method to populate course titles in a spinner
    private void populateCourseTitles() {
        // Find the spinner in the layout and get course titles from the database
        Spinner courseTitleSpinner = findViewById(R.id.select_course);
        List<String> courseTitles = dbHelper.getAllCourseTitles();

        // Set up an ArrayAdapter to populate the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseTitleSpinner.setAdapter(adapter);

        // Set up a listener for when an item is selected in the spinner
        courseTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When a course is selected, update the list of valid dates
                if (position > 0) {
                    selectedCourseTitle = courseTitles.get(position);
                    String selectedDayOfWeek = dbHelper.getDayOfWeekByCourseTitle(selectedCourseTitle);
                    List<String> validDates = dbHelper.getValidDatesForCourse(selectedDayOfWeek);
                    ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(NewClassForm.this, android.R.layout.simple_list_item_1, validDates);
                    dateListView.setAdapter(dateAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Required method but not used
            }
        });

        // Set up a listener for when an item is clicked in the list of dates
        dateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Update the selected date when an item is clicked
                selectedDate = (String) parent.getItemAtPosition(position);
                updateDate(selectedDate);
            }
        });
    }

    // Method to update the displayed date
    private void updateDate(String date) {
        dateView.setText(date);
    }

    // Method to save a class instance
    private void saveClassInstance() {
        // Validate input fields before saving
        if (!validateFields()) {
            return;
        }

        // Get text from input fields
        String teacher = teacherEditText.getText().toString();
        String comments = commentsEditText.getText().toString();
        // Get course ID from the database
        int courseId = dbHelper.getCourseIdByTitle(selectedCourseTitle);

        // Insert class instance into the database and show a toast message based on success or failure
        if (dbHelper.insertClassInstance(selectedDate, teacher, comments, courseId)) {
            Toast.makeText(this, "Class instance added successfully", Toast.LENGTH_SHORT).show();
            // Reset the form fields after saving
            resetFormFields();
        } else {
            Toast.makeText(this, "Failed to add class instance", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to validate input fields before saving
    private boolean validateFields() {
        String teacher = teacherEditText.getText().toString().trim();

        // Check if course title or teacher's name is not selected or empty
        if (selectedCourseTitle.isEmpty() || selectedCourseTitle.equals("--select course--")) {
            Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (teacher.isEmpty()) {
            teacherEditText.setError("Please enter a teacher's name");
            teacherEditText.requestFocus();
            return false;
        }

        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    // Method to reset the form fields to their default states
    private void resetFormFields() {
        teacherEditText.setText("");
        commentsEditText.setText("");
        selectedCourseTitle = "";
        selectedDate = "";
        dateView.setText("--select date of class--");
        // Reset the course spinner to the default selection
        Spinner courseTitleSpinner = findViewById(R.id.select_course);
        courseTitleSpinner.setSelection(0);
    }
}
