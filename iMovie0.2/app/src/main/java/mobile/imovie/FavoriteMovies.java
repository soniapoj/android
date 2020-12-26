package mobile.imovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteMovies extends AppCompatActivity {
    ListDBHelper mydb;
    TextView title;
    TextView year;
    TextView genre;
    GridView lstView;
    ImageView imgView;
    private RequestQueue queue;
    String foundYear = "";
    String foundGenre = "";
    String foundDirector = "";
    String foundWriter = "";
    String foundActor1 = "";
    String foundActor2 = "";
    String foundPosterUrl = "";
    String foundTitle = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.show_watched);
        setContentView(R.layout.activity_show_watchlist);
        this.lstView = findViewById(R.id.lstView);
        this.mydb = new ListDBHelper(this);
        showFavorites();

    }

    private void showFavorites() {
        try {
            if (this.mydb == null)
                this.mydb = new ListDBHelper(this);
            Cursor c = mydb.getAllFilms("favorites");
            System.out.println("showFavorites()");
            System.out.println(c.getCount());
            CustomCursorAdapterFavorites customCursorAdapter = new CustomCursorAdapterFavorites(this, c);
            lstView.setAdapter(customCursorAdapter);
        } catch (Exception ex) {
            Toast.makeText(FavoriteMovies.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void deletefilm(View view) {
        View parent = (View) view.getParent();
        View granParent = (View) parent.getParent();
        View titleLayout = (View) granParent.findViewById(R.id.relativeLayout);
        TextView titleView = titleLayout.findViewById(R.id.firstListElement);
        TextView yearView = parent.findViewById(R.id.secondListElement);
        System.out.println(titleView);
        System.out.println(yearView);
        System.out.println(titleView.getText() + "   " + yearView.getText());
        if (mydb.deleteFilm("favorites", titleView.getText().toString(), Integer.parseInt(yearView.getText().toString())) > 0) {
            Toast.makeText(getApplicationContext(), "Successfully Deleted! xD", Toast.LENGTH_SHORT).show();
            showFavorites();
        } else {
            Toast.makeText(getApplicationContext(), "Record not deleted :( :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void addMovie(View v) {
        setContentView(R.layout.activity_display_film);
        this.title = findViewById(R.id.editTextTitle);
        this.year = findViewById(R.id.editTextYear);
        this.genre = findViewById(R.id.editTextGenre);
        this.imgView = findViewById(R.id.imageView);
        queue = Volley.newRequestQueue(this);
        title.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                queue.add(searchNameStringRequest(s.toString()));
                if (foundYear != "")
                    year.setText(foundYear);
                if (foundGenre != "")
                    genre.setText(foundGenre);
                if (!foundPosterUrl.equals(""))
                    Picasso.get().load(foundPosterUrl).into(imgView);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });


    }

    private StringRequest searchNameStringRequest(String Title) {
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
                        } catch (JSONException e) {
                            Toast.makeText(FavoriteMovies.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        if (mydb.addFilm("watched", this.foundTitle, Integer.parseInt(this.year.getText().toString()), this.genre.getText().toString(), this.foundPosterUrl, this.foundDirector, this.foundWriter, this.foundActor1, this.foundActor2)) {
            startActivity(new Intent(FavoriteMovies.this, ListManager.class));
            Toast.makeText(getApplicationContext(), "Successfully Added! xD", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Record not added :( :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}

