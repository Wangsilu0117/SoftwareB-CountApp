package com.hyl.accountbook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hyl.fragment.BarChartFragment;
import com.hyl.fragment.PieChartFragment;
import com.hyl.view.BaseBottomBar;


public class ChartActivity extends AppCompatActivity implements BaseBottomBar.OnBottomBarListener {

    private FrameLayout Container;
    private BaseBottomBar mBottomBar;
    private Toolbar toolbar;
    // 当前选中的tab位置
    private int index = 0;
    private FragmentManager mFragmentManager;

    private static final int MSG_CLICK = 20151009;
    private Boolean mCanClickBoolean = true;
    private int MSG_CLICK_DURATION = 200;//能够响应点击事件的时间间隔为0.2s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        toolbar = (Toolbar) findViewById(R.id.chart_toolbar);
        Container = (FrameLayout) findViewById(R.id.chart_container);
        mBottomBar = (BaseBottomBar) findViewById(R.id.chart_bottom_bar);

        toolbar.setTitle("图表分析");
        mFragmentManager = getSupportFragmentManager();
        mBottomBar.setOnBottomBarListener(this);
        mBottomBar.showTab(0);

        // 设置关闭键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mHandler == null) {
                return;
            }
            int msgNum = msg.what;
            switch (msgNum) {
                case MSG_CLICK:
                    mCanClickBoolean = true;
                    break;
            }
        }
    };


    @Override
    public void showFragment(int index) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(index));
        if (fragment == null) {
            if (index == 0) {
                fragment = new PieChartFragment();
            } else if (index == 1) {
                fragment = new BarChartFragment();
            }
            if (!fragment.isAdded()) {
                obtainFragmentTransaction(index).add(R.id.chart_container, fragment, String.valueOf(index)).commitAllowingStateLoss();
            }
        }
        if (fragment != null) {
            obtainFragmentTransaction(index).show(fragment).commitAllowingStateLoss();
        }
        this.index = index;
    }

    @Override
    public void hideFragment(int lastIndex, int curIndex) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(index));
        if (fragment != null) {
            obtainFragmentTransaction(curIndex).hide(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void refreshView(int index) {
        if (index == 0) {
        } else if (index == 1) {
        } else if (index == 2) {
        }
    }

    @Override
    public void onTabClick(View view) {
        if (!mCanClickBoolean) {
            return;
        }
        mCanClickBoolean = false;
        mHandler.removeMessages(MSG_CLICK);
        mHandler.sendEmptyMessageDelayed(MSG_CLICK,
                MSG_CLICK_DURATION);

        mBottomBar.setCurrentView(view);
        if (index == 0) {
            toolbar.setTitle("饼图分析");
        } else if (index == 1) {
            toolbar.setTitle("条形图分析");
        }
    }


    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        // 设置切换动画
        if (index > this.index) {
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        } else {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        }
        return ft;
    }


}
