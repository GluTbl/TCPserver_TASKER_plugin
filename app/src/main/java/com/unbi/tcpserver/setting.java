package com.unbi.tcpserver;

import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PublicKey;

import static com.unbi.tcpserver.MainActivity.booltoast;

public class setting  extends AppCompatActivity implements View.OnClickListener {

    private TextView Port;
    private Switch toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        Port = (TextView) findViewById(R.id.port_setting);
        Port.setText(String.valueOf(MainActivity.SERVER_PORT));
        toast=(Switch) findViewById(R.id.toast);
        if(booltoast){toast.setChecked(true);}else{toast.setChecked(false);}
        toast.setOnClickListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toast:{
                boolean on = toast.isChecked();
//                NotificationManager noti = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                noti.cancel(001);


                if (on) {
                    booltoast=true;
//                    Log.d("Toast","ON");

                }else{
                    booltoast=false;
//                    Log.d("Toast","OFF");
                }
                SharedPreferences spref = getSharedPreferences("port", MODE_PRIVATE);
                SharedPreferences.Editor editor = spref.edit();
                editor.putBoolean("booltoast", booltoast);
                editor.apply();

                break;
            }
    }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            Log.d(this.getClass().getName(), "back button pressed");
            String content = Port.getText().toString();
            if(Integer.valueOf(content)<65535&&Integer.valueOf(content)>0)
            {MainActivity.SERVER_PORT=Integer.valueOf(content);
            SharedPreferences spref = getSharedPreferences("port", MODE_PRIVATE);
            SharedPreferences.Editor editor = spref.edit();
            editor.putInt("SERVER_PORT", MainActivity.SERVER_PORT);
            editor.apply();
            Toast.makeText(this, "Force Stop the App in setting to make effect", Toast.LENGTH_SHORT).show();}
            else{
                Toast.makeText(this, "Please enter a valid PORT", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}

