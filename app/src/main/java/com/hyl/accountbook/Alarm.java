package com.hyl.accountbook;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.TimeZone;



/**
 *
 提醒功能
 *
 */
public class Alarm extends Activity {

    private static final String TAG = Alarm.class.getSimpleName();

    private TimePicker mTimePicker;
    private Button mButton1;
    private Button mButton2;
    private Button mButtonCancel;

    private int mHour = -1;
    private int mMinute = -1;

    public static final long DAY = 1000L * 60 * 60 * 24;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        if(mHour == -1 && mMinute == -1) {
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
        }

        mTimePicker = (TimePicker)findViewById(R.id.timePicker);
        mTimePicker.setCurrentHour(mHour);
        mTimePicker.setCurrentMinute(mMinute);
        mTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                mHour = hourOfDay;
                mMinute = minute;
            }
        });


        mButton1 = (Button)findViewById(R.id.normal_button);
        mButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Alarm.this, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getActivity(Alarm.this, 0, intent,0);//getBroadcast

                // 过10s 执行这个闹铃
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                calendar.add(Calendar.SECOND, 10);

                // 进行闹铃注册
                AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

                Toast.makeText(Alarm.this, "设置简单提醒成功!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        mButton2 = (Button)findViewById(R.id.repeating_button);
        mButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Alarm.this, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getActivity(Alarm.this, 0, intent, 0);//getBroadcast

                long firstTime = SystemClock.elapsedRealtime();	// 开机之后到现在的运行时间(包括睡眠时间)
                long systemTime = System.currentTimeMillis();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // 这里时区需要设置一下，不然会有8个小时的时间差
                calendar.set(Calendar.MINUTE, mMinute);
                calendar.set(Calendar.HOUR_OF_DAY, mHour);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                // 选择的每天定时时间
                long selectTime = calendar.getTimeInMillis();

                // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
                if(systemTime > selectTime) {
                    Toast.makeText(Alarm.this, "设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    selectTime = calendar.getTimeInMillis();
                }

                // 计算现在时间到设定时间的时间差
                long time = selectTime - systemTime;
                firstTime += time;

                // 进行闹铃注册
                AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        firstTime, AlarmManager.INTERVAL_DAY, sender);//24*60*60*1000

                Log.i(TAG, "time ==== " + time + ", selectTime ===== "
                        + selectTime + ", systemTime ==== " + systemTime + ", firstTime === " + firstTime);

                Toast.makeText(Alarm.this, "设置每日重复提醒成功! ", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        mButtonCancel = (Button)findViewById(R.id.cancel_button);
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Alarm.this, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getActivity(Alarm.this,
                        0, intent, 0);

                // 取消闹铃
                AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
                am.cancel(sender);
            }
        });

        //保存提醒到日历相关代码

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
