package com.example.corona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.corona.adapter.AdapterArticle;
import com.example.corona.database.AppDatabase;
import com.example.corona.interfaces.AbstractView;
import com.example.corona.interfaces.InterfaceBetweenListAndAdapter;
import com.example.corona.interfaces.AfterGetListFromDatabase;
import com.example.corona.model.ModelRoom;
import com.example.corona.presenter.PresenterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends AppCompatActivity implements AfterGetListFromDatabase,
        InterfaceBetweenListAndAdapter, View.OnClickListener
        , DownloadTask.AfterDownload, AbstractView
{
    RecyclerView recyclerArticles;
    ProgressBar progressBar;
    AdapterArticle adapterArticle;
    List<ModelRoom> listArticlesMain =new ArrayList<>();
    private int page=1;
    private int loadIndex;
    public static final String TAG="aaa";
    FrameLayout frameListArticles;
    FrameLayout frameOneArticle;
    TextView txtTitle,txtYear,txtAbstraction;
    LinearLayout layoutDownload;
    String fileName;
    String linkOneArticle;
    String category;
    String catGroup;
    PresenterImpl presenterImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        getExtrasFromIntent();
        findViews();
        setClickListener();
        setUp();
        getResponse();

        presenterImpl = new PresenterImpl(this);

    }

    private void getExtrasFromIntent()
    {
        Bundle extras = getIntent().getExtras();
        if (extras!= null)
        {
            if (extras.containsKey("tagGroup"))
            {
                catGroup = extras.getString("tagGroup");
                if (extras.containsKey("tagButton"))
                {
                    Log.d(TAG, "getExtrasFromIntent: 0 "+extras.getString("tagGroup"));
                    category = extras.getString("tagGroup").equals("subject")  ?
                            "%" + extras.getString("tagButton") + "%"
                            : extras.getString("tagButton");
                }


            }
        }
        Log.d(TAG, "getExtrasFromIntent: 1 "+catGroup);
        Log.d(TAG, "getExtrasFromIntent: 2 "+category);
    }

    private void setClickListener() {
        layoutDownload.setOnClickListener(this);
    }

    private void findViews() {

        recyclerArticles = findViewById(R.id.recyclerNews);
        progressBar = findViewById(R.id.progressBarListActivity);
        frameListArticles = findViewById(R.id.frameList);
        frameOneArticle = findViewById(R.id.frameOneArticle);

        txtTitle = findViewById(R.id.txtTitleResponse);
        txtYear= findViewById(R.id.txtDateResponse);
        txtAbstraction = findViewById(R.id.txtAbstract);
        
        layoutDownload = findViewById(R.id.layoutDownload);
    }

    private void setUp()
    {
        recyclerArticles.setLayoutManager(new LinearLayoutManager(this));
        adapterArticle = new AdapterArticle(listArticlesMain, recyclerArticles,this);
        recyclerArticles.setAdapter(adapterArticle);
        Log.d("aaa","setUp()");
        adapterArticle.setOnLoadMoreListener(() ->
        {
            Log.d(TAG, "responseList.size() in setOnLoadMoreListener "+listArticlesMain.size()+"page "+page);
            if (listArticlesMain.size() / Global.RECORDS_PER_PAGE == page)
            {
                page++;
                listArticlesMain.add(null);
                loadIndex = listArticlesMain.size() - 1;
                adapterArticle.notifyItemInserted(loadIndex);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getResponse();
                    }
                }, 500);

            }
        });

    }

    private void getResponse()
    {

        if (page == 1) {
            progressBar.setVisibility(View.VISIBLE);
        }
        int from_record_num=(Global.RECORDS_PER_PAGE*page)-Global.RECORDS_PER_PAGE;
        AppDatabase.ReadDatabase(AppDatabase.getInstance(this),this,
                from_record_num,category,catGroup);

    }

    @Override
    public void GetListArticles(List<ModelRoom> listArticles)
    {

        if (page != 1) {
            listArticlesMain.remove(loadIndex);
            adapterArticle.notifyItemRemoved(loadIndex);
        }

        for (int i = 0; i < listArticles.size(); i++)
        {
            boolean existThisTitle=false;
            for (int j = 0; j < listArticlesMain.size(); j++)
            {
                if( listArticles.get(i).getTitle().equals
                        (listArticlesMain.get(j).getTitle()))
                {
                    existThisTitle=true;
                    break;
                }
            }
            if(!existThisTitle)
                listArticlesMain.add(listArticles.get(i));
        }

        //listArticlesMain.addAll(listArticles);
        adapterArticle.notifyDataSetChanged();
        adapterArticle.setLoading(false);
        progressBar.setVisibility(View.GONE);



    }


    @Override
    public void getOneArticleProperties(String title, String year,
                                        String abstractionn, String link) {

        presenterImpl.getOneArticleProperties(title,year,abstractionn,link);
    }

    @Override
    public void onBackPressed() {

        Log.d(TAG, "onBackPressed: ");
        if(frameOneArticle.getVisibility() == View.VISIBLE)
        {
            frameOneArticle.setVisibility(View.GONE);
            frameListArticles.setVisibility(View.VISIBLE);
        }
        else
        {
            super.onBackPressed();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.layoutDownload:
                presenterImpl.askForPermissions2();
                break;
        }
    }


    @Override
    public void doThisAfterDownload(String str) {
        if(str != null)
        {
            //Toast.makeText(this, "خطا در دانلود", Toast.LENGTH_SHORT).show();
            if(str.contains("UnknownHostException"))
                Toast.makeText(this, "no Internet connection", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "An error has occurred", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "File downloaded successfully", Toast.LENGTH_SHORT).show();
            openPdfActivity2(fileName);
        }
    }

    @Override
    public void openPdfActivity2(String fileNameParam) {
        Intent intent = new Intent(ArticleListActivity.this,
                PdfActivity.class);
        fileName = fileNameParam;
        intent.putExtra("fileName",fileName);
        startActivity(intent);
    }

    @Override
    public void setWidgetsOneArticleView(String title, String year, String abstraction) {
        txtTitle.setText(title);
        txtYear.setText(year);
        txtAbstraction.setText(abstraction);
        if(frameOneArticle.getVisibility() == View.GONE)
        {
            frameOneArticle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void openPermissionDialogForAndroid11() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        startActivity(intent);
    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void makeDownloadTaskSample(String link, String fileNameParam)
    {
        fileName = fileNameParam;
        DownloadTask downloadTask = new DownloadTask(this
                ,link, fileName);
        downloadTask.execute();
    }
}