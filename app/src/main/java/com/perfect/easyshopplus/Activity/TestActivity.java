package com.perfect.easyshopplus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.perfect.easyshopplus.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String status = "Hello";
        Intent data = new Intent();
        data.putExtra("status", status);
        setResult(RESULT_OK, data);
//---close the activity---
        finish();
    }
}