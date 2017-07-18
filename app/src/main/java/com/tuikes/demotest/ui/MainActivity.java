package com.tuikes.demotest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tuikes.demotest.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.listView, R.id.PickerView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.listView:
                startActivity(new Intent(this, DemoScrollBarPanelActivity.class));
                break;
            case R.id.PickerView:

                break;
        }
    }

}
