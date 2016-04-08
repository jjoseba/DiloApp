package com.jjoseba.pecsmobile.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;
import com.jjoseba.pecsmobile.model.Card;

import java.util.ArrayList;

public class ShowCardsActivity extends TTSActivity {

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cards);

        Bundle extras = getIntent().getExtras();
        ArrayList<Card> selectedCards = (ArrayList<Card>) extras.getSerializable("result");
        if (selectedCards == null) selectedCards = new ArrayList<>();
        title = "";
        for (Card card : selectedCards){ title += card.getLabel() + ", ";  }

        GridView cardsList = (GridView) findViewById(R.id.selectedCards);
        if (selectedCards.size() == 2){
            cardsList.setNumColumns(2);
        }
        else if (selectedCards.size() == 1){
            cardsList.setNumColumns(1);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            cardsList.setPadding(metrics.widthPixels / 4, 0, metrics.widthPixels / 4, 0);
            cardsList.setColumnWidth(metrics.widthPixels / 2);
        }
        CardGridAdapter adapter = new CardGridAdapter(this, R.layout.card_gridview, selectedCards);
        cardsList.setAdapter(adapter);
        final String finalTitle = title;
        cardsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                speak(finalTitle);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                speak(title, false);
            }
        }, 100);
    }

}
