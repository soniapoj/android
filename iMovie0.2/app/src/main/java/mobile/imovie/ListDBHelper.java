package mobile.imovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

public class ListDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db.movies";
    public static final String TABLE_NAME = "movie_lists";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LIST_NAME = "list_name";
    public static final String COLUMN_MOVIE_TITLE = "movie_title";
    public static final String COLUMN_MOVIE_YEAR = "movie_year";
    public static final String COLUMN_MOVIE_POSTER = "movie_poster";
    public static final String COLUMN_MOVIE_GENRE_1 = "movie_genre";
    public static final String COLUMN_MOVIE_DIRECTOR = "movie_director";
    public static final String COLUMN_MOVIE_WRITER = "movie_writer";
    public static final String COLUMN_MOVIE_ACTOR_1 = "movie_actor1";
    public static final String COLUMN_MOVIE_ACTOR_2 = "movie_actor2";
    public static final String COLUMN_MOVIE_RUNTIME = "movie_runtime";
    public static final String COLUMN_MOVIE_RATED = "movie_rated";
    public static final String COLUMN_MOVIE_PLOT = "movie_plot";
    public static final String COLUMN_MOVIE_AWARDS = "movie_awards";
    public static final String COLUMN_MOVIE_IMDBRATING = "movie_imdbrating";


    public ListDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ListDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public ListDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 29);
        //deleteAllFilms("recommended");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table movie_lists " +
                        "(_id integer primary key autoincrement, list_name text,movie_title text, movie_year integer, movie_genre text, movie_director text, movie_writer text, movie_actor1 text, movie_actor2 text,movie_poster text, movie_runtime text,movie_rated text, movie_plot text, movie_awards text, movie_imdbrating text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS movie_lists");
            onCreate(db);
        }
    }


    public boolean addFilm(String listName, String filmTitle, Integer releaseYear, String filmGenre, String posterURL, String Director, String Writer, String Actor1, String Actor2, String Runtime, String Rated, String Plot, String Awards, String imdbRating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(COLUMN_LIST_NAME, listName);
        contantValues.put(COLUMN_MOVIE_TITLE, filmTitle);
        contantValues.put(COLUMN_MOVIE_YEAR, releaseYear);
        contantValues.put(COLUMN_MOVIE_POSTER, posterURL);
        contantValues.put(COLUMN_MOVIE_GENRE_1, filmGenre);
        contantValues.put(COLUMN_MOVIE_DIRECTOR, Director);
        contantValues.put(COLUMN_MOVIE_WRITER, Writer);
        contantValues.put(COLUMN_MOVIE_ACTOR_1, Actor1);
        contantValues.put(COLUMN_MOVIE_ACTOR_2, Actor2);
        contantValues.put(COLUMN_MOVIE_RUNTIME, Runtime);
        contantValues.put(COLUMN_MOVIE_RATED, Rated);
        contantValues.put(COLUMN_MOVIE_PLOT, Plot);
        contantValues.put(COLUMN_MOVIE_AWARDS, Awards);
        contantValues.put(COLUMN_MOVIE_IMDBRATING, imdbRating);
        try {
            db.insert(TABLE_NAME, null, contantValues);
            db.close();
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public Integer deleteFilm(String title, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("movie_lists", "movie_title=? and movie_year=?", new String[]{title, Integer.toString(year)});
        db.close();
        return result;
    }

    public Integer deleteFilm(String listName, String title, int year) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("movie_lists", "list_name=? and movie_title=? and movie_year=?", new String[]{listName, title, Integer.toString(year)});
        db.close();
        return result;
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

    public void deleteAllFilms(String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME + " where list_name='" + listName + "'");
        db.close();
    }

    public DBHelper.Film getFilmByTitleAndYear(String title, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from movie_lists where movie_title=? and movie_year=?", new String[]{title, year});
        res.moveToFirst();
        System.out.println(res.getString(res.getColumnIndex("movie_writer")));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return new DBHelper.Film(res.getString(res.getColumnIndex("movie_year")), res.getString(res.getColumnIndex("movie_title")), res.getString(res.getColumnIndex("movie_poster")), res.getString(res.getColumnIndex("movie_genre")), res.getString(res.getColumnIndex("movie_director")), res.getString(res.getColumnIndex("movie_writer")), res.getString(res.getColumnIndex("movie_actor1")), res.getString(res.getColumnIndex("movie_actor2")),res.getString(res.getColumnIndex("movie_runtime")),res.getString(res.getColumnIndex("movie_rated")),res.getString(res.getColumnIndex("movie_plot")),res.getString(res.getColumnIndex("movie_awards")),res.getString(res.getColumnIndex("movie_imdbrating")));
    }

    public String getFilmPoster(String title, String year) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select movie_poster from movie_lists where movie_title=? and movie_year=?", new String[]{title, year});
        System.out.println(Arrays.toString(res.getColumnNames()));
        System.out.println(res.getColumnIndexOrThrow("movie_poster"));
        System.out.println(res.getCount());
        res.moveToFirst();
        System.out.println(res.getColumnIndex("movie_poster"));
        return res.getString(res.getColumnIndex("movie_poster"));
    }
}
