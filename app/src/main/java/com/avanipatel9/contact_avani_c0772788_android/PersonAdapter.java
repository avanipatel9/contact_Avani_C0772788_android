package com.avanipatel9.contact_avani_c0772788_android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonAdapter extends ArrayAdapter {

    Context mContext;
    int layoutRes;
    List<Person> personList;
    ContactDatabaseHelper contactDatabaseHelper;
    ArrayList<Person> arraylist;

    public PersonAdapter(@NonNull Context mContext, int layoutRes, List<Person> personList, ContactDatabaseHelper contactDatabaseHelper) {
        super(mContext, layoutRes,personList);
        this.mContext = mContext;
        this.layoutRes = layoutRes;
        this.personList = personList;
        this.arraylist = new ArrayList<Person>();
        this.arraylist.addAll(personList);
        this.contactDatabaseHelper = contactDatabaseHelper;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(layoutRes,null);
        TextView tvname = v.findViewById(R.id.tv_Fname);
        TextView tvdept = v.findViewById(R.id.tv_Phone);
        TextView tvjoinDate = v.findViewById(R.id.tv_Address);
        TextView tvEmail = v.findViewById(R.id.tv_Email);

        final Person person = personList.get(position);
        tvname.setText(person.getFname().toUpperCase() + "  " + person.getLname().toUpperCase());
        //tvsalary.setText(person.getLname().toUpperCase());
        tvdept.setText(person.getPhone());
        tvjoinDate.setText(person.getAddress());
        tvEmail.setText(person.getEmail());
        v.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee(person);
            }
        });

        v.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee(person);
            }
        });

        return  v;

    }

    private void deleteEmployee(final Person person) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if( contactDatabaseHelper.deleteContact(person.getId())){
                    loadData();
                }


            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateEmployee(final Person person) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Update Person");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View customLayout = inflater.inflate(R.layout.update_contact, null);
        builder.setView(customLayout);

        final EditText updateFName = customLayout.findViewById(R.id.update_Fname);
        final EditText updateLName = customLayout.findViewById(R.id.update_Lname);
        final EditText updatePhone = customLayout.findViewById(R.id.update_phone);
        final EditText updateAddress = customLayout.findViewById(R.id.update_Address);
        final EditText updateEmail = customLayout.findViewById(R.id.update_email);

        updateFName.setText(person.getFname());
        updateLName.setText(person.getLname());
        updatePhone.setText(person.getPhone());
        updateAddress.setText(person.getAddress());
        updateEmail.setText(person.getEmail());


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        customLayout.findViewById(R.id.btn_update_emp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = updateFName.getText().toString().trim();
                String lname = updateLName.getText().toString().trim();

                String phone = updatePhone.getText().toString().trim();
                String address = updateAddress.getText().toString().trim();
                String email = updateEmail.getText().toString().trim();

                if (fname.isEmpty()){
                    updateFName.setError("Please enter First Name");
                    updateFName.requestFocus();
                    return;

                }
                if (lname.isEmpty()){
                    updateLName.setError("Please enter Last Name");
                    updateLName.requestFocus();
                    return;
                }
                if (phone.isEmpty()){
                    updatePhone.setError("Please enter Phone Number");
                    updatePhone.requestFocus();
                    return;
                }
                if (address.isEmpty()){
                    updateAddress.setError("Please enter Address");
                    updateAddress.requestFocus();
                    return;
                }

                if (email.isEmpty()){
                    updateEmail.setError("Please enter Email");
                    updateEmail.requestFocus();
                    return;
                }

                if (contactDatabaseHelper.updateContact(person.getId(),fname,lname,phone,address,email)){
                    Toast.makeText(mContext, "Contact Details Updated", Toast.LENGTH_SHORT).show();
                    loadData();
                }else {
                    Toast.makeText(mContext, "Contact Details not Updated", Toast.LENGTH_SHORT).show();
                }

                alertDialog.dismiss();
            }
        });


    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        personList.clear();
        if (charText.length() == 0) {
            personList.addAll(arraylist);
        } else {
            for (Person person : arraylist) {
                if (person.getPhone().toLowerCase(Locale.getDefault())
                        .contains(charText) || person.getFname().toLowerCase(Locale.getDefault())
                        .contains(charText)|| person.getLname().toLowerCase(Locale.getDefault())
                        .contains(charText) || person.getAddress().toLowerCase(Locale.getDefault())
                        .contains(charText) || person.getEmail().toLowerCase(Locale.getDefault())
                        .contains(charText))
                {
                    personList.add(person);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void loadData() {

        String sql = "SELECT * FROM contact_list";
        Cursor c = contactDatabaseHelper.getAllData();
        personList.clear();
        if (c.moveToFirst()){


            do {
                personList.add(new Person(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4),c.getString(5)));

            }while (c.moveToNext());
            c.close();

        }
        notifyDataSetChanged();
    }
}
