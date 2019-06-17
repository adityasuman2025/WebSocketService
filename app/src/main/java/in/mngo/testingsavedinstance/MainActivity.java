package in.mngo.testingsavedinstance;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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

        serviceIntent = new Intent(getApplicationContext(), MyService.class);
        startService(serviceIntent);

        text = findViewById(R.id.text);

        Log.e(String.valueOf(getApplicationContext()), "Thread id: " + Thread.currentThread().getId());

        bindService();

        setView();
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
}
