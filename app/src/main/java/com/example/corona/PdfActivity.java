package com.example.corona;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfActivity extends AppCompatActivity {
    PDFView pdfView;
    String fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfView = findViewById(R.id.pdfView);

        Bundle extras=getIntent().getExtras();
        if(extras!=null) {
            if (extras.containsKey("fileName")) {
                fileName = extras.getString("fileName");
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + Global.FOLDER_NAME_APP + fileName);
                if (dir.exists())
                    displayFromFile(dir);
            }
        }
    }
    public void displayFromFile(File file)
    {

        pdfView.fromFile(file)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false)
                .password(null)
                .scrollHandle(null)
                .load();
    }
}