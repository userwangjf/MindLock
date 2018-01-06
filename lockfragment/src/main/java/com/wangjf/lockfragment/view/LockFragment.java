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
import com.wangjf.myutils.ShareDataUtils;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangjf on 18-1-6.
 */

public class LockFragment extends Fragment {

    private PatternLockerView mLockerView;
    private Context mContext;
    private IntfLockFragment mCallBack;
    private TextView mLockMess;
    private boolean mCheckMode = true;
    private String mPassA;
    private String mPassB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_lockfragment, container, false);

        mLockMess = (TextView)v.findViewById(R.id.id_lock_mess);
        mLockMess.setText("请验证密码");

        String main_key = (String) ShareDataUtils.getParam(getActivity(),"MAIN_KEY","NULL");
        Toast.makeText(getActivity(),main_key,Toast.LENGTH_SHORT).show();

        mLockerView = (PatternLockerView) v.findViewById(R.id.id_pass_view);
        mLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {
                mLockMess.setText("开始测试");
            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {
                //mLockMess.setText("输出变化 " + EncryptUtils.encryptMD5ToString(hitList.toString()));

            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {

                if(mCheckMode)
                    mCallBack.onFinish();
                else
                {
                    if(mPassA == null) {
                        mLockMess.setText("请再次输入密码");
                        mPassA = hitList.toString();
                    } else if(mPassB == null) {
                        mPassB = hitList.toString();
                        if(!mPassA.equals(mPassB)) {
                            mLockMess.setText("2次输入的密码不匹配，请重试!");
                            mPassA = null;
                            mPassB = null;
                        } else {
                            mLockMess.setText("密码输入成功:" + mPassA + "=" + mPassB);
                            ShareDataUtils.setParam(getActivity(),"MAIN_KEY",mPassA);
                            Toast.makeText(getActivity(),"密码修改成功",Toast.LENGTH_SHORT).show();
                            mPassA = null;
                            mPassB = null;
                            mCallBack.onFinish();
                        }

                    }
                }
            }

            @Override
            public void onClear(PatternLockerView view) {

            }
        });

        return v;
    }

    public void setCheckMode(boolean mode)
    {
        this.mCheckMode = mode;
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
        if(mCheckMode) {
            mLockMess.setText("请验证密码");
        } else {
            mLockMess.setText("请输入新密码");
        }
        Log.i("WJF","LockFragment: onHiddenChanged " + mCheckMode);
    }
}
