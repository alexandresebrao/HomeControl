package project.alexandre.homecontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;


public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        setContentView(R.layout.activity_login);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void login(View v) {
        EditText username = (EditText) findViewById(R.id.txtUser);
        EditText password = (EditText) findViewById(R.id.txtPassword);

        if (!isNetworkAvailable()){
            Toast t = Toast.makeText(this,"No Internet Connection Available :(", Toast.LENGTH_SHORT);
            t.show();
        } else  {

            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                public void done(ParseUser user, com.parse.ParseException e) {
                    if (user != null) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Error " + e.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });

        }


    }
    public void onRegisterClick(View v){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}


