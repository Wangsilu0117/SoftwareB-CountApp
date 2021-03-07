package com.hyl.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyl.view.Lock9View;


public class GesturePwdSet extends AppCompatActivity {
    private ImageView userIconIv;
    private LinkageGroup linkageParentView;
    private TextView hintTv;
    private TextView hintDescTv;
    private Lock9View lock9View;
    private SharedPreferences spref;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_pwd_set);
        userIconIv = (ImageView) findViewById(R.id.user_icon_iv);
        linkageParentView = (LinkageGroup) findViewById(R.id.linkage_parent_view);
        hintTv = (TextView) findViewById(R.id.hint_tv);
        hintDescTv = (TextView) findViewById(R.id.hint_desc_tv);
        lock9View = (Lock9View) findViewById(R.id.lock_9_view);
        spref = getSharedPreferences("spref", Context.MODE_PRIVATE);

        password = spref.getString("password", "");
        linkageParentView.setVisibility(View.VISIBLE);
        userIconIv.setVisibility(View.GONE);
        lock9View.setGestureCallback(new Lock9View.GestureCallback() {

            @Override
            public void onNodeConnected(@NonNull int[] numbers) {
                if (linkageParentView.getVisibility() == View.VISIBLE) {
                    linkageParentView.autoLinkage(numbers, lock9View.lineColor);
                }
            }

            @Override
            public boolean onGestureFinished(@NonNull int[] numbers) {
                StringBuilder builder = new StringBuilder();
                for (int number : numbers) {
                    builder.append(number);
                }
                // ToastUtils.with(LStyleActivity.this).show("= " + builder.toString());
                String value = builder.toString();
                String tmp = spref.getString("tmp_password", "");
                if (tmp.isEmpty()) {
                    if (numbers.length < 4) {
                        hintDescTv.setTextColor(Color.RED);
                        hintDescTv.setText("至少链接4个点,请重新绘制");
                        if (linkageParentView.getVisibility() == View.VISIBLE) {
                            linkageParentView.clearLinkage();
                        }
                        return true;
                    } else {
                        if (linkageParentView.getVisibility() == View.VISIBLE) {
                            linkageParentView.clearLinkage();
                        }
                        hintDescTv.setTextColor(Color.GRAY);
                        hintDescTv.setText("请再次绘制解锁图案");
                        spref.edit().putString("tmp_password", value).apply();
                    }
                } else {
                    if (numbers.length < 4) {
                        spref.edit().putString("tmp_password", "").commit();
                        hintDescTv.setTextColor(Color.RED);
                        hintDescTv.setText("至少链接4个点,请重新绘制");
                        if (linkageParentView.getVisibility() == View.VISIBLE) {
                            linkageParentView.clearLinkage();
                        }
                        return true;
                    } else {
                        if (tmp.equals(value)) {
                            hintDescTv.setText("设置手势密码成功");
                            hintDescTv.setTextColor(Color.GRAY);
                            spref.edit().putString("tmp_password", "").commit();
                            spref.edit().putString("password", value).commit();
                            // 网络请求
                            // 提示
                            if (linkageParentView.getVisibility() == View.VISIBLE) {
                                linkageParentView.clearLinkage();
                            }
                            //Toast.makeText(this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                            Toast.makeText(GesturePwdSet.this, "密码修改成功！",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ChoosePwd.class));
                            finish();
                        } else {
                            if (linkageParentView.getVisibility() == View.VISIBLE) {
                                linkageParentView.clearLinkage();
                            }
                            hintDescTv.setText("两次绘制不一致,请重新绘制");
                            hintDescTv.setTextColor(Color.RED);
                            spref.edit().putString("tmp_password", "").commit();
                            return true;
                        }
                    }
                }
                return false;
            }

        });

    }
}
