package com.jjoseba.pecsmobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;

public class ShowTextActivity extends TTSActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);

        Bundle extras = getIntent().getExtras();
        final String title = extras.getString("text");

        TextView text = (TextView) findViewById(R.id.selected_text);
        text.setText(title);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            speak(title);
            }
        });
    }
}
