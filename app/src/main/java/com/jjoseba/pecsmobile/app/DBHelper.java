package com.jjoseba.pecsmobile.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.jjoseba.pecsmobile.model.CardPECS;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteAssetHelper {

        private static final String DATABASE_NAME = "pecs.sqlite";
        private static final int DATABASE_VERSION = 1;
        private static final String TABLE_CARDS = "pecs";
        private static final String COLUMN_ID = "id_card";
        private static final String COLUMN_LABEL = "label";
        private static final String COLUMN_CATEGORY = "is_category";
        private static final String COLUMN_COLOR = "color";
        private static final String COLUMN_PARENT = "parent";

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public ArrayList<CardPECS> getCards(int parent) {

            ArrayList<CardPECS> cards = new ArrayList<CardPECS>();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_CARDS, new String[]{COLUMN_ID, COLUMN_CATEGORY, COLUMN_LABEL, COLUMN_COLOR, "image", COLUMN_PARENT},
                    COLUMN_PARENT + " == " + parent, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CardPECS card = new CardPECS();
                card.setCardId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                card.setLabel(cursor.getString(cursor.getColumnIndex(COLUMN_LABEL)));
                card.setAsCategory(cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY)) == 1);
                card.setCardColor(cursor.getString(cursor.getColumnIndex(COLUMN_COLOR)));
                cards.add(card);

                cursor.moveToNext();
            }
            cursor.close();
            return cards;

        }

        public boolean addCard(int parent, CardPECS newCard){

            SQLiteDatabase db = getReadableDatabase();
            ContentValues cardValues = new ContentValues();
            cardValues.put(COLUMN_LABEL, newCard.getLabel());
            cardValues.put(COLUMN_CATEGORY, newCard.isCategory());
            cardValues.put(COLUMN_COLOR, newCard.getHexCardColor());
            cardValues.put(COLUMN_PARENT, parent);
            long id = db.insert(TABLE_CARDS, null, cardValues);
            if (id > 0) newCard.setCardId((int)id);
            return (id > 0);
        }

        public boolean deleteCard(CardPECS cardToDelete){
            SQLiteDatabase db = getReadableDatabase();

            int result = db.delete(TABLE_CARDS, COLUMN_ID + " == " + cardToDelete.getCardId(), null);
            return (result > 0);
        }
}
