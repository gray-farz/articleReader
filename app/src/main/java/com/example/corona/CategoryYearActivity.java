package com.example.corona;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CategoryYearActivity extends AppCompatActivity implements View.OnClickListener {
    CardView cardView2020, cardView2021, cardView2022;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_year);
        findview();
        setClickListener();
    }

    private void setClickListener() {
        cardView2020.setOnClickListener(this);
        cardView2021.setOnClickListener(this);
        cardView2022.setOnClickListener(this);
    }

    private void findview() {
        cardView2020 = findViewById(R.id.cardview2020);
        cardView2021 = findViewById(R.id.cardview2021);
        cardView2022 = findViewById(R.id.cardview2022);
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(CategoryYearActivity.this,ArticleListActivity.class);
        intent.putExtra("tagButton",view.getTag().toString());
        intent.putExtra("tagGroup","year");
        startActivity(intent);

    }
}