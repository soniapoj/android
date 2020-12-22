package mobile.imovie;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class RecommendActivity extends AppCompatActivity {

    private ListView lstView;
    private ListDBHelper watchedDB;
    private JSONObject recList;
    SimpleCursorAdapter adapter;
    private String Title;
    private String foundYear;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        this.lstView = findViewById(R.id.lstView);
        this.watchedDB = new ListDBHelper(this);
        showRecommended();
    }

    private StringRequest getReleaseYear(String str){
        final String API = "&apikey=853dfb92";
        final String TITLE_SEARCH = "&t=";
        final String TYPE = "&type=movie";
        final String RELEASE_YEAR = "&y=";
        final String URL_PREFIX = "http://www.omdbapi.com/?";

        //String url = URL_PREFIX + API + TITLE_SEARCH +Title + TYPE + RELEASE_YEAR + Year;
        String url = URL_PREFIX + API + TITLE_SEARCH +Title + TYPE;

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
        final String URL_PREFIX = "http://1d8d977ebaed.ngrok.io/predict?movie=";

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
                            JSONArray result;
                            response = response.replace("\\", "");
                            result = new JSONArray(response);
                            System.out.println(response);
                            Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
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
            int[] id = {R.id.firstListElement, R.id.secondListElement};
            String[] title = new String[]{"title", "year"};
            if (this.watchedDB == null)
                this.watchedDB = new ListDBHelper(this);
            Cursor c = watchedDB.getAllFilms("watched");
            String str = "";
            queue = Volley.newRequestQueue(this);
            if (c.moveToFirst()) {
                //System.out.println(Arrays.toString(c.getColumnNames()));
                c.moveToNext();
                c.moveToNext();
                str = c.getString(c.getColumnIndex("movie_title"));
                System.out.println(str);
            }
            queue.add(getRecommandations(str));
//            adapter = new SimpleCursorAdapter(this,
//                    R.layout.watched_list_template, c, title, id, 0);
//            lstView.setAdapter(adapter);

        } catch (Exception ex) {
            Toast.makeText(RecommendActivity.this, "bubu" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}