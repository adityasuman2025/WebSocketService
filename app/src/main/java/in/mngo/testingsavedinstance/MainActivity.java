package in.mngo.testingsavedinstance;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    TextView text;

    private MyService myService;
    private ServiceConnection serviceConnection;
    Intent serviceIntent;

    private boolean isServiceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);

    //starting service
        serviceIntent = new Intent(getApplicationContext(), MyService.class);
        startService(serviceIntent);

        Log.e(String.valueOf(getApplicationContext()), "Thread id: " + Thread.currentThread().getId());

    //creating a channel for notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

    //sending notification
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("Source", "notificationMap");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        pushNotification("Beacon Alert", "New beacon alert detected", pendingIntent);
    }

    private void bindService()
    {
        if(serviceConnection == null)
        {
            serviceConnection = new ServiceConnection()
            {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder)iBinder;
                    myService = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                }
            };
        }

        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setView()
    {
        if(isServiceBound)
        {
            text.setText(myService.getData());
        }
        else
        {
            text.setText("Service is not bounded");
        }
    }

    //function to send custom push notification
    public void pushNotification(String title, String body, PendingIntent pendingIntent)
    {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "MyNotifications");

        notificationBuilder.setSmallIcon(R.drawable.img);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(body);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC); //for showing notification at lock screen

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(999,  notificationBuilder.build());
    }
}
