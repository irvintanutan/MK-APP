package com.irvin.makeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.irvin.makeapp.Adapters.CategoryAdapter;
import com.irvin.makeapp.Adapters.ProductAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    RecyclerView recyclerView, recyclerView2;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    List<Category> categories;
    List<Products> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Products View");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)


        init();

    }

    private void init() {

        recyclerView2 = findViewById(R.id.category_view);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        categories = new ArrayList<>();
        categories.add(new Category("ALL" , false));
        categories.add(new Category("SKIN CARE TIMEWISE-3D", false));
        categories.add(new Category("TIMEWISE", false));
        categories.add(new Category("TIMEWISE REPAIR", false));
        categories.add(new Category("LUMIVIE", false));
        categories.add(new Category("CLEARPROOF", false));
        categories.add(new Category("BOTANICAL EFFECTS", false));
        categories.add(new Category("MK MEN", false));
        categories.add(new Category("SKIN SUPPLEMENTS", false));
        categories.add(new Category("COLOR FOUNDATION", false));
        categories.add(new Category("POWDER", false));
        categories.add(new Category("Concealer", false));
        categories.add(new Category("LIP COLOR", false));
        categories.add(new Category("BLUSH", false));
        categories.add(new Category("Contour & Highlight", false));
        categories.add(new Category("EYE COLOR", false));
        categories.add(new Category("Eyeliner", false));
        categories.add(new Category("BROWS", false));
        categories.add(new Category("MASCARA", false));
        categories.add(new Category("Finishing Spray PRIMER", false));
        categories.add(new Category("Accessories", false));
        categories.add(new Category("BODY CARE Satin Body", false));
        categories.add(new Category("BODY CARE Satin Hands", false));
        categories.add(new Category("BODY CARE Satin Lips", false));
        categories.add(new Category("BODY CARE Sun Care", false));
        ModGlobal.categories = categories;


        categoryAdapter = new CategoryAdapter(categories , this);
        recyclerView2.setAdapter(categoryAdapter);
        recyclerView2.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {

                searchCategories(categories.get(position).getName());
                updateCategory(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }


        }));

        recyclerView = findViewById(R.id.product_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadList();
    }

    private void updateCategory(int position){
        categories = new ArrayList<>();
        categories = ModGlobal.categories;

        for (int a = 0 ; a < categories.size() ; a++)
            categories.set(a , new Category(categories.get(a).getName() , false));


        categories.set(position , new Category(categories.get(position).getName() , true));
        categoryAdapter.update(categories);

    }

    private void searchCategories(String text){

        List<Products> temp = new ArrayList();

        if (text.equals("ALL"))
            temp = ModGlobal.ProductModelList;
        else {
            for (Products p : ModGlobal.ProductModelList) {
                //or use .contains(text)
                if (p.getProduct_category().equals(text)) {
                    temp.add(p);
                }
            }
        }
        //update recyclerview
        productAdapter.update(temp);

    }

    private void loadList() {
        products = databaseHelper.getAllProducts();
        ModGlobal.ProductModelList = products;
        Log.e("size", Integer.toString(products.size()));

        productAdapter = new ProductAdapter(products, this);
        recyclerView.setAdapter(productAdapter);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProductActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ProductActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }

        return super.onOptionsItemSelected(item);
    }
}
