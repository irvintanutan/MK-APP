package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.irvin.makeapp.Adapters.CategoryAdapter;
import com.irvin.makeapp.Adapters.ProductAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    RecyclerView recyclerView, recyclerView2;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    List<Category> categories;
    List<Products> products;
    LinearLayout nothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
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
        nothing = findViewById(R.id.nothing);


        recyclerView2 = findViewById(R.id.category_view);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        categories = new ArrayList<>();
        categories.add(new Category("ALL", false));
        categories.add(new Category("Accessories", false));
        categories.add(new Category("BLUSH", false));
        categories.add(new Category("BODY CARE Satin Body", false));
        categories.add(new Category("BODY CARE Satin Hands", false));
        categories.add(new Category("BODY CARE Satin Lips", false));
        categories.add(new Category("BODY CARE Sun Care", false));
        categories.add(new Category("BROWS", false));
        categories.add(new Category("BOTANICAL EFFECTS", false));
        categories.add(new Category("Contour & Highlight", false));
        categories.add(new Category("CLEARPROOF", false));
        categories.add(new Category("COLOR FOUNDATION", false));
        categories.add(new Category("Concealer", false));
        categories.add(new Category("EYE COLOR", false));
        categories.add(new Category("Eyeliner", false));
        categories.add(new Category("Finishing Spray PRIMER", false));
        categories.add(new Category("LIP COLOR", false));
        categories.add(new Category("LUMIVIE", false));
        categories.add(new Category("MK MEN", false));
        categories.add(new Category("MASCARA", false));
        categories.add(new Category("POWDER", false));
        categories.add(new Category("SKIN SUPPLEMENTS", false));
        categories.add(new Category("SKIN CARE TIMEWISE-3D", false));
        categories.add(new Category("TIMEWISE", false));
        categories.add(new Category("TIMEWISE REPAIR", false));


        ModGlobal.categories = categories;


        categoryAdapter = new CategoryAdapter(categories, this);
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

    private void updateCategory(int position) {
        categories = new ArrayList<>();
        categories = ModGlobal.categories;

        for (int a = 0; a < categories.size(); a++)
            categories.set(a, new Category(categories.get(a).getName(), false));


        categories.set(position, new Category(categories.get(position).getName(), true));
        categoryAdapter.update(categories);

    }

    private void searchCategories(String text) {

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

    private void filter(String text) {

        List<Products> temp = new ArrayList();

        if (text.equals(""))
            temp = ModGlobal.ProductModelList;
        else {
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
        products = databaseHelper.getAllProducts();

        if (products.size() > 0) {
            nothing.setVisibility(View.GONE);
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ProductActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else if (item.getItemId() == R.id.action_sync){

            new GetProductTask(ProductActivity.this).execute("1");

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        filter(newText);
        return false;
    }


    public class GetProductTask extends AsyncTask<String, String, String> {
        boolean warning_indicator = true;
        ArrayList<Products> productsList = new ArrayList<>();
        private DatabaseHelper databaseHelper;
        Context serviceContext;
        ProgressDialog progressDialog;

        public GetProductTask(Context serviceContext) {
            this.serviceContext = serviceContext;
            databaseHelper = new DatabaseHelper(serviceContext);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(serviceContext);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Product Sync Process");
            progressDialog.setMessage("Downloading Products");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }


        protected void onProgressUpdate(String... progUpdate) {

            int max = Integer.parseInt(progUpdate[0]), prog = Integer.parseInt(progUpdate[1]);

            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress((int) (prog * 100 / max));


            progressDialog.setMessage("Downloading Products " + ((int) (prog * 100 / max) + "%"));

        }



        @Override
        protected String doInBackground(String... params) {

            try {
                final String isUpdate = params[0];
                warning_indicator = true;

                FirebaseApp.initializeApp(serviceContext);
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("products");
                Query query = ref.orderByChild("products");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ModGlobal.dataSnapshot = dataSnapshot;
                        for (DataSnapshot postSnapshot : ModGlobal.dataSnapshot.getChildren()) {

                            Products products = postSnapshot.getValue(Products.class);
                            productsList.add(products);
                        }

                        for (int a = 0; a < productsList.size(); a++) {
                            Log.e(productsList.get(a).getProduct_id() ,productsList.get(a).getProduct_name());
                            if (isUpdate.equals("0")) {
                                databaseHelper.addProduct(productsList.get(a));
                            }else {
                                databaseHelper.updateProduct(productsList.get(a));
                            }
                            publishProgress(String.format("%d", productsList.size())
                                    , String.format("%d", a));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.e("aaa", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                Logger.CreateNewEntry(e , new File(getExternalFilesDir("") , ModGlobal.logFile));
                Log.e("asd", e.toString());
                warning_indicator = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {

            String message = warning_indicator ? "Synchronization Successful" : "Can't Access Server!";
            progressDialog.dismiss();
            AlertDialog.Builder alert = new AlertDialog.Builder(serviceContext);
            alert.setTitle("Products");
            alert.setMessage(message);

            alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {

                    startActivity(new Intent(ProductActivity.this, ProductActivity.class));
                    finish();

                }
            });

            alert.show();
        }


    }
}
