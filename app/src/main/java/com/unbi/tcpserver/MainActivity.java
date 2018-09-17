package com.unbi.tcpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tvClientMsg, tvServerIP, tvServerPort;
    public static int SERVER_PORT = 8080;
    private String Server_Name = "Unbi";
    public static boolean booltoast;
    public static String msg = "";
    Button clear;
    IntentFilter intentFilter = new IntentFilter();
    Switch switchmsgme;
    static Boolean boolshowmsg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spref = getSharedPreferences("port", MODE_PRIVATE);
        SERVER_PORT = spref.getInt("SERVER_PORT", 8080);
        booltoast = spref.getBoolean("booltoast", true);

        setContentView(R.layout.activity_main);
        switchmsgme = (Switch) findViewById(R.id.switchmsg);
//        domsgswutch(switchmsgme, boolshowmsg);
        if (boolshowmsg) {
            switchmsgme.setChecked(true);
        } else {
            switchmsgme.setChecked(false);
        }

        switchmsgme.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchmsgme.isChecked()) {
                    boolshowmsg = true;
                } else {
                    boolshowmsg = false;
                }
            }
        });
        tvClientMsg = (TextView) findViewById(R.id.textViewClientMessage);
        tvServerIP = (TextView) findViewById(R.id.textViewServerIP);
        tvServerPort = (TextView) findViewById(R.id.textViewServerPort);
        tvServerPort.setText(Integer.toString(SERVER_PORT));
        getDeviceIpAddress();
        clear = (Button) findViewById(R.id.button1);
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tvClientMsg.setText("");
            }
        });
        startService(new Intent(this, TCPservice.class));
        intentFilter.addAction("SERVICE");
    }

    /**
     * Get ip address of the device
     */
    public void getDeviceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements(); ) {
                NetworkInterface networkInterface = enumeration.nextElement();
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface
                        .getInetAddresses(); enumerationIpAddr
                             .hasMoreElements(); ) {
                    InetAddress inetAddress = enumerationIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress.getAddress().length == 4) {
                        tvServerIP.setText(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            Log.e("ERROR:", e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
//            Log.d("LOG HERE","SETTING");
            Intent intent = new Intent(getBaseContext(), setting.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        registerReceiver(broadcastReceiver, intentFilter);
        if (boolshowmsg) {
            switchmsgme.setChecked(true);
        } else {
            switchmsgme.setChecked(false);
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        /** Receives the broadcast that has been fired */
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == "SERVICE") {
                //HERE YOU WILL GET VALUES FROM BROADCAST THROUGH INTENT EDIT YOUR TEXTVIEW///////////
                String receivedValue = intent.getStringExtra("MSG");
//                Log.d("LOCALINTENT",receivedValue);
                if(boolshowmsg){tvClientMsg.append(receivedValue + "\n");}
            }
        }
    };



    public static boolean ismsgshow(){
        return boolshowmsg;
    }
//    public static void domsgswutch(Switch switchit, Boolean boolshow) {
//        if (boolshow) {
//            switchit.setChecked(true);
//        } else {
//            switchit.setChecked(false);
//        }
//    }
}