package com.avanipatel9.contact_avani_c0772788_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ShowContactsActivity extends AppCompatActivity {

    ContactDatabaseHelper contactDatabaseHelper;
    EditText serchText;
    List<Person> personList;

    ListView listView;
    PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts);
        getSupportActionBar().setTitle("Contacts");

        FloatingActionButton fab = findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowContactsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        contactDatabaseHelper = new ContactDatabaseHelper(this);
        listView = findViewById(R.id.lvUser);
        serchText = findViewById(R.id.searchView);
        personList = new ArrayList<>();
        loadData();

        personAdapter = new PersonAdapter(this, R.layout.cell_contact, personList, contactDatabaseHelper);
        listView.setTextFilterEnabled(true);
        listView.setAdapter(personAdapter);

        serchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String text = serchText.getText().toString();
                (personAdapter).filter(text);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadData() {


        Cursor cursor = contactDatabaseHelper.getAllData();
        if (cursor.moveToFirst()){

            do {
                personList.add(new Person(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));


            }while (cursor.moveToNext());

            cursor.close();
        }
    }
}