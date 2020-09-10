package com.jjoseba.pecsmobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.CardsGridListener;
import com.jjoseba.pecsmobile.ui.cards.ButtonCard;
import com.jjoseba.pecsmobile.ui.cards.TempButtonCard;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class CardsPage extends Fragment {

    public static String PARENT_CATEGORY = "parentCategory";

    private Card parentCategory;
    private ArrayList<Card> pecs = new ArrayList<>();
    private CardGridAdapter cardsAdapter;
    private CardsGridListener clickListener;
    private int specialButtonsCount = 0;

    public static CardsPage newInstance(Card parentCategory, boolean showAddButton, boolean showTempButton) {
        CardsPage f = new CardsPage();
        Bundle args = new Bundle();
        args.putBoolean(PrefsActivity.SHOW_ADD_CARD, showAddButton);
        args.putBoolean(PrefsActivity.SHOW_TEMPTEXT_CARD, showTempButton);
        args.putSerializable(PARENT_CATEGORY, parentCategory);
        f.setArguments(args);

        return f;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.parentCategory = (Card) args.get(PARENT_CATEGORY);

        pecs.addAll(getCards());

        boolean showAddButton = args.getBoolean(PrefsActivity.SHOW_ADD_CARD);
        boolean showTempButton = args.getBoolean(PrefsActivity.SHOW_TEMPTEXT_CARD);
        if (showAddButton) {
            specialButtonsCount++;
            pecs.add(new ButtonCard());
        }
        if (showTempButton){
            specialButtonsCount++;
            pecs.add(new TempButtonCard());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.clickListener = (CardsGridListener) activity;
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

        GridView gridView = rootView.findViewById(R.id.cards_gridview);
        cardsAdapter = new CardGridAdapter(this.getActivity(), R.layout.card_gridview, pecs);
        gridView.setAdapter(cardsAdapter);
        gridView.setOnItemClickListener((parent, v, position, id) -> {
            if (clickListener != null) {
                Card clicked = pecs.get(position);
                if (clicked instanceof ButtonCard) {
                    if (clicked instanceof TempButtonCard)
                        clickListener.onTempCardButton();
                    else
                        clickListener.onAddCardButton(parentCategory);
                } else if (!clicked.isDisabled()){
                    clickListener.onCardSelected(clicked);
                }
            }
        });

        gridView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (clickListener != null) {
                Card clicked = pecs.get(position);
                if (!(clicked instanceof ButtonCard)) {
                    clickListener.onCardLongClick(clicked);
                }
            }
            return true;
        });
        return rootView;
    }


    public void addCard(Card card) {
        pecs.add(pecs.size() - specialButtonsCount, card);
        cardsAdapter.notifyDataSetChanged();
    }

    public ArrayList<Card> getCards(){
        DBHelper db = DBHelper.getInstance(this.getActivity());
        ArrayList<Card> pecsList = db.getCards(parentCategory == null? 0 : parentCategory.getCardId());
        for (Card card : pecs){
            if (card.getHexCardColor() == null){
                card.setCardColor(parentCategory.getHexCardColor());
            }
        }
        return pecsList;
    }

    public void notifyCardChanged(Card card, boolean deleted){

        if (deleted){
            cardsAdapter.removeCard(card);
        }
        cardsAdapter.notifyDataSetChanged();
    }
}
