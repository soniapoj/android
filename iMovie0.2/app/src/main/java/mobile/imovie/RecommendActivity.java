package mobile.imovie;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RecommendActivity extends AppCompatActivity {

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


    private static List<DBHelper.Film> recommendedList = new ArrayList<>();
    private GridView lstView;
    private ListDBHelper recommendedDB = new ListDBHelper(this);
    private ListDBHelper watchedDB;
    // private ListDBHelper previousDB = new ListDBHelper(this);
    private JSONObject recList;
    SimpleCursorAdapter adapter;
    //private String Title;
    private static String foundYear;
    private static String foundTitle;
    private static String foundPosterUrl = "";
    private static String foundDirector = "";
    private static String foundWriter = "";
    private static String foundActor1 = "";
    private static String foundActor2 = "";
    private static String foundGenre = "";
    private static String foundRuntime = "";
    private static String foundRated = "";
    private static String foundPlot = "";
    private static String foundAwards = "";
    private static String foundimdbRating = "";
    private ImageView imgView;
    private RequestQueue queue;
    private RequestQueue queue1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recommendedList.clear();
        setContentView(R.layout.activity_show_recommended);
        this.lstView = findViewById(R.id.lstView);
        queue = Volley.newRequestQueue(this);
        queue1 = Volley.newRequestQueue(this);
        getWatchHistory();
        //showRecommended();
    }

    private void getWatchHistory() {
        try {
            if (this.watchedDB == null)
                this.watchedDB = new ListDBHelper(this);
            Cursor c = watchedDB.getAllFilms("favorites");
            c.moveToFirst();
            if (c.getCount() == 0)
                setContentView(R.layout.activity_no_movies);
            while (!c.isAfterLast()) {
                String title = c.getString(c.getColumnIndex("movie_title"));
                int year = Integer.parseInt(c.getString(c.getColumnIndex("movie_year")));
                if (year <= 2017) {

                    queue.add(getRecommandations(title));
                }
                c.moveToNext();
            }
        } catch (Exception ex) {

        }

    }

    private StringRequest getMovieDetails(String Title) {
        final String API = "&apikey=853dfb92";
        final String TITLE_SEARCH = "&t=";
        final String TYPE = "&type=movie";
        final String RELEASE_YEAR = "&y=";
        final String URL_PREFIX = "http://www.omdbapi.com/?";


        String url = URL_PREFIX + API + TITLE_SEARCH + Title + TYPE;

        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject result;
                            result = new JSONObject(response);
                            foundTitle = result.getString("Title");
                            foundYear = result.getString("Year");
                            foundGenre = result.getString("Genre");
                            foundDirector = result.getString("Director");
                            foundWriter = result.getString("Writer");
                            foundRuntime = result.getString("Runtime");
                            foundRuntime = foundRuntime.replace(" min", "");
                            foundRated = result.getString("Rated");
                            foundPlot = result.getString("Plot");
                            foundAwards = result.getString("Awards");
                            foundimdbRating = result.getString("imdbRating");
                            List<String> actorsList = new ArrayList<>();
                            String actorsstring = result.getString("Actors");
                            int len = actorsstring.split(",").length;
                            if (len == 1) {
                                foundActor1 = (actorsstring.split(",")[0]);
                                foundActor2 = "";
                            } else if (len == 2) {
                                foundActor1 = (actorsstring.split(",")[0]);
                                foundActor2 = (actorsstring.split(",")[1]);
                            } else {
                                foundActor1 = "";
                                foundActor2 = "";
                            }
                            foundActor1 = result.getString("Actors");
                            foundActor2 = "";


                            foundPosterUrl = result.getString("Poster");
                            //String foundYear, String foundTitle, String foundPosterUrl, String foundGenre, String foundDirector, String foundScreenwriter, String foundactor1, String foundactor2
                            recommendedList.add(new DBHelper.Film(foundYear, foundTitle, foundPosterUrl, foundGenre, foundDirector, foundWriter, foundActor1, foundActor2, foundRuntime, foundRated, foundPlot, foundAwards, foundimdbRating));
                            System.out.println(recommendedList);
                            showRecommended();




                        } catch (JSONException e) {

                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    private StringRequest getRecommandations(String title) {
        final String URL_PREFIX = "http://eb7c31dbbb3e.ngrok.io/predict?movie=";

        String url = URL_PREFIX + title;

        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // SUCCESS //you can do it!
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {

                        try {
                            List<String> result;
                            response = response.replace("\\", "");
                            response = response.replace("[", "");
                            response = response.replace("]", "");
                            result = Arrays.asList(response.split(","));

                            for (String res : result) {
                                res = res.replace("'", "");

                                queue1.add(getMovieDetails(res));

                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Ooopsie ^^ " + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }


    public void addToWatchlist(View view) {
        View parent = (View) view.getParent();
        View granParent = (View) parent.getParent();
        View titleLayout = (View) granParent.findViewById(R.id.relativeLayout);
        TextView titleView = titleLayout.findViewById(R.id.firstListElement);
        TextView yearView = parent.findViewById(R.id.secondListElement);
        DBHelper watchlistDB = new DBHelper(this);
        DBHelper.Film found = recommendedDB.getFilmByTitleAndYear(titleView.getText().toString(), yearView.getText().toString());
        watchlistDB.addFilm(titleView.getText().toString(), Integer.parseInt(yearView.getText().toString()), found.foundGenre, recommendedDB.getFilmPoster(titleView.getText().toString(), yearView.getText().toString()), found.foundDirector, found.foundScreenwriter, found.foundactor1, found.foundactor2, found.Runtime, found.Rated, found.Plot, found.Awards, found.imdbRating);
        showRecommended();
        Toast.makeText(getApplicationContext(), "Moved to Watchlist!", Toast.LENGTH_SHORT).show();

    }

    private void showRecommended() {
        recommendedDB.deleteAllFilms("recommended");
        System.out.println("here");
        System.out.println(recommendedList);

        for (DBHelper.Film film : recommendedList) {

            recommendedDB.addFilm("recommended", film.foundTitle, Integer.parseInt(film.foundYear), film.foundGenre, film.foundPosterUrl, film.foundDirector, film.foundScreenwriter, film.foundactor1, film.foundactor2, film.Runtime, film.Rated, film.Plot, film.Awards, film.imdbRating);
        }
        try {
            if (this.recommendedDB == null)
                this.recommendedDB = new ListDBHelper(this);
            Cursor c = recommendedDB.getAllFilms("recommended");

            this.lstView = findViewById(R.id.lstView);
            CustomCursorAdapterRecommended customCursorAdapter = new CustomCursorAdapterRecommended(this, c);
            lstView.setAdapter(customCursorAdapter);

        } catch (Exception ex) {
            Toast.makeText(RecommendActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void showDetails(View view) {
        View parent = (View) view.getParent();
        View granParent = (View) parent.getParent();
        View titleLayout = (View) granParent.findViewById(R.id.relativeLayout);
        TextView titleView = titleLayout.findViewById(R.id.firstListElement);
        TextView yearView = parent.findViewById(R.id.secondListElement);

        String Title = titleView.getText().toString();
        String Year = yearView.getText().toString();
        DBHelper.Film thisfilm = recommendedDB.getFilmByTitleAndYear(Title, Year);
        setContentView(R.layout.detailed_film);
        TextView titletext = findViewById(R.id.titleDetail);
        titletext.setText(thisfilm.foundTitle);
        String a = "<b>Title:</b> " + thisfilm.foundTitle;
        a += "\n";
        titletext.setText(Html.fromHtml(a));
        titletext.append("\n");
        a = "";
        a = "<b>Year:</b> " + thisfilm.foundYear;
        TextView yeartext = findViewById(R.id.yearDetail);
        a += "\n";
        yeartext.setText(Html.fromHtml(a));
        yeartext.append("\n");
        ImageView imgView = findViewById(R.id.PosterDetail);
        Picasso.get().load(thisfilm.foundPosterUrl).into(imgView);
        a = "";
        a = "<b>Genre:</b> " + thisfilm.foundGenre;
        TextView genretext = findViewById(R.id.genreDetail);
        genretext.setText(Html.fromHtml(a));
        genretext.append("\n");
        a = "";
        a = "<b>Director:</b> " + thisfilm.foundDirector;
        TextView directortext = findViewById(R.id.DirectorDetail);
        directortext.setText(Html.fromHtml(a));
        directortext.append("\n");
        //directortext.setText(a);
        a = "";
        a = "<b>Screenwriter:</b> " + thisfilm.foundScreenwriter;
        //TextView screenwritertext = findViewById(R.id.ScreenwriterDetail);
        /// screenwritertext.setText(a);
        if (!thisfilm.foundactor1.equals("")) {
            a = "";
            a = "<b>Actor:</b> " + thisfilm.foundactor1;
            if (!thisfilm.foundactor2.equals("")) {
                a += ", ";
                a += thisfilm.foundactor2;
                TextView actor1text = findViewById(R.id.Actor1Detail);
                actor1text.setText(Html.fromHtml(a));
                actor1text.append("\n");
            }
            TextView actor1text = findViewById(R.id.Actor1Detail);
            actor1text.setText(Html.fromHtml(a));
            actor1text.append("\n");
        } else {
            TextView actor1text = findViewById(R.id.Actor1Detail);
            actor1text.setText("");
        }


        a = "";
        a = "<b>Runtime:</b> " + thisfilm.Runtime;
        TextView runtimetext = findViewById(R.id.RuntimeDetail);
        runtimetext.setText(Html.fromHtml(a));
        runtimetext.append("\n");
        a = "";
        a = "<b>Rated:</b> " + thisfilm.Rated;
        TextView ratedtext = findViewById(R.id.RatedDetail);
        ratedtext.setText(Html.fromHtml(a));
        ratedtext.append("\n");
        a = "";
        a = "<b>Plot:</b> " + thisfilm.Plot;
        TextView plottext = findViewById(R.id.PlotDetail);
        plottext.setText(Html.fromHtml(a));
        plottext.append("\n");
        a = "";
        a = "<b>Awards:</b> " + thisfilm.Awards;
        TextView awardstext = findViewById(R.id.AwardsDetail);
        awardstext.setText(Html.fromHtml(a));
        awardstext.append("\n");
        a = "";
        a = "<b>imdbRating:</b> " + thisfilm.imdbRating;
        a += "/10";
        TextView imdbtext = findViewById(R.id.imdbRatingDetail);
        imdbtext.setText(Html.fromHtml(a));
        imdbtext.append("\n");
        a = "";
        a += "         ";
        TextView emptytext = findViewById(R.id.empty);
        emptytext.setText(a);
        emptytext.append("\n");


    }
}