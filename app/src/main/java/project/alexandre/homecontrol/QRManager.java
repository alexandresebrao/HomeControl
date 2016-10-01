package project.alexandre.homecontrol;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Alexandre on 2015-03-30.
 */
public class QRManager {
    private QRManagerCallback callback;

    private ParseUser currentUser = ParseUser.getCurrentUser();
    public void addCoffeeMaker(final String identity, int minutes, int seconds) {

        final ParseObject coffeeMaker = new ParseObject("CoffeeMaker");

        coffeeMaker.put("identity",identity);
        coffeeMaker.put("inUse",false);
        coffeeMaker.put("minutes",minutes);
        coffeeMaker.put("seconds",seconds);
        coffeeMaker.setACL(new ParseACL(this.currentUser));

        coffeeMaker.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    callback.result(true, identity, "CoffeeMaker");
                } else {
                    callback.result(false,identity,"CoffeeMaker");
                }

            }
        });

    }

    public void addMicroWave(final String identity){
        final ParseObject microwaveobject = new ParseObject("MicroWave");

        microwaveobject.put("identity",identity);
        microwaveobject.put("inUse",false);
        microwaveobject.setACL(new ParseACL(this.currentUser));

        microwaveobject.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {


                    callback.result(true, identity, "MicroWave");

                } else {
                    callback.result(false, identity, "MicroWave");
                }
            }
        });
    }
    public void setCallback(QRManagerCallback callback){

        this.callback = callback;

    }


    public interface QRManagerCallback{
        public void result(boolean result,String identity, String type);
    }
}
