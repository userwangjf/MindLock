package com.wangjf.passfragment.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azhon.suspensionfab.FabAttributes;
import com.azhon.suspensionfab.OnFabClickListener;
import com.azhon.suspensionfab.SuspensionFab;
import com.wangjf.adduser.AddUser;
import com.wangjf.myutils.AESCrypt;
import com.wangjf.passfragment.R;
import com.wangjf.passfragment.bean.Bean;
import com.wangjf.passtextedit.EditTextWithClear;
import com.wangjf.passtextedit.PasswordEditText;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
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
        for(int i=0;i<50;i++) {
            Bean.UserBean mUserBean = new Bean.UserBean();
            mUserBean.setUserPurpose("Purpose " + i);
            mUserBean.setUserName("Name " + i);
            mUserBean.setUserPass("Pass " + i);
            mUserBean.setUserMore("More " + i);

            mData.add(mUserBean);
        }

        mAdapter.setData(mData);
    }

    public void initMenu(View v){

        fabMenu = (SuspensionFab) v.findViewById(R.id.fab_menu);

        FabAttributes menu_add = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#2096F3"))
                .setSrc(getResources().getDrawable(R.drawable.menu_add))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .setPressedTranslationZ(10)
                .setTag(1)
                .build();

        FabAttributes menu_locked = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#2096F3"))
                .setSrc(getResources().getDrawable(R.drawable.menu_locked))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .setPressedTranslationZ(10)
                .setTag(2)
                .build();

        FabAttributes menu_setting = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#2096F3"))
                .setSrc(getResources().getDrawable(R.drawable.menu_setting))
                .setFabSize(FloatingActionButton.SIZE_NORMAL)
                .setPressedTranslationZ(10)
                .setTag(3)
                .build();

        fabMenu.addFab(menu_add,menu_locked,menu_setting);

        fabMenu.setFabClickListener(this);
    }

    @Override
    public void onFabClick(FloatingActionButton fab, Object tag) {
        if(tag.equals(1)) {
            //
            try {
                String ret = AESCrypt.encrypt("12345678", "hello world!");
                Log.i("WJF","ENC:"+ret);
                String raw = AESCrypt.decrypt("12345678", ret);
                Log.i("WJF", "RAW:" + raw);
            } catch (GeneralSecurityException e) {

            }
        } else if(tag.equals(2)) {
            mCallBack.onFinish(false);
        } else if(tag.equals(3)) {
            Intent intent = AddUser.newIntent(getActivity());
            startActivity(intent);
        }
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
        mCallBack.onFinish(true);
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
                    UpdateData(position);
                } else {
                    mMayEdit = true;
                    Log.i("WJF", "item click: " + position + " start edit");
                }
            } else {
                //取消选择
                mMayEdit = false;
                mSelect = -2;
                Log.i("WJF", "item click: " + position);
            }
            mAdapter.notifyDataSetChanged();

        }

        public void UpdateData(int position) {

            if(position == 0) {
                //增加
                Bean.UserBean newData = new Bean.UserBean();
                newData.setCreateTime("11111");
                newData.setUserPurpose(mUserPurpose.getText().toString());
                newData.setUserName(mUserName.getText().toString());
                newData.setUserPass(mUserPass.getText().toString());
                newData.setUserMore(mUserMore.getText().toString());
                mData.add(0,newData);
            } else {
                //更新
                mData.get(position-1).setCreateTime("11111");
                mData.get(position-1).setUserPurpose(mUserPurpose.getText().toString());
                mData.get(position-1).setUserName(mUserName.getText().toString());
                mData.get(position-1).setUserPass(mUserPass.getText().toString());
                mData.get(position-1).setUserMore(mUserMore.getText().toString());
            }
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
            Log.i("WJF","item click: " + position);
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
                } else {
                    ShowHolder.mShowInfo.setText(mData.get(pos).getUserPurpose());
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
                InfoHolder.mItemInfo.setText(mData.get(pos).getUserPurpose());

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
                return mData.size();
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
