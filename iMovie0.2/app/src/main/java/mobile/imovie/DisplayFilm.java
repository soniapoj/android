package mobile.imovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.view.View.VISIBLE;

public class DisplayFilm extends AppCompatActivity {


    DBHelper mydb;
    TextView title;
    TextView year;
    TextView genre;
    GridView lstView;
    ImageView imgView;
    private RequestQueue queue;
    EditText titleEdit;
    EditText yearEdit;
    EditText genreEdit;
    static String foundYear = "";
    static String foundGenre = "";
    String foundDirector = "";
    String foundWriter = "";
    String foundActor1 = "";
    String foundActor2 = "";
    static String foundPosterUrl = "";
    String foundTitle = "";
    String foundRuntime = "";
    String foundRated = "";
    String foundPlot = "";
    String foundAwards = "";
    String foundimdbRating = "";
    SimpleCursorAdapter adapter;
    RequestFuture<JSONObject> future = RequestFuture.newFuture();


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
            if (this.mydb == null)
                this.mydb = new DBHelper(this);
            Cursor c = mydb.getAllFilms();
            if (c.getCount() == 0) {
                ImageView i = findViewById(R.id.showifnotimg);
                i.setVisibility(VISIBLE);
                TextView t = findViewById(R.id.showifnottxt);
                t.setVisibility(VISIBLE);
            }
            this.lstView = findViewById(R.id.lstView);
            CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, c);
            lstView.setAdapter(customCursorAdapter);

        } catch (Exception ex) {
            Toast.makeText(DisplayFilm.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void addMovie(View v) {
        setContentView(R.layout.activity_display_film);
        queue = Volley.newRequestQueue(this);
        this.title = findViewById(R.id.editTextTitle);
        title.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                queue.add(searchNameStringRequest(s.toString()));
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
                            title = findViewById(R.id.editTextTitle);
                            year = findViewById(R.id.editTextYear);
                            genre = findViewById(R.id.editTextGenre);
                            imgView = findViewById(R.id.imageView);
                            if (foundYear != "")
                                year.setText(foundYear);
                            if (foundGenre != "")
                                genre.setText(foundGenre);
                            if (!foundPosterUrl.equals(""))
                                Picasso.get().load(foundPosterUrl).into(imgView);
                        } catch (JSONException e) {
                           // Toast.makeText(DisplayFilm.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
        if (mydb.addFilm(this.foundTitle, Integer.parseInt(this.year.getText().toString()), this.genre.getText().toString(), this.foundPosterUrl, this.foundDirector, this.foundWriter, this.foundActor1, this.foundActor2, this.foundRuntime, this.foundRated, this.foundPlot, this.foundAwards, this.foundimdbRating)) {
            startActivity(new Intent(DisplayFilm.this, DisplayFilm.class));
            Toast.makeText(getApplicationContext(), "Successfully Added! xD", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "Record not added :( :(", Toast.LENGTH_SHORT).show();
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
        if (mydb.deleteFilm(titleView.getText().toString(), Integer.parseInt(yearView.getText().toString())) > 0) {
            Toast.makeText(getApplicationContext(), "Successfully Deleted! xD", Toast.LENGTH_SHORT).show();
            showWatchlist();
        } else {
            Toast.makeText(getApplicationContext(), "Record not deleted :( :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void addToWatched(View view) {
        View parent = (View) view.getParent();
        View granParent = (View) parent.getParent();
        View titleLayout = (View) granParent.findViewById(R.id.relativeLayout);
        TextView titleView = titleLayout.findViewById(R.id.firstListElement);
        TextView yearView = parent.findViewById(R.id.secondListElement);
        ListDBHelper listDB = new ListDBHelper(this);
        DBHelper.Film found = mydb.getFilmByTitleAndYear(titleView.getText().toString(), yearView.getText().toString());
        System.out.println(found);
        listDB.addFilm("watched", titleView.getText().toString(), Integer.parseInt(yearView.getText().toString()), found.foundGenre, mydb.getFilmPoster(titleView.getText().toString(), yearView.getText().toString()), found.foundDirector, found.foundScreenwriter, found.foundactor1, found.foundactor2, found.Runtime, found.Rated, found.Plot, found.Awards, found.imdbRating);
        if (mydb.deleteFilm(titleView.getText().toString(), Integer.parseInt(yearView.getText().toString())) > 0) {
            showWatchlist();
        }
        Toast.makeText(getApplicationContext(), "Moved to Watched!", Toast.LENGTH_SHORT).show();

    }


    public void showDetails(View view) {
        View parent = (View) view.getParent();
        View granParent = (View) parent.getParent();
        View titleLayout = (View) granParent.findViewById(R.id.relativeLayout);
        TextView titleView = titleLayout.findViewById(R.id.firstListElement);
        TextView yearView = parent.findViewById(R.id.secondListElement);
        //System.out.println(titleView);
        //System.out.println(yearView);
        // System.out.println(titleView.getText() + "   " + yearView.getText());
        String Title = titleView.getText().toString();
        String Year = yearView.getText().toString();
        DBHelper.Film thisfilm = mydb.getFilmByTitleAndYear(Title, Year);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finishAffinity();
    }
}

