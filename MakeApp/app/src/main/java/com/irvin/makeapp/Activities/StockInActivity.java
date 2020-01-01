package com.irvin.makeapp.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irvin.makeapp.Adapters.CategoryAdapter;
import com.irvin.makeapp.Adapters.ProductAdapter;
import com.irvin.makeapp.Adapters.StockInAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;

public class StockInActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    RecyclerView recyclerView, recyclerView2;
    StockInAdapter stockInAdapter;
    CategoryAdapter categoryAdapter;
    List<Category> categories;
    List<Products> products;
    TextView itemCount, itemView;
    LinearLayout btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stockin);

        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Stock In");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)

        init();
        itemCount.setText("" + ModGlobal.stockIns.size());


    }

    private void init() {

        itemCount = findViewById(R.id.itemCount);
        itemView = findViewById(R.id.itemView);
        btnView = findViewById(R.id.btnView);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ModGlobal.stockIns.size() > 0) {

                    Intent intent = new Intent(StockInActivity.this, StockInDetailsActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockInActivity.this);
                    builder.setTitle("Alert");
                    builder.setIcon(getResources().getDrawable(R.drawable.warning));
                    builder.setMessage("There is/are no item/s in the cart");

                    builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }
        });

        recyclerView2 = findViewById(R.id.category_view);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        categories = new ArrayList<>();
        categories.add(new Category("ALL", false));
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
        stockInAdapter.update(temp);

    }

    private void loadList() {
        products = databaseHelper.getAllProducts();
        ModGlobal.ProductModelList = products;
        Log.e("size", Integer.toString(products.size()));

        stockInAdapter = new StockInAdapter(products, this);
        recyclerView.setAdapter(stockInAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);


                    if (ModGlobal.itemIsDuplicate(ModGlobal.ProductModelList.get(position).getProduct_id())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StockInActivity.this);
                        builder.setTitle("Alert");
                        builder.setIcon(getResources().getDrawable(R.drawable.warning));
                        builder.setMessage(ModGlobal.ProductModelList.get(position).getProduct_id() + "  " +
                                ModGlobal.ProductModelList.get(position).getProduct_name()
                                + " is already in cart");

                        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {

                        StockIn stockIn = new StockIn(ModGlobal.ProductModelList.get(position).getProduct_name()
                                , ModGlobal.ProductModelList.get(position).getProduct_id(), "1",
                                ModGlobal.ProductModelList.get(position).getProduct_price());

                        ModGlobal.stockIns.add(stockIn);
                        RunAnimation();

                    }


                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    private void RunAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        itemCount.setText("" + ModGlobal.stockIns.size());

        itemCount.clearAnimation();
        itemCount.startAnimation(a);
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
        stockInAdapter.update(temp);

    }


    private void goBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
        builder.setMessage("Are you sure you want to quit ? Your data won't be saved.");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {


                startActivity(new Intent(StockInActivity.this, StockInMainActivity.class));
                finish();
                ModGlobal.stockIns.clear();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);

            }

        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        goBack();
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
            goBack();
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
}
