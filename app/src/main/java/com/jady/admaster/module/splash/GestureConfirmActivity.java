package com.jady.admaster.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jady.admaster.R;
import com.jady.admaster.module.main.MainActivity;

/**
 * Created by lipingfa on 2017/7/14.
 */
public class GestureConfirmActivity extends AppCompatActivity implements View.OnClickListener {
    protected Button btnGestureConfirm;
    protected Button btnGestureFailure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.gesture_aty);
        initView();
    }

    private void initView() {
        btnGestureConfirm = (Button) findViewById(R.id.btn_gesture_confirm);
        btnGestureConfirm.setOnClickListener(GestureConfirmActivity.this);
        btnGestureFailure = (Button) findViewById(R.id.btn_gesture_failure);
        btnGestureFailure.setOnClickListener(GestureConfirmActivity.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_gesture_confirm) {
            boolean should_return = getIntent().getBooleanExtra("should_return", false);
            if (should_return) {
                setResult(RESULT_OK);
                finish();
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        } else if (view.getId() == R.id.btn_gesture_failure) {
        }
    }
}
