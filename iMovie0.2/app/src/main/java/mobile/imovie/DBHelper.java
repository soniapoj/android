package mobile.imovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
        super(context, DATABASE_NAME, null, 7);
    }

    @Override
    public void
    onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table watchlist" +
                        "(_id integer primary key autoincrement, title text,year integer,genre1 text, genre2 text, genre3 text, director text, screenwriter text, actor1 text, actor2 text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
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
        contantValues.put(COLUMN_GENRE_1, filmGenre);
        contantValues.put(COLUMN_GENRE_2, "");
        contantValues.put(COLUMN_GENRE_3, "");
        contantValues.put(COLUMN_DIRECTOR, "");
        contantValues.put(COLUMN_WRITER, "");
        contantValues.put(COLUMN_ACTOR_1, "");
        contantValues.put(COLUMN_ACTOR_2, "");
        db.insert(TABLE_NAME, null, contantValues);
        db.close();
        return true;
    }

//    public boolean addFilm(String filmTitle, Integer releaseYear, String genre1, String genre2, String genre3, String director, String writer, String actor1, String actor2) {
//        /*,*/
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contantValues = new ContentValues();
//        contantValues.put(COLUMN_TITLE, filmTitle);
//        contantValues.put(COLUMN_RELEASE_YEAR, releaseYear);
//        contantValues.put(COLUMN_GENRE_1, genre1);
//        contantValues.put(COLUMN_GENRE_2, genre2);
//        contantValues.put(COLUMN_GENRE_3, genre3);
//        contantValues.put(COLUMN_DIRECTOR, director);
//        contantValues.put(COLUMN_WRITER, writer);
//        contantValues.put(COLUMN_ACTOR_1, actor1);
//        contantValues.put(COLUMN_ACTOR_2, actor2);
//        db.insert(TABLE_NAME, null, contantValues);
//        db.close();
//        return true;
//    }

    public boolean updateFilm(Integer filmId, String filmTitle, Integer releaseYear, String genre1, String genre2, String genre3, String director, String writer, String actor1, String actor2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(COLUMN_TITLE, filmTitle);
        contantValues.put(COLUMN_RELEASE_YEAR, releaseYear);
        contantValues.put(COLUMN_GENRE_1, genre1);
        contantValues.put(COLUMN_GENRE_2, genre2);
        contantValues.put(COLUMN_GENRE_3, genre3);
        contantValues.put(COLUMN_DIRECTOR, director);
        contantValues.put(COLUMN_WRITER, writer);
        contantValues.put(COLUMN_ACTOR_1, actor1);
        contantValues.put(COLUMN_ACTOR_2, actor2);
        db.update(TABLE_NAME, contantValues, "_id = ?", new String[]{Integer.toString(filmId)});
        db.close();
        return true;
    }

    public Integer deleteFilm(String title, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        //String whereClause = "title=" + title + " and year=?";
        return db.delete("watchlist", "title=? and year=?", new String[]{title, Integer.toString(year)});
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
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getFilmId(String title, String year){
        Integer id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select _id from watchlist where title=? and year=?", new String[] {title, year});
        return res.getInt(res.getColumnIndex("_id"));
    }
}
