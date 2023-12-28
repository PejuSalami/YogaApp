package com.ojs.yogaapp;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONObject;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    public static final String DBNAME = "Admin.db";
    // Define table name
    public static final String TABLE_COURSE = "courses";


    // Define column names
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_COURSE_TITLE = "course_title";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CAPACITY = "capacity";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String TABLE_CLASS_INSTANCES = "class_instances";
    public static final String COLUMN_INSTANCE_DATE = "date";
    public static final String COLUMN_INSTANCE_TEACHER = "teacher";
    public static final String COLUMN_INSTANCE_COMMENTS = "comments";
    public static final String COLUMN_COURSE_ID = "course_id";


    public DBHelper(Context context) {
        super(context, "Admin.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(name TEXT, username TEXT primary key, password TEXT)");
        String createTableQuery = "CREATE TABLE " + TABLE_COURSE + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_TITLE + " TEXT, " +
                COLUMN_DAY + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_CAPACITY + " TEXT, " +
                COLUMN_START_TIME + " TEXT, " +
                COLUMN_END_TIME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT)";
        MyDB.execSQL(createTableQuery);

        // Create a table for class instances
        String createClassInstanceTable = "CREATE TABLE " + TABLE_CLASS_INSTANCES + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_INSTANCE_DATE + " TEXT, " +
                COLUMN_INSTANCE_TEACHER + " TEXT, " +
                COLUMN_INSTANCE_COMMENTS + " TEXT, " +
                COLUMN_COURSE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_COURSE + "(_id))";
        MyDB.execSQL(createClassInstanceTable);
    }



    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("DROP TABLE IF EXISTS courses");
        onCreate(MyDB);

    }
    public Boolean insertData (String name, String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if (result==-1) return false;
        else
            return true;

    }
    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?",new String[] {username,password});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }


    public Boolean insertCourseData(String courseTitle, String day, String price, String type, String capacity,
                                    String startTime, String endTime, String description) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("course_title", courseTitle);
        contentValues.put("day", day);
        contentValues.put("price", price);
        contentValues.put("type", type);
        contentValues.put("capacity", capacity);
        contentValues.put("start_time", startTime);
        contentValues.put("end_time", endTime);
        contentValues.put("description", description);

        long result = MyDB.insert("courses", null, contentValues);
        return result != -1;
    }

    public Boolean insertClassInstance(String date, String teacher, String comments, int courseId) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("teacher", teacher);
        contentValues.put("comments", comments);
        contentValues.put("course_id", courseId);

        long result = MyDB.insert(TABLE_CLASS_INSTANCES, null, contentValues);
        return result != -1;
    }

    @SuppressLint("Range")
    public String getDayOfWeekByCourseTitle(String courseTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_DAY + " FROM " + TABLE_COURSE + " WHERE " + COLUMN_COURSE_TITLE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{courseTitle});

        String dayOfWeek = "";
        if (cursor.moveToFirst()) {
            dayOfWeek = cursor.getString(cursor.getColumnIndex(COLUMN_DAY));
        }
        cursor.close();
        db.close();
        return dayOfWeek;
    }
    public List<String> getValidDatesForCourse(String dayOfWeek) {
        List<String> validDates = new ArrayList();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(2); // Adjust the duration as needed

        while (startDate.isBefore(endDate)) {
            if (startDate.getDayOfWeek().toString().equalsIgnoreCase(dayOfWeek)) {
                validDates.add(startDate.toString());
            }
            startDate = startDate.plusDays(1);
        }
        return validDates;
    }


    public List<String> getAllCourseTitles() {
        List<String> courseTitles = new ArrayList<>();
        // Add placeholder item at the beginning
        courseTitles.add("--select course--");

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_COURSE_TITLE + " FROM " + TABLE_COURSE;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_TITLE));
                courseTitles.add(title);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return courseTitles;
    }
    /* public ArrayList<String> getUsers() {
         ArrayList<String> List = new ArrayList<>();
         SQLiteDatabase db = this.getReadableDatabase();
         String query = "SELECT * FROM " + TABLE_COURSE + " WHERE " + COLUMN_COURSE_ID + " = ?";
         Cursor cursor = db.query(getAllCourseTitles());

         return null;
     }*/
    public ArrayList<String> getUsers() {
        ArrayList<String> courseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve data from
        String[] projection = {COLUMN_COURSE_ID, /* add other column names here */};

        // You need to specify the table name and the columns you want to retrieve
        Cursor cursor = db.query(TABLE_COURSE, projection, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve data from the cursor and add it to the list
                String courseTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TITLE));
                courseList.add(courseTitle);
            } while (cursor.moveToNext());

            // Close the cursor after retrieving data
            cursor.close();
        }

        // Return the list of users
        return courseList;
    }



    public List<String> getClassInstancesByCourseId(int courseId) {
        List<String> instances = new ArrayList<>();
        SQLiteDatabase MyDB = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CLASS_INSTANCES + " WHERE " + COLUMN_COURSE_ID + " = ?";
        Cursor cursor = MyDB.rawQuery(query, new String[]{String.valueOf(courseId)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_INSTANCE_DATE));
                @SuppressLint("Range") String teacher = cursor.getString(cursor.getColumnIndex(COLUMN_INSTANCE_TEACHER));
                @SuppressLint("Range") String comments = cursor.getString(cursor.getColumnIndex(COLUMN_INSTANCE_COMMENTS));
                instances.add(date + " - " + teacher + (comments.isEmpty() ? "" : " - " + comments));
            } while (cursor.moveToNext());
        }
        cursor.close();
        MyDB.close();
        return instances;
    }


    @SuppressLint("Range")
    public String getCourseDataAsJson(String userId) {
        JSONArray detailList = new JSONArray();

        // Query to get courses
        String courseQuery = "SELECT * FROM " + TABLE_COURSE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor courseCursor = db.rawQuery(courseQuery, null);

        try {
            while (courseCursor.moveToNext()) {
                JSONObject courseJson = new JSONObject();
                @SuppressLint("Range") int courseId = courseCursor.getInt(courseCursor.getColumnIndex("_id"));
                courseJson.put("dayOfWeek", courseCursor.getString(courseCursor.getColumnIndex(COLUMN_DAY)));
                courseJson.put("timeOfDay", courseCursor.getString(courseCursor.getColumnIndex(COLUMN_START_TIME)));

                // Query to get class instances for each course
                JSONArray classList = new JSONArray();
                String classInstanceQuery = "SELECT * FROM " + TABLE_CLASS_INSTANCES + " WHERE " + COLUMN_COURSE_ID + " = ?";
                Cursor classCursor = db.rawQuery(classInstanceQuery, new String[]{String.valueOf(courseId)});

                while (classCursor.moveToNext()) {
                    JSONObject classJson = new JSONObject();
                    classJson.put("date", classCursor.getString(classCursor.getColumnIndex(COLUMN_INSTANCE_DATE)));
                    classJson.put("teacher", classCursor.getString(classCursor.getColumnIndex(COLUMN_INSTANCE_TEACHER)));
                    // Add other class instance details as needed
                    classList.put(classJson);
                }
                classCursor.close();

                courseJson.put("classList", classList);
                // Add other course details as needed
                detailList.put(courseJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        } finally {
            courseCursor.close();
            db.close();
        }

        JSONObject finalJson = new JSONObject();
        try {
            finalJson.put("userId", userId);
            finalJson.put("detailList", detailList);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }

        return finalJson.toString();
    }


    @SuppressLint("Range")
    public int getCourseIdByTitle(String courseTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT _id FROM " + TABLE_COURSE + " WHERE " + COLUMN_COURSE_TITLE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{courseTitle});

        int courseId = -1;
        if (cursor.moveToFirst()) {
            courseId = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        cursor.close();
        db.close();
        return courseId;
    }




}