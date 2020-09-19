package com.jjoseba.pecsmobile.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
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
    private boolean manuallyActivated = false;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
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
        if (isUserManuallyActivated && spoken){
            boolean closeSecondTap = prefs.getBoolean(PrefsActivity.CLOSE_DOUBLETAP, false);
            if (closeSecondTap){
                finish(); return;
            }
        }

        // Set it as lowercase to avoid cases where it is spelled as if it were an acronym
        String lowercaseMsg = text.toLowerCase();

        if (myTTS != null && !myTTS.isSpeaking()){
            manuallyActivated = isUserManuallyActivated;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myTTS.speak(lowercaseMsg, TextToSpeech.QUEUE_FLUSH, null, lowercaseMsg);
            }
            else{
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "dime"+this.hashCode());
                myTTS.speak(lowercaseMsg, TextToSpeech.QUEUE_FLUSH, params);
            }
        }

    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.ERROR){
            Toast.makeText(this, R.string.tts_failed, Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECK_TTS_DATA) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
                myTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        spoken = spoken || manuallyActivated;
                        Log.d("TTS", "spoken:" + spoken);
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Toast.makeText(TTSActivity.this, R.string.tts_failed, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
}
