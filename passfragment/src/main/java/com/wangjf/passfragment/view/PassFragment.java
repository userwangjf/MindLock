package com.wangjf.passfragment.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azhon.suspensionfab.FabAttributes;
import com.azhon.suspensionfab.OnFabClickListener;
import com.azhon.suspensionfab.SuspensionFab;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangjf.myutils.AESCrypt;
import com.wangjf.myutils.MyLogUtils;
import com.wangjf.myutils.TimeUtils;
import com.wangjf.myutils.Utf8FileUtils;
import com.wangjf.passfragment.R;
import com.wangjf.passfragment.bean.Bean;
import com.wangjf.passtextedit.EditTextWithClear;
import com.wangjf.passtextedit.PasswordEditText;
import com.wangjf.promptdialog.PromptDialog;
import com.wangjf.setting.SettingActivity;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjf on 18-1-6.
 */

public class PassFragment extends Fragment implements OnFabClickListener {

    private List<Bean.UserBean> mData;
    private RecyclerView mRecyclerView;
    private InfoAdapter mAdapter;
    private SuspensionFab fabMenu;
    private IntfPassFragment mCallBack;
    private int mSelect = -2;
    final int VIEW_ITEM = 0;
    final int VIEW_SHOW = 1;
    private boolean mMayEdit = false;
    final int MENU_TAG_IMPORT = 10;
    final int MENU_TAG_EXPORT = 20;
    final int MENU_TAG_SETTING = 30;
    final int MENU_TAG_LOCK = 40;
    private String mPass;

    public void setPass(String pass) {
        mPass = pass;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_passfragment, container, false);

        initRvList(v);
        initMenu(v);

