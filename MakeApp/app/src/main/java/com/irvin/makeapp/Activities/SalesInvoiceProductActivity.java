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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.irvin.makeapp.Adapters.CategoryAdapter;
import com.irvin.makeapp.Adapters.SearchCustomerAdapter;
import com.irvin.makeapp.Adapters.StockInAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.Category;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Products;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;

import java.util.ArrayList;
import java.util.List;

public class SalesInvoiceProductActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    List<CustomerModel> customerModelList;
    List<CustomerModel> tempCust = new ArrayList<>();
    RecyclerView recyclerView, recyclerView2;
    StockInAdapter stockInAdapter;
    CategoryAdapter categoryAdapter;
    List<Category> categories;
    List<Products> products;
    TextView itemCount, itemView, customerName;
    LinearLayout btnView;
    List<Products> temp = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_invioce_product);

        Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Production Selection");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)

        init();
        customerName.setText(ModGlobal.customerName);
        searchCustomer();
    }

    void init() {

        customerName = findViewById(R.id.customerName);
        itemCount = findViewById(R.id.itemCount);
        itemView = findViewById(R.id.itemView);
        btnView = findViewById(R.id.btnView);

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ModGlobal.stockIns.size() > 0) {

                /*    Intent intent = new Intent(SalesInvoiceProductActivity.this, StockInDetailsActivity.class);
                    ModGlobal.indicator = false;
                    startActivity(intent);
                    finish();*/

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesInvoiceProductActivity.this);
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

    void searchCustomer() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_search_customer, null);


        final SearchCustomerAdapter customerAdapter;
        RecyclerView recyclerView;
        final EditText searchCustomer = alertLayout.findViewById(R.id.search);
        ImageView done = alertLayout.findViewById(R.id.done);

        recyclerView = alertLayout.findViewById(R.id.customer_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        customerModelList = databaseHelper.getAllCustomer();
        ModGlobal.customerModelList = customerModelList;
        tempCust = customerModelList;

        Log.e("size", Integer.toString(customerModelList.size()));

        customerAdapter = new SearchCustomerAdapter(customerModelList, this);
        recyclerView.setAdapter(customerAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

            @Override
            public void onClick(View view, int position) {
                ModGlobal.position = position;
                customerAdapter.notifyDataSetChanged();

                ModGlobal.customerId = tempCust.get(position).getId();
                ModGlobal.customerName = tempCust.get(position).getFirstName() + " " + tempCust.get(position).getMiddleName()
                        + " " + tempCust.get(position).getLastName();


            }

            @Override
            public void onLongClick(View view, final int position) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                };

            }
        }));

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.show();


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerName.setText(ModGlobal.customerName);
                dialog.dismiss();
            }
        });

        searchCustomer.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                tempCust = new ArrayList();
                String text = searchCustomer.getText().toString();
                if (text.equals(""))
                    tempCust = ModGlobal.customerModelList;
                else {
                    for (CustomerModel p : ModGlobal.customerModelList) {
                        //or use .contains(text)
                        if (p.getFirstName().toLowerCase().contains(text.toLowerCase()) ||
                                p.getLastName().toLowerCase().contains(text.toLowerCase())) {
                            tempCust.add(p);
                        }
                    }
                }
                //update recyclerview
                customerAdapter.update(tempCust);
            }
        });

    }


    private void filter(String text) {

        temp = new ArrayList();

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
        temp = products;
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(SalesInvoiceProductActivity.this);
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

                        StockIn stockIn = new StockIn(temp.get(position).getProduct_name()
                                , temp.get(position).getProduct_id(), "1",
                                temp.get(position).getProduct_price());

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

        temp = new ArrayList<>();

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


                startActivity(new Intent(SalesInvoiceProductActivity.this, SalesInvoiceActivity.class));
                finish();
                ModGlobal.stockIns.clear();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                ModGlobal.customerName = "";

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


    public void customer(View view) {
        searchCustomer();
    }
}
