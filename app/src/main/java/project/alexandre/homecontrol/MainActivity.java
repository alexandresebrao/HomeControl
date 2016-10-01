package project.alexandre.homecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity implements QRManager.QRManagerCallback {
    ParseUser currentUser = ParseUser.getCurrentUser();
    private QRManager qrManager = new QRManager();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView welcome = (TextView) findViewById(R.id.lblWelcome);
        welcome.setText("Welcome " + currentUser.getUsername().toString());
        QRManager qr = new QRManager();
        this.qrManager.setCallback(this);


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

        if (id == R.id.action_coffeemaker) {
            Intent intent = new Intent(this,CoffeeMaker.class);
            startActivity(intent);
        }

        if (id == R.id.action_microwave) {
            Intent intent = new Intent(this,MicroWave.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(View v){
        ParseUser.logOut();
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        finish();
    }

    public void onScanner(View v){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a QRCODE");
        integrator.setResultDisplayDuration(0);
        integrator.setScanningRectangle(400,400);  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);
        // Use a specific camera of the device

        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT"); //this is the result

                String[] parts = contents.split(";");

                if (parts[0].equals("HomeControl")){
                    if (parts[1].equals("CoffeeMaker")) {

                        this.qrManager.addCoffeeMaker(parts[2], Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));

                    }
                    if (parts[1].equals("MicroWave")){

                        this.qrManager.addMicroWave(parts[2]);
                    }
                } else {
                    Toast.makeText(this,"This QRCode is not related to this app",Toast.LENGTH_LONG).show();
                }


            } else
            if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    private void QRresult(String identity, String type, boolean result){
        if (result){
            Toast.makeText(this,type + identity + " Added",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,type + " " + identity + " is already registered",Toast.LENGTH_LONG ).show();
        }

    }

    @Override
    public void result(boolean result, String identity, String type) {
        QRresult(identity,type,result);
    }
}
