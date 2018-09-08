package com.unbi.tcpserver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Sendmessage  {
    public static void sendmsg (Intent intent, TCPservice.doNetwork stuff ){

        Log.d("LOG START HERE", "SimpleMsg");
        String address=intent.getStringExtra("address");
        String msgid=intent.getStringExtra("msgid");
        String message=intent.getStringExtra("message");
        Thread myNet;
        ipandport ipport=addresstoip(address);
        stuff.ipport=ipport.port;
        stuff.ipadress=ipport.ip;
        stuff.message=message;
        myNet = new Thread(stuff);
        myNet.start();
    }

    public static void sendmsgAutoRem (Intent intent,TCPservice.doNetwork stuff){
        Log.d("LOG START HERE", "Autoremote");
        String address=intent.getStringExtra("address");
        String msgid=intent.getStringExtra("msgid");
        String message=intent.getStringExtra("message");
        message=autoremappend(message);
        Thread myNet;
        ipandport ipport=addresstoip(address);
        stuff.ipport=ipport.port;
        stuff.ipadress=ipport.ip;
        stuff.message=message;
        myNet = new Thread(stuff);
        myNet.start();

    }


    private static ipandport addresstoip(String adress){
        ipandport ipport=new ipandport();
        String[] parts = adress.split(":");
        ipport.ip=parts[0];
        ipport.port=Integer.valueOf(parts[1]);
        return ipport;
    }

    private static String autoremappend(String string){
        int length=string.length();
        length=327+length;
        String newkey="qwertyuiop[asdfghjklzxcvbnmasdfghjklxcvbnsdfghsdfgh45678sdfghxcvbsdfghasdfghsdfghxdfghedrtedfgdfghdfghxcvbdfghjdfgh45678dfghjdcvbsdfghzxcver";
        String newstring="POST / HTTP/1.1\r\nUser-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.215 Safari/535.1\r\nAccept: application/json,text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5\r\nContent-Type: application/json\r\nContent-Length: "+String.valueOf(length)+"\r\nHost: 192.168.1.253:1818\r\nConnection: Keep-Alive\r\n\r\n{\"communication_base_params\":{\"type\":\"Message\",\"fallback\":false,\"via\":\"Wifi\"},\"sender\":\""+newkey+"\",\"ttl\":-12114,\"collapseKey\":\"\",\"password\":\"\",\"message\":\""+string+"\",\"target\":\"\",\"files\":\"\",\"version\":\"1.63\"}";

        return newstring;
    }

}



