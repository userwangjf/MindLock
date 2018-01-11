package com.wangjf.adduser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
    }

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddUser.class);
        //可携带参数
        //intent.putExtra("xxx","xxx");
        return intent;
    }

}
