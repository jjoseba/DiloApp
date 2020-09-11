package com.jjoseba.pecsmobile.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardsAdapter;
import com.jjoseba.pecsmobile.model.Card;

import java.util.ArrayList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        int gridColumns = getResources().getInteger(R.integer.grid_columns);
        if (selectedCards.size() < gridColumns){
            gridColumns = selectedCards.size();
        }


        RecyclerView gridView = findViewById(R.id.cards_gridview);
        gridView.setLayoutManager(new GridLayoutManager(this, gridColumns));
        if (gridColumns == 1){
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            gridView.setPadding(metrics.widthPixels / 4, 0, metrics.widthPixels / 4, 0);
        }

        CardsAdapter adapter = new CardsAdapter(this, selectedCards);
        gridView.setAdapter(adapter);

        final String finalTitle = title;
        adapter.setOnClickListener(new CardsAdapter.CardListener() {
            @Override
            public void cardClicked(Card card) {
                speak(finalTitle);
            }
            @Override
            public void cardLongClicked(Card card) { }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                speak(title, false), 100);
    }

}
