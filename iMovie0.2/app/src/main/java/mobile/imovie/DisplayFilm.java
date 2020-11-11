package mobile.imovie;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayFilm extends AppCompatActivity {
    DBHelper mydb;
    TextView title;
    TextView year;
    TextView genre;
    ListView lstView;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_watchlist);
        this.lstView = findViewById(R.id.lstView);
        this.mydb = new DBHelper(this);
        showWatchlist();

    }

    private void showWatchlist() {
        try {
            int[] id = {R.id.firstListElement, R.id.secondListElement};
            String[] title = new String[]{"title", "year"};
            if (this.mydb == null)
                this.mydb = new DBHelper(this);
            Cursor c = mydb.getAllFilms();
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    R.layout.list_template, c, title, id, 0);
            lstView.setAdapter(adapter);

        } catch (Exception ex) {
            Toast.makeText(DisplayFilm.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void addMovie(View v) {
        setContentView(R.layout.activity_display_film);

    }
    private StringRequest searchNameStringRequest(String Title, int Year) {
        final String API = "&apikey=853dfb92";
        final String TITLE_SEARCH = "&t=";
        final String TYPE = "&type=movie";
        final String RELEASE_YEAR = "&y=";
        final String URL_PREFIX = "http://www.omdbapi.com/?";

        String url = URL_PREFIX + API + TITLE_SEARCH +Title + TYPE + RELEASE_YEAR + Year;

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
                            //System.out.println("YOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
                            Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
                          //  result = new JSONObject(response).getJSONObject("list");
                           // int maxItems = result.getInt("end");
                          //  JSONArray resultList = result.getJSONArray("item");


                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            //Toast.makeText(AddFoodItems.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"Ooopsie ^^",Toast.LENGTH_LONG).show();
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

    public void saveData(View view) {
        this.title = findViewById(R.id.editTextTitle);
        this.year = findViewById(R.id.editTextYear);
        this.genre = findViewById(R.id.editTextGenre);
        if (mydb.addFilm(this.title.getText().toString(), Integer.parseInt(this.year.getText().toString()), this.genre.getText().toString())) {
           // Toast.makeText(getApplicationContext(), "Successfully Added! xD", Toast.LENGTH_SHORT).show();
            queue = Volley.newRequestQueue(this);
            queue.add(searchNameStringRequest(this.title.getText().toString(), Integer.parseInt(this.year.getText().toString())));
            //searchNameStringRequest("It", 2017);

        } else {
            Toast.makeText(getApplicationContext(), "Record not added :( :(", Toast.LENGTH_SHORT).show();
        }

    }

    public void deletefilm(View view){
        this.title = findViewById(R.id.firstListElement);
        this.year = findViewById(R.id.secondListElement);
        if (mydb.deleteFilm(this.title.getText().toString(), Integer.parseInt(year.getText().toString()))>0) {
            Toast.makeText(getApplicationContext(), "Successfully Deleted! xD", Toast.LENGTH_SHORT).show();
            showWatchlist();

                } else {
            Toast.makeText(getApplicationContext(), "Record not deleted :( :(", Toast.LENGTH_SHORT).show();
        }

    }
}

