package com.liwenquan.sl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by LWQ on 2016/2/22.
 */
public class AlarmRecevier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(PendingIntent.getBroadcast(context, getResultCode(), new Intent(context, AlarmRecevier.class), 0));
        Intent i = new Intent(context, PlayAlarmActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
    }
}