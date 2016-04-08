package com.jjoseba.pecsmobile.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.jjoseba.pecsmobile.R;

import java.util.HashMap;

public class TTSActivity extends BaseActivity implements TextToSpeech.OnInitListener {

    private static final int CHECK_TTS_DATA = 0X123;
    private TextToSpeech myTTS;

    private boolean spoken = false;
    private boolean speaking = false;
    private boolean manuallyActivated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

        //check for TTs data
        Intent checkTtsDataIntent=new Intent();
        checkTtsDataIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTtsDataIntent, CHECK_TTS_DATA);

        ActionBar actionBar = getActionBar();
        if (actionBar != null){
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
        }
    }

    protected void speak(String text){ speak(text, true); }
    protected void speak(String text, boolean isUserManuallyActivated){
        if (!isUserManuallyActivated || !spoken){
            if (myTTS != null && !myTTS.isSpeaking()){
                manuallyActivated = isUserManuallyActivated;

                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "pecsmobile");
                myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, params);
            }
        }
        else{
            finish();
        }
    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.ERROR){
            Toast.makeText(this, R.string.tts_failed, Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHECK_TTS_DATA) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Log.d("TTS", "Creating TTS!");
                myTTS = new TextToSpeech(this, this);
                myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override public void onStart(String utteranceId) {}
                    @Override public void onDone(String utteranceId) {
                        speaking = false;
                        spoken = spoken || manuallyActivated;
                    }
                    @Override public void onError(String utteranceId) {
                        Toast.makeText(TTSActivity.this, R.string.tts_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
}
