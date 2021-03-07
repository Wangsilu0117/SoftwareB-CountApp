package com.hyl.accountbook;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.M)
public class Finger extends AppCompatActivity implements View.OnClickListener {
    private final static int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 0;
    Button testfingerprint;
    CancellationSignal mCancellationSignal = new CancellationSignal();
    FingerprintManager manager;
    KeyguardManager mKeyManager;
    String userID = "11111111111";
    //回调方法
    FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
            Toast.makeText(Finger.this, errString, Toast.LENGTH_SHORT).show();
            showAuthenticationScreen();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

            Toast.makeText(Finger.this, helpString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

            Toast.makeText(Finger.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
            //将登陆用户信息存储到SharedPreferences中
            SharedPreferences mySharedPreferences= getSharedPreferences("setting", Activity.MODE_PRIVATE); //实例化SharedPreferences对象
            SharedPreferences.Editor editor = mySharedPreferences.edit();//实例化SharedPreferences.Editor对象
            editor.putString("userID", userID); //用putString的方法保存数据
            editor.commit(); //提交当前数据
            Intent intent=new Intent(Finger.this,MainActivity.class);
            Finger.this.startActivity(intent);
            Finger.this.finish();
        }

        @Override
        public void onAuthenticationFailed() {
            Toast.makeText(Finger.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
        }
    };
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger);
        testfingerprint = (Button) findViewById(R.id.testfingerprint);
        testfingerprint.setOnClickListener(this);
        //初始化指纹识别管理器
        manager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
        mKeyManager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testfingerprint://测试指纹识别
                if (isFinger()) {
                    Toast.makeText(Finger.this, "请进行指纹识别", Toast.LENGTH_LONG).show();
                    Log(TAG, "keyi");
                    startListening(null);
                }
                break;
        }
    }

    public boolean isFinger() {

        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log(TAG, "有指纹权限");
        //判断硬件是否支持指纹识别
        if (!manager.isHardwareDetected()) {
            Toast.makeText(this, "没有指纹识别模块", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log(TAG, "有指纹模块");
        //判断 是否开启锁屏密码

        if (!mKeyManager.isKeyguardSecure()) {
            Toast.makeText(this, "没有开启锁屏密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log(TAG, "已开启锁屏密码");
        //判断是否有指纹录入
        if (!manager.hasEnrolledFingerprints()) {
            Toast.makeText(this, "没有录入指纹", Toast.LENGTH_SHORT).show();
            return false;
        }
        Log(TAG, "已录入指纹");

        return true;
    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return;
        }
        manager.authenticate(cryptoObject, mCancellationSignal, 0, mSelfCancelled, null);
    }

    /**
     * 锁屏密码
     */
    private void showAuthenticationScreen() {

        Intent intent = mKeyManager.createConfirmDeviceCredentialIntent("finger", "测试指纹识别");
        if (intent != null) {
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "识别成功", Toast.LENGTH_SHORT).show();
                //将登陆用户信息存储到SharedPreferences中
                SharedPreferences mySharedPreferences= getSharedPreferences("setting", Activity.MODE_PRIVATE); //实例化SharedPreferences对象
                SharedPreferences.Editor editor = mySharedPreferences.edit();//实例化SharedPreferences.Editor对象
                editor.putString("userID", userID); //用putString的方法保存数据
                editor.commit(); //提交当前数据
                Intent intent=new Intent(Finger.this,MainActivity.class);
                Finger.this.startActivity(intent);
                Finger.this.finish();
            } else {
                Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Log(String tag, String msg) {
        Log.d(tag, msg);
    }

}

