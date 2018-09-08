package com.unbi.tcpserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, TCPservice.class);
            context.startService(pushIntent);
        }
        else if ("Intent.unbi.tcpSend.TCP_MSG".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, TCPservice.class);
            Bundle bundle = intent.getExtras();
            Bundle newbundle=new Bundle();
            newbundle.putInt("putInt#@###", 1);
            pushIntent.putExtras(newbundle);
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
//                    Log.d("TAG EXTRA", String.format("%s %s (%s)", key,
//                            value.toString(), value.getClass().getName()));
                    pushIntent.putExtra(key,value.toString());

                }
            }
//            pushIntent.putExtra("putInt#@###", "1");
            context.startService(pushIntent);
//            String keyid = intent.getStringExtra("keyrow");
//            Log.d("LOG START HERE",keyid);
        }
        else if("Intent.unbi.tcpSend.AUTOREM".equals(intent.getAction())){
            Intent pushIntent = new Intent(context, TCPservice.class);
            Bundle bundle = intent.getExtras();
            Bundle newbundle=new Bundle();
            newbundle.putInt("putInt#@###", 2);
            pushIntent.putExtras(newbundle);
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
//                    Log.d("TAG EXTRA", String.format("%s %s (%s)", key,
//                            value.toString(), value.getClass().getName()));
                    pushIntent.putExtra(key,value.toString());

                }
            }
//            pushIntent.putExtra("putInt#@###", "2");
            context.startService(pushIntent);
        }

    }
}

//VAriable are %address,%msg,%msgid