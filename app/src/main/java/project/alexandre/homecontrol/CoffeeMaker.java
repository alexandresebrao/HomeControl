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


public class CoffeeMaker extends ActionBarActivity implements CoffeeMakerAdapter.CoffeeMakerAdapterCallback {
    int ADD_NEW_COFFEEMAKER = 1;

    private CoffeeMakerAdapter adapter;

    private ArrayList<CoffeeMakerItem> lstCoffeemakers = new ArrayList<CoffeeMakerItem>();

    private ParseUser currentUser = ParseUser.getCurrentUser();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_maker);
        this.listView = (ListView) findViewById(R.id.lstCoffeMachines);

        //POPULATE THE LIST
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("CoffeeMaker");
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {

                if (e == null) {

                    for (Iterator<ParseObject> i = parseObjects.iterator(); i.hasNext(); ) {
                        ParseObject object = i.next();
                        CoffeeMakerItem item = new CoffeeMakerItem(object.getString("identity"), object.getObjectId(), object.getBoolean("inUse"), object.getInt("minutes"),object.getInt("seconds"));
                        CoffeeMaker.this.lstCoffeemakers.add(item);
                    }
                    CoffeeMaker.this.adapter = new CoffeeMakerAdapter(getApplicationContext(),R.layout.activity_coffee_maker,CoffeeMaker.this.lstCoffeemakers);
                    CoffeeMaker.this.adapter.setCallback(CoffeeMaker.this);
                    CoffeeMaker.this.listView.setAdapter(CoffeeMaker.this.adapter);

                } else {
                    Log.d("ERROR:", "" + e.getMessage());
                }
            }

            ;
        });
        // LETS SCHEDULE A TIMER TASK



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void goToCoffeeMakerAdd(View v){

        Intent intent = new Intent(this,CoffeeMakerAdd.class);
        startActivityForResult(intent, ADD_NEW_COFFEEMAKER);
    }



    // ADD ITEM
    private void AddItem(final String identity, final int minutes, final int seconds){
        final ParseObject coffeeMaker = new ParseObject("CoffeeMaker");

        coffeeMaker.put("identity",identity);
        coffeeMaker.put("inUse",false);
        coffeeMaker.put("minutes",minutes);
        coffeeMaker.put("seconds",seconds);
        coffeeMaker.setACL(new ParseACL(currentUser));

        coffeeMaker.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {


                    CoffeeMakerItem item = new CoffeeMakerItem(identity, coffeeMaker.getObjectId(),false,minutes,seconds);
                    CoffeeMaker.this.lstCoffeemakers.add(item);
                    CoffeeMaker.this.adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_NEW_COFFEEMAKER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle MBuddle = data.getExtras();
                String identity = MBuddle .getString("identity");
                int minutes = MBuddle.getInt("minutes");
                int seconds = MBuddle.getInt("seconds");

                AddItem(identity,minutes,seconds);
            }
        }
    }

    // DELETE ITEM
    private void DeleteItem(String position){

        int value = 0;
        boolean found = false;
        CoffeeMakerItem itemtodelete = null;
        for (CoffeeMakerItem item :  this.lstCoffeemakers){

            if (item.id.equals(position)) {
              itemtodelete = item;
              found = true;
            }

        }

        if (found){
            this.lstCoffeemakers.remove(itemtodelete);
            Toast t = Toast.makeText(this,Integer.toString(value),Toast.LENGTH_LONG);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("CoffeeMaker");
            query.getInBackground(position, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        object.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    CoffeeMaker.this.adapter.notifyDataSetChanged();
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

    private void deleteCoffeeMaker(final String position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete the Coffee Maker?")
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


    public void deletePressed(String position) {
        deleteCoffeeMaker(position);
    }

    private void toast_coffee(String message, int toast_length){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_coffee_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(toast_length);
        toast.setView(layout);
        toast.show();
    }

    // MAKE COFFEE
    private void UdpateLocalListCoffeeStatus(boolean coffeestatus, String identity){
        for (CoffeeMakerItem item : this.lstCoffeemakers){
            if (item.id.equals(identity)){
                item.inUse = coffeestatus;
            }
        }
        this.adapter.notifyDataSetChanged();
    }
    private void UpdateCoffeeStatus(final boolean coffeestatus, final String identity){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("CoffeeMaker");

        query.getInBackground(identity, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("inUse", coffeestatus);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            UdpateLocalListCoffeeStatus(coffeestatus,identity);
                        }
                    });
                } else {
                    Log.d("Shit1","Shit1");
                }
            }
        });



    }

    private void makeCoffee(final String identity, final String id, int totaltime){
        toast_coffee(identity + " is now making a coffee", Toast.LENGTH_SHORT);
        UpdateCoffeeStatus(true,id);

        Long time = new GregorianCalendar().getTimeInMillis()+totaltime*1000;

        Intent intentAlarm = new Intent(this, CoffeeMakerAlarm.class);
        intentAlarm.putExtra("id",id);
        intentAlarm.putExtra("message",identity + " just made a Coffee!");

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
                toast_coffee(identity + " just Made a Coffee!!!.", Toast.LENGTH_LONG);
                UpdateCoffeeStatus(false,id);
            }
        }, totaltime*1000);
    }

    public void makeCoffeePressed(String name,String id, int totaltime){ makeCoffee(name,id,totaltime);}

}
