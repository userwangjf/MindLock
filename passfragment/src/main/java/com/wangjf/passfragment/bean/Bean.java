package com.wangjf.passfragment.bean;

import java.util.List;

/**
 * Created by wangjf on 18-1-4.
 */

public class Bean {

    private String mAppVersion;         //版本
    private String mSaveDate;           //保存日期
    private String mSign;               //签名
    private List<UserBean> mUserInfo;  //数据

    public String getAppVersion() {
        return mAppVersion;
    }

    public void setAppVersion(String appVersion) {
        mAppVersion = appVersion;
    }

    public String getSaveDate() {
        return mSaveDate;
    }

    public void setSaveDate(String saveDate) {
        mSaveDate = saveDate;
    }

    public String getSign() {
        return mSign;
    }

    public void setSign(String sign) {
        mSign = sign;
    }

    public List<UserBean> getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(List<UserBean> userCount) {
        mUserInfo = userCount;
    }

    public  static class UserBean {
        private String mCreateTime;     //创建时间
        private String mUserPurpose;    //用途
        private String mUserName;       //帐号
        private String mUserPass;       //密码
        private String mUserMore;       //更多信息

        public String getCreateTime() {
            return mCreateTime;
        }

        public void setCreateTime(String createTime) {
            mCreateTime = createTime;
        }

        public String getUserPurpose() {
            return mUserPurpose;
        }

        public String getUserName() {
            return mUserName;
        }

        public String getUserPass() {
            return mUserPass;
        }

        public String getUserMore() {
            return mUserMore;
        }

        public void setUserPurpose(String userPurpose) {
            mUserPurpose = userPurpose;
        }

        public void setUserName(String userName) {
            mUserName = userName;
        }

        public void setUserPass(String userPass) {
            mUserPass = userPass;
        }

        public void setUserMore(String userMore) {
            mUserMore = userMore;
        }
    }

}
