package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.irvin.makeapp.Adapters.SalesInvoiceDetailsAdapter;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.TranStatus;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Database.DatabasePayment;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.Models.Payment;
import com.irvin.makeapp.Models.Reminder;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.CalendarReminder;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SalesInvoiceProductDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SalesInvoiceDetailsAdapter stockInAdapter;
    LinearLayout btnView, layoutBottom;
    boolean indicator = false;
    public static TextView totalAmount;

    private AlertDialog finalDialog = null;
    DecimalFormat dec = new DecimalFormat("#,##0.00");
    private static double finalSubTotal = 0.00;
    private double finalTotal = 0.00;
    private double finalCash = 0.00;
    private double finalChange = 0.00;
    private double finalDiscount = 0.00;
    String dueDate = "";
    DatabaseInvoice databaseInvoice = new DatabaseInvoice(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_invoice_product_details);
        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);

        indicator = ModGlobal.indicator;


        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        if (indicator)
            ab.setTitle("Sales Invoice Summary");
        else
            ab.setTitle("Sales Invoice Details");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)

        init();

        calculateTotal();
    }

    private void init() {
        btnView = findViewById(R.id.btnView);
        totalAmount = findViewById(R.id.totalAmount);
        layoutBottom = findViewById(R.id.layoutBottom);
        LinearLayout stockIn = findViewById(R.id.stockIn);

        if (indicator)
            stockIn.setVisibility(View.GONE);
        //else
        //layoutBottom.setVisibility(View.GONE);


        recyclerView = findViewById(R.id.product_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager2);

        stockInAdapter = new SalesInvoiceDetailsAdapter(ModGlobal.stockIns, this);
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

    void goBack() {
        if (indicator) {
            startActivity(new Intent(SalesInvoiceProductDetailsActivity.this, SalesInvoiceActivity.class));
            ModGlobal.stockIns.clear();
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            startActivity(new Intent(SalesInvoiceProductDetailsActivity.this, SalesInvoiceProductActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            goBack();
        }

        return super.onOptionsItemSelected(item);
    }


    public void checkOut(View view) {

        if (ModGlobal.stockIns.isEmpty()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning");
            builder.setIcon(getResources().getDrawable(R.drawable.warning));
            builder.setMessage("Cart is Empty!");

            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        } else {

            finalTotal = finalSubTotal;
            finalCash = 0.00;
            finalChange = 0.00;

            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.payment_view, null);


            final CardView pay1 = alertLayout.findViewById(R.id.pay1);
            final CardView pay5 = alertLayout.findViewById(R.id.pay5);
            final CardView pay10 = alertLayout.findViewById(R.id.pay10);
            final CardView pay20 = alertLayout.findViewById(R.id.pay20);
            final CardView pay50 = alertLayout.findViewById(R.id.pay50);
            final CardView pay100 = alertLayout.findViewById(R.id.pay100);
            final CardView pay200 = alertLayout.findViewById(R.id.pay200);
            final CardView pay500 = alertLayout.findViewById(R.id.pay500);
            final CardView pay1000 = alertLayout.findViewById(R.id.pay1000);
            final CardView pay0 = alertLayout.findViewById(R.id.pay0);


            final CardView clear = alertLayout.findViewById(R.id.clear);
            final CardView clearDiscount = alertLayout.findViewById(R.id.clearDiscount);
            final CardView discount = alertLayout.findViewById(R.id.discount);
            final CardView checkOut = alertLayout.findViewById(R.id.checkOut);
            final ImageView close = alertLayout.findViewById(R.id.close);

            final EditText subTotalValue = alertLayout.findViewById(R.id.subTotalValue);
            final EditText discountValue = alertLayout.findViewById(R.id.discountValue);
            final EditText totalValue = alertLayout.findViewById(R.id.totalValue);
            final EditText cashValue = alertLayout.findViewById(R.id.cashValue);
            final EditText changeValue = alertLayout.findViewById(R.id.changeValue);


            subTotalValue.setFocusable(false);
            discountValue.setFocusable(false);
            totalValue.setFocusable(false);
            cashValue.setFocusable(false);
            changeValue.setFocusable(false);

            subTotalValue.setText("₱ " + dec.format(finalTotal));
            discountValue.setText("₱ " + 0.00);
            totalValue.setText("₱ " + dec.format(finalTotal));
            cashValue.setText("₱ " + dec.format(finalCash));
            double ch = finalCash - finalTotal;
            finalChange = ch;
            changeValue.setText("₱ " + dec.format(ch));
            changeValue.setTextColor(Color.RED);


            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finalTotal = finalSubTotal;
                    finalCash = 0.00;
                    finalChange = 0.00;

                    subTotalValue.setText("₱ " + dec.format(finalTotal));
                    discountValue.setText("₱ " + 0.00);
                    totalValue.setText("₱ " + dec.format(finalTotal));
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));
                    changeValue.setTextColor(Color.RED);

                }
            });

            checkOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesInvoiceProductDetailsActivity.this);

                    builder.setTitle("Processing Payment");
                    builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
                    builder.setMessage("Are you sure you want to place the order ?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            if (finalCash < finalTotal) {
                                dueDateTime();
                            } else {
                                new InvoiceTask(SalesInvoiceProductDetailsActivity.this).execute("");
                            }


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
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalDialog.dismiss();
                }
            });


            clearDiscount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //  ModGlobal.discount = 0.00;
                    //  ModGlobal.discType = 0;
                    //  discountValue.setText(dec.format(ModGlobal.discount));
                    discountValue.setTextColor(Color.BLACK);

                }
            });


            discount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


            pay1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 1;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }

                }
            });


            pay5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 2;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });

            pay10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 3;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });


            pay20.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 4;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });

            pay50.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 5;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });

            pay100.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 6;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });

            pay200.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 7;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });

            pay500.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 8;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });


            pay1000.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    finalCash += 9;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });

            pay0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalCash *= 10;
                    cashValue.setText("₱ " + dec.format(finalCash));
                    double ch = finalCash - finalTotal;
                    finalChange = ch;
                    changeValue.setText("₱ " + dec.format(ch));

                    if (ch < 0) {
                        changeValue.setTextColor(Color.RED);
                    } else {
                        changeValue.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
                    }


                }
            });


            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);
            finalDialog = alert.create();
            finalDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            finalDialog.show();

        }
    }


    public class InvoiceTask extends AsyncTask<String, String, String> {
        boolean warning_indicator = true;
        private DatabaseHelper databaseHelper;
        private DatabasePayment databasePayment;
        Context serviceContext;
        ProgressDialog progressDialog;

        public InvoiceTask(Context serviceContext) {
            this.serviceContext = serviceContext;
            databaseHelper = new DatabaseHelper(serviceContext);
            databasePayment = new DatabasePayment(serviceContext);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(serviceContext);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }


        @Override
        protected String doInBackground(String... params) {

            for (int a = 0; a < ModGlobal.stockIns.size(); a++) {

                Log.e(ModGlobal.stockIns.get(a).getProductCode(), ModGlobal.stockIns.get(a).getQuantity());

                databaseHelper.stockIn(ModGlobal.stockIns.get(a).getProductCode()
                        , ModGlobal.stockIns.get(a).getQuantity(), false);
            }

            String json = new Gson().toJson(ModGlobal.stockIns);

            databasePayment.addPayment(new Payment("", Double.toString(finalCash), databaseInvoice.getLastInvoiceId(), "",
                    Double.toString(finalChange)));

            Invoice invoice = new Invoice();
            invoice.setCustomerId(Integer.toString(ModGlobal.customerId));
            invoice.setCustomerName(ModGlobal.customerName);
            invoice.setTotalAmount(Double.toString(finalTotal));
            invoice.setDiscount(Double.toString(finalDiscount));
            invoice.setDueDate(dueDate);

            if (finalCash < finalTotal) {
                invoice.setStatus(TranStatus.PENDING.toString());


                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Calendar mCalendar = Calendar.getInstance();
                Date date;
                try {
                    date = dateTimeFormat.parse(dueDate + " 07:00:00");
                    mCalendar.setTime(date);

                    String eventId = "";
                    eventId = CalendarReminder.addEvent(new Reminder("Due for " + ModGlobal.toTitleCase(invoice.getCustomerName()),
                                    Integer.toString(ModGlobal.customerId), "#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(databaseInvoice.getLastInvoiceId())) +
                                    " - ₱" + dec.format(finalTotal - finalCash),
                                    dueDate + " 07:00:00", "", eventId, databaseInvoice.getLastInvoiceId()), mCalendar, invoice.getCustomerName(),
                            SalesInvoiceProductDetailsActivity.this);

                    long id = databaseHelper.createReminder(new Reminder("Due for " + ModGlobal.toTitleCase(invoice.getCustomerName()),
                            Integer.toString(ModGlobal.customerId), "#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(databaseInvoice.getLastInvoiceId())) +
                            " - ₱" + dec.format(finalTotal - finalCash),
                            dueDate + " 07:00:00", "", eventId, databaseInvoice.getLastInvoiceId()));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("asd", e.getMessage());
                }
            } else invoice.setStatus(TranStatus.PAID.toString());
            invoice.setInvoiceDetail(json);


            Log.e("status", invoice.getStatus());

            databaseInvoice.addInvoice(invoice);


            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {

            finalDialog.dismiss();
            progressDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(SalesInvoiceProductDetailsActivity.this);
            builder.setTitle("Success");
            builder.setIcon(getResources().getDrawable(R.drawable.check));
            builder.setMessage("Transaction Successful");

            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ModGlobal.stockIns.clear();
                    startActivity(new Intent(SalesInvoiceProductDetailsActivity.this, SalesInvoiceActivity.class));
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }


    }


    public void dueDateTime() {

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.duedatetime, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final DatePicker datePicker = alertLayout.findViewById(R.id.date_picker);

        builder.setView(alertLayout);
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Set", null);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String append = "";
                String appendMonth = "";
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                if (month < 10) appendMonth = "0";

                String date = year + "-" + appendMonth + month + "-" + day;
                dueDate = date;

                new InvoiceTask(SalesInvoiceProductDetailsActivity.this).execute("");

            }
        });

        builder.show();
    }


    public static void calculateTotal() {
        DecimalFormat dec = new DecimalFormat("#,##0.00");
        double total = 0;

        for (StockIn stockIn : ModGlobal.stockIns) {

            total += Double.parseDouble(stockIn.getPrice().replace(",", "")) * Integer.parseInt(stockIn.getQuantity());

        }
        finalSubTotal = total;
        totalAmount.setText("₱ " + dec.format(total));
    }
}