package com.jjoseba.pecsmobile.fragment;


import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
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

    public static String PARENT_CATEGORY = "parentCategory";

    private CardPECS parentCategory;
    private ArrayList<CardPECS> pecs = new ArrayList<CardPECS>();
    private CardGridAdapter cardsAdapter;
    private GridItemClickedListener clickListener;


    public static CardsPage newInstance(CardPECS parentCategory) {
        CardsPage f = new CardsPage();
        Bundle args = new Bundle();
        args.putSerializable(PARENT_CATEGORY, parentCategory);
        f.setArguments(args);

        Log.d("parent", "" + parentCategory.getLabel());
        return f;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.parentCategory = (CardPECS) args.get(PARENT_CATEGORY);

        CardPECS pec1 = new CardPECS();
        pec1.setLabel("Prueba1");
        pec1.setCardColor("#00BCD4");
        pecs.add(pec1);

        CardPECS pec2 = new CardPECS();
        pec2.setLabel("Prueba2");
        pec2.setCardColor("#F44336");
        pecs.add(pec2);

        CardPECS pec3 = new CardPECS();
        pec3.setLabel("Prueba3");
        pec3.setCardColor("#FF5722");
        pecs.add(pec3);

        CardPECS pec4 = new CardPECS();
        pec4.setLabel("Prueba4");
        pec4.setCardColor("#4CAF50");
        pecs.add(pec4);

        CardPECS pec5 = new CardPECS();
        pec5.setLabel("Prueba5");
        pec5.setCardColor("#FFC107");
        CardPECS pec6 = new CardPECS();
        pec6.setLabel("Prueba6");
        pec6.setCardColor("#9C27B0");

        pec1.setAsCategory(true);
        pec2.setAsCategory(true);
        pec3.setAsCategory(true);
        pec4.setAsCategory(true);
        pec5.setAsCategory(true);

        pecs.add(pec6);
        pecs.add(pec5);
        pecs.add(pec3);
        pecs.add(pec1);
        pecs.add(pec5);
        pecs.add(pec5);
        pecs.add(pec5);
        pecs.add(new ButtonCard());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.clickListener = (GridItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement GridItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.clickListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.screen_slide, container, false);

        rootView.setBackgroundColor(parentCategory.getCardColor());

        GridView gridView = (GridView) rootView.findViewById(R.id.cards_gridview);
        cardsAdapter = new CardGridAdapter(this.getActivity(), R.layout.card_gridview, pecs);
        gridView.setAdapter(cardsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d("Grid-clicked", "pos:" + position);

                if (clickListener != null){
                    CardPECS clicked = pecs.get(position);
                    if (clicked instanceof ButtonCard){
                        clickListener.onClick(parentCategory, true);
                    }
                    else{
                        clickListener.onClick(clicked, false);
                    }
                }
            }
        });
        return rootView;
    }


    public void addCard(CardPECS card) {
        pecs.add(pecs.size()-1, card);
        cardsAdapter.notifyDataSetChanged();
    }
}
