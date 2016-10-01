package project.alexandre.homecontrol;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;


public class MicroWave extends ActionBarActivity implements MicroWaveAdapter.MicroWaveAdapterCallback{

    private ArrayList<MicroWaveItem> lstmicrowave = new ArrayList<MicroWaveItem>();

    private MicroWaveAdapter adapter;

    private ListView listView;

    private ParseUser currentUser = ParseUser.getCurrentUser();

    static int ADD_MICROWAVE = 1;

    static int START_MICROWAVE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_wave);
        //POPULATE THE LIST
        this.listView = (ListView) findViewById(R.id.lstMicroWave);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MicroWave");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {

                if (e == null) {

                    for (Iterator<ParseObject> i = parseObjects.iterator(); i.hasNext(); ) {
                        ParseObject object = i.next();
                        MicroWaveItem item = new MicroWaveItem(object.getString("identity"), object.getObjectId(), object.getBoolean("inUse"));
                        MicroWave.this.lstmicrowave.add(item);
                    }
                    MicroWave.this.adapter = new MicroWaveAdapter(getApplicationContext(),R.layout.activity_micro_wave,MicroWave.this.lstmicrowave);
                    MicroWave.this.adapter.setCallback(MicroWave.this);
                    MicroWave.this.listView.setAdapter(MicroWave.this.adapter);

                } else {
                    Log.d("ERROR:", "" + e.getMessage());
                }
            }

            ;
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_micro_wave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToMicroWaveAdd(View v){

        Intent intent = new Intent(this,MicroWaveAdd.class);
        startActivityForResult(intent, ADD_MICROWAVE);
    }


    private void AddItem(final String identity) {
        final ParseObject microwaveobject = new ParseObject("MicroWave");

        microwaveobject.put("identity",identity);
        microwaveobject.put("inUse",false);
        microwaveobject.setACL(new ParseACL(currentUser));

        microwaveobject.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {


                    MicroWaveItem item = new MicroWaveItem(identity, microwaveobject.getObjectId(),false);
                    MicroWave.this.lstmicrowave.add(item);
                    MicroWave.this.adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    private MicroWaveItem getItem(String id){
        MicroWaveItem finalitem = null;


        return finalitem;
    }


    // DELETE ITEM
    private void DeleteItem(String position) {

        int value = 0;
        boolean found = false;
        MicroWaveItem itemtodelete = null;
        for (MicroWaveItem item : this.lstmicrowave) {

            if (item.id.equals(position)) {
                itemtodelete = item;
                found = true;
            }

        }

        if (found) {
            this.lstmicrowave.remove(itemtodelete);
            Toast t = Toast.makeText(this, Integer.toString(value), Toast.LENGTH_LONG);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("MicroWave");
            query.getInBackground(position, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    MicroWave.this.adapter.notifyDataSetChanged();
                                } else {
                                    Log.d("Error", "Delete :(");
                                }
                            }
                        });


                    } else {
                        Log.d("ERROR", "Not Again!" + e.getMessage());
                    }
                }
            });
        }
    }

    private void DeleteMicroWave(final String position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete the Microwave?")
                .setTitle("Warning");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteItem(position);
            }


        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void deleteMicroWavePressed(String position) {
        DeleteMicroWave(position);
    }

    private void startPrepareMicroWave(MicroWaveItem item){

            Intent intent = new Intent(this,MicroWaveStart.class);
            intent.putExtra("identity",item.identity);
            intent.putExtra("id", item.id);
            startActivityForResult(intent, START_MICROWAVE);
    }

    //MAKE MICROWAVEWORK
    private void toast_microwave(String message, int toast_length){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_microwave,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.toastmicrowavetext);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(toast_length);
        toast.setView(layout);
        toast.show();
    }

    private void UdpateLocalListCoffeeStatus(boolean microwavestatus, String identity){
        for (MicroWaveItem item : this.lstmicrowave){
            if (item.id.equals(identity)){
                item.inUse = microwavestatus;
            }
        }
        this.adapter.notifyDataSetChanged();
    }
    private void UpdateMicroWaveStatus(final boolean microwavestatus, final String identity){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MicroWave");

        query.getInBackground(identity, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("inUse", microwavestatus);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            UdpateLocalListCoffeeStatus(microwavestatus,identity);
                        }
                    });
                } else {
                    Log.d("Shit1","Shit1");
                }
            }
        });



    }

    private void turnMicroWaveOn(final String identity, final String id, int totaltime){
        toast_microwave(identity + " has started", Toast.LENGTH_SHORT);
        UpdateMicroWaveStatus(true, id);

        Long time = new GregorianCalendar().getTimeInMillis()+totaltime*1000;

        Intent intentAlarm = new Intent(this, MicroWaveAlarm.class);
        intentAlarm.putExtra("id",id);
        intentAlarm.putExtra("message",identity + " finished cooking!");

        //Shared Preferences
        SharedPreferences preferences = getSharedPreferences("NOTIFICATION",0);
        int notid = preferences.getInt("notid",0);
        int requestcode = preferences.getInt("requestCode",0);
        intentAlarm.putExtra("notid",notid);
        notid += 1;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("notid",notid);

        //ALARM
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time, PendingIntent.getBroadcast(this, requestcode, intentAlarm, PendingIntent.FLAG_ONE_SHOT));
        requestcode += 1;
        editor.putInt("requestCode",requestcode);
        editor.commit();

        // Handler which will run after 60 seconds. - THE ACTIVITY MUST BE OPEN TO WORK
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                toast_microwave(identity + " finish cooking!!!.", Toast.LENGTH_LONG);
                UpdateMicroWaveStatus(false, id);
            }
        }, totaltime*1000);
    }


    private void PrepareStartMicrowave(String id){

        for (MicroWaveItem item : this.lstmicrowave){
            if (item.id.equals(id)){
                startPrepareMicroWave(item);


            }
        }

    }

    @Override
    public void startMicroWavePressed(String id) {
        PrepareStartMicrowave(id);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if(resultCode == RESULT_OK){
            if (requestCode == START_MICROWAVE){
                Bundle Mbuddle = data.getExtras();
                String id = Mbuddle.getString("id");
                int time = Mbuddle.getInt("totaltime");
                String identity = Mbuddle.getString("identity");
                turnMicroWaveOn(identity,id,time);

            }
            if (requestCode == ADD_MICROWAVE) {
                // Make sure the request was successful

                Bundle MBuddle = data.getExtras();
                String identity = MBuddle .getString("identity");

                AddItem(identity);

            }
        }

    }
}