        return v;
    }

    public void initRvList(View v) {
        //微博列表
        mRecyclerView = (RecyclerView) v.findViewById(R.id.id_recycler_view);
        mAdapter = new InfoAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mData = new ArrayList<>();

        /*
        for(int i=0;i<50;i++) {
            Bean.UserBean mUserBean = new Bean.UserBean();
            mUserBean.setUserPurpose("Purpose " + i);
            mUserBean.setUserName("Name " + i);
            mUserBean.setUserPass("Pass " + i);
            mUserBean.setUserMore("More " + i);

            mData.add(mUserBean);
        }
        */

        mAdapter.setData(mData);
    }

    public void initMenu(View v){

        fabMenu = (SuspensionFab) v.findViewById(R.id.fab_menu);

        FabAttributes menu_setting = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#99ee99"))
                .setSrc(getResources().getDrawable(R.drawable.menu_setting))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .setPressedTranslationZ(10)
                .setTag(MENU_TAG_SETTING)
                .build();

        FabAttributes menu_import = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#99ee99"))
                .setSrc(getResources().getDrawable(R.drawable.menu_import))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .setPressedTranslationZ(10)
                .setTag(MENU_TAG_IMPORT)
                .build();

        FabAttributes menu_export = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#99ee99"))
                .setSrc(getResources().getDrawable(R.drawable.menu_export))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .setPressedTranslationZ(10)
                .setTag(MENU_TAG_EXPORT)
                .build();

        FabAttributes menu_lock = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#99ee99"))
                .setSrc(getResources().getDrawable(R.drawable.menu_locked))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .setPressedTranslationZ(10)
                .setTag(MENU_TAG_LOCK)
                .build();

        fabMenu.addFab(menu_setting,menu_import,menu_export,menu_lock);
        fabMenu.setFabClickListener(this);
    }

    @Override
    public void onFabClick(FloatingActionButton fab, Object tag) {
        if(tag.equals(MENU_TAG_SETTING)) {
            Intent intent = SettingActivity.newIntent(getActivity());
            startActivity(intent);
        } else if(tag.equals(MENU_TAG_IMPORT)) {//import
            String path = getImportPath();
            ImportDialog(path);
        } else if(tag.equals(MENU_TAG_EXPORT)) {//export
            String path = getExportPath();
            ExportDialog(path);
        } else if(tag.equals(MENU_TAG_LOCK)) {//pass setting
            this.mCallBack.onFinish(1);
        }
    }

    //确认导入
    private void ImportDialog(final String path) {

        new PromptDialog.Builder(getActivity())
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("确定导入, 覆盖旧数据?\n" + path)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        if(import_data(path)) {
                            Toast.makeText(getActivity(),"导入数据成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setButton2("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"取消导入数据",Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    //确认导出
    private void ExportDialog(final String path) {

        new PromptDialog.Builder(getActivity())
                .setTitle("提示")
                .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                .setMessage("确定导出数据?\n" + path)
                .setButton1("确定", new PromptDialog.OnClickListener() {

                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        if(export_data(path)) {
                            Toast.makeText(getActivity(), "导出数据成功: " + path, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setButton2("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"取消导出数据",Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    public boolean import_data(String path) {
        String read = Utf8FileUtils.ReadUTF8File(path);
        if(read == null) {
            MyLogUtils.i("文件不存在："+path);
            Toast.makeText(getActivity(),"文件不存在："+path,Toast.LENGTH_SHORT).show();
            return false;
        }
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Bean myBean = gson.fromJson(read,Bean.class);

        for(int i=0;i<myBean.getUserInfo().size();i++) {
            try {
                myBean.getUserInfo().get(i).setUserPurpose(AESCrypt.decrypt(mPass,myBean.getUserInfo().get(i).getUserPurpose()));
                myBean.getUserInfo().get(i).setUserName(AESCrypt.decrypt(mPass,myBean.getUserInfo().get(i).getUserName()));
                myBean.getUserInfo().get(i).setUserPass(AESCrypt.decrypt(mPass,myBean.getUserInfo().get(i).getUserPass()));
                myBean.getUserInfo().get(i).setUserMore(AESCrypt.decrypt(mPass,myBean.getUserInfo().get(i).getUserMore()));
                myBean.getUserInfo().get(i).setCreateTime(AESCrypt.decrypt(mPass,myBean.getUserInfo().get(i).getCreateTime()));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }

        mData = myBean.getUserInfo();
        mAdapter.setData(mData);

        return true;

    }

    public boolean export_data(String path) {
        //没有密码
        if(mPass.equals("")) {
            Toast.makeText(getActivity(),"未设置密码，无法导出数据",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mData == null || mData.size() == 0)
        {
            Toast.makeText(getActivity(),"无数据可以导出",Toast.LENGTH_SHORT).show();
            return false;
        }

        MyLogUtils.i("Export Path: " + path);
        //Gson gson = new Gson();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Bean myBean = new Bean();
        List<Bean.UserBean> mSaveList = new ArrayList<>();

        for(int i=0;i<mData.size();i++) {
            Bean.UserBean mSaveData = new Bean.UserBean();
            try {
                mSaveData.setUserPurpose(AESCrypt.encrypt(mPass, mData.get(i).getUserPurpose()));
                mSaveData.setUserName(AESCrypt.encrypt(mPass,mData.get(i).getUserName()));
                mSaveData.setUserPass(AESCrypt.encrypt(mPass,mData.get(i).getUserPass()));
                mSaveData.setUserMore(AESCrypt.encrypt(mPass,mData.get(i).getUserMore()));
                mSaveData.setCreateTime(AESCrypt.encrypt(mPass,mData.get(i).getCreateTime()));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

            mSaveList.add(mSaveData);
        }

        myBean.setUserInfo(mSaveList);
        myBean.setAppVersion("myapp v1.0");
        myBean.setSaveDate("date");
        myBean.setSign("sign");

        String data = gson.toJson(myBean);
        Utf8FileUtils.WriteUTF8File(path,data);

        return true;
    }

    public String getExportPath() {
        String path = SettingActivity.getSettingExportPath();
        return path;
    }

    public String getImportPath() {
        String path = SettingActivity.getSettingImportPath();
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static PassFragment newInstance() {
        PassFragment mPassFragment = new PassFragment();
        //添加参数？
        //Bundle args = new Bundle();
        //args.putBoolean("comeFromAccoutActivity", comeFromAccoutActivity);
        //mPassFragment.setArguments(args);
        return mPassFragment;
    }

    public void setCallBack(IntfPassFragment callback) {
        mCallBack = callback;
    }

    @Override
    public void onPause() {
        super.onPause();
        String path = getActivity().getFilesDir() + "/save_files.sav";
        MyLogUtils.i("onPause保存文件：" + path);
        export_data(path);
        mData = null;
        mCallBack.onFinish(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        String path = getActivity().getFilesDir() + "/save_files.sav";
        MyLogUtils.i("onResume恢复文件：" + path);
        //Toast.makeText(getActivity(),"加载：" + path,Toast.LENGTH_SHORT).show();
        import_data(path);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    //多种布局
    public class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mShowInfo;
        RelativeLayout mShowBackground;
        ImageView mEditSelect;
        EditTextWithClear mUserPurpose;
        EditTextWithClear mUserName;
        PasswordEditText mUserPass;
        EditTextWithClear mUserMore;


        public ShowViewHolder(View itemView) {
            super(itemView);
            mShowInfo = (TextView)itemView.findViewById(R.id.id_show_info);
            mShowInfo.setOnClickListener(this);

            mEditSelect = (ImageView) itemView.findViewById(R.id.id_edit_select);
            mEditSelect.setClickable(true);
            mEditSelect.setOnClickListener(this);

            mShowBackground = (RelativeLayout)itemView.findViewById(R.id.id_show_bg);

            mUserPurpose = (EditTextWithClear)itemView.findViewById(R.id.id_user_purpose);
            mUserName = (EditTextWithClear)itemView.findViewById(R.id.id_user_name);
            mUserPass = (PasswordEditText)itemView.findViewById(R.id.id_user_pass);
            mUserMore = (EditTextWithClear)itemView.findViewById(R.id.id_user_more);

        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            int id = view.getId();

            if(id == R.id.id_edit_select) {
                if(mMayEdit) {
                    //再次点击，则提示保存或更新
                    showNormalDialog(position);
                    //UpdateData(position);
                } else {
                    mMayEdit = true;
                    mAdapter.notifyDataSetChanged();
                    MyLogUtils.i("item click: " + position + " start edit");
                }
            } else {
                //取消选择
                mMayEdit = false;
                mSelect = -2;
                mAdapter.notifyDataSetChanged();
                MyLogUtils.i("item click: " + position);
            }

        }

        private void showNormalDialog(final int position){

            new PromptDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setViewStyle(PromptDialog.VIEW_STYLE_TITLEBAR_SKYBLUE)
                    .setMessage("确定更新数据?")
                    .setButton1("确定", new PromptDialog.OnClickListener() {

                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                            int ret = UpdateData(position);
                            if(ret == 0) {
                                Toast.makeText(getActivity(),"数据操作失败", Toast.LENGTH_SHORT).show();
                            } else if(ret == 1) {
                                Toast.makeText(getActivity(),"数据添加成功",Toast.LENGTH_SHORT).show();
                            } else if(ret == 2) {
                                Toast.makeText(getActivity(),"数据更新成功",Toast.LENGTH_SHORT).show();
                            } else if(ret == 3) {
                                Toast.makeText(getActivity(),"数据删除成功",Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setButton2("取消", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                            UpdateData(-2);
                            Toast.makeText(getActivity(),"取消更新数据",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();

            //提示对话框
            /*
            AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(getActivity());
            normalDialog.setTitle("确认");
            normalDialog.setMessage("确认更新数据?");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UpdateData(position);
                            if(position == 0)
                                Toast.makeText(getActivity(),"添加数据完成",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(),"更新数据完成",Toast.LENGTH_SHORT).show();
                        }
                    });
            normalDialog.setNegativeButton("关闭",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UpdateData(-2);
                            Toast.makeText(getActivity(),"取消更新数据",Toast.LENGTH_SHORT).show();
                        }
                    });
            // 显示
            normalDialog.show();
            */
        }

        /*
        返回值：
        0：数据操作失败
        1：数据添加成功
        2：数据更新成功
        3：数据删除成功
         */
        public int UpdateData(int position) {

            //当前时间秒
            int curSeconds = (int)(System.currentTimeMillis() / 1000);
            int ret = 0;

            if(position == 0) {
                //增加
                Bean.UserBean newData = new Bean.UserBean();
                newData.setCreateTime(Integer.toString(curSeconds));
                newData.setUserPurpose(mUserPurpose.getText().toString());
                newData.setUserName(mUserName.getText().toString());
                newData.setUserPass(mUserPass.getText().toString());
                newData.setUserMore(mUserMore.getText().toString());
                mData.add(0,newData);
                mMayEdit = false;
                ret = 1;
            } else if(position > 0) {
                //更新
                if(mUserPurpose.getText().toString().equals("")) {
                    mData.remove(position-1);
                    ret = 3;
                } else {
                    mData.get(position - 1).setCreateTime(Integer.toString(curSeconds));
                    mData.get(position - 1).setUserPurpose(mUserPurpose.getText().toString());
                    mData.get(position - 1).setUserName(mUserName.getText().toString());
                    mData.get(position - 1).setUserPass(mUserPass.getText().toString());
                    mData.get(position - 1).setUserMore(mUserMore.getText().toString());
                    ret = 2;
                }
                mMayEdit = false;
            } else {
                mMayEdit = false;
                ret = 0;
            }
            mAdapter.notifyDataSetChanged();
            return ret;
        }
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mItemInfo;
        RelativeLayout mItemBackground;

        public InfoViewHolder(View itemView) {
            super(itemView);
            mItemInfo = (TextView) itemView.findViewById(R.id.id_item_info);
            mItemBackground = (RelativeLayout) itemView.findViewById(R.id.id_item_bg);
            mItemInfo.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();

            mSelect = position;
            MyLogUtils.i("item click: " + position);
            mAdapter.notifyDataSetChanged();
        }
    }

    public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Context mContext;

        public void setData(List<Bean.UserBean> data) {
            mData = data;
            notifyDataSetChanged();
        }

        public InfoAdapter(Context context) {
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 0) {
                View v = LayoutInflater.from(mContext).inflate(R.layout.layout_data_item, parent, false);
                return new InfoViewHolder(v);
            } else {
                View v = LayoutInflater.from(mContext).inflate(R.layout.layout_data_show, parent, false);
                return new ShowViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            int pos = position - 1;

            if(position == mSelect) {
                //设置数据
                ShowViewHolder ShowHolder = (ShowViewHolder) holder;
                if(position == 0) {
                    ShowHolder.mShowInfo.setText("新增");
                    ShowHolder.mUserPurpose.setText("");
                    ShowHolder.mUserName.setText("");
                    ShowHolder.mUserPass.setText("");
                    ShowHolder.mUserMore.setText("");
                } else {
                    ShowHolder.mShowInfo.setText(mData.get(pos).getUserPurpose() + "  -  " + mData.get(pos).getUserName());
                    ShowHolder.mUserPurpose.setText(mData.get(pos).getUserPurpose());
                    ShowHolder.mUserName.setText(mData.get(pos).getUserName());
                    ShowHolder.mUserPass.setText(mData.get(pos).getUserPass());
                    ShowHolder.mUserMore.setText(mData.get(pos).getUserMore());
                }

                if(mMayEdit) {
                    ShowHolder.mUserPurpose.setFocusable(true);
                    ShowHolder.mUserName.setFocusable(true);
                    ShowHolder.mUserPass.setFocusable(true);
                    ShowHolder.mUserMore.setFocusable(true);

                    ShowHolder.mUserPurpose.setFocusableInTouchMode(true);
                    ShowHolder.mUserName.setFocusableInTouchMode(true);
                    ShowHolder.mUserPass.setFocusableInTouchMode(true);
                    ShowHolder.mUserMore.setFocusableInTouchMode(true);

                    ShowHolder.mUserMore.requestFocus();
                    ShowHolder.mUserPass.requestFocus();
                    ShowHolder.mUserName.requestFocus();
                    ShowHolder.mUserPurpose.requestFocus();
                } else {
                    ShowHolder.mUserPurpose.setFocusable(false);
                    ShowHolder.mUserName.setFocusable(false);
                    ShowHolder.mUserPass.setFocusable(false);
                    ShowHolder.mUserMore.setFocusable(false);
                }

                //设置背景
                if (position % 2 == 0)
                    ShowHolder.mShowBackground.setBackgroundColor(Color.rgb(0xee, 0xee, 0xee));
                else
                    ShowHolder.mShowBackground.setBackgroundColor(Color.rgb(0xdd, 0xdd, 0xdd));

            } else if(position == 0) {
                InfoViewHolder InfoHolder = (InfoViewHolder) holder;
                InfoHolder.mItemInfo.setText("新增");
                InfoHolder.mItemBackground.setBackgroundColor(Color.rgb(0xf0, 0xa0, 0xa0));
            } else {
                //设置数据
                InfoViewHolder InfoHolder = (InfoViewHolder) holder;
                InfoHolder.mItemInfo.setText(mData.get(pos).getUserPurpose() + "  -  " + mData.get(pos).getUserName());

                //设置背景
                if (position % 2 == 0)
                    InfoHolder.mItemBackground.setBackgroundColor(Color.rgb(0xee, 0xee, 0xee));
                else
                    InfoHolder.mItemBackground.setBackgroundColor(Color.rgb(0xdd, 0xdd, 0xdd));
            }
        }

        @Override
        public int getItemCount() {
            if(mData == null)
                return 1;//新增
            else
                return mData.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if(mData == null)
                return VIEW_ITEM;
            else if(position == mSelect)
                return VIEW_SHOW;
            else if(position == 0)
                return VIEW_ITEM;
            else
                return VIEW_ITEM;
        }

    }

}
