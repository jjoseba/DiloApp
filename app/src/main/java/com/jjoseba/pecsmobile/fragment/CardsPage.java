package com.jjoseba.pecsmobile.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;
import com.jjoseba.pecsmobile.model.CardPECS;
import com.jjoseba.pecsmobile.ui.ButtonCard;
import com.jjoseba.pecsmobile.ui.GridItemClickedListener;

import java.util.ArrayList;

/**
 * Created by Joseba on 26/12/2014.
 */
public class CardsPage extends Fragment {

    private int numPage;

    public CardsPage(){
        super();
    }
    private GridItemClickedListener clickListener;

    public CardsPage(int position) {
        super();
        numPage = position;
    }

    public void setOnClickListener(GridItemClickedListener listener){
        this.clickListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.screen_slide, container, false);

        rootView.setBackgroundColor(getResources().getColor(numPage>0?(numPage>1? R.color.blue:R.color.green):R.color.mainbg));

        GridView gridView = (GridView) rootView.findViewById(R.id.cards_gridview);

        ArrayList<CardPECS> pecs = new ArrayList<CardPECS>();
        CardPECS pec1 = new CardPECS();
        pec1.setLabel("Prueba");
        pec1.setCardColor("#00BCD4");
        pecs.add(pec1);

        CardPECS pec2 = new CardPECS();
        pec2.setLabel("Prueba");
        pec2.setCardColor("#F44336");
        pecs.add(pec2);

        CardPECS pec3 = new CardPECS();
        pec3.setLabel("Prueba");
        pec3.setCardColor("#FF5722");
        pecs.add(pec3);

        CardPECS pec4 = new CardPECS();
        pec4.setLabel("Prueba");
        pec4.setCardColor("#4CAF50");
        pecs.add(pec4);

        CardPECS pec5 = new CardPECS();
        pec5.setLabel("Prueba");
        pec5.setCardColor("#FFC107");
        CardPECS pec6 = new CardPECS();
        pec6.setLabel("Prueba");
        pec6.setCardColor("#9C27B0");
        pecs.add(pec6);
        pecs.add(pec5);
        pecs.add(pec3);
        pecs.add(pec1);
        pecs.add(pec5);
        pecs.add(pec5);
        pecs.add(pec5);
        pecs.add(new ButtonCard());

        gridView.setAdapter(new CardGridAdapter(this.getActivity(), R.layout.card_gridview, pecs));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("Grid-clicked", "pos:" + position);
                if (clickListener != null){
                    clickListener.onClick(position);
                }
            }
        });
        return rootView;
    }
}
