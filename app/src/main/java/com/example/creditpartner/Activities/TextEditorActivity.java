package com.example.creditpartner.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.creditpartner.R;

import in.nashapp.androidsummernote.Summernote;

public class TextEditorActivity extends AppCompatActivity implements View.OnTouchListener {

    private Summernote summernote;
    private Toolbar mToolbar;
    private TextView doneEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_editor);

        Initialize();



        summernote.setOnTouchListener(TextEditorActivity.this);

        doneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = summernote.getText().toString();

                Intent intent = new Intent(TextEditorActivity.this, AddCompanyActivity.class);
                intent.putExtra("text",s);
             //   intent.putExtra("type","null");
               // intent.putExtra("edit","null");
                setResult(1, intent);
                finish();
            }
        });
    }

    private void Initialize() {
        summernote = (Summernote)

                findViewById(R.id.summernote);

        summernote.setRequestCodeforFilepicker(5);

        SetupToolbar();
        doneEdit = (TextView)findViewById(R.id.done_edit);


    }


    @Override

    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (summernote.hasFocus()) {

            view.getParent().requestDisallowInterceptTouchEvent(true);

            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_SCROLL:

                    view.getParent().requestDisallowInterceptTouchEvent(false);

                    return true;

            }

        }


        return false;

    }

    private void SetupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.text_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Features");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

}
