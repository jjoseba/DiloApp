package com.jjoseba.pecsmobile.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.cards.CardPECS;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteAssetHelper {

        private static final String DATABASE_NAME = "pecs.sqlite";
        private static final int DATABASE_VERSION = 2;
        private static final String TABLE_CARDS = "pecs";
        private static final String COLUMN_ID = "id_card";
        private static final String COLUMN_LABEL = "label";
        private static final String COLUMN_CATEGORY = "is_category";
        private static final String COLUMN_COLOR = "color";
        private static final String COLUMN_PARENT = "parent";
        private static final String COLUMN_IMAGE = "image";
        private static final String COLUMN_DISABLED = "is_disabled";
        private static DBHelper instance;

        public static DBHelper getInstance(Context ctx){
            if (instance == null){
                instance = new DBHelper(ctx);
            }
            return instance;
        }

        private DBHelper(Context context) {
            super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        }

        protected ContentValues getRowValuesFromCard(Card card){
            ContentValues cardValues = new ContentValues();
            cardValues.put(COLUMN_LABEL, card.getLabel());
            cardValues.put(COLUMN_CATEGORY, card.isCategory());
            cardValues.put(COLUMN_COLOR, card.getHexCardColor());
            cardValues.put(COLUMN_PARENT, card.getParentID());
            cardValues.put(COLUMN_IMAGE, card.getImageFilename());
            cardValues.put(COLUMN_DISABLED, card.isDisabled());

            return cardValues;
        }

        public ArrayList<Card> getCards(int parent) {

            ArrayList<Card> cards = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_CARDS, new String[]{COLUMN_ID, COLUMN_CATEGORY, COLUMN_LABEL, COLUMN_COLOR, COLUMN_IMAGE, COLUMN_PARENT, COLUMN_DISABLED},
                    COLUMN_PARENT + " == " + parent, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Card card = new CardPECS();
                card.setCardId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                card.setParentID(cursor.getInt(cursor.getColumnIndex(COLUMN_PARENT)));
                card.setLabel(cursor.getString(cursor.getColumnIndex(COLUMN_LABEL)));
                card.setAsCategory(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY)) == 1);
                card.setCardColor(cursor.getString(cursor.getColumnIndex(COLUMN_COLOR)));
                card.setImageFilename(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                card.setDisabled(cursor.getInt(cursor.getColumnIndex(COLUMN_DISABLED)) == 1);
                cards.add(card);

                cursor.moveToNext();
            }
            cursor.close();
            return cards;

        }

        public boolean addCard(int parent, Card newCard){

            SQLiteDatabase db = getReadableDatabase();
            newCard.setParentID(parent);
            long id = db.insert(TABLE_CARDS, null, getRowValuesFromCard(newCard));
            if (id > 0) newCard.setCardId((int)id);

            return (id > 0);
        }

        public boolean deleteCard(Card cardToDelete){
            SQLiteDatabase db = getReadableDatabase();
            int cardID = cardToDelete.getCardId();
            int result = db.delete(TABLE_CARDS, COLUMN_ID + " == " + cardID + " OR " + COLUMN_PARENT + " == " + cardID, null);
            return (result > 0);
        }

        public boolean updateCard(Card card){
            SQLiteDatabase db = getReadableDatabase();
            int changes = db.update(TABLE_CARDS, getRowValuesFromCard(card), COLUMN_ID + " == " + card.getCardId(), null);
            return (changes > 0);
        }
}
