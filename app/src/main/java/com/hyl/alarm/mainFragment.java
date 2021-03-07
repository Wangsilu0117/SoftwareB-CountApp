package com.hyl.alarm;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.app.Fragment;

import com.hyl.accountbook.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class mainFragment extends Fragment {


    View mRootView;
    Button mButton;
    RemindPara mPara = new RemindPara();
    private final String LOG_TAG = this.getClass().getSimpleName();

    public mainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);
        Log.e(LOG_TAG,"onCreateView");
        mButton = mRootView.findViewById(R.id.button_add_one);
        TextView text_add_one = mRootView.findViewById(R.id.text_add_one);
        text_add_one.setText("将提醒存入系统日历中，为期一年");

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int requestCode = 100;
                if (isAddButton())
                {
                    mPara.action = "add";
                }
                else
                {
                    mPara.action = "delete";
                }
                int checkSelfPermission_calendar_write,checkSelfPermission_calendar_read;
                try {
                    checkSelfPermission_calendar_write = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR);
                    checkSelfPermission_calendar_read = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALENDAR);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return;
                }

                // 如果有授权，走正常插入日历逻辑
                if ((checkSelfPermission_calendar_read == PackageManager.PERMISSION_GRANTED)
                        &&(checkSelfPermission_calendar_write == PackageManager.PERMISSION_GRANTED)){
                    SetAutoRemindAsyncTask task = new SetAutoRemindAsyncTask(getContext(),mPara);
                    task.setPostExecuteListener(new SetAutoRemindAsyncTask.onPostExecuteListener() {
                        @Override
                        public void PostExecuteListener(long eventId) {

                            if (isAddButton())
                            {
                                mPara.remindEventId = eventId;
                                mButton.setText("删除这条提醒");

                            }
                            else
                            {
                                mPara.remindEventId = -1;
                                mButton.setText("添加这条提醒");
                            }
                        }
                    });
                    task.execute();
                    return;
                } else {
                    // 如果没有授权，就请求用户授权
                    requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR,
                            Manifest.permission.READ_CALENDAR}, requestCode);
                }
            }
        });


        return mRootView;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == 0 && grantResults[1] == 0)/*Calendar读写申请成功*/
        {
            /*向calendar中写入提醒事件*/
            SetAutoRemindAsyncTask task = new SetAutoRemindAsyncTask(getContext(),mPara);
            task.setPostExecuteListener(new SetAutoRemindAsyncTask.onPostExecuteListener() {
                @Override
                public void PostExecuteListener(long eventId) {
                    if (isAddButton())
                    {
                        mPara.remindEventId = eventId;
                        mButton.setText("从日历删除提醒");

                    }
                    else
                    {
                        mPara.remindEventId = -1;
                        mButton.setText("添加提醒到日历");
                    }
                }
            });
            task.execute();
        }
    }

    public boolean isAddButton()
    {
        return (mButton.getText().toString().indexOf("添加") >= 0);
    }

}
