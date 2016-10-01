package project.alexandre.homecontrol;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by Alexandre on 2015-03-27.
 */
public class HomeControlApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("MLe2eEWUfNBjnx5dbDsqZkTnVCGarz8v5fFcMt5f")
                .clientKey("bR8f2LNkWjgctXvbfviX2K3YjhiOTNk9VaGaEJcq")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .enableLocalDataStore()
                .server("https://parseapi.back4app.com").build());



        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }



}
