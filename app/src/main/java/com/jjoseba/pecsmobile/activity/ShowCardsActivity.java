package com.jjoseba.pecsmobile.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;
import com.jjoseba.pecsmobile.model.Card;

import java.util.ArrayList;

public class ShowCardsActivity extends BaseActivity implements OnInitListener {

    private static final int MY_DATA_CHECK_CODE = 0;

    private ArrayList<Card> selectedCards = new ArrayList<Card>();
    private GridView cardsList;

    private TextToSpeech myTTS;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_show_cards);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);

        myTTS = new TextToSpeech(this, this);

        Bundle extras = getIntent().getExtras();
        selectedCards = (ArrayList<Card>) extras.getSerializable("result");
        if (selectedCards == null) selectedCards = new ArrayList<>();
        title = "";
        for (Card card : selectedCards){ title += card.getLabel() + ", ";  }

        ActionBar actionBar = getActionBar();
        if (actionBar != null){
            getActionBar().setDisplayHomeAsUpEnabled(false);
            getActionBar().setHomeButtonEnabled(false);
            getActionBar().setTitle(title);
        }

        cardsList = (GridView) findViewById(R.id.selectedCards);
        if (selectedCards.size() == 2){
            cardsList.setNumColumns(2);
        }
        else if (selectedCards.size() == 1){
            cardsList.setNumColumns(1);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            cardsList.setPadding(metrics.widthPixels / 4, 0,metrics.widthPixels / 4, 0);
            cardsList.setColumnWidth(metrics.widthPixels / 2);
        }
        CardGridAdapter adapter = new CardGridAdapter(this, R.layout.card_gridview, selectedCards);
        cardsList.setAdapter(adapter);
        final String finalTitle = title;
        cardsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myTTS.speak(finalTitle, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                myTTS.speak(title, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 100);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_cards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onInit(int status) {

    }

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
