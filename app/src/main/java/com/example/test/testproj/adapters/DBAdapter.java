package com.example.test.testproj.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.example.test.testproj.helpers.DBHelper;
import com.example.test.testproj.models.Show;

/**
 * An adapter that helps to manipulate data in database
 *
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */


public class DBAdapter {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DBAdapter(Context context) {
        dbHelper = new DBHelper(context.getApplicationContext());
        dbHelper.create_db();
    }

    public DBAdapter open() {
        database = dbHelper.open();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public Cursor getAllEntries() {
        String[] columns = new String[]{DBHelper.COLUMN_ID, DBHelper.COLUMN_NAME, DBHelper.COLUMN_DESCRIPTION, DBHelper.COLUMN_IMAGE, DBHelper.COLUMN_URL, DBHelper.COLUMN_RATE, DBHelper.COLUMN_FAVORITE};
        return database.query(DBHelper.TABLE, columns, null, null, null, null, null);
    }

    public List<Show> getShows() {
        ArrayList<Show> shows = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
                String image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE));
                String url = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
                double rate = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_RATE));
                int favorite = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAVORITE));
                shows.add(new Show(id, name, description, image, url, rate, favorite));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return shows;
    }

    public boolean showAlreadyExists(Show show) {
        boolean bool = false;
        List<Show> allFavoritesShow = getShows();
        if (allFavoritesShow != null) {
            for (Show checkShow : allFavoritesShow) {
                if(show.getUrl().equals(checkShow.getUrl())&&show.getName().equals(checkShow.getName())) bool =true;
            }
        }
        return bool;
    }

    public long getCount() {
        return DatabaseUtils.queryNumEntries(database, DBHelper.TABLE);
    }

    public List<Show> getShowsByNonFullName(String showNonFullName) {
        ArrayList<Show> shows = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s LIKE ?", DBHelper.TABLE, DBHelper.COLUMN_NAME);
        Cursor cursor = database.rawQuery(query, new String[]{"%" + showNonFullName + "%"});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
                String image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE));
                String url = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
                double rate = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_RATE));
                int favorite = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAVORITE));
                shows.add(new Show(id, name, description, image, url, rate, favorite));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return shows;
    }

    public Show getShowByName(String showName) {
        Show show = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DBHelper.TABLE, DBHelper.COLUMN_NAME);
        Cursor cursor = database.rawQuery(query, new String[]{showName});
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
            String image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE));
            String url = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
            double rate = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_RATE));
            int favorite = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAVORITE));
            show = new Show(id, name, description, image, url, rate, favorite);
        }
        cursor.close();
        return show;
    }

    public Show getShowById(long id) {
        Show show = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DBHelper.TABLE, DBHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION));
            String image = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_IMAGE));
            String url = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
            double rate = cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_RATE));
            int favorite = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FAVORITE));
            show = new Show(id, name, description, image, url, rate, favorite);
        }
        cursor.close();
        return show;
    }

    public long insert(Show show) {

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAME, show.getName());
        cv.put(DBHelper.COLUMN_DESCRIPTION, show.getDescription());
        cv.put(DBHelper.COLUMN_IMAGE, show.getImage());
        cv.put(DBHelper.COLUMN_URL, show.getUrl());
        cv.put(DBHelper.COLUMN_RATE, show.getRate());
        cv.put(DBHelper.COLUMN_FAVORITE, show.getFav());

        return database.insert(DBHelper.TABLE, null, cv);
    }

    public long delete(long showId) {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(showId)};
        return database.delete(DBHelper.TABLE, whereClause, whereArgs);
    }

    public long update(Show show) {

        String whereClause = DBHelper.COLUMN_ID + "=" + String.valueOf(show.getId());
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAME, show.getName());
        cv.put(DBHelper.COLUMN_DESCRIPTION, show.getDescription());
        cv.put(DBHelper.COLUMN_IMAGE, show.getImage());
        cv.put(DBHelper.COLUMN_URL, show.getUrl());
        cv.put(DBHelper.COLUMN_RATE, show.getRate());
        cv.put(DBHelper.COLUMN_FAVORITE, show.getFav());
        return database.update(DBHelper.TABLE, cv, whereClause, null);
    }
}