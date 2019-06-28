package com.mataprojects.androidcourse_firebaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mataprojects.androidcourse_firebaseproject.Model.DataDatabase;

import java.text.DateFormat;
import java.util.Date;



public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingAddButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private String title;
    private String description;
    private String budget;
    private String post_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Firebase Complete project");
        setSupportActionBar(toolbar);

        //recycler view
        recyclerView = findViewById(R.id.recyclerId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();
        String userId = currentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllData").child(userId);




        //floating button+
        floatingAddButton = findViewById(R.id.floatingAddButton);
        floatingAddButton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });


    }
    public void addData(){
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_layout,null);
        myDialog.setView(myView);
        final AlertDialog dialog  = myDialog.create();

        final EditText mTitle = myView.findViewById(R.id.title);
        final EditText mDescription = myView.findViewById(R.id.description);
        Button mSaveButton =  myView.findViewById(R.id.saveButton);
        final EditText mBudget =  myView.findViewById(R.id.budget);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString().trim();
                String description = mDescription.getText().toString().trim();
                String budget = mBudget.getText().toString().trim();

                String mDate = DateFormat.getDateInstance().format(new Date());
                String id = databaseReference.push().getKey();

                DataDatabase dataDatabase = new DataDatabase(title,description,budget,id,mDate);
                databaseReference.child(id).setValue(dataDatabase);

                Toast.makeText(getApplicationContext(),"Data Inserted ",Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<DataDatabase,myViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<DataDatabase, myViewHolder>
                (
                        DataDatabase.class,
                        R.layout.dataitem,
                        myViewHolder.class,
                        databaseReference
                ) {
            @Override
            protected void populateViewHolder(myViewHolder viewHolder, final DataDatabase model, final int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setBudget(model.getBudget());
                viewHolder.setDescription(model.getDescription());
                viewHolder.setDate(model.getDate());

                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(position).getKey();
                        title = model.getTitle();
                        description= model.getDescription();
                        budget = model.getBudget();
                        updateData();


                    }
                });

            }
        };
    recyclerView.setAdapter(recyclerAdapter);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        View myView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }
        public void setTitle(String title){
            TextView mTitle= myView.findViewById(R.id.title_Item);
            mTitle.setText(title);
        }
        public void setDescription(String description){
            TextView mDescription = myView.findViewById(R.id.description_Item);
            mDescription.setText(description);
        }
        public void setBudget(String budget){
            TextView mBudget = myView.findViewById(R.id.budget_Item);
            mBudget.setText("$ "+budget);
        }
        public void setDate(String date){
            TextView mDate = myView.findViewById(R.id.date_Item);
            mDate.setText(date);
        }


    }

    public void updateData(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.update_layout,null);
        dialogBuilder.setView(myView);
        final AlertDialog alertDialog = dialogBuilder.create();


        final EditText mTitle = myView.findViewById(R.id.title_update);
        final EditText mDescription = myView.findViewById(R.id.description_update);;
        final EditText mBudget =myView.findViewById(R.id.budget_update);
        Button updateButton = myView.findViewById(R.id.updateButton_update);
        Button deleteButton =myView.findViewById(R.id.deleteButton_update);

        //set data inside edit
        mTitle.setText(title);
        mTitle.setSelection(title.length());

        mDescription.setText(description);
        mDescription.setSelection(description.length());

        mBudget.setText(budget);
        mBudget.setSelection(budget.length());


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = mTitle.getText().toString().trim();
                description = mDescription.getText().toString().trim();
                budget = mBudget.getText().toString().trim();
                String mDate = DateFormat.getDateInstance().format(new Date());
                DataDatabase data = new DataDatabase(title,description,budget,post_key,mDate);
                databaseReference.child(post_key).setValue(data);
                alertDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(post_key).removeValue();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutItem:
                firebaseAuth.signOut();
                startActivity(new Intent(getApplication(),MainActivity.class));
        }

        return super.onOptionsItemSelected(item);

    }
}
