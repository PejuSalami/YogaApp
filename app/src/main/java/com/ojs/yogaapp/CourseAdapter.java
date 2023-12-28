package com.ojs.yogaapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

// CustomAdapter.java
public class CourseAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> courseList;
    private DBHelper dbHelper;


    public CourseAdapter(Context context, List<String> courseList) {
        super(context, R.layout.list_item_layout, courseList);
        this.context = context;
        this.courseList = courseList;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.list_item_layout, parent, false);


        TextView courseNameTextView = rowView.findViewById(R.id.courseNameTextView);
        ImageView editIcon = rowView.findViewById(R.id.editIcon);
        ImageView deleteIcon = rowView.findViewById(R.id.deleteIcon);

        String currentCourse = courseList.get(position);




        // Set course name to the TextView
        courseNameTextView.setText(currentCourse);

        // Set position as a tag to deleteIcon
        deleteIcon.setTag(position);


        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected course directly using the currentCourse variable
            String selectedCourse = currentCourse;


                Log.d("SelectedCourse", selectedCourse); // Add this line

                // Create an intent to open the edit form
                Intent editIntent = new Intent(context, NewCourseForm.class);

                // Pass the selected course data to the edit form
                editIntent.putExtra("editMode", true);
                editIntent.putExtra("courseTitle", selectedCourse);

                // Start the edit form activity
                context.startActivity(editIntent);
            }
        });








        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();

                Log.d("DeleteIconClick", "Clicked on delete icon at position: " + position);

                // Get the primary key (_id) of the item from the list
                DBHelper dbHelper = new DBHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Assuming your _id is in the first column
                Cursor cursor = db.query(
                        DBHelper.TABLE_COURSE,
                        new String[]{"_id"},
                        null,
                        null,
                        null,
                        null,
                        null
                );

                // Move the cursor to the desired position
                if (cursor.moveToPosition(position)) {
                    // Retrieve the _id from the cursor
                    int columnIndexId = cursor.getColumnIndexOrThrow("_id");
                    String[] whereArgs = {String.valueOf(cursor.getLong(columnIndexId))};

                    // Delete the record from the courses table
                    int deletedRows = db.delete(DBHelper.TABLE_COURSE, "_id=?", whereArgs);

                    if (deletedRows > 0) {
                        // If deletion is successful, remove the item from the list
                        courseList.remove(position);

                        // Notify the adapter that the data set has changed
                        notifyDataSetChanged();

                        // Show a Toast message indicating successful deletion
                        Toast.makeText(context, "Record  deleted", Toast.LENGTH_SHORT).show();

                        Log.d("DeleteIconClick", "Item removed from the list and record deleted from the database");
                    } else {
                        // Show a Toast message indicating failure to delete
                        Toast.makeText(context, "Failed to delete record", Toast.LENGTH_SHORT).show();

                        Log.d("DeleteIconClick", "Failed to delete record from the database");
                    }
                } else {
                    Log.d("DeleteIconClick", "Cursor moveToPosition failed");
                }

                // Close the cursor
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

                // Close the database
                db.close();
            }
        });


        return rowView;
    }


}
