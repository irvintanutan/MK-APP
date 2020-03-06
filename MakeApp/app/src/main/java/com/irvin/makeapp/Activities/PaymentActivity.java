package com.irvin.makeapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.irvin.makeapp.Adapters.PaymentAdapter;
import com.irvin.makeapp.Constant.ClickListener;
import com.irvin.makeapp.Constant.MarshMallowPermission;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Constant.RecyclerTouchListener;
import com.irvin.makeapp.Constant.TranStatus;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Database.DatabaseInvoice;
import com.irvin.makeapp.Database.DatabasePayment;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.Models.Invoice;
import com.irvin.makeapp.Models.Payment;
import com.irvin.makeapp.Models.Reminder;
import com.irvin.makeapp.Models.StockIn;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.CalendarReminder;
import com.irvin.makeapp.Services.Logger;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentActivity extends AppCompatActivity {

    LinearLayout checkOut;
    RecyclerView recyclerView;
    PaymentAdapter paymentAdapter;
    List<Payment> payments;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseInvoice databaseInvoice = new DatabaseInvoice(this);
    DatabaseCustomer databaseCustomer = new DatabaseCustomer(this);
    DatabasePayment databasePayment = new DatabasePayment(this);
    TextView customerName;
    TextView totalAmountPaid;
    TextView invoiceId;
    TextView dateCreated;
    TextView balance;
    public static TextView totalAmount;
    String status = "";

    private AlertDialog finalDialog = null;
    DecimalFormat dec = new DecimalFormat("#,##0.00");
    private static double finalSubTotal = 0.00;
    private double finalTotal = 0.00;
    private double finalCash = 0.00;
    private double finalChange = 0.00;
    private double finalDiscount = 0.00;
    String dueDate = "";
    int maxWidth;
    int maxHeight;
    int width;
    int height;
    Double scale;
    Image image;
    PdfWriter pdfWriter;
    MarshMallowPermission marshMallowPermission;

    FileOutputStream outFile;

    private static String FILE = Environment.getExternalStorageDirectory()
            + "/HelloWorld.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        marshMallowPermission = new MarshMallowPermission(this);
        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Payment History");
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true); // disable the default title element here (for centered title)


        Intent intent = getIntent();

        if (intent.hasExtra("invoice"))
            ModGlobal.invoice = databaseInvoice.getInvoiceById(intent.getStringExtra("invoice")).get(0);


        init();


    }

    private void init() {
        try {
            balance = findViewById(R.id.balance);
            customerName = findViewById(R.id.customerName);
            totalAmountPaid = findViewById(R.id.totalAmountPaid);
            invoiceId = findViewById(R.id.invoiceId);
            dateCreated = findViewById(R.id.dateCreated);
            checkOut = findViewById(R.id.checkOut);
            totalAmount = findViewById(R.id.totalAmount);


            payments = new ArrayList<>();
            payments = databasePayment.getPaymentPerInvoice(ModGlobal.invoice.getInvoiceId());


            double total = 0.00;
            for (Payment payment : payments) {

                total += Double.parseDouble(payment.getAmount());

            }

            customerName.setText(ModGlobal.invoice.getCustomerName());
            dateCreated.setText(ModGlobal.invoice.getDateCreated());
            invoiceId.setText("#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(ModGlobal.invoice.getInvoiceId())));
            totalAmountPaid.setText("₱ " + dec.format(total));
            ModGlobal.totalAmountPaid = dec.format(total);
            total = Double.parseDouble(ModGlobal.invoice.getTotalAmount()) - total;

            finalSubTotal = total;
            ModGlobal.totalBalance = dec.format(total);
            totalAmount.setText("₱ " + dec.format(total));


            if (ModGlobal.invoice.getStatus().equals(TranStatus.PAID.toString())) {

                checkOut.setVisibility(View.GONE);
                balance.setText("INV Amount:");
                totalAmount.setText("₱ " + dec.format(Double.parseDouble(ModGlobal.invoice.getTotalAmount())));
            }

            recyclerView = findViewById(R.id.payment_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(layoutManager);
            paymentAdapter = new PaymentAdapter(payments, this);
            recyclerView.setAdapter(paymentAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {

                @Override
                public void onClick(View view, int position) {


                }

                @Override
                public void onLongClick(View view, int position) {

                }


            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void checkOut(View view) {


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


        discount.setVisibility(View.GONE);
        clearDiscount.setVisibility(View.GONE);


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


                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);

                builder.setTitle("Processing Payment");
                builder.setIcon(getResources().getDrawable(R.drawable.confirmation));
                builder.setMessage("Are you sure you want to place the order ?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (finalCash < finalTotal) {
                            status = TranStatus.PENDING.toString();
                            dueDateTime();
                        } else {
                            status = TranStatus.PAID.toString();
                            new PaymentActivity.PaymentTask(PaymentActivity.this).execute("");
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
                String appendMonth = "";
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();

                if (month < 10) appendMonth = "0";

                String date = year + "-" + appendMonth + month + "-" + day;
                dueDate = date;

                new PaymentActivity.PaymentTask(PaymentActivity.this).execute("");

            }
        });

        builder.show();
    }


    public class PaymentTask extends AsyncTask<String, String, String> {
        private DatabaseHelper databaseHelper;
        Context serviceContext;
        ProgressDialog progressDialog;

        public PaymentTask(Context serviceContext) {
            this.serviceContext = serviceContext;
            databaseHelper = new DatabaseHelper(serviceContext);
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


            databasePayment.addPayment(new Payment("", Double.toString(finalCash), ModGlobal.invoice.getInvoiceId(), "",
                    Double.toString(finalChange)));

            Invoice invoice = ModGlobal.invoice;
            invoice.setStatus(status);

            if (status.equals(TranStatus.PENDING.toString())) {
                invoice.setDueDate(dueDate);

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
                Calendar mCalendar = Calendar.getInstance();
                Date date;
                try {

                    Reminder reminder = databaseHelper.getAllRemindersByInvoice(invoice.getInvoiceId());

                    date = dateTimeFormat.parse(dueDate + " 07:00:00");
                    mCalendar.setTime(date);

                    databaseHelper.updateReminder(new Reminder(reminder.getKEY_TITLE(),
                            reminder.getKEY_CUSTOMER_ID(), "#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(reminder.getKEY_INVOICE_ID())) +
                            " - ₱" + dec.format(finalChange),
                            dueDate + " 07:00:00", reminder.getKEY_ROWID(), reminder.getKEY_EVENT_ID()
                            , reminder.getKEY_INVOICE_ID()), reminder.getKEY_ROWID());

                    CalendarReminder.updateEvent(Long.parseLong(reminder.getKEY_EVENT_ID()), PaymentActivity.this,
                            mCalendar, new Reminder(reminder.getKEY_TITLE(),
                                    reminder.getKEY_CUSTOMER_ID(), "#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() + "d", Integer.parseInt(reminder.getKEY_INVOICE_ID())) +
                                    " - ₱" + dec.format(finalChange),
                                    dueDate + " 07:00:00", reminder.getKEY_ROWID(), reminder.getKEY_EVENT_ID()
                                    , reminder.getKEY_INVOICE_ID()), invoice.getCustomerName());

                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
                }
            } else {
                try {  Reminder reminder = databaseHelper.getAllRemindersByInvoice(invoice.getInvoiceId());

                    CalendarReminder.deleteEvent(Long.parseLong(reminder.getKEY_EVENT_ID()), PaymentActivity.this);

                databaseHelper.deleteReminder(Long.parseLong(reminder.getKEY_ROWID()));

                }catch (Exception e){
                    e.printStackTrace();
                    Logger.CreateNewEntry(e, new File(getExternalFilesDir(""), ModGlobal.logFile));
                }
            }

            databaseInvoice.updateInvoice(invoice, invoice.getInvoiceId());


            return null;
        }

        @Override
        protected void onPostExecute(String strFromDoInBg) {

            finalDialog.dismiss();
            progressDialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
            builder.setTitle("Success");
            builder.setIcon(getResources().getDrawable(R.drawable.check));
            builder.setMessage("Transaction Successful");

            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ModGlobal.stockIns.clear();
                    startActivity(new Intent(PaymentActivity.this, SalesInvoiceActivity.class));
                    finish();
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }


    }


    @Override
    public void onBackPressed() {
        if (ModGlobal.InvoiceOriginView.equals("SALES_INVOICE_CUSTOMER")) {
            startActivity(new Intent(PaymentActivity.this, CustomerProfileViewActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        } else {
            startActivity(new Intent(PaymentActivity.this, SalesInvoiceActivity.class));
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.payment_view, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (ModGlobal.InvoiceOriginView.equals("SALES_INVOICE_CUSTOMER")) {
                startActivity(new Intent(PaymentActivity.this, SalesInvoiceActivity.class));
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            } else {
                startActivity(new Intent(PaymentActivity.this, CustomerProfileViewActivity.class));
                finish();
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        } else if (item.getItemId() == R.id.action_report) {
            generatePDF();
        } else if (item.getItemId() == R.id.action_call) {
            if (!marshMallowPermission.checkPermissionForCallPhone()) {
                marshMallowPermission.requestPermissionForCallPhone();
            } else {
                callCustomer();
            }

        } else if (item.getItemId() == R.id.action_message) {

            messageCustomer();

        }
        return super.onOptionsItemSelected(item);
    }

    public void generatePDF() {
        maxHeight = 5000;
        maxWidth = 600;

        String downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        createPDF(downloadsPath + File.separator + "Notes.pdf");

        ModGlobal.imageFilePath = downloadsPath + File.separator + "Notes.pdf";
        //generateEmail();
        startActivity(new Intent(PaymentActivity.this, PDFViewActivity.class));
    }


    private void createPDF(String pdfFilename) {

        Document doc = new Document();
        PdfWriter docWriter = null;

        DecimalFormat df = new DecimalFormat("0.00");

        try {

            //special font sizes
            Font mainFont = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD, new BaseColor(0, 0, 0));

            Font mainFont2 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL, new BaseColor(0, 0, 0));
            Font mainFont3 = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.NORMAL, new BaseColor(0, 0, 0));

            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 18);
            Font bfBold20 = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD, new BaseColor(0, 0, 0));


            //file path
            String path = pdfFilename;
            docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));

            //document header attributes
            doc.addAuthor("PinkHeart");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("PinkHeart.com");
            doc.addTitle("Sale Invoice");
            doc.setPageSize(PageSize.LETTER);

            //open document
            doc.open();

            //create a paragraph
            Paragraph paragraph = new Paragraph("Sales Invoice");


            //specify column widths
            float[] columnWidths = {1.5f, 5f, 1f, 2f, 2.5f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(100f);


            String invoiceNumber = "#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() +
                    "d", Integer.parseInt(ModGlobal.invoice.getInvoiceId()));
            insertCell(table, ModGlobal.invoice.getCustomerName(), Element.ALIGN_LEFT, 3, mainFont);
            insertCell(table, invoiceNumber, Element.ALIGN_LEFT, 2, mainFont3);
            insertCell(table, "Date : " + ModGlobal.invoice.getDateCreated(), Element.ALIGN_LEFT, 3, mainFont2);
            String dueDate = "";

            if (ModGlobal.invoice.getStatus().equals(TranStatus.PENDING.toString())) {
                dueDate = ModGlobal.invoice.getDueDate();
            }

            insertCell(table, "Due Date : " + dueDate, Element.ALIGN_LEFT, 2, mainFont2);

            insertCell(table, ModGlobal.invoice.getStatus(), Element.ALIGN_RIGHT, 5, mainFont2);
            //insert column headings

            insertCell(table, "", Element.ALIGN_RIGHT, 5, bfBold12);
            insertCell(table, "", Element.ALIGN_RIGHT, 5, bfBold12);
            insertCell(table, "code", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "description", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "qty", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "price", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Total Price", Element.ALIGN_RIGHT, 1, bfBold12);
            table.setHeaderRows(1);

            JSONArray jsonArray = new JSONArray(ModGlobal.invoice.getInvoiceDetail());
            ArrayList<StockIn> stockIns = new ArrayList<>();
            for (int a = 0; a < jsonArray.length(); a++) {

                JSONObject object = jsonArray.getJSONObject(a);
                StockIn stockIn = new StockIn(object.getString("productName")
                        , object.getString("productCode"), object.getString("quantity")
                        , object.getString("price"));

                stockIns.add(stockIn);
            }
            ModGlobal.stockIns = stockIns;
            DecimalFormat dec = new DecimalFormat("#,##0.00");

            double finalTotalPrice = 0.00;
            for (StockIn stockIn : ModGlobal.stockIns) {

                String description = stockIn.getProductName();
                String productCode = stockIn.getProductCode();
                String qty = stockIn.getQuantity();
                String price = stockIn.getPrice();
                double totalPriceDouble = Integer.parseInt(qty) * Double.parseDouble(price.replace(",", ""));
                finalTotalPrice += totalPriceDouble;
                String totalPrice = dec.format(totalPriceDouble);

                insertCell(table, productCode, Element.ALIGN_LEFT, 1, bf12);
                insertCell(table, description, Element.ALIGN_LEFT, 1, bf12);
                insertCell(table, qty, Element.ALIGN_CENTER, 1, bf12);
                insertCell(table, "₱" + price, Element.ALIGN_LEFT, 1, bf12);
                insertCell(table, "₱" + totalPrice, Element.ALIGN_RIGHT, 1, bf12);

            }
            insertCell(table, "Total Amount", Element.ALIGN_RIGHT, 4, bf12);
            insertCell(table, dec.format(finalTotalPrice), Element.ALIGN_RIGHT, 1, bfBold20);
            insertCell(table, "Cash", Element.ALIGN_RIGHT, 4, bf12);
            insertCell(table, ModGlobal.totalAmountPaid, Element.ALIGN_RIGHT, 1, bfBold20);

            String balance = "";
            if (Double.parseDouble(ModGlobal.totalBalance.replace(",", "")) <= 0) {
                balance = "0.00";
            } else {
                balance = ModGlobal.totalBalance;
            }
            insertCell(table, "Balance", Element.ALIGN_RIGHT, 4, bf12);
            insertCell(table, balance, Element.ALIGN_RIGHT, 1, bfBold20);


            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);

        } catch (DocumentException dex) {
            dex.printStackTrace();
            Logger.CreateNewEntry(dex, new File(getExternalFilesDir(""), ModGlobal.logFile));
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.CreateNewEntry(ex, new File(getExternalFilesDir(""), ModGlobal.logFile));
        } finally {
            if (doc != null) {
                //close the document
                doc.close();
            }
            if (docWriter != null) {
                //close the writer
                docWriter.close();
            }
        }
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setPadding(5);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }


    private void callCustomer() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        CustomerModel customerModel = databaseCustomer.getAllCustomer(Integer.parseInt(ModGlobal.invoice.getCustomerId()));
        callIntent.setData(Uri.parse("tel:" + customerModel.getContactNumber()));

        if (ActivityCompat.checkSelfPermission(PaymentActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        }
        startActivity(callIntent);
    }

    private void messageCustomer() {

        try {
            CustomerModel customerModel = databaseCustomer.getAllCustomer(Integer.parseInt(ModGlobal.invoice.getCustomerId()));
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setType("vnd.android-dir/mms-sms");
            i.setData(Uri.parse("smsto:" + customerModel.getContactNumber()));
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
        }
    }
}
