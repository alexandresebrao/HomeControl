package project.alexandre.homecontrol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class MicroWaveStart extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private int selectedMinutes;
    private int selectedSeconds;
    private String id;
    private String identity;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.cboMinutes_Microwave)
        {
            this.selectedMinutes = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        }
        else if(spinner.getId() == R.id.cboMicrowave_Seconds)
        {
            this.selectedSeconds = Integer.parseInt(parent.getItemAtPosition(pos).toString());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_wave_start);
        Spinner spinner = (Spinner) findViewById(R.id.cboMinutes_Microwave);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.minutes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener (this);
        Spinner spinner2 = (Spinner) findViewById(R.id.cboMicrowave_Seconds);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.seconds_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        //POPULATE THE NAME
        Intent intent = getIntent();
        this.identity = intent.getExtras().getString("identity");
        this.id = intent.getExtras().getString("id");
        TextView microwavename = (TextView) findViewById(R.id.lblMicrowaveNameStart);

        microwavename.setText(this.identity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_microwave_start, menu);
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

    public void onReturnMicroWave(View v){

        Intent resultIntent = getIntent();
        resultIntent.putExtra("identity", identity);
        resultIntent.putExtra("id",id);
        int totaltime = (this.selectedMinutes*60) + this.selectedSeconds;
        Log.d("TIMER:",String.valueOf(totaltime));
        resultIntent.putExtra("totaltime", totaltime);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
