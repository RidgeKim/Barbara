package com.app.dextrous.barbara.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.model.User;

import java.util.List;

import static com.app.dextrous.barbara.constant.BarbaraConstants.TAG;

public class UserArrayAdapter extends ArrayAdapter<User> {

    private final Context context;
    private final List<User> values;

    public UserArrayAdapter(Context context, List<User> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.user_list_item_view, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
        User item = values.get(position);
        textView.setText(item.getFullName());
        // change the icon for Windows and iPhone
        textView2.setText(item.getSpeakerProfileId());
        Log.d(TAG, item.toString());
        return rowView;
    }

    @Override
    public long getItemId(int position) {
        return values.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}