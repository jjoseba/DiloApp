package com.jjoseba.pecsmobile.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTextActivity extends TTSActivity{

    @BindView(R.id.selected_text) TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        final String title = extras.getString("text");

        text.setText(title);
        text.setOnClickListener(v -> speak(title));
    }
}
