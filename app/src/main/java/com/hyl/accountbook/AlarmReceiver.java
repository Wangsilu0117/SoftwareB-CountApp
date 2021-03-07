package com.hyl.accountbook;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 *
 * @ClassName: AlarmReceiver
 * @Description: 闹铃时间到了会进入这个活动，这个时候可以做一些该做的业务。
 * @author HuHood
 * @date 2013-11-25 下午4:44:30
 *
 */
public class AlarmReceiver extends AppCompatActivity {//BroadcastReceiver
    private static final String CHANNEL_ID = "11111";
    private static final CharSequence CHANNEL_NAME = "NAME";
    private static final String CHANNEL_DESCRIPTION = "DESCRIPTION";
    private static final int NOTIFY_ID = 0x123;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification() {
        /**
         *先创建渠道，才能发送通知
         */
        createChannel();
        Intent intent=new Intent(this, OpenView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("记账管家提醒您")
                .setContentText("养成每天记账好习惯~")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFY_ID,builder.build());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {//Context context, Intent intent
        //Toast.makeText(context, "账本管家提醒您，养成记账好习惯~", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        sendNotification();
        //Toast.makeText(AlarmReceiver.this, "账本管家提醒您，养成记账好习惯~", Toast.LENGTH_LONG).show();
        //Intent intent=new Intent(AlarmReceiver.this, MainActivity.class);
        //AlarmReceiver.this.startActivity(intent);
        finish();
    }

}
