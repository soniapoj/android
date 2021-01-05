package mobile.imovie;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PersonalStatistics extends AppCompatActivity {
    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList pieEntryLabels;
    ListDBHelper mydb;
    ListDBHelper mydb1;
    ListDBHelper favoritesDB;
    DBHelper wishlistDB;
    HashMap<String, Integer> distinctGenres;
    Cursor cursor;
    Cursor cursor1;
    Cursor cursor2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_statistics);
        pieChart = findViewById(R.id.pieChart);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setText("");
        pieChart.getLegend().setEnabled(false);
        pieChart.setUsePercentValues(true);
        getGenres();
        pieDataSet = new PieDataSet(pieEntries, "");
        pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(new DecimalFormat("#.#")));
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(20);
        pieDataSet.setValueTextSize(17);
        pieDataSet.setSliceSpace(5f);
        setTotals();

    }

    private void setTotals() {
        TextView watchtime = findViewById(R.id.watchtime);
        TextView watched = findViewById(R.id.watched);
        TextView wishlist = findViewById(R.id.wishlist);
        TextView favorites = findViewById(R.id.favorites);
        this.mydb1 = new ListDBHelper(this);
        cursor = mydb1.getAllFilms("watched");
        if(cursor.getCount() > 0) {
            String text = "Number of movies watched: " + cursor.getCount();
            watched.setText(text);
        }
        this.wishlistDB = new DBHelper(this);
        this.favoritesDB = new ListDBHelper(this);
        cursor = wishlistDB.getAllFilms();
        cursor1 = mydb1.getAllFilms("watched");
        cursor2 = favoritesDB.getAllFilms("favorites");
        if(cursor1.getCount() == 0 && cursor2.getCount() == 0) {
            setContentView(R.layout.activity_no_movies);
        }
        if(cursor.getCount() > 0){
            String text = "Number of movies on watchlist: " + cursor.getCount();
            wishlist.setText(text);
        }
        if(cursor.getCount() > 0){
            String text = "Number of favourite movies: " + cursor2.getCount();
            favorites.setText(text);
        }
        watchtime.setText(getRuntime());
    }

    private void getGenres() {
        pieEntries = new ArrayList<>();
        this.mydb = new ListDBHelper(this);
        //this.mydb1 = new ListDBHelper(this);
        distinctGenres = new HashMap<>();
        cursor = mydb.getAllFilms("watched");
        cursor.moveToFirst();
        String currentGenres = "";
        int nrOfGenres = cursor.getCount();
        while(!cursor.isAfterLast()){
            currentGenres = cursor.getString(cursor.getColumnIndex("movie_genre"));
            String[] genres = currentGenres.split(", ");
            for(String currentGenre: genres) {
                if (distinctGenres.containsKey(currentGenre))
                    distinctGenres.put(currentGenre, distinctGenres.get(currentGenre) + 1);
                else
                    distinctGenres.put(currentGenre, 1);
            }
            cursor.moveToNext();
        }
        cursor = mydb.getAllFilms("favorites");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            currentGenres = cursor.getString(cursor.getColumnIndex("movie_genre"));
            String[] genres = currentGenres.split(", ");
            for(String currentGenre: genres) {
                if (distinctGenres.containsKey(currentGenre))
                    distinctGenres.put(currentGenre, distinctGenres.get(currentGenre) + 1);
                else
                    distinctGenres.put(currentGenre, 1);
            }
            cursor.moveToNext();
        }
        for(Map.Entry<String, Integer> genre: distinctGenres.entrySet()){
            PieEntry newEntry = new PieEntry((float)genre.getValue()/nrOfGenres * 100, genre.getKey());
            pieEntries.add(newEntry);
        }
    }

    private String getRuntime() {

        this.mydb = new ListDBHelper(this);
        cursor = mydb.getAllFilms("watched");
        cursor.moveToFirst();
        int count = 0;

        while(!cursor.isAfterLast()){
            count += Integer.parseInt(cursor.getString(cursor.getColumnIndex("movie_runtime")));

            System.out.println("************************************************************************************************");

            cursor.moveToNext();
        }
        int hours = count / 60;
        int minutes = count % 60;
        String here = "Total watchtime: ";
        here+=hours;
        here+=" Hours and ";
        here+=minutes;
        here+=" Minutes";
        return here;
    }
}