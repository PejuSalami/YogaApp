package com.ojs.yogaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.ojs.yogaapp.DBHelper;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.ojs.yogaapp.NewClassForm;

import java.util.List;

public class ClassFragment extends Fragment {
    private DBHelper dbHelper;
    private String selectedCourseTitle = "";
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class, container, false);
        dbHelper = new DBHelper(getContext());

        // Initialize the ListView
        listView = view.findViewById(R.id.ListView);

        // Populate course titles in the Spinner
        populateCourseTitles(view);



        Button btnAddClass = view.findViewById(R.id.IconAddClass);
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click here
                openNewClassForm();
            }
        });

        // Set a listener for item selection in the Spinner
        Spinner courseTitleSpinner = view.findViewById(R.id.courseSpinner);
        courseTitleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update the selected course title
                selectedCourseTitle = parentView.getItemAtPosition(position).toString();

                // Update the ListView with class instances for the selected course
                updateListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        return view;
    }

    private void populateCourseTitles(View view) {
        Spinner courseTitleSpinners = view.findViewById(R.id.courseSpinner);
        List<String> courseTitles = dbHelper.getAllCourseTitles();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, courseTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseTitleSpinners.setAdapter(adapter);
    }

    private void updateListView() {
        // Get the course ID for the selected course title
        int courseId = dbHelper.getCourseIdByTitle(selectedCourseTitle);

        // Get class instances for the selected course ID
        List<String> classInstances = dbHelper.getClassInstancesByCourseId(courseId);

        // Update the ListView with the class instances
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, classInstances);
        listView.setAdapter(adapter);
    }

    private void openNewClassForm() {
        // Start the NewClassForm activity
        Intent intent = new Intent(getActivity(), NewClassForm.class);
        startActivity(intent);
    }
}
