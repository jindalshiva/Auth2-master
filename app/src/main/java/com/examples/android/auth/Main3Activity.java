package com.examples.android.auth;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
    EditText startTime,endTime;
    Button showTime;
    private TimePickerDialog timePickerDialog;
    int hour;
    int min;
    Calendar c;


    private static final String ARTIST_ID = "com.examples.android.auth.appname";
    private static final String ARTIST_NAME = "com.examples.android.auth.applicationid";
    Spinner spinnerItems, spinnerPlaces;
    Button buttonAdd;
    EditText name;
    private FirebaseDatabase mFirebaseDatabase;
    TextView schedule;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CheckBox checkBox;
    DatabaseReference databaseReference;
    private String uid;


    ListView listView;

    List<Appliances> applianceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main3);

        spinnerItems = (Spinner) findViewById (R.id.spinner_items);
//        spinnerPlaces = (Spinner) findViewById (R.id.spinner_places);
        schedule = (TextView) findViewById (R.id.editSchedule);
        buttonAdd = (Button) findViewById (R.id.add_button);
        name = (EditText) findViewById(R.id.editText5);
        checkBox = (CheckBox) findViewById (R.id.checkbox_On);

        listView = (ListView) findViewById (R.id.listitemAppliances);
        applianceList = new ArrayList<> ();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        Toast.makeText (getApplicationContext (),"id: " + uid,Toast.LENGTH_SHORT).show ();

        databaseReference = FirebaseDatabase.getInstance ().getReference (uid);

        schedule.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder (Main3Activity.this);
                View mview = getLayoutInflater ().inflate (R.layout.scheduledialog,null);

                startTime = (EditText) mview.findViewById (R.id.edit_start_time);
                endTime = (EditText) mview.findViewById (R.id.edit_End_time);
                showTime = (Button) mview.findViewById (R.id.showTIME);

                startTime.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        c = Calendar.getInstance ();
                        hour = c.get (Calendar.HOUR_OF_DAY);
                        min = c.get (Calendar.MINUTE);

                        timePickerDialog = new TimePickerDialog (Main3Activity.this, new TimePickerDialog.OnTimeSetListener ( ) {
                            @Override
                            public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                                startTime.setText (i +":"+ i1);
                            }

                        },hour,min,false);

                        timePickerDialog.show ();

                    }
                });

                endTime.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        c = Calendar.getInstance ( );
                        hour = c.get (Calendar.HOUR_OF_DAY);
                        min = c.get (Calendar.MINUTE);

                        timePickerDialog = new TimePickerDialog (Main3Activity.this, new TimePickerDialog.OnTimeSetListener ( ) {
                            @Override
                            public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                                endTime.setText (i + ":" + i1);
                            }

                        }, hour, min, false);

                        timePickerDialog.show ();
                    }
                });

                showTime.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {

                            schedule.setText (hour+"+"+min);
                        }
                });

                builder.setView (mview);
                AlertDialog dialog = builder.create ();
                dialog.show ();

            }
        });
        buttonAdd.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                appliance();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appliances appliances = applianceList.get(i);
                final String namet = appliances.getItemName();
                final String typet = appliances.getType();
                final String schedulea = appliances.getSchedule();
                final String statuess = appliances.getStatus();
                final String userId = appliances.getUserId();
                String stats;

                if (statuess.equals("ON"))
                {
//                    Toast.makeText (getApplicationContext (),"sadk",Toast.LENGTH_SHORT).show ();

                    stats = "OFF";
                    update(userId, stats,namet, typet, schedulea);

                }
                else if (statuess.equals("OFF"))

                {

//                    Toast.makeText (getApplicationContext (),"asln",Toast.LENGTH_SHORT).show ();

                    stats="ON";
                    update(userId, stats,namet, typet, schedulea);

                }


            }});

    }




//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Appliances appliances = applianceList.get(i);
////                Intent intent = new Intent(getApplicationContext(), Appliances.class);
////                intent.putExtra(ARTIST_ID, appliances.getUserId());
////                intent.putExtra(ARTIST_NAME, appliances.getItemName());
////                showUpdateDeleteDialog(i, appliances.getUserId(), appliances.getItemName());
//
//
//
//
//            }});
//    }

//    private void showUpdateDeleteDialog(final int i, final String userId, final String itemName) {
//        Appliances appliances = applianceList.get(i);
//
//        final String namet = appliances.getItemName();
//        final String typet = appliances.getPlaceName();
//        final String schedulea = appliances.getSchedule();
//        final String statuess = appliances.getStatus();


//        Toast.makeText (getApplicationContext (),nametakesa,Toast.LENGTH_SHORT).show ();

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.updatee, null);
//        dialogBuilder.setView(dialogView);
//
////        TextView name =findViewById(R.id.textView);
////        TextView type =findViewById(R.id.textView2);
////        TextView schedule =findViewById(R.id.textView3);
//        Button status = (Button) findViewById(R.id.buttonUp);
//        dialogBuilder.setTitle(namet);
////        type.setText(appliances.getPlaceName().toString());
////        schedule.setText(appliances.getSchedule().toString());
//        final AlertDialog b = dialogBuilder.create();
//        b.show();
//        status.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
////                String stats;
////
////            if (statuess == "ON")
////            {
////                stats = "OFF";
////                update(userId, stats,namet, typet, schedulea);
////
////            }
////            else if(statuess=="OFF")
////            {
////                stats="ON";
////                update(userId, stats,namet, typet, schedulea);
////
////            }
//
//
////            }
//
//    }});

//        }

//        public void aaa (int position){
//
////            Appliances appliances = applianceList.get(position);
////            final String namet = appliances.getItemName();
////            final String typet = appliances.getPlaceName();
////            final String schedulea = appliances.getSchedule();
////            final String statuess = appliances.getStatus();
////            final String userId = appliances.getUserId();
////
////            String stats;
////
////            if (statuess == "ON")
////            {
////                stats = "OFF";
////                update(userId, stats,namet, typet, schedulea);
////
////            }
////            else if(statuess=="OFF")
////            {
////                stats="ON";
////                update(userId, stats,namet, typet, schedulea);
////
////            }
//
//
//            }






    private boolean update(String userID, String status, String namet, String type, String schedule)
    {

        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(uid);


        Appliances appliances = new Appliances(userID,namet,type,status,schedule);
        dR.child(userID).setValue(appliances);
        Toast.makeText(getApplicationContext(), "Status: "+ status, Toast.LENGTH_LONG).show();

        return true;


    }
    @Override
    protected void onStart() {
        super.onStart ( );
        databaseReference.addValueEventListener (new ValueEventListener ( ) {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                applianceList.clear ();
                for (DataSnapshot applianceSnapshot : dataSnapshot.getChildren ()){

                    Appliances appliance = applianceSnapshot.getValue (Appliances.class);
                    applianceList.add (appliance);
                }
                Appliance_List adapter = new Appliance_List (Main3Activity.this, applianceList);
                listView.setAdapter (adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void appliance()
    {
        String na = name.getText().toString();
        String type = spinnerItems.getSelectedItem ().toString ().trim ();
        String status;
        if (checkBox.isChecked()) {
            status = "ON";

            checkBox.setChecked(false);
        }
        else status = "OFF";


        String id = databaseReference.push ().getKey ();
        Appliances appliances = new Appliances (id,na,type,status,"NONE");

        databaseReference.child(id).setValue (appliances);

        Toast.makeText (this, "Appliance added",Toast.LENGTH_LONG).show ();
    }
}
