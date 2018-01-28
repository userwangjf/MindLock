package com.wangjf.lockfragment.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.wangjf.lockfragment.R;
import com.wangjf.myutils.EncryptUtils;
import com.wangjf.myutils.MyLogUtils;
import com.wangjf.myutils.ShareDataUtils;
import com.wangjf.myutils.TimerUtils;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangjf on 18-1-6.
 */

public class LockFragment extends Fragment {

    final static int MODE_VERIFY = 0;
    final static int MODE_CHECK = 1;
    final static int MODE_STEP_A = 2;
    final static int MODE_STEP_B = 3;

    private PatternLockerView mLockerView;
    private Context mContext;
    private IntfLockFragment mCallBack;
    private TextView mLockMess;
    private int mCheckMode = MODE_VERIFY;
    private String mPassA;
    private String mPassB;
    private int mWaitTimer = 1;
    private int mWaitCnt = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_lockfragment, container, false);

        mLockMess = (TextView)v.findViewById(R.id.id_lock_mess);
        setCheckMode(mCheckMode);

        mLockerView = (PatternLockerView) v.findViewById(R.id.id_pass_view);
        mLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {

            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {

            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {

                String mPass = hitList.toString();

                doPassCheck(mPass);

            }

            @Override
            public void onClear(PatternLockerView view) {
                if(mWaitCnt > 0) {
                    view.setEnabled(false);
                }
            }
        });

        //锁定定时器
        TimerUtils mTimer = new TimerUtils() {
            @Override
            protected void onTime() {
                if(mWaitCnt == 1) {
                    mWaitCnt--;
                    mLockMess.setText("请重新输入密码!");
                    mLockerView.setEnabled(true);
                } else if(mWaitCnt > 0) {
                    mWaitCnt--;
                    mLockMess.setText("输入错误，请等待"+mWaitCnt+"秒");
                }
            }
        };
        mTimer.start(1000);

        return v;
    }

    public void doPassCheck(String mPass) {
        if(MODE_VERIFY == mCheckMode) {
            //读出原密码
            String old_pass = (String) ShareDataUtils.getParam(getActivity(), "MAIN_KEY", "NULL");
            //Toast.makeText(getActivity(),old_pass,Toast.LENGTH_SHORT).show();
            if (!old_pass.equals("NULL")) {
                String mPassMd5 = EncryptUtils.encryptMD5ToString(mPass);
                //校验密码
                if (mPassMd5.equals(old_pass)) {
                    Toast.makeText(getActivity(),"密码验证成功",Toast.LENGTH_SHORT).show();
                    mWaitTimer = 1;
                    mCallBack.onFinish(mPass);
                } else {
                    mWaitTimer *= 2;
                    mWaitCnt = mWaitTimer;
                    Toast.makeText(getActivity(),"密码验证失败，请重试！",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(),"当前密码为空，清设置密码",Toast.LENGTH_SHORT).show();
                mCallBack.onFinish("");
            }
        } else if(MODE_CHECK == mCheckMode) {
            //读出原密码
            String old_pass = (String) ShareDataUtils.getParam(getActivity(), "MAIN_KEY", "NULL");
            Toast.makeText(getActivity(),old_pass,Toast.LENGTH_SHORT).show();
            if (!old_pass.equals("NULL")) {
                String mPassMd5 = EncryptUtils.encryptMD5ToString(mPass);
                //校验密码
                if (mPassMd5.equals(old_pass)) {
                    setCheckMode(MODE_STEP_A);
                } else {
                    mWaitTimer *= 2;
                    mWaitCnt = mWaitTimer;
                }
            } else {
                setCheckMode(MODE_STEP_A);
            }
        } else if(MODE_STEP_A == mCheckMode) {
            mPassA = mPass;
            setCheckMode(MODE_STEP_B);
        } else if(MODE_STEP_B == mCheckMode) {
            mPassB = mPass;
            if(mPassB.equals(mPassA)) {
                //密码修改成功，保存新密码，重新验证登录
                String mPassMd5 = EncryptUtils.encryptMD5ToString(mPassA);
                ShareDataUtils.setParam(getActivity(),"MAIN_KEY",mPassMd5);
                setCheckMode(MODE_VERIFY);
                Toast.makeText(getActivity(),"密码修改成功，请验证新密码",Toast.LENGTH_SHORT).show();
            } else {
                setCheckMode(MODE_CHECK);
            }
        }
    }

    public void setCheckMode(int mode)
    {
        this.mCheckMode = mode;
        if(mLockMess != null) {
            if (MODE_VERIFY == mCheckMode) {
                mLockMess.setText("请验证密码！");
            } else if (MODE_CHECK == mCheckMode) {
                mLockMess.setText("请验证旧密码！");
            } else if (MODE_STEP_A == mCheckMode) {
                mLockMess.setText("请输入新密码！");
            } else if (MODE_STEP_B == mCheckMode) {
                mLockMess.setText("请重新输入新密码！");
            }
        }
    }

    public void setCallBack(IntfLockFragment callback) {
        this.mCallBack = callback;
    }

    public static LockFragment newInstance() {
        LockFragment mLockFragment = new LockFragment();
        //添加参数？
        //Bundle args = new Bundle();
        //args.putBoolean("comeFromAccoutActivity", comeFromAccoutActivity);
        //mLockFragment.setArguments(args);
        return mLockFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        setCheckMode(mCheckMode);

        MyLogUtils.i("LockFragment: onHiddenChanged " + mCheckMode);
    }
}
