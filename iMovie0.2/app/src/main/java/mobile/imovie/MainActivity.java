package mobile.imovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate
                .setDefaultNightMode(
                        AppCompatDelegate
                                .MODE_NIGHT_YES);
    }

    public void showWatchlist(View v){
        startActivity(new Intent(MainActivity.this, DisplayFilm.class));

    }

    public void showWatched(View v){
        startActivity(new Intent(MainActivity.this, ListManager.class));
    }

    public void showRecommended(View v){
        startActivity(new Intent(MainActivity.this, RecommendActivity.class));
    }
    public void showStatistics(View v){
        startActivity(new Intent(MainActivity.this, PersonalStatistics.class));
    }
    public void showFavorites(View v){
        startActivity(new Intent(MainActivity.this, FavoriteMovies.class));
    }
}