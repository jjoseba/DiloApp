package com.jjoseba.pecsmobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.PrefsActivity;
import com.jjoseba.pecsmobile.adapter.CardsAdapter;
import com.jjoseba.pecsmobile.app.DBHelper;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.CardsGridListener;
import com.jjoseba.pecsmobile.ui.cards.ButtonCard;
import com.jjoseba.pecsmobile.ui.cards.TempButtonCard;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CardsPageFragment extends Fragment {

    public static final String PARENT_CATEGORY = "parentCategory";

    private Card parentCategory;
    private List<Card> pecs = new ArrayList<>();
    private CardsAdapter cardsAdapter;
    private CardsGridListener clickListener;
    private int specialButtonsCount = 0;

    public static CardsPageFragment newInstance(Card parentCategory, boolean showAddButton, boolean showTempButton) {
        CardsPageFragment f = new CardsPageFragment();
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
    public void onAttach(@NonNull Activity activity) {
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
                R.layout.fragment_card_slide, container, false);

        rootView.setBackgroundColor(parentCategory.getCardColor());
        RecyclerView gridView = rootView.findViewById(R.id.cards_gridview);
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(new GridLayoutManager(this.getContext(), getResources().getInteger(R.integer.grid_columns)));
        cardsAdapter = new CardsAdapter(this.getActivity(), pecs);
        gridView.setAdapter(cardsAdapter);
        cardsAdapter.setOnClickListener(new CardsAdapter.CardListener() {
            @Override
            public void cardClicked(Card clicked) {
                if (clickListener == null) {
                    return;
                }
                if (clicked instanceof ButtonCard) {
                    if (clicked instanceof TempButtonCard)
                        clickListener.onTempCardButton();
                    else
                        clickListener.onAddCardButton(parentCategory);
                } else if (!clicked.isDisabled()){
                    clickListener.onCardSelected(clicked);
                }
            }

            @Override
            public void cardLongClicked(Card clicked) {
                if ((clickListener != null) && !(clicked instanceof ButtonCard)) {
                    clickListener.onCardLongClick(clicked);
                }
            }
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

    public void notifyCardChanged(Card changed, boolean deleted){

        if (deleted){
            pecs.remove(changed);
        }
        else{
            //Substitute the previous card for the new one
            for (int i=0; i<pecs.size(); i++){
                if (pecs.get(i).getCardId() == changed.getCardId()){
                    pecs.set(i, changed);
                    break;
                }
            }
        }
        cardsAdapter.notifyDataSetChanged();
    }
}
