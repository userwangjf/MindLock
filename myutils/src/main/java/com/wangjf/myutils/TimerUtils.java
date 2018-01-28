package com.wangjf.myutils;

import android.os.Handler;
import android.os.Looper;

import java.util.Timer;

/**
 * Created by wangjf on 18-1-18.
使用示例：
 首先创建对象
 HandleTimer mTimer = new HandleTimer() {
@Override
protected void onTime() {
// 在这里做更新ui的操作
setTitle("" + System.currentTimeMillis());
// 也可以在达到一定条件后直接停掉timer
// mTimer.stop();
}
};
 然后调用相关方法
 mTimer.start(period); // 直接启动timer，参数为定时控制的间隔时间
 mTimer.start(delay, period); // 延迟启动timer
 mTimer.restart(period); // 重新启动timer
 mTimer.restart(delay, period); // 重新启动timer
 mTimer.stop(); // 停止timer
 */

public abstract class TimerUtils {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable;

    public TimerUtils() {
    }

    public synchronized void restart(final long period) {
        restart(0, period);
    }

    public synchronized void restart(long delay, final long period) {
        stop();
        start(delay, period);
    }

    public synchronized void start(final long period) {
        start(0, period);
    }

    public synchronized void start(long delay, final long period) {
        if (mRunnable != null) {
            return;
        }
        mRunnable = new Runnable() {
            public void run() {
                mHandler.postDelayed(this, period);
                onTime();
            }
        };
        mHandler.postDelayed(mRunnable, delay);
    }

    public synchronized void stop() {
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
            mRunnable = null;
        }
    }

    protected abstract void onTime();
}


