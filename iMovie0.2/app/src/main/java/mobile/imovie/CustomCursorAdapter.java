package mobile.imovie;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CustomCursorAdapter extends CursorAdapter {
    public CustomCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_template, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView titleView = (TextView) view.findViewById(R.id.firstListElement);
        TextView yearView = (TextView) view.findViewById(R.id.secondListElement);
        ImageView posterView = (ImageView) view.findViewById(R.id.imageView);
        // Extract properties from cursor
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
        String posterUrl = cursor.getString(cursor.getColumnIndexOrThrow("poster"));
        // Populate fields with extracted properties
        titleView.setText(title);
        yearView.setText(String.valueOf(year));
        Picasso.get().load(posterUrl).into(posterView);
    }
}