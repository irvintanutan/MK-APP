package com.irvin.makeapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.Database.DatabaseCustomer;
import com.irvin.makeapp.Database.DatabaseHelper;
import com.irvin.makeapp.Models.CustomerModel;
import com.irvin.makeapp.R;
import com.irvin.makeapp.Services.Logger;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG = "PDFViewActivity";
    int position = -1;
    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    DatabaseCustomer databaseCustomer = new DatabaseCustomer( this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
        @SuppressLint("WrongViewCast") Toolbar tb = findViewById(R.id.app_bar);
        setSupportActionBar(tb);
        final ActionBar ab = getSupportActionBar();

        ab.setTitle("Sales Invoice Report");
        ab.setDisplayShowCustomEnabled(true);
        // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(true);
        // disable the default title element here (for centered title)

        init();
    }

    private void init() {
        pdfView = findViewById(R.id.pdfview);
        position = getIntent().getIntExtra("position", -1);
        displayFromSdcard();
    }

    private void displayFromSdcard() {

        pdfFileName = ModGlobal.imageFilePath;
        File file = new File(pdfFileName); 
        Log.e("File path", file.getAbsolutePath());
        pdfView.fromFile(file)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    public void generateEmail() {
        try {
            CustomerModel customerModel = databaseCustomer.getAllCustomer(Integer.parseInt(ModGlobal.invoice.getCustomerId()));
            String email = customerModel.getEmail() != null ? customerModel.getEmail() : "";
            String subject = "Sales Invoice " + customerModel.getFullName();
            String text ="#INV-" + String.format("%0" + ModGlobal.receiptLimit.length() +
                    "d", Integer.parseInt(ModGlobal.invoice.getInvoiceId()));

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            // Need to grant this permission
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Attachment
            intent.setType("message/rfc822");

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(ModGlobal.imageFilePath)));

            intent.setPackage("com.google.android.gm");
            startActivityForResult(intent, 101);
        }catch (Exception e){
            e.printStackTrace();
            Logger.CreateNewEntry(e , new File(getExternalFilesDir("") , ModGlobal.logFile));
            Log.e("d" , e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

     if (item.getItemId() == R.id.action_email) {
            generateEmail();
        }
        return super.onOptionsItemSelected(item);
    }

}
