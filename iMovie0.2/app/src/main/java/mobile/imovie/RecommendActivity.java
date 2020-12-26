package mobile.imovie;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
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

    public class Film {

        public String foundYear;
        public String foundTitle;
        public String foundPosterUrl = "";
        public String foundGenre;
        public String foundDirector;
        public String foundScreenwriter;
        public String foundactor1;
        public String foundactor2;


        public Film(String foundYear, String foundTitle, String foundPosterUrl, String foundGenre, String foundDirector, String foundScreenwriter, String foundactor1, String foundactor2) {
            this.foundYear = foundYear;
            this.foundTitle = foundTitle;
            this.foundPosterUrl = foundPosterUrl;
            this.foundGenre = foundGenre;
            this.foundDirector = foundDirector;
            this.foundScreenwriter = foundScreenwriter;
            this.foundactor1 = foundactor1;
            this.foundactor2 = foundactor2;
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

        //recommendedDB = new ListDBHelper(this);

        //recommendedDB = new ListDBHelper(this);
        getWatchHistory();
        //showRecommended();
    }

    private void getWatchHistory() {
        try {
            if (this.watchedDB == null)
                this.watchedDB = new ListDBHelper(this);
            Cursor c = watchedDB.getAllFilms("favorites");
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String title = c.getString(c.getColumnIndex("movie_title"));
                int year = Integer.parseInt(c.getString(c.getColumnIndex("movie_year")));
                if (year <= 2017) {
                   // Toast.makeText(RecommendActivity.this, "Because you liked: \n" + title, Toast.LENGTH_LONG).show();
                    queue.add(getRecommandations(title));
                }
                c.moveToNext();
            }
        }
         catch (Exception ex) {
            Toast.makeText(RecommendActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        //showRecommended();
    }

    private StringRequest getMovieDetails(String Title) {
        final String API = "&apikey=853dfb92";
        final String TITLE_SEARCH = "&t=";
        final String TYPE = "&type=movie";
        final String RELEASE_YEAR = "&y=";
        final String URL_PREFIX = "http://www.omdbapi.com/?";

        //String url = URL_PREFIX + API + TITLE_SEARCH +Title + TYPE + RELEASE_YEAR + Year;
        String url = URL_PREFIX + API + TITLE_SEARCH + Title + TYPE;

        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result;
                            result = new JSONObject(response);
                            foundTitle = result.getString("Title");
                            foundYear = result.getString("Year");
                            foundGenre = result.getString("Genre");
                            foundDirector = result.getString("Director");
                            foundWriter = result.getString("Writer");
                            String actorsstring = result.getString("Actors");
                            //Toast.makeText(getApplicationContext(), foundGenre,Toast.LENGTH_LONG).show();
                            List<String> actorsList = new ArrayList<>();
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

                            foundPosterUrl = result.getString("Poster");

                            //String foundYear, String foundTitle, String foundPosterUrl, String foundGenre, String foundDirector, String foundScreenwriter, String foundactor1, String foundactor2
                            recommendedList.add(new DBHelper.Film(foundYear, foundTitle, foundPosterUrl, foundGenre, foundDirector, foundWriter, foundActor1, foundActor2));
                            System.out.println(recommendedList);
                            showRecommended();
                            //recommendedDB.addFilm("recommended", foundTitle, Integer.valueOf(foundYear), foundGenre,foundPosterUrl,foundDirector,foundWriter,foundActor1,foundActor2);
                            //  result = new JSONObject(response).getJSONObject("list");
                            // int maxItems = result.getInt("end");
                            //  JSONArray resultList = result.getJSONArray("item");


                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            //Toast.makeText(AddFoodItems.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(),"Ooopsie ^^",Toast.LENGTH_LONG).show();
                        }
                    } // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        //Toast.makeText(AddFoodItems.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private StringRequest getRecommandations(String title) {
        final String URL_PREFIX = "http://e92a8a983e22.ngrok.io/predict?movie=";

        String url = URL_PREFIX + title;

        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // SUCCESS //you can do it!
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {
                        //System.out.println(response);
                        // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            List<String> result;
                            response = response.replace("\\", "");
                            response = response.replace("[", "");
                            response = response.replace("]", "");
                            result = Arrays.asList(response.split(","));
                            //Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
                            for (String res : result) {
                                res = res.replace("'", "");
                                //Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                                queue1.add(getMovieDetails(res));
                                //Toast.makeText(getApplicationContext(), foundPosterUrl, Toast.LENGTH_LONG).show();
                            }
                            //showRecommended();
                            // System.out.println(result);
                            // Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Ooopsie ^^ " + e.getMessage(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        //Toast.makeText(AddFoodItems.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
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
        DBHelper.Film found = recommendedDB.getFilmByTitleAndYear(titleView.getText().toString(),yearView.getText().toString());
        watchlistDB.addFilm(titleView.getText().toString(), Integer.parseInt(yearView.getText().toString()), found.foundGenre, recommendedDB.getFilmPoster(titleView.getText().toString(), yearView.getText().toString()),found.foundDirector,found.foundScreenwriter,found.foundactor1,found.foundactor2);
        showRecommended();
        Toast.makeText(getApplicationContext(), "Moved to Watchlist!", Toast.LENGTH_SHORT).show();

    }
    private void showRecommended() {
        recommendedDB.deleteAllFilms("recommended");
        System.out.println("here");
        System.out.println(recommendedList);
        //public boolean addFilm(String listName, String filmTitle, Integer releaseYear, String filmGenre, String posterURL, String Director, String Writer, String Actor1, String Actor2)
        for(DBHelper.Film film:recommendedList){
            recommendedDB.addFilm("recommended", film.foundTitle, Integer.parseInt(film.foundYear), film.foundGenre, film.foundPosterUrl, film.foundDirector, film.foundScreenwriter, film.foundactor1, film.foundactor2);
        }
        try {
            if (this.recommendedDB == null)
                this.recommendedDB = new ListDBHelper(this);
            Cursor c = recommendedDB.getAllFilms("recommended");
            //Toast.makeText(getApplicationContext(), String.valueOf(c.getCount()), Toast.LENGTH_LONG).show();
            this.lstView = findViewById(R.id.lstView);
            CustomCursorAdapterRecommended customCursorAdapter = new CustomCursorAdapterRecommended(this, c);
            lstView.setAdapter(customCursorAdapter);

        } catch (Exception ex) {
            Toast.makeText(RecommendActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}