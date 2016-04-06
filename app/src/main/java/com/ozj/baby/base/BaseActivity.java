package com.ozj.baby.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVAnalytics;

/**
 * Created by Roger ou on 2016/3/25.
 * BaseAvtivity
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }
    
}
