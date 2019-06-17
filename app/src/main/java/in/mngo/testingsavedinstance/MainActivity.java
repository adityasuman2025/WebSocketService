package in.mngo.testingsavedinstance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
