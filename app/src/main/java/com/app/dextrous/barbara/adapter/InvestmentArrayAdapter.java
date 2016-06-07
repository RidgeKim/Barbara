package com.app.dextrous.barbara.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.dextrous.barbara.R;
import com.app.dextrous.barbara.model.InvestmentPlan;

import java.util.List;

import static com.app.dextrous.barbara.constant.BarbaraConstants.TAG;

public class InvestmentArrayAdapter extends ArrayAdapter<InvestmentPlan> {

    private final Context context;
    private final List<InvestmentPlan> values;

    public InvestmentArrayAdapter(Context context, List<InvestmentPlan> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.investment_item_view, parent, false);
        TextView descriptionText = (TextView) rowView.findViewById(R.id.descriptionLabel);
        TextView amountText = (TextView) rowView.findViewById(R.id.amountValueLabel);
        TextView investmentTypeText = (TextView) rowView.findViewById(R.id.typeValueLabel);
        InvestmentPlan item = values.get(position);
        descriptionText.setText(item.getDescription());
        amountText.setText(String.valueOf(item.getAmount()));
        investmentTypeText.setText(item.getType());
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