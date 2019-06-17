package in.mngo.testingsavedinstance;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(getApplicationContext(), MyService.class);
        startService(serviceIntent);

        Log.e(String.valueOf(getApplicationContext()), "Thread id: " + Thread.currentThread().getId());
    }
}
