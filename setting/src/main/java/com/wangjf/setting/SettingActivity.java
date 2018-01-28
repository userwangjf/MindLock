package com.wangjf.setting;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wangjf.myutils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SettingActivity extends AppCompatActivity {

    private EditText mEditExportPath;
    private EditText mEditImportPath;
    private EditText mEditRandKey;
    private Button mButtonSettingOK;
    private Button mButtonGenRandKey;
    private Button mButtonCopyRandKey;

    public static String mSettingExportPath;
    public static String mSettingImportPath;
    public static String mSettingRandKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        mEditExportPath = (EditText)findViewById(R.id.id_setting_export_path);
        mEditImportPath = (EditText)findViewById(R.id.id_setting_import_path);
        mEditRandKey = (EditText)findViewById(R.id.id_setting_rand_key);
        mEditExportPath.setText(getSettingExportPath());
        mEditImportPath.setText(getSettingImportPath());
        mEditRandKey.setText(genRandomString(8));

        mButtonSettingOK = (Button)findViewById(R.id.id_setting_ok);
        mButtonSettingOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSettingImportPath = mEditImportPath.getText().toString();
                mSettingExportPath = mEditExportPath.getText().toString();
                mSettingRandKey = mEditRandKey.getText().toString();
                finish();
            }
        });

        mButtonGenRandKey = (Button)findViewById(R.id.id_gen_rand_key);
        mButtonGenRandKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditRandKey.setText(genRandomString(8));
            }
        });

        mButtonCopyRandKey = (Button)findViewById(R.id.id_copy_rand_key);
        mButtonCopyRandKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mEditRandKey.getText().toString());
                String msg = "复制成功"+cm.getText();
                Toast.makeText(SettingActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String getSettingExportPath() {
        if(mSettingExportPath == null) {
            mSettingExportPath = getStroagePath();
        }
        return mSettingExportPath;
    }

    public static String getSettingImportPath() {
        if(mSettingImportPath == null) {
            mSettingImportPath = getStroagePath();
        }
        return mSettingImportPath;
    }

    public static String getSettingRandKey() {
        return mSettingRandKey;
    }

    public String genRandomString(int len) {
        Random rand = new Random();
        rand.setSeed(TimeUtils.getNowMills());
        String rstr = "";
        for(int i=0;i<len;i++) {
            int r = rand.nextInt(77);
            rstr += Character.toString((char)(r+48));
        }
        return rstr;
    }

    public static String getStroagePath() {
        Date date = TimeUtils.millis2Date(TimeUtils.getNowMills());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fname = "/save-" + format.format(date) + ".txt";
        String path = Environment.getExternalStorageDirectory().getPath() + fname;
        return path;
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SettingActivity.class);
        //可携带参数
        //intent.putExtra("xxx","xxx");
        return intent;
    }

}
