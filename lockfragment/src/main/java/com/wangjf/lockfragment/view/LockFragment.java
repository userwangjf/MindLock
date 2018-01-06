package com.wangjf.lockfragment.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.wangjf.lockfragment.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by wangjf on 18-1-6.
 */

public class LockFragment extends Fragment {

    private PatternLockerView mLockerView;
    private Context mContext;
    private IntfLockFragment mCallBack;
    private TextView mLockMess;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_lockfragment, container, false);

        mLockMess = (TextView)v.findViewById(R.id.id_lock_mess);

        mLockerView = (PatternLockerView) v.findViewById(R.id.id_pass_view);
        mLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {
                mLockMess.setText("开始测试");
            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {
                mLockMess.setText("输出变化");
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {
                mLockMess.setText("完成");
                mCallBack.onFinish();
            }

            @Override
            public void onClear(PatternLockerView view) {
                mLockMess.setText("清空");
            }
        });

        return v;
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
}
