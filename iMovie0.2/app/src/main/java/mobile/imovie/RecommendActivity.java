package mobile.imovie;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
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

public class RecommendActivity extends AppCompatActivity {

    private ListView lstView;
    private ListDBHelper recommendedDB;
    private ListDBHelper watchedDB;
    private JSONObject recList;
    SimpleCursorAdapter adapter;
    private String Title;
    private static String foundYear;
    private static String foundTitle;
    private static String foundPosterUrl = "";
    private ImageView imgView;
    private RequestQueue queue;
    private RequestQueue queue1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        this.lstView = findViewById(R.id.lstView);
        this.recommendedDB = new ListDBHelper(this);
        this.recommendedDB.deleteAllFilms("recommended");
        queue = Volley.newRequestQueue(this);
        queue1 = Volley.newRequestQueue(this);
        getWatchHistory();
        //showRecommended();
    }

    private void getWatchHistory() {
        try {
            if (this.watchedDB == null)
                this.watchedDB = new ListDBHelper(this);
            Cursor c = watchedDB.getAllFilms("watched");
            c.moveToFirst();
            c.moveToNext();
            String title = c.getString(c.getColumnIndex("movie_title"));
            int year = Integer.parseInt(c.getString(c.getColumnIndex("movie_year")));
            if(year <= 2017){
                queue.add(getRecommandations(title));
                //Toast.makeText(RecommendActivity.this, title, Toast.LENGTH_LONG).show();
                showRecommended();
            }
        } catch (Exception ex) {
            Toast.makeText(RecommendActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private StringRequest getMovieDetails(String str){
        final String API = "&apikey=853dfb92";
        final String TITLE_SEARCH = "&t=";
        final String TYPE = "&type=movie";
        final String RELEASE_YEAR = "&y=";
        final String URL_PREFIX = "http://www.omdbapi.com/?";

        //String url = URL_PREFIX + API + TITLE_SEARCH +Title + TYPE + RELEASE_YEAR + Year;
        String url = URL_PREFIX + API + TITLE_SEARCH + str + TYPE;
        // 1st param => type of method (GET/PUT/POST/PATCH/etc)
        // 2nd param => complete url of the API
        // 3rd param => Response.Listener -> Success procedure
        // 4th param => Response.ErrorListener -> Error procedure
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS //you can do it!
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {
                        // try/catch block for returned JSON data
                        // see API's documentation for returned format
                        try {
                            JSONObject result;
                            result = new JSONObject(response);
                            foundYear = result.getString("Year");
                            foundPosterUrl = result.getString("Poster");
                            //System.out.println(foundPosterUrl);
                           // Toast.makeText(getApplicationContext(),foundPosterUrl,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Ooopsie ^^",Toast.LENGTH_SHORT).show();
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

    private StringRequest getRecommandations(String title){
        final String URL_PREFIX = "http://bf721c0b70ba.ngrok.io/predict?movie=";

        String url = URL_PREFIX + title;

        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // SUCCESS //you can do it!
                    @SuppressLint("ShowToast")
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                       // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        try {
                            List<String> result;
                            response = response.replace("\\", "");
                            response = response.replace("[", "");
                            response = response.replace("]", "");
                            result = Arrays.asList(response.split(","));
                            //Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
                            for(String res: result){
                                res = res.replace("'", "");
                             //   Toast.makeText(getApplicationContext(),res,Toast.LENGTH_LONG).show();
                                queue1.add(getMovieDetails(res));
                                Toast.makeText(getApplicationContext(), foundPosterUrl, Toast.LENGTH_LONG).show();
                                if(!foundPosterUrl.equals("")) {
                                    recommendedDB.addFilm("recommended", res, Integer.valueOf(foundYear), foundPosterUrl);

                                }
                            }
                           // System.out.println(result);
                           // Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"Ooopsie ^^ " + e.getMessage(),Toast.LENGTH_LONG).show();
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

    private void showRecommended() {
        try {
            if (this.recommendedDB == null)
                this.recommendedDB = new ListDBHelper(this);
            Cursor c = recommendedDB.getAllFilms("recommended");
            Toast.makeText(getApplicationContext(), "cursor", Toast.LENGTH_LONG).show();
            this.lstView = findViewById(R.id.lstView);
            CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, c);
            lstView.setAdapter(customCursorAdapter);

        } catch (Exception ex) {
            Toast.makeText(RecommendActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}