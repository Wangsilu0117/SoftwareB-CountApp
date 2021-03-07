package com.hyl.alarm;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class SetAutoRemindAsyncTask extends AsyncTask<Integer, Integer, String> {

    static Context mContext;
    static ArrayList<RemindPara> mRParaList;
    static long mEventId = -1;
    private static final String LOG_TAG = "SetAutoRemindAsyncTask";
    private onPostExecuteListener mOnListener;

    private static String CALENDAR_URL = "content://com.android.calendar/calendars";
    private static String CALENDAR_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDAR_REMINDER_URL = "content://com.android.calendar/reminders";

    private static String CALENDARS_NAME = "mang";
    private static String CALENDARS_ACCOUNT_NAME = "mang@mmd.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.mang";
    private static String CALENDARS_DISPLAY_NAME = "mang";



    public void setPostExecuteListener(onPostExecuteListener listener) {
        mOnListener = listener;
    }
    interface onPostExecuteListener {

        void PostExecuteListener(long eventId);
    }

    public SetAutoRemindAsyncTask(Context c, RemindPara rPara) {
        super();
        this.mContext = c;
        this.mRParaList = new ArrayList<RemindPara>();
        mRParaList.add(rPara);

    }
   public SetAutoRemindAsyncTask(Context c, ArrayList<RemindPara> paraList) {
        super();
        this.mContext = c;
        this.mRParaList = paraList;
    }
    @Override
    protected String doInBackground(Integer... integers) {
        operateCalendar();
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (mOnListener != null)
        {
            mOnListener.PostExecuteListener(mEventId);
        }

    }

    public void operateCalendar()
    {
        int calId = checkAndAddCalendarAccount(mContext); // 获取日历账户的id
        if (calId < 0) { // 获取账户id失败直接返回，添加日历事件失败
            return ;
        }
        for (RemindPara para : mRParaList) {
            if (para.action.equals("add"))
            {
                insertOneCalendarEvent(mContext, calId, para);
            }
            else
            {
                deleteOneCalendarEvent(mContext, para);
            }
        }
    }
    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDAR_URL),
                null, null, null, null);
        try {
            if (userCursor == null) { // 查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { // 存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }
    private static long addCalendarAccount(Context context) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDAR_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,
                        CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }
    public static int checkAndAddCalendarAccount(Context context)    {
        int id = checkCalendarAccount(context);
        if (id != -1) return id;
        else return (int)addCalendarAccount(context);

    }

    public static boolean insertOneCalendarEvent(Context context, int calId, RemindPara para)
    {
        long eventId = para.remindEventId;
        if (eventId == -1)
        {
            addNewRemindEvent(context,calId,para);
        }
        else
        {
            updateRemindEvent(context,calId,para);
        }
        return true;
    }
    public static boolean addNewRemindEvent(Context context, int calId, RemindPara para)
    {
        String beginTime = getTimeString(para,"begin");
        //String endTime = getTimeString(para,"end");

        //String endTime = getTimeString(para,"end");

        long beginTimeMillis = Utils.getTimeInMillis(beginTime);
        long endTimeMillis = beginTimeMillis+10*60*1000;
        long eventId;

        //Log.e(LOG_TAG,"addNewRemindEvent" + scheduleInformation.id + "..." +  scheduleInformation.remindEventId+ "..." +  mCourseInfo.autoRemind);

        try {
            /** 插入日程 */

            ContentValues eventValues = new ContentValues();
            eventValues.put(CalendarContract.Events.DTSTART, beginTimeMillis);
            eventValues.put(CalendarContract.Events.DTEND, endTimeMillis);//(byte[]) null
            //eventValues.put(CalendarContract.Events.DURATION,endTimeMillis-beginTimeMillis);
            eventValues.put(CalendarContract.Events.TITLE, "记账管家提醒您，养成每日记账好习惯~");//para.action
            eventValues.put(CalendarContract.Events.DESCRIPTION,"来自记账管家" );//mContext.getString(R.string.app_name)
            eventValues.put(CalendarContract.Events.CALENDAR_ID, calId);
            eventValues.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_DEFAULT);
            eventValues.put(CalendarContract.Events.EVENT_LOCATION, "here");
            eventValues.put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=365");//" + until + "

            TimeZone tz = TimeZone.getDefault(); // 获取默认时区
            eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

            Uri eUri = context.getContentResolver().insert(Uri.parse(CALENDAR_EVENT_URL), eventValues);
            eventId = ContentUris.parseId(eUri);
            if (eventId == 0) { // 插入失败
                return false;
            }
            mEventId = eventId;

            /** 插入提醒 - 依赖插入日程成功 */
            ContentValues reminderValues = new ContentValues();
            reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventId);
            reminderValues.put(CalendarContract.Reminders.MINUTES, 1); // 提前1分钟提醒
            reminderValues.put(CalendarContract.Reminders.METHOD,
                    CalendarContract.Reminders.METHOD_ALERT);
            Uri rUri = context.getContentResolver().insert(Uri.parse(CALENDAR_REMINDER_URL),
                    reminderValues);
            if (rUri == null || ContentUris.parseId(rUri) == 0) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
    public static boolean updateRemindEvent(Context context, int calId, RemindPara para)
    {
        String beginTime = getTimeString(para,"begin");
        //String endTime = getTimeString(para,"end");
        long beginTimeMillis = Utils.getTimeInMillis(beginTime);
        long endTimeMillis = beginTimeMillis+10*60*1000;
        //long endTimeMillis = Utils.getTimeInMillis(endTime);
        long eventId = para.remindEventId;

        try {
            /** 更新日程 */
            ContentValues eventValues = new ContentValues();
            eventValues.put(CalendarContract.Events.DTSTART, beginTimeMillis);
            eventValues.put(CalendarContract.Events.DTEND, endTimeMillis);
            eventValues.put(CalendarContract.Events.TITLE, "记账管家提醒您，养成每日记账好习惯~");
            eventValues.put(CalendarContract.Events.DESCRIPTION, "来自记账管家");
            eventValues.put(CalendarContract.Events.CALENDAR_ID, calId);
            eventValues.put(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_DEFAULT);
            eventValues.put(CalendarContract.Events.EVENT_LOCATION, "here");

            TimeZone tz = TimeZone.getDefault(); // 获取默认时区
            eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

            Uri updateUri = ContentUris.withAppendedId(Uri.parse(CALENDAR_EVENT_URL), eventId);
            int rowNum = context.getContentResolver().update(updateUri, eventValues, null, null);
            if (rowNum <= 0)/*更新event不成功，说明用户在日历中删除了提醒事件，重新添加*/
            {
                return addNewRemindEvent(context,calId,para);
            }
            else
            {
                /** 更新提醒 - 依赖更新日程成功 */
                ContentValues reminderValues = new ContentValues();
                reminderValues.put(CalendarContract.Reminders.MINUTES, 1); // 提前提醒
                Uri rUri = Uri.parse(CALENDAR_REMINDER_URL);
                context.getContentResolver().update(rUri, reminderValues, CalendarContract.Reminders.EVENT_ID + "= ?", new String[]{String.valueOf(eventId)});
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
    }


    }
    public static void deleteOneCalendarEvent(Context context, RemindPara para) {
        if (context == null) {
            return;
        }

        if (para.remindEventId == -1)
        return;

        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDAR_EVENT_URL), para.remindEventId);
        int rows = context.getContentResolver().delete(deleteUri, null, null);
    }


    public static String getTimeString(RemindPara schedule, String tag)
    {
        String date ;//= schedule.date
        String time;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //mDay = calendar.get(Calendar.DATE);
        //mHour = calendar.get(Calendar.HOUR_OF_DAY);
        //mMinute = calendar.get(Calendar.MINUTE);
        //beginTime = mHour +":"+mMinute;
        //beginTimeMillis = Utils.getTimeInMillis(beginTime);

        date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
        if (tag.equals("begin"))
        {
            //time = schedule.start_time;
            time = calendar.get(Calendar.HOUR_OF_DAY) +":"+ calendar.get(Calendar.MINUTE);
        }
        else
        {
            time = "00:00";
        }

        String timeString = date + "  " + time + ":00";
        return timeString;
    }

}
