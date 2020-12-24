package mobile.imovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class ListDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db.movies";
    public static final String TABLE_NAME = "movie_lists";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LIST_NAME = "list_name";
    public static final String COLUMN_MOVIE_TITLE = "movie_title";
    public static final String COLUMN_MOVIE_YEAR = "movie_year";
    public static final String COLUMN_MOVIE_POSTER = "movie_poster";

    public ListDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ListDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public ListDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 12);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table movie_lists " +
                        "(_id integer primary key autoincrement, list_name text,movie_title text, movie_year integer, movie_poster text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS movie_lists");
            onCreate(db);
        }
    }


    public boolean addFilm(String listName, String movieTitle, Integer movieYear, String moviePoster) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(COLUMN_LIST_NAME, listName);
        contantValues.put(COLUMN_MOVIE_TITLE, movieTitle);
        contantValues.put(COLUMN_MOVIE_YEAR, movieYear);
        contantValues.put(COLUMN_MOVIE_POSTER, moviePoster);
        db.insert(TABLE_NAME, null, contantValues);
        db.close();
        return true;
    }

    public Integer deleteFilm(String title, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("movie_lists", "title=? and year=?", new String[]{title, Integer.toString(year)});
    }

    public Cursor getAllFilms(String listName) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("Select * from movie_lists where list_name=?", new String[]{listName});
            return cursor;
        } catch (Exception e) {
            return null;
        }
    }
    public int deleteAllFilms(String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("movie_lists", "list_name=?", new String[]{listName});
    }
}
