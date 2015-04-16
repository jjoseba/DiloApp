package com.jjoseba.pecsmobile.app;

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

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public ArrayList<CardPECS> getCards(int parent) {

            ArrayList<CardPECS> cards = new ArrayList<CardPECS>();

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_CARDS, new String[]{"id_card", "is_category", "label", "color", "image", "parent"},
                    "parent == " + parent, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CardPECS card = new CardPECS();
                card.setCardId(cursor.getInt(cursor.getColumnIndex("id_card")));
                card.setLabel(cursor.getString(cursor.getColumnIndex("label")));
                Log.d("CARD", "cat " + cursor.getInt(cursor.getColumnIndex("is_category")));
                card.setAsCategory(cursor.getInt(cursor.getColumnIndex("is_category")) == 1);
                card.setCardColor(cursor.getString(cursor.getColumnIndex("color")));
                cards.add(card);

                cursor.moveToNext();
            }
            cursor.close();
            return cards;

        }
}
