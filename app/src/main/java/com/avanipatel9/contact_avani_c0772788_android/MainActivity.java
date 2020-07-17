package com.avanipatel9.contact_avani_c0772788_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText fname,lname,phone,address, email;
    ContactDatabaseHelper contactDatabaseHelper;
    TextView tv;
    ArrayList<String> number;
    boolean isSame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Add Contact");

        fname = findViewById(R.id.etFname);
        lname = findViewById(R.id.etLname);
        phone = findViewById(R.id.etPhone);
        address = findViewById(R.id.etAddress);
        email = findViewById(R.id.etEmail);

        findViewById(R.id.btnAddPerson).setOnClickListener(this);

        contactDatabaseHelper = new ContactDatabaseHelper(this);
        loadData();

    }

    private int loadData() {

        Cursor c = contactDatabaseHelper.getAllData();
        number = new ArrayList<>();
        if (c.moveToFirst()){


            do {
                number.add(c.getString(3));

            }while (c.moveToNext());
            c.close();

        }
        return c.getCount();


    }

    @Override
    protected void onStart() {
        super.onStart();
        fname.setText("");
        lname.setText("");
        phone.setText("");
        address.setText("");
        email.setText("");
        loadData();
    }

    @Override
    public void onClick(View v) {

        String fName = fname.getText().toString().trim();
        String lName = lname.getText().toString().trim();
        String mPhone = phone.getText().toString().trim();
        String mAddress = address.getText().toString().trim();
        String mEmail = email.getText().toString().trim();

        if(fName.isEmpty() && lName.isEmpty() && mPhone.isEmpty() && mEmail.isEmpty() && mAddress.isEmpty()) {
            fname.setError("Please enter First Name");
            fname.requestFocus();
            lname.setError("Please enter Last Name");
            lname.requestFocus();
            phone.setError("Please enter Phone number");
            phone.requestFocus();
            email.setError("Please enter Email");
            email.requestFocus();
            address.setError("Please enter address");
            address.requestFocus();
            return;
        }
        else if (fName.isEmpty()){
            fname.setError("Please enter First Name");
            fname.requestFocus();
            return;
        }
        else if (lName.isEmpty()){
            lname.setError("Please enter Last Name");
            lname.requestFocus();
            return;
        }
         else if (mPhone.isEmpty()){
            phone.setError("Please enter Phone number");
            phone.requestFocus();
            return;
        }
        else if (mEmail.isEmpty()){
            email.setError("Please enter Email");
            email.requestFocus();
            return;
        }
        else if (mAddress.isEmpty()){
            address.setError("Please enter address");
            address.requestFocus();
            return;
        }
        else {
            addPersonData();
            Intent mIntent = new Intent(this, ShowContactsActivity.class);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent);
        }
    }

    private void addPersonData() {

        String fName = fname.getText().toString().trim();
        String lName = lname.getText().toString().trim();
        String mPhone = phone.getText().toString().trim();
        String mAddress = address.getText().toString().trim();
        String mEmail = email.getText().toString().trim();

        for (int i =0;i<number.size();i++){
            if (number.contains(mPhone)){
                isSame = true;
            }
            else {
                isSame = false;
            }
        }
        if (!isSame){

            if (contactDatabaseHelper.addContact(fName,lName,mPhone,mAddress,mEmail)){
                Toast.makeText(this, "Person added", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Person  not added", Toast.LENGTH_SHORT).show();
            }
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Phone Number Already Exist");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }
}