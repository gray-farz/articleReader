package com.example.corona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.corona.adapter.AdapterSearch;
import com.example.corona.database.AppDatabase;
import com.example.corona.interfaces.AbstractView;
import com.example.corona.interfaces.AfterGetListFromDatabase;
import com.example.corona.interfaces.InterfaceBetweenListAndAdapter;
import com.example.corona.model.ModelRoom;
import com.example.corona.presenter.PresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements AfterGetListFromDatabase, InterfaceBetweenListAndAdapter,
        AbstractView, DownloadTask.AfterDownload {
    EditText edtSearch;
    LinearLayout toolbarSearch,layoutRadioGroup;
    public static final String TAG="aaa";
    String catGroup="searchTitle";
    String strSearch="shock";
    RadioButton radioSearchTitle, radioSearchAbstract;
    RecyclerView recyclerSearch;
    AdapterSearch adapterArticle;
    List<ModelRoom> listArticlesMain =new ArrayList<>();
    ProgressBar progressBar;
    ImageView imgClear,imgBack;
    TextView txtEmpty;
    PresenterImpl presenterImpl;
    String fileName;
    TextView txtTitle,txtYear,txtAbstraction;
    FrameLayout frameOneArticle;
    LinearLayout layoutDownload;
    FrameLayout frameListArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        presenterImpl = new PresenterImpl(this);

        findview();
        setOnChangeRadios();
        clickListener();



    }


    private void clickListener()
    {
        imgBack.setOnClickListener(view -> {
            goSearch(edtSearch.getText().toString());
        });
        imgClear.setOnClickListener(view -> {
            edtSearch.setText("");
        });
        layoutDownload.setOnClickListener(view ->
        {
            Log.d(TAG, "clickListener: download");
            presenterImpl.askForPermissions2();
        });

    }

    private void goSearch(String str) {
        if (str.length() > 0) {
            recyclerSearch.setVisibility(View.VISIBLE);
            strSearch = "%"+ str.toString()+"%";
            getResponse();
            imgClear.setVisibility(View.VISIBLE);
        } else {
            imgClear.setVisibility(View.INVISIBLE);
            recyclerSearch.setVisibility(View.INVISIBLE);
        }
    }

    private void getResponse()
    {

        AppDatabase.ReadDatabase(AppDatabase.getInstance(this),this,
                strSearch,catGroup);
    }

    private void setOnChangeRadios() {
        radioSearchTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    catGroup="searchTitle";
                    radioSearchAbstract.setChecked(false);
                }

            }
        });
        radioSearchAbstract.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                {
                    catGroup="searchAbstract";
                    radioSearchTitle.setChecked(false);
                }

            }
        });
    }

    private void findview()
    {
        imgBack = findViewById(R.id.imgBack);
        edtSearch = findViewById(R.id.edtSearch);
        imgClear = findViewById(R.id.imgClear);
        recyclerSearch = findViewById(R.id.recyclerSearch);
        txtEmpty = findViewById(R.id.txtEmpty);
        progressBar = findViewById(R.id.progressBar);
        radioSearchTitle = findViewById(R.id.radioSearchTitle);
        radioSearchAbstract = findViewById(R.id.radioSearchAbstract);

        txtTitle = findViewById(R.id.txtTitleResponse);
        txtYear= findViewById(R.id.txtDateResponse);
        txtAbstraction = findViewById(R.id.txtAbstract);
        frameOneArticle = findViewById(R.id.frameOneArticle);
        layoutDownload =findViewById(R.id.layoutDownload);
        frameListArticles = findViewById(R.id.frameListArticles);

        imgClear.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        imgClear.setVisibility(View.INVISIBLE);

    }

    @Override
    public void GetListArticles(List<ModelRoom> listArticles)
    {
        listArticlesMain.clear();
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
        if(listArticlesMain.size() == 0)
        {
            txtEmpty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            recyclerSearch.setVisibility(View.INVISIBLE);
        }
        else
        {
            txtEmpty.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.GONE);
            adapterArticle = new AdapterSearch(listArticlesMain,this);
            adapterArticle.notifyDataSetChanged();
            recyclerSearch.setAdapter(adapterArticle);
        }
    }

    @Override
    public void getOneArticleProperties(String title, String year, String abstractionn, String link) {
        presenterImpl.getOneArticleProperties(title,year,abstractionn,link);
    }

    @Override
    public void openPdfActivity2(String fileNameParam) {
        Intent intent = new Intent(SearchActivity.this,
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
    public void makeDownloadTaskSample(String link, String fileNameParam) {
        fileName = fileNameParam;
        Log.d(TAG, "makeDownloadTaskSample: "+fileName);
        DownloadTask downloadTask = new DownloadTask(this
                ,link, fileName);
        downloadTask.execute();
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
    public void onBackPressed() {
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
}