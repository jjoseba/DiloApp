package com.jjoseba.pecsmobile.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;

public class ShowTextActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    private static final int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_show_text);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

        myTTS = new TextToSpeech(this, this);

        Bundle extras = getIntent().getExtras();
        final String title = extras.getString("text");

        ActionBar actionBar = getActionBar();
        if (actionBar != null){
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
            getActionBar().setTitle(title);
        }

        TextView text = (TextView) findViewById(R.id.selected_text);
        text.setText(title);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTTS.speak(title, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
}
