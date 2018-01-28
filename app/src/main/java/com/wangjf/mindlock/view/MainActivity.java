package com.wangjf.mindlock.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.wangjf.lockfragment.view.IntfLockFragment;
import com.wangjf.lockfragment.view.LockFragment;
import com.wangjf.mindlock.R;
import com.wangjf.myutils.MyLogUtils;
import com.wangjf.passfragment.view.IntfPassFragment;
import com.wangjf.passfragment.view.PassFragment;

import java.util.List;
import java.util.concurrent.locks.Lock;


public class MainActivity extends AppCompatActivity {

    private FragmentTransaction mFragmentTrans;
    private FragmentManager mFragmentManger;
    private LockFragment mLockFragment;
    private PassFragment mPassFragment;
    private String mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //禁止截屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);


        mFragmentManger = getSupportFragmentManager();

        showLockFragment(0);
        //showPassFragment();

    }

    public void setPass(String pass) {
        mPass = pass;
    }

    public void showLockFragment(int checkMode) {
        mFragmentTrans = mFragmentManger.beginTransaction();
        hideAllFragments();
        if (mLockFragment == null) {
            mLockFragment = LockFragment.newInstance();
            mLockFragment.setCallBack(new IntfLockFragment() {
                @Override
                public void onFinish(String mPass) {
                    setPass(mPass);
                    showPassFragment();
                }
            });
            mFragmentTrans.add(R.id.fragment_container, mLockFragment, "LockFragment");
        } else {
            mFragmentTrans.show(mLockFragment);
            setPass("");
        }
        MyLogUtils.i("PassFragment onFinish: " + checkMode);
        mLockFragment.setCheckMode(checkMode);
        mFragmentTrans.commit();
    }

    public void showPassFragment() {
        mFragmentTrans = mFragmentManger.beginTransaction();
        hideAllFragments();
        if (mPassFragment == null) {
            mPassFragment = PassFragment.newInstance();
            mPassFragment.setCallBack(new IntfPassFragment() {
                @Override
                public void onFinish(int checkMode) {
                    showLockFragment(checkMode);
                }
            });
            mFragmentTrans.add(R.id.fragment_container, mPassFragment, "LockFragment");
        } else {
            mFragmentTrans.show(mPassFragment);
        }
        mPassFragment.setPass(mPass);
        setPass("");
        mFragmentTrans.commit();
    }

    private void hideAllFragments() {
        if (mLockFragment != null) {
            mFragmentTrans.hide(mLockFragment);
        }
        if (mPassFragment != null) {
            mFragmentTrans.hide(mPassFragment);
        }
    }


}
