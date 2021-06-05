package com.example.mobilepaindiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        Toast.makeText(context,"It's time to save your Daily Record",Toast.LENGTH_SHORT).show();
        System.out.println("i am alarm");
    }
}
