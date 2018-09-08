package com.unbi.tcpserver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import static com.unbi.tcpserver.MainActivity.SERVER_PORT;
import static com.unbi.tcpserver.MainActivity.booltoast;
import static com.unbi.tcpserver.MainActivity.msg;

public class TCPservice extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onStart(Intent intent, int startId) {



//        int mNotificationId = 001;
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(getApplicationContext())
//                        .setSmallIcon(R.drawable.ic_stat_router)
//                        .setContentTitle("TCP server")
//                        .setContentText("Running.....")
//                        .setOngoing(true);
//        NotificationManager nmnger=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        nmnger.notify(mNotificationId,mBuilder.build());



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
//            if (intent.getStringExtra("putInt#@###") != null) {
////                int putInt = Integer.valueOf(intent.getStringExtra("putInt"));
////                switch (putInt) {
////                    case 1:
////                        Log.d("LOG START HERE", "Intent");
////                        return START_STICKY;
////                    case 2:
////                        Log.d("LOG START HERE", "Autoremote");
////                        return START_STICKY;
////                }
////            }
            Bundle b = intent.getExtras();
            if (b!=null) {
                Integer extra = b.getInt("putInt#@###");
                switch (extra) {
                    case 1:
                        doNetwork stuff = new doNetwork();
//                        stuff.ipadress="192.168.1.253";
//                        stuff.ipport=1818;
                        Sendmessage.sendmsg(intent,stuff);
                        return START_STICKY;
                    case 2:
                        doNetwork stuffarm = new doNetwork();
                        Sendmessage.sendmsgAutoRem(intent,stuffarm);
                        return START_STICKY;
                }
            }
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ServerSocket socServer = new ServerSocket(SERVER_PORT);
                    Socket socClient = null;
                    while (true) {
                        socClient = socServer.accept();
                        TCPservice.ServerAsyncTask serverAsyncTask = new TCPservice.ServerAsyncTask();
                        serverAsyncTask.execute(new Socket[]{socClient});
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        showNotification();
        return START_STICKY;
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, TCPservice.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        int ONGOING_NOTIFICATION_ID = 001;
        Notification notification =
                new Notification.Builder(this)
                        .setContentTitle("TCP server")
                        .setContentText("Running....")
                        .setSmallIcon(R.drawable.ic_stat_router)
                        .setContentIntent(pendingIntent)
                        .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.cancel(001);

    }



    /**
     * AsyncTask which handles the commiunication with clients
     */
    class ServerAsyncTask extends AsyncTask<Socket, Void, String> {
        @Override
        protected String doInBackground(Socket... params) {
//            Log.d("LOG HERE","SERVER CONNECTED");
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

//                Log.d("LOG HERE","CLOSE ERROR");
            }


//            Log.d("LOG HERE","SERVER CLOSE");
            return result;
        }


        @Override
        protected void onPostExecute(String s) {

            //TODO HERE IS THE MSG RECEIVED

//            Log.d("LOG HERE",s+"\n");
//            List<String> items = Arrays.asList("\\Q=:=\\E"));
//            String o= items.get(0);
//            String ipport= items.get(1);
            if (s != null) {
                msg = s;
            } else {
                s = "null";
                msg = "null";
            }
            ArrayIntents(s);
            new Thread(){
                public void run() {
                    Object result=null;


                    Looper l = Looper.getMainLooper();
                    Handler h = new Handler(l);
                    h.post(new Runnable() {
                        @Override
                        public void run(){
                            //update ui here
                            // display toast here
                            Intent i=new Intent();
                            i.setAction("SERVICE");
                            i.putExtra("MSG", msg);
                            sendBroadcast(i);

                            if(booltoast){
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                };
            }.start();

//            runOnUiThread(new Runnable(){
//
//                @Override
//                public void run(){
//                    //update ui here
//                    // display toast here
//                    if(booltoast){
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();}
//                }
//            });



        }

    }

//    tcppar()
//    tcpcomm()

    private  void ArrayIntents(String arg){
        List<String> items= Arrays.asList(msg.split("=:="));
        Intent intent = new Intent();
        intent.setAction("Intent.unbi.tcpserver.TCP_MSG");

        String par=items.get(0);
        //items.remove(0);
        intent.putExtra("tcppar", par);
//        Log.d("tcppar",par);
        List<String> tcppar=Arrays.asList(par.split(" "));
        int i=0;
        for (String nstr:tcppar){
            i=i+1;
//            Log.d("tcppar"+String.valueOf(i),nstr);
            intent.putExtra("tcppar"+String.valueOf(i),nstr);
        }

        int n=0;
        for(String str: items) {
            n=n+1;
            if(n>1){
//            Log.d("tcpcomm"+String.valueOf(n-1)+"=",str);//Igot some error in removing array object so i am doing like this
            intent.putExtra("tcpcomm"+String.valueOf(n-1), str);
            }
        }
        intent.putExtra("tcpmsg", arg);
        sendBroadcast(intent);
    }






    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("handleMessage",(msg.getData().getString("msg")));
        }

    };
    public void mkmsg(String str) {
        //handler junk, because thread can't update screen!
        Message msg = new Message();
        Bundle b = new Bundle();
        b.putString("msg", str);
        msg.setData(b);
        handler.sendMessage(msg);
    }
    class doNetwork  implements Runnable {
        public PrintWriter out;
        public  BufferedReader in;
        int ipport = 8080;
        String ipadress = "192.168.1.1";
        String message = "null";

        public void run() {


            mkmsg("host is "+ ipadress +"\n");
            mkmsg(" Port is " + ipport + "\n");
            try {
                InetAddress serverAddr = InetAddress.getByName(ipadress);
                mkmsg("Attempt Connecting..." + ipadress +"\n");
                Socket socket = new Socket(serverAddr, ipport);


                //made connection, setup the read (in) and write (out)
                out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())),true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //now send a message to the server and then read back the response.
                try {
                    //write a message to the server
                    mkmsg("Attempting to send message ...\n");
                    out.println(message);
                    mkmsg("Message sent...\n");

                    //read back a message from the server.
                    mkmsg("Attempting to receive a message ...\n");
                    String str = in.readLine();
                    mkmsg("received a message:\n" + str+"\n");

                    mkmsg("We are done, closing connection\n");
                } catch(Exception e) {
                    mkmsg("Error happened sending/receiving\n");

                } finally {
                    in.close();
                    out.close();
                    socket.close();
                }

            } catch (Exception e) {
                mkmsg("Unable to connect...\n");
            }
        }
    }


}
