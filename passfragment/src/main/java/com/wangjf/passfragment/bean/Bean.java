package com.wangjf.passfragment.bean;

import java.util.List;

/**
 * Created by wangjf on 18-1-4.
 */

public class Bean {

    private String app_version;         //版本
    private String save_date;           //保存日期
    private List<UserBean> user_count; //数据


    public  static class UserBean {
        private String mUserPurpose;    //用途
        private String mUserName;       //帐号
        private String mUserPass;       //密码
        private String mUserMore;       //更多信息

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
