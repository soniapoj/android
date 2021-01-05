package mobile.imovie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static class Film {

        public String foundYear;
        public String foundTitle;
        public String foundPosterUrl = "";
        public String foundGenre;
        public String foundDirector;
        public String foundScreenwriter;
        public String foundactor1;
        public String foundactor2;
        public String Runtime;
        public String Rated;
        public String Plot;
        public String Awards;
        public String imdbRating;


        public Film(String foundYear, String foundTitle, String foundPosterUrl, String foundGenre, String foundDirector, String foundScreenwriter, String foundactor1, String foundactor2, String runtime, String rated, String plot, String awards, String imdbRating) {
            this.foundYear = foundYear;
            this.foundTitle = foundTitle;
            this.foundPosterUrl = foundPosterUrl;
            this.foundGenre = foundGenre;
            this.foundDirector = foundDirector;
            this.foundScreenwriter = foundScreenwriter;
            this.foundactor1 = foundactor1;
            this.foundactor2 = foundactor2;
            Runtime = runtime;
            Rated = rated;
            Plot = plot;
            Awards = awards;
            this.imdbRating = imdbRating;
        }

        @Override
        public String toString() {
            return "Film{" +
                    "foundYear='" + foundYear + '\'' +
                    ", foundTitle='" + foundTitle + '\'' +
                    ", foundPosterUrl='" + foundPosterUrl + '\'' +
                    ", foundGenre='" + foundGenre + '\'' +
                    ", foundDirector='" + foundDirector + '\'' +
                    ", foundScreenwriter='" + foundScreenwriter + '\'' +
                    ", foundactor1='" + foundactor1 + '\'' +
                    ", foundactor2='" + foundactor2 + '\'' +
                    ", Runtime='" + Runtime + '\'' +
                    ", Rated='" + Rated + '\'' +
                    ", Plot='" + Plot + '\'' +
                    ", Awards='" + Awards + '\'' +
                    ", imdbRating='" + imdbRating + '\'' +
                    '}';
        }

        public Film(String foundTitle) {
            this.foundTitle = foundTitle;
        }
    }

    public static final String DATABASE_NAME = "db.movies";
    public static final String TABLE_NAME = "watchlist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RELEASE_YEAR = "year";
    //public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_GENRE_1 = "genre1";
    public static final String COLUMN_DIRECTOR = "director";
    public static final String COLUMN_WRITER = "screenwriter";
    public static final String COLUMN_ACTOR_1 = "actor1";
    public static final String COLUMN_ACTOR_2 = "actor2";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_RUNTIME = "runtime";
    public static final String COLUMN_RATED = "rated";
    public static final String COLUMN_PLOOT = "plot";
    public static final String COLUMN_AWARDS = "awards";
    public static final String COLUMN_IMDBRATING = "imdbrating";


    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 29);
    }

    @Override
    public void
    onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table watchlist" +
                        "(_id integer primary key autoincrement, title text,year integer,genre1 text, director text, screenwriter text, actor1 text, actor2 text, poster text, runtime text, rated text, plot text, awards text, imdbrating text, unique(title, year))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS watchlist");
            onCreate(db);
        }
    }

    public boolean addFilm(String filmTitle, Integer releaseYear, String filmGenre, String posterURL, String Director, String Writer, String Actor1, String Actor2, String Runtime, String Rated, String Plot, String Awards, String imdbRating) {
        /*,*/
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contantValues = new ContentValues();
        contantValues.put(COLUMN_TITLE, filmTitle);
        contantValues.put(COLUMN_RELEASE_YEAR, releaseYear);
        contantValues.put(COLUMN_GENRE_1, filmGenre);
        contantValues.put(COLUMN_DIRECTOR, Director);
        contantValues.put(COLUMN_WRITER, Writer);
        contantValues.put(COLUMN_ACTOR_1, Actor1);
        contantValues.put(COLUMN_ACTOR_2, Actor2);
        contantValues.put(COLUMN_POSTER, posterURL);
        contantValues.put(COLUMN_RUNTIME, Runtime);
        contantValues.put(COLUMN_RATED, Rated);
        contantValues.put(COLUMN_PLOOT, Plot);
        contantValues.put(COLUMN_AWARDS, Awards);
        contantValues.put(COLUMN_IMDBRATING, imdbRating);
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

//    public boolean updateFilm(Integer filmId, String filmTitle, Integer releaseYear, String genre1, String director, String writer, String actor1, String actor2) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contantValues = new ContentValues();
//        contantValues.put(COLUMN_TITLE, filmTitle);
//        contantValues.put(COLUMN_RELEASE_YEAR, releaseYear);
//        contantValues.put(COLUMN_GENRE_1, genre1);
//        //contantValues.put(COLUMN_GENRE_2, genre2);
//        //contantValues.put(COLUMN_GENRE_3, genre3);
//        contantValues.put(COLUMN_DIRECTOR, director);
//        contantValues.put(COLUMN_WRITER, writer);
//        contantValues.put(COLUMN_ACTOR_1, actor1);
//        contantValues.put(COLUMN_ACTOR_2, actor2);
//        db.update(TABLE_NAME, contantValues, "_id = ?", new String[]{Integer.toString(filmId)});
//        db.close();
//        return true;
//    }

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

    public Integer getFilmId(String title, String year) {
        Integer id = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select _id from watchlist where title=? and year=?", new String[]{title, year});
        return res.getInt(res.getColumnIndex("_id"));
    }

    public Film getFilmByTitleAndYear(String title, String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from watchlist where title=? and year=?", new String[]{title, year});
        res.moveToFirst();
        return new Film(res.getString(res.getColumnIndex("year")), res.getString(res.getColumnIndex("title")), res.getString(res.getColumnIndex("poster")), res.getString(res.getColumnIndex("genre1")), res.getString(res.getColumnIndex("director")), res.getString(res.getColumnIndex("screenwriter")), res.getString(res.getColumnIndex("actor1")), res.getString(res.getColumnIndex("actor2")),res.getString(res.getColumnIndex("runtime")),res.getString(res.getColumnIndex("rated")),res.getString(res.getColumnIndex("plot")),res.getString(res.getColumnIndex("awards")),res.getString(res.getColumnIndex("imdbrating")));
    }

    public String getFilmPoster(String title, String year) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select poster from watchlist where title=? and year=?", new String[]{title, year});
        System.out.println(Arrays.toString(res.getColumnNames()));
        System.out.println(res.getColumnIndexOrThrow("poster"));
        System.out.println(res.getCount());
        res.moveToFirst();
        System.out.println(res.getColumnIndex("poster"));
        return res.getString(res.getColumnIndex("poster"));
    }

}