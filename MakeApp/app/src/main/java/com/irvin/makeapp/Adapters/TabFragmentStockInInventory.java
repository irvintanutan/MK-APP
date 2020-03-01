package com.irvin.makeapp.Adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by irvin on 2/7/17.
 */
public class TabFragmentStockInInventory extends Fragment {
    View view;
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    List<Category> categories;
    List<Products> products;
    LinearLayout nothing;
    TextView totalAmount;
    SearchView searchView;
    DecimalFormat dec = new DecimalFormat("#,##0.00");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inventory_tab, container, false);
        databaseHelper = new DatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.recycler_view);
        nothing = view.findViewById(R.id.nothing);
        totalAmount = view.findViewById(R.id.totalAmount);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        loadList();

        searchView = view.findViewById(R.id.searchLayout);
        searchView.setQuery("", false);
        searchView.setQueryHint("Search Product");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);

                return false;
            }
        });

        return view;
    }

    private void filter(String text) {

        List<Products> temp = new ArrayList();

        if ("".equals(text)) {
            temp = ModGlobal.ProductModelList;
        } else {
            for (Products p : ModGlobal.ProductModelList) {
                //or use .contains(text)
                if (p.getProduct_category().toLowerCase().contains(text.toLowerCase()) ||
                        p.getProduct_name().toLowerCase().contains(text.toLowerCase())) {
                    temp.add(p);
                }
            }
        }
        //update recyclerview
        productAdapter.update(temp);

    }

    private void loadList() {
        products = databaseHelper.getAllProducts2();

        if (products.size() > 0) {
            nothing.setVisibility(View.GONE);
        }

        ModGlobal.ProductModelList = products;
        Log.e("size", Integer.toString(products.size()));

        productAdapter = new ProductAdapter(products, getActivity());
        recyclerView.setAdapter(productAdapter);

        double total = 0.00;

        for (Products product : products){
            total += Double.parseDouble(product.getProduct_price().replace(",","")) * Integer.parseInt(product.getProduct_quantity());
        }

        totalAmount.setText("₱ " + dec.format(total));
    }
}
