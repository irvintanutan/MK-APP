package com.irvin.makeapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.Models.StockInList;
import com.irvin.makeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class StockInMainAdapter extends RecyclerView.Adapter<StockInMainAdapter.ViewHolder> {
    private List<StockInList> products;
    private Context mContext = null;

    public StockInMainAdapter(List<StockInList> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    @Override
    public StockInMainAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_in_card_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StockInMainAdapter.ViewHolder viewHolder, final int position) {

        try {
            viewHolder.stockInId.setText("STC-"+products.get(position).getId());


            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(products.get(position).getDateCreated());
            DateFormat formatter = new SimpleDateFormat("E, MMM dd, yyyy HH:mm:ss");
            viewHolder.dateCreated.setText(formatter.format(date));


            JSONArray json = new JSONArray(products.get(position).getDetails());

            viewHolder.numberOfItems.setText(Integer.toString(json.length()));

            double total  = 0;

            for (int a = 0 ; a < json.length() ; a++){

                JSONObject object =  json.getJSONObject(a);

                double price = Double.parseDouble(object.getString("price").replace(",",""));
                int qty = Integer.parseInt(object.getString("quantity"));

                total += price * qty;
            }

            DecimalFormat dec=new DecimalFormat("#,##0.00");

            viewHolder.totalAmount.setText("₱ " +dec.format(total));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void update(List<StockInList> products) {
        this.products = products;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView stockInId, dateCreated, numberOfItems, totalAmount;


        public ViewHolder(View view) {
            super(view);

            stockInId = view.findViewById(R.id.stockInId);
            dateCreated = view.findViewById(R.id.dateCreated);
            numberOfItems = view.findViewById(R.id.numberOfItems);
            totalAmount = view.findViewById(R.id.totalAmount);
        }
    }


}