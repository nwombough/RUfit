package edu.rowanuniversity.rufit;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import edu.rowanuniversity.rufit.rufitObjects.RunInfo;

/**
 * Created by Naomi on 3/28/2017.
 *
 * Allows a user to manually enter a previous run.
 */

public class AddRunActivity extends AppCompatActivity {

    private TextView distanceText, timeText, paceText, paceDisplay, dateText, typeText, feelText, notesText;
    private EditText editDistance, editTime, dateEdit, notesEdit;
    private Spinner typeSpinner;
    private SeekBar seekBar;
    private Button submit;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private String userID;
    private String runID;
    final Context context = this;
    private RunInfo run;

    protected void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.add_run_activity);

        Intent intent = getIntent();

        Toolbar t = (Toolbar) findViewById(R.id.topToolBar);
        setSupportActionBar(t);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        distanceText = (TextView) findViewById(R.id.distanceText);
        timeText = (TextView) findViewById(R.id.timeText);
        paceText = (TextView) findViewById(R.id.paceText);
        paceDisplay = (TextView) findViewById(R.id.paceDisplay);
        dateText = (TextView) findViewById(R.id.dateText);
        typeText = (TextView) findViewById(R.id.typeText);
        feelText = (TextView) findViewById(R.id.feelText);
        notesText = (TextView) findViewById(R.id.notesText);

        editDistance = (EditText) findViewById(R.id.editDistance);
        editTime = (EditText) findViewById(R.id.editTime);
        dateEdit = (EditText) findViewById(R.id.dateEdit);
        notesEdit = (EditText) findViewById(R.id.notesEdit);

        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        submit = (Button) findViewById(R.id.submit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        //database instance
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //retrieve current user
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userID = user.getUid();

        editDistance.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!editTime.getText().toString().equals("") ||
                        !editTime.getText().toString().equals(null)) {
                    float distance = Float.parseFloat(editDistance.getText().toString());
                    int time = Integer.parseInt(editTime.getText().toString());
                    float pace = time/distance;
                    paceDisplay.setText(Float.toString(pace));
                }
                return false;
            }
        });

        editTime.setOnEditorActionListener(new EditText.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(!editDistance.getText().toString().equals("") ||
                        !editDistance.getText().toString().equals(null)) {
                    float distance = Float.parseFloat(editDistance.getText().toString());
                    int time = Integer.parseInt(editTime.getText().toString());
                    float pace = time/distance;
                    paceDisplay.setText(Float.toString(pace));
                }
                return false;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When submit button clicked, update user's changes to database
                float distance = Float.parseFloat(editDistance.getText().toString());
                int time = Integer.parseInt(editTime.getText().toString());
                float pace = (float) time/distance;
                String date = dateEdit.getText().toString();
                int feel = seekBar.getProgress();
                String type = typeSpinner.getSelectedItem().toString();
                String notes = notesEdit.getText().toString();
                myRef.child("users").child(userID).child("runs").setValue(new RunInfo(distance,pace,time,date,
                        feel,type,notes));
                leaveActivity();
            }

        });

    }

    public void leaveActivity() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
    }

    /*
     * This method updates text displays of user's personal information whenever it
     * reads a change in database reference
     * @param dataSnapshot 'snapshot' of entire database
     */
    private void showData(DataSnapshot dataSnapshot) {
        //update text displays
        editDistance.setText("Enter distance in miles");
        editTime.setText("Enter time in minutes");
        paceDisplay.setText("Enter distance and time");
        dateEdit.setText("Enter date");
        seekBar.setProgress(20);
        notesEdit.setText("Enter notes...");
    }
}
