
package com.ojs.yogaapp;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CourseFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> courseList;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        // Load course table data
        listView = view.findViewById(R.id.listView);
        courseList = new ArrayList<>();
        adapter = new CourseAdapter(requireContext(), courseList); // Use the custom adapter
        listView.setAdapter(adapter);

        // Load and display courses
        loadCourses();

        Button btnUpload = view.findViewById(R.id.upload);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(getContext());
                String jsonPayload = dbHelper.getCourseDataAsJson("os9372y");
                uploadCourseData(jsonPayload);
                Log.d("JSONOutput", jsonPayload);
            }
        });

        Button btnAddCourse = view.findViewById(R.id.IconAddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click here
                openNewCourseForm();
            }
        });







        return view;
    }







// ...

    private void uploadCourseData(String jsonPayload) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                StringBuilder response = new StringBuilder();

                try {
                    URL url = new URL("https://stuiis.cms.gre.ac.uk/COMP1424CoreWS/comp1424cw");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setDoOutput(true);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonPayload.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    Log.d("SOMETHING HERE", "HELLO WORLD");
                    int responseCode = conn.getResponseCode();
                    Log.d("THE RESPONSE CODE", "" + responseCode);
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read the response from the input stream
                        Log.d("ALL IS GOOD", "" + responseCode);
                        try (InputStream is = conn.getInputStream();
                             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                            String responseLine = "";
                            while ((responseLine = br.readLine()) != null) {
                                response.append(responseLine.trim());
                            }
                            Log.d("FINAL RESPONSE", response.toString());
                        }
                    } else {
                        // Handle error
                        response.append("Error response code: ").append(responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.append("Exception: ").append(e.getMessage());
                }
                return response.toString();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                // Update UI based on response
                // For example, you can use a TextView to display the response
            }
        }.execute(jsonPayload);
    }



    private void openNewCourseForm() {
        // Start the NewCourseForm activity
        Intent intent = new Intent(getActivity(), NewCourseForm.class);
        startActivity(intent);

    }

    private void loadCourses() {
        DBHelper dbHelper = new DBHelper(requireContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                DBHelper.COLUMN_DAY,
                DBHelper.COLUMN_PRICE,
                DBHelper.COLUMN_TYPE,
                DBHelper.COLUMN_CAPACITY,
                DBHelper.COLUMN_START_TIME,
                DBHelper.COLUMN_END_TIME,
                DBHelper.COLUMN_DESCRIPTION
        };

        courseList.clear();

        // Query the database to get all rows from the course table
        Cursor cursor = db.query(
                DBHelper.TABLE_COURSE,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // don't sort order
        );

        // Iterate through the cursor and add course details to the list
        while (cursor.moveToNext()) {
            String day = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DAY));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRICE));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TYPE));
            String capacity = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CAPACITY));
            String startTime = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_TIME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DESCRIPTION));

            String courseDetails = String.format(
                    "Day: %s\nPrice: %s\nType: %s\nCapacity: %s\nStart Time: %s\nEnd Time: %s\nDescription: %s\n",
                    day, price, type, capacity, startTime, endTime, description
            );

            courseList.add(courseDetails);
        }

        // Close the cursor and database
        cursor.close();
        dbHelper.close();

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
    }

}