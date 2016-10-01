package project.alexandre.homecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class Register extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void goRegister(View v){

        final Button register = (Button) findViewById(R.id.btnRegister);

        EditText pass1 = (EditText) findViewById(R.id.txtPassword);
        EditText pass2 = (EditText) findViewById(R.id.txtPasswordVerify);
        EditText username = (EditText) findViewById(R.id.txtUser);




        boolean test = false;
        if (!isNetworkAvailable()){
            Toast t = Toast.makeText(this,"No Internet Connection Available :(", Toast.LENGTH_SHORT);
            t.show();
            test = true;
        } else  {

        }
        if ((username.length() < 4) && (!test)){
            Toast toast = Toast.makeText(this, "Username to Short", Toast.LENGTH_LONG);
            toast.show();
            test = true;
        }

        if ((pass1.length() < 6) && (!test)) {
            Toast toast = Toast.makeText(this, "Password to Short", Toast.LENGTH_LONG);
            toast.show();
            test = true;
        }

        if ((!pass1.getText().toString().equals(pass2.getText().toString())) && (!test)) {
            String result = pass1.getText() + " : " + pass2.getText();
            Toast toast = Toast.makeText(this, "Password Doesn't Match", Toast.LENGTH_LONG);
            toast.show();
            test = true;
        }

        ParseUser user = new ParseUser();

        user.setUsername(username.getText().toString());
        user.setPassword(pass1.getText().toString());

        if (!test){
            register.setEnabled(false);
            user.signUpInBackground(new SignUpCallback() {
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        Intent intend = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intend);
                        finish();
                    } else {
                        Toast t = Toast.makeText(getApplicationContext(),"Error" + e.getMessage(), Toast.LENGTH_LONG);
                        t.show();
                        register.setEnabled(true);
                    }
                }

            });


        }


    }
}
