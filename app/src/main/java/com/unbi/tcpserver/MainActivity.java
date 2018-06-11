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

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvClientMsg, tvServerIP, tvServerPort;
    public static int SERVER_PORT = 8080;
    private String Server_Name = "Unbi";
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences spref = getSharedPreferences("port", MODE_PRIVATE);
        SERVER_PORT = spref.getInt("SERVER_PORT", 8080);


        setContentView(R.layout.activity_main);
        tvClientMsg = (TextView) findViewById(R.id.textViewClientMessage);
        tvServerIP = (TextView) findViewById(R.id.textViewServerIP);
        tvServerPort = (TextView) findViewById(R.id.textViewServerPort);
        tvServerPort.setText(Integer.toString(SERVER_PORT));
        getDeviceIpAddress();

        clear = (Button)findViewById(R.id.button1);
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                tvClientMsg.setText("");

            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                Log.d("LOG HERE","HERE1");

                try {
                    ServerSocket socServer = new ServerSocket(SERVER_PORT);
                    Socket socClient = null;
                    while (true) {
                        socClient = socServer.accept();
                        ServerAsyncTask serverAsyncTask = new ServerAsyncTask();
                        serverAsyncTask.execute(new Socket[] { socClient });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.d("LOG HERE","HERE2");
    }

    /**
     * Get ip address of the device
     */
    public void getDeviceIpAddress() {
        try {

            for (Enumeration<NetworkInterface> enumeration = NetworkInterface
                    .getNetworkInterfaces(); enumeration.hasMoreElements();) {
                NetworkInterface networkInterface = enumeration.nextElement();
                for (Enumeration<InetAddress> enumerationIpAddr = networkInterface
                        .getInetAddresses(); enumerationIpAddr
                             .hasMoreElements();) {
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
            Log.d("LOG HERE","SETTING");
            Intent intent = new Intent(getBaseContext(), setting.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * AsyncTask which handles the commiunication with clients
     */
    class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
        @Override
        protected String doInBackground(Socket... params) {
            Log.d("LOG HERE","SERVER CONNECTED");
            //TODO Here is the server coonected
            String result = null;
            Socket mySocket = params[0];
            try {

                InputStream is = mySocket.getInputStream();
                PrintWriter out = new PrintWriter(mySocket.getOutputStream(),
                        true);

                //out.println("Welcome to \""+Server_Name+"\" Server");

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));

                result = br.readLine();

                //mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mySocket.close();
            } catch (IOException e) {

                Log.d("LOG HERE","CLOSE ERROR");
            }


            Log.d("LOG HERE","SERVER CLOSE");
            return result;
        }


        @Override
        protected void onPostExecute(String s) {

           //TODO HERE IS THE MSG RECEIVED
            tvClientMsg.append(s+"\n");
            Log.d("LOG HERE",s+"\n");



        }

    }

}