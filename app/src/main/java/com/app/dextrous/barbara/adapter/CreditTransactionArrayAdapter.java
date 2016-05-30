package com.app.dextrous.barbara.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.constant.BarbaraConstants;
import com.app.dextrous.barbara.model.CreditTransaction;
import com.app.dextrous.barbara.model.Transaction;

import java.util.List;

import static com.app.dextrous.barbara.constant.BarbaraConstants.DELIMITER_SPACE;

public class CreditTransactionArrayAdapter extends ArrayAdapter<CreditTransaction> {

    private final Context context;
    private final List<CreditTransaction> values;
    String TAG = CreditTransactionArrayAdapter.class.getName();

    public CreditTransactionArrayAdapter(Context context, List<CreditTransaction> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.credit_item_view, parent, false);
        TextView descriptionText = (TextView) rowView.findViewById(R.id.descriptionLabel);
        TextView amountText = (TextView) rowView.findViewById(R.id.amountValueLabel);
        TextView transactionOnText = (TextView) rowView.findViewById(R.id.transferredOnValueLabel);
        CreditTransaction item = values.get(position);
        descriptionText.setText(item.getDescription());
        amountText.setText(String.format("%s%s%s", item.getUser().getCurrencyType(), DELIMITER_SPACE, item.getAmount()));
        transactionOnText.setText(item.getCreatedTS().toString());
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