package project.alexandre.homecontrol;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * Created by Alexandre on 2015-03-29.
 */
public class MicroWaveAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        final String id = intent.getExtras().getString("id");
        final String message = intent.getExtras().getString("message");
        final int notid = intent.getExtras().getInt("notid");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MicroWave");

        query.getInBackground(id, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("inUse", false);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Notification(context,message,notid);
                        }
                    });
                } else {
                    Log.d("Shit", "Shit");
                }
            }
            public void Notification(Context context, String message, int notid) {
                // Set Notification Title
                String strtitle = "MicroWave Finish it Cooking!";
                // Open NotificationView Class on Notification Click

                // Create Notification using NotificationCompat.Builder
                Notification builder = new NotificationCompat.Builder(
                        context)
                        // Set Icon
                        .setSmallIcon(R.drawable.ic_microwave_green)
                                // Set Ticker Message
                        .setTicker(message)
                                // Set Title
                        .setContentTitle(strtitle)
                                // Set Text
                        .setContentText(message)
                                // Add an Action Button below Notification
                        .build();
                // Create Notification Manager
                NotificationManager notificationmanager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                // Build Notification with Notification Manager
                notificationmanager.notify(notid,builder);

            }

        });
    }
}
