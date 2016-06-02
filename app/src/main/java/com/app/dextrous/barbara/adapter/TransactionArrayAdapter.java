package com.app.dextrous.barbara.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.model.Transaction;

import java.util.List;

public class TransactionArrayAdapter extends ArrayAdapter<Transaction> {

    private final Context context;
    private final List<Transaction> values;
    String TAG = TransactionArrayAdapter.class.getName();

    public TransactionArrayAdapter(Context context, List<Transaction> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.transaction_item_view, parent, false);
        TextView descriptionText = (TextView) rowView.findViewById(R.id.descriptionLabel);
        TextView fromUserText = (TextView) rowView.findViewById(R.id.fromUserValueLabel);
        TextView toUserText = (TextView) rowView.findViewById(R.id.toUserValueLabel);
        Transaction item = values.get(position);
        descriptionText.setText(item.getDescription());
        // change the icon for Windows and iPhone
        fromUserText.setText(item.getFromUser().getFullName());
        toUserText.setText(item.getToUser().getFullName());
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