package mobile.imovie;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;

public class ListManager extends AppCompatActivity {
    ListDBHelper mydb;
    TextView title;
    TextView year;
    TextView genre;
    ListView lstView;
    private RequestQueue queue;
    EditText titleEdit;
    EditText yearEdit;
    EditText genreEdit;
    String foundYear = "";
    String foundGenre = "";
    SimpleCursorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_watched);
        this.lstView = findViewById(R.id.lstView);
        this.mydb = new ListDBHelper(this);
        showWatched();

    }

    private void showWatched() {
        try {
            int[] id = {R.id.firstListElement, R.id.secondListElement};
            String[] title = new String[]{"movie_title", "movie_year"};
            if (this.mydb == null)
                this.mydb = new ListDBHelper(this);
            Cursor c = mydb.getAllFilms("watched");
            adapter = new SimpleCursorAdapter(this,
                    R.layout.watched_list_template, c, title, id, 0);
            lstView.setAdapter(adapter);

        } catch (Exception ex) {
            Toast.makeText(ListManager.this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
//    public void deletefilm(View view) {
//        View parent = (View) view.getParent();
//        TextView titleView = parent.findViewById(R.id.firstListElement);
//        TextView yearView = parent.findViewById(R.id.secondListElement);
//        System.out.println(titleView.getText() + "   " + yearView.getText());
//        if (mydb.deleteFilm(titleView.getText().toString(), Integer.parseInt(yearView.getText().toString())) > 0) {
//            Toast.makeText(getApplicationContext(), "Successfully Deleted! xD", Toast.LENGTH_SHORT).show();
//            showWatched();
//        } else {
//            Toast.makeText(getApplicationContext(), "Record not deleted :( :(", Toast.LENGTH_SHORT).show();
//        }
//    }

}
