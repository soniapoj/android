package mobile.imovie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    String foundYear = "";
    String foundGenre = "";
    String foundDirector = "";
    String foundWriter = "";
    String foundActor1 = "";
    String foundActor2 = "";
    String foundPosterUrl = "";
    String foundTitle = "";
    SimpleCursorAdapter adapter;


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

// Attach cursor adapter to the ListView

//            int[] id = {R.id.firstListElement, R.id.secondListElement};
//            String[] title = new String[]{"title", "year"};
            if (this.mydb == null)
                this.mydb = new DBHelper(this);
            Cursor c = mydb.getAllFilms();
            this.lstView = findViewById(R.id.lstView);
            CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, c);
//            adapter = new SimpleCursorAdapter(this,
//                    R.layout.list_template, c, title, id, 0);
            //lstView.setAdapter(adapter);
            lstView.setAdapter(customCursorAdapter);

        } catch (Exception ex) {
            Toast.makeText(DisplayFilm.this, ex.getMessage(), Toast.LENGTH_LONG).show();
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
//                queue.add(searchNameStringRequest(title.getText().toString()));
//                if(foundYear != "")
//                    year.setText(foundYear);
//                if(foundGenre != "")
//                    genre.setText(foundGenre);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
//                String toSearch = title.getText().toString() + " ";
//                //queue.add(searchNameStringRequest(this.title.getText().toString(), Integer.parseInt(this.year.getText().toString())));
//                queue.add(searchNameStringRequest(toSearch));
//                if(foundYear != "")
//                    year.setText(foundYear);
//                if(foundGenre != "")
//                    genre.setText(foundGenre);
//                if(!foundPosterUrl.equals(""))
//                    Picasso.get().load(foundPosterUrl).into(imgView);
            }
        });


    }

    private StringRequest searchNameStringRequest(String Title) {
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
                            // Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
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

    public void saveData(View view) {
        if (mydb.addFilm(this.foundTitle, Integer.parseInt(this.year.getText().toString()), this.genre.getText().toString(), this.foundPosterUrl, this.foundDirector, this.foundWriter, this.foundActor1, this.foundActor2)) {
            startActivity(new Intent(DisplayFilm.this, DisplayFilm.class));
            Toast.makeText(getApplicationContext(), "Successfully Added! xD", Toast.LENGTH_SHORT).show();
            // queue = Volley.newRequestQueue(this);
            //queue.add(searchNameStringRequest(this.title.getText().toString(), Integer.parseInt(this.year.getText().toString())));
            //queue.add(searchNameStringRequest(this.title.getText().toString()));
            //searchNameStringRequest("It", 2017);

        } else {
            Toast.makeText(getApplicationContext(), "Record not added :( :(", Toast.LENGTH_SHORT).show();
        }

    }

    public void deletefilm(View view) {
        System.out.println("**********************************************************************************************************************************");
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
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        View parent = (View) view.getParent();
        View granParent = (View) parent.getParent();
        View titleLayout = (View) granParent.findViewById(R.id.relativeLayout);
        TextView titleView = titleLayout.findViewById(R.id.firstListElement);
        TextView yearView = parent.findViewById(R.id.secondListElement);
        ListDBHelper listDB = new ListDBHelper(this);
        DBHelper.Film found = mydb.getFilmByTitleAndYear(titleView.getText().toString(),yearView.getText().toString());
        System.out.println(found);
        listDB.addFilm("watched", titleView.getText().toString(), Integer.parseInt(yearView.getText().toString()), found.foundGenre,mydb.getFilmPoster(titleView.getText().toString(), yearView.getText().toString()),found.foundDirector,found.foundScreenwriter,found.foundactor1,found.foundactor2);
        if (mydb.deleteFilm(titleView.getText().toString(), Integer.parseInt(yearView.getText().toString())) > 0) {
            showWatchlist();
        }
        Toast.makeText(getApplicationContext(), "Moved to Watched!", Toast.LENGTH_SHORT).show();

    }


    public void showDetails(View view) {
        //System.out.println("**********************************************************************************************************************************");
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
       DBHelper.Film thisfilm = mydb.getFilmByTitleAndYear(Title,Year);
       setContentView(R.layout.detailed_film);
       TextView titletext = findViewById(R.id.titleDetail);
       titletext.setText(thisfilm.foundTitle);
    }
}

