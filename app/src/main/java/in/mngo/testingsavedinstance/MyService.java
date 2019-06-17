package in.mngo.testingsavedinstance;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyService extends Service
{
    private OkHttpClient client;
    EchoWebSocketListener listener;

//web-socket class
    private final class EchoWebSocketListener extends WebSocketListener
    {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        String output = "";

        @Override
        public void onOpen(WebSocket webSocket, Response response)
        {
            output = ("Connected to web-socket server");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output = ("Receiving : " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output = ("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output = ("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output = ("Error : " + t.getMessage());
        }

        public String getOutput()
        {
            return output;
        }
    }

//    class MyServiceBinder extends Binder
//    {
//        public MyService getService()
//        {
//            return MyService.this;
//        }
//    }
//
//    private IBinder mBinder = new MyServiceBinder();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
    //making connection wth web-socket server
        client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://192.168.43.10:3030").build();
        // in android emulator localhost/127.0.0.1 is equal to 10.0.2.2
        // in android mobile phone localhost/127.0.0.1 is equal to 192.168.X.X
        //Open cmd in windows
        //type "ipconfig" or "ifconfig" then press enter
        //find IPv4 Address. . . . . . . . . . . : 192.168.X.

        listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();

    //getting data from web-socket in a new thread
        HandlerThread ht = new HandlerThread("MySuperAwesomeHandlerThread");
        ht.start();

        final Handler someHandler = new Handler(ht.getLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                //Toast.makeText(MyService.this, "Thread id: "+Thread.currentThread().getId() + "\n" + listener.getOutput(), Toast.LENGTH_SHORT).show();

                Log.e(String.valueOf(getApplicationContext()), "Thread id: " + Thread.currentThread().getId() + "\n" + listener.getOutput());
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

//        new Handler(Looper.getMainLooper()).post(new Runnable()
//        {
//            @Override
//            public void run() {
//                Toast.makeText(MyService.this, "Thread id: "+Thread.currentThread().getId() + "\n" + listener.getOutput(), Toast.LENGTH_SHORT).show();
//            }
//        });

        return START_STICKY;
    }

}