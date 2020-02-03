package com.irvin.makeapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.irvin.makeapp.Constant.ModGlobal;
import com.irvin.makeapp.R;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PDFViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    String TAG = "PDFViewActivity";
    int position = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);
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


        generateEmail();
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

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"rvngames.inc@gmail.com"});
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "sample");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "sample");
            // Need to grant this permission
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Attachment
            intent.setType("message/rfc822");

            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(ModGlobal.imageFilePath)));

            intent.setPackage("com.google.android.gm");
            startActivityForResult(intent, 101);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("d" , e.toString());
        }
    }

}
