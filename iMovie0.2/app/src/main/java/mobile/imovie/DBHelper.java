package mobile.imovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db.movies";
    public static final String TABLE_NAME = "watchlist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RELEASE_YEAR = "year";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_GENRE_1 = "genre1";
    public static final String COLUMN_GENRE_2 = "genre2";
    public static final String COLUMN_GENRE_3 = "genre3";
    public static final String COLUMN_DIRECTOR = "director";
    public static final String COLUMN_WRITER = "screenwriter";
    public static final String COLUMN_ACTOR_1 = "actor1";
    public static final String COLUMN_ACTOR_2 = "actor2";


    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }

    @Override
    public void
    onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table watchlist " +
                        "(_id integer primary key autoincrement, title text,year integer,genre text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS watchlist");
            onCreate(db);
        }
    }

    public boolean addFilm(String filmTitle, Integer releaseYear, String filmGenre) {
        /*,*/
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(COLUMN_TITLE, filmTitle);
        contantValues.put(COLUMN_RELEASE_YEAR, releaseYear);
        contantValues.put(COLUMN_GENRE, filmGenre);
        db.insert(TABLE_NAME, null, contantValues);
        db.close();
        return true;
    }

    public boolean updateFilm(Integer filmId, String filmTitle, Integer releaseYear, String filmGenre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(COLUMN_TITLE, filmTitle);
        contantValues.put(COLUMN_RELEASE_YEAR, releaseYear);
        contantValues.put(COLUMN_GENRE, filmGenre);
        db.update(TABLE_NAME, contantValues, "_id = ?", new String[]{Integer.toString(filmId)});
        db.close();
        return true;
    }

    public Integer deleteFilm(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("watchlist", "_id = ?", new String[]{Integer.toString(id)});
    }

    public Cursor getData(int filmId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from watchlist where _id = " + filmId + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getWritableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public Cursor getAllFilms() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("Select * from watchlist", null);
            return cursor;
        }catch (Exception e){
            return null;
        }
    }
}
