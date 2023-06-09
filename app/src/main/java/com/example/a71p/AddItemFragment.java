package com.example.a71p;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a71p.data.DatabaseHelper;
import com.example.a71p.model.user;
import com.example.a71p.util.Util;


public class AddItemFragment extends Fragment {
    private RadioGroup post_type;
    private RadioButton RadioButtonlost;
    private RadioButton RadioButtonfound;
    private EditText EditTextId;
    private EditText EditTextName;
    private EditText EditTextPhonenumber;
    private EditText EditTextTitle;
    private EditText EditTextDescription;
    private EditText EditTextDate;
    private EditText EditTextLocation;
    private Button insertButton;
    private Button deleteButton;
    private Button fetchButton;
    private Button updateButton;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        //databaseHelper = new DatabaseHelper(getContext());

        post_type = view.findViewById(R.id.post_type_radio_group);
        EditTextId = view.findViewById(R.id.id_edit_text);
        EditTextName = view.findViewById(R.id.name_edit_text);
        EditTextPhonenumber = view.findViewById(R.id.phone_number_edit_text);
        EditTextTitle = view.findViewById(R.id.title_edit_text);
        EditTextDescription = view.findViewById(R.id.description_edit_text);
        EditTextDate = view.findViewById(R.id.date_edit_text);
        EditTextLocation = view.findViewById(R.id.location_edit_text);
        insertButton = view.findViewById(R.id.insert_button);
        deleteButton = view.findViewById(R.id.delete_button);
        fetchButton = view.findViewById(R.id.fetch_button);
        updateButton = view.findViewById(R.id.update_button);

        //databaseHelper = new DatabaseHelper(this, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        databaseHelper = new DatabaseHelper(getContext());

        RadioButtonlost = view.findViewById(R.id.lost_radio_button);
        RadioButtonfound = view.findViewById(R.id.found_radio_button);

        post_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lost_radio_button) {
                    // Handle lost radio button selection
                } else if (checkedId == R.id.found_radio_button) {
                    // Handle found radio button selection
                }
            }
        });



        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
            }
        });

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchUser();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upgradeUser();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
        return view;
    }
    private void insertUser() {
        String post_type = RadioButtonlost.getText().toString().trim();
        String name = EditTextName.getText().toString().trim();
        String phoneNumber = EditTextPhonenumber.getText().toString().trim();
        String item_title = EditTextTitle.getText().toString().trim();
        String description = EditTextDescription.getText().toString().trim();
        String date = EditTextDate.getText().toString().trim();
        String location = EditTextLocation.getText().toString().trim();

        if (name.isEmpty() || phoneNumber.isEmpty() || description.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        user newUser = new user(post_type, name, phoneNumber, item_title, description, date, location);
        long newRowId = databaseHelper.insertUser(newUser);

        if (newRowId != -1) {
            Toast.makeText(getContext(), "User inserted successfully", Toast.LENGTH_SHORT).show();
            clearInputFields();
        } else {
            Toast.makeText(getContext(), "Failed to insert user", Toast.LENGTH_SHORT).show();
        }
    }


    //private void fetchUser() {
        // Pass an empty search query for fetching all users
      //  String searchQuery = "";
        //Cursor cursor = databaseHelper.fetchUserList(searchQuery);
        //if (cursor.getCount() == 0) {
          //  Log.d("TAG", "No data found");
            //return;
        //}
        //while (cursor.moveToNext()) {
          //  int id = cursor.getInt(0);
            //String name = cursor.getString(1);
            //String phoneNumber = cursor.getString(2);
            //String description = cursor.getString(3);
            //Log.d("TAG", "id: " + id + " name: " + name + " phoneNumber: " + phoneNumber + " description: " + description);
        //}
    //}

    private void fetchUser() {
        String name = EditTextName.getText().toString().trim();
        String phoneNumber = EditTextPhonenumber.getText().toString().trim();

        boolean userExists = databaseHelper.fetchUser(name, phoneNumber);
        if (userExists) {
            Toast.makeText(getContext(), "User exists in the database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "User does not exist in the database", Toast.LENGTH_SHORT).show();
        }
    }




    private void upgradeUser() {
        // Get the updated user information from the input fields
        String idString = EditTextId.getText().toString();
        String post_type = RadioButtonlost.getText().toString();
        String name = EditTextName.getText().toString();
        String phoneNumber = EditTextPhonenumber.getText().toString();
        String title = EditTextTitle.getText().toString();
        String description = EditTextDescription.getText().toString();
        String date = EditTextDate.getText().toString();
        String location = EditTextLocation.getText().toString();

        // Validate that the user has entered an ID
        if (idString.isEmpty()) {
            Toast.makeText(getContext(), "Please enter an ID to update", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the user ID
        int id = Integer.parseInt(idString);

        // Create a ContentValues object to store the updated values
        ContentValues values = new ContentValues();
        values.put(Util.NAME, name);
        values.put(Util.PHONENUMBER, phoneNumber);
        values.put(Util.DESCRIPTION, description);

        // Update the user data in the database
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int rowsAffected = database.update(Util.TABLE_NAME, values, Util.USER_ID + " = ?", new String[] {String.valueOf(id)});

        // Check if the update was successful
        if (rowsAffected > 0) {
            Toast.makeText(getContext(), "User updated successfully", Toast.LENGTH_SHORT).show();
            // Clear the input fields after successful update
            clearInputFields();
        } else {
            Toast.makeText(getContext(), "Failed to update user", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteUser() {
        String name = EditTextName.getText().toString();
        String phoneNumber = EditTextPhonenumber.getText().toString();

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int deletedRows = database.delete(Util.TABLE_NAME, Util.NAME + "=? AND " + Util.PHONENUMBER + "=?", new String[]{name, phoneNumber});

        if (deletedRows > 0) {
            Toast.makeText(getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
            // Clear the input fields after successful deletion
            clearInputFields();
        } else {
            Toast.makeText(getContext(), "Failed to delete user", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearInputFields() {
        EditTextName.setText("");
        EditTextPhonenumber.setText("");
        EditTextTitle.setText("");
        EditTextDescription.setText("");
        EditTextDate.setText("");
        EditTextLocation.setText("");
    }
}
