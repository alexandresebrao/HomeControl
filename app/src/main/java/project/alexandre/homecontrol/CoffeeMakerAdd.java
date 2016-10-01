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
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseUser;


public class CoffeeMakerAdd extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    ParseUser currentUser = ParseUser.getCurrentUser();
    private int selectedMinutes = 0;
    private int selectedSeconds = 0;


    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.cboMinutes)
        {
            selectedMinutes = Integer.parseInt(parent.getItemAtPosition(pos).toString());
            Log.d("Minutes","Value" + String.valueOf(selectedMinutes));
        }
        else if(spinner.getId() == R.id.cboSeconds)
        {
            selectedSeconds = Integer.parseInt(parent.getItemAtPosition(pos).toString());
            Log.d("Minutes","Value" + String.valueOf(selectedSeconds));
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffe_maker_add);
        Spinner spinner = (Spinner) findViewById(R.id.cboMinutes);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.minutes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener (this);
        Spinner spinner2 = (Spinner) findViewById(R.id.cboSeconds);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.seconds_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
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

    public void onAddCoffeeMaker(View v) {
        EditText coffeid = (EditText) findViewById(R.id.txtCoffeeID);

        Intent resultIntent = getIntent();
        resultIntent.putExtra("identity", coffeid.getText().toString());
        resultIntent.putExtra("minutes", selectedMinutes);
        resultIntent.putExtra("seconds",selectedSeconds);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

}
