package com.example.corona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.corona.adapter.AdapterSearch;
import com.example.corona.database.AppDatabase;
import com.example.corona.interfaces.AfterGetListFromDatabase;
import com.example.corona.model.ModelRoom;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements AfterGetListFromDatabase {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
            adapterArticle = new AdapterSearch(listArticlesMain);
            adapterArticle.notifyDataSetChanged();
            recyclerSearch.setAdapter(adapterArticle);
        }
    }
}