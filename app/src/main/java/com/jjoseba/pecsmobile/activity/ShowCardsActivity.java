package com.jjoseba.pecsmobile.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;
import com.jjoseba.pecsmobile.adapter.SelectedCardsAdapter;
import com.jjoseba.pecsmobile.model.CardPECS;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowCardsActivity extends Activity {

    private ArrayList<CardPECS> selectedCards = new ArrayList<CardPECS>();
    private GridView cardsList;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cards);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setHomeButtonEnabled(false);

        Bundle extras = getIntent().getExtras();
        selectedCards = (ArrayList<CardPECS>) extras.getSerializable("result");
        String title = "";
        for (CardPECS card : selectedCards){ title += card.getLabel() + " ";  }
        getActionBar().setTitle(title);
        cardsList = (GridView) findViewById(R.id.selectedCards);
        CardGridAdapter adapter = new CardGridAdapter(this, R.layout.card_results, selectedCards);
        cardsList.setAdapter(adapter);
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
}
