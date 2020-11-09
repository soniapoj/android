package mobile.imovie;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayFilm extends AppCompatActivity {
    DBHelper mydb;
    TextView title;
    TextView year;
    TextView genre;
    ListView lstView;

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

    public void saveData(View view) {
        this.title = findViewById(R.id.editTextTitle);
        this.year = findViewById(R.id.editTextYear);
        this.genre = findViewById(R.id.editTextGenre);
        if (mydb.addFilm(this.title.getText().toString(), Integer.parseInt(this.year.getText().toString()), this.genre.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Successfully Added! xD", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Record not added :(", Toast.LENGTH_SHORT).show();
        }
    }
}

