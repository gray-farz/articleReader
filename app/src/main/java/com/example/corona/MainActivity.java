package com.example.corona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Menu menuGlobal;
    ImageView imageMedical, imageEconomic,imageEducation,imageManagement,imageMedicine,imagePsycology,imageTourism;
    public static final int PERM=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        setClickListener();
        checkPermissions();
    }

    private void checkPermissions()
    {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERM);
        }
        else
            Toast.makeText(this, "قبلا مجوزها داده شده است", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERM)
        {
            for (int i = 0; i < grantResults.length; i++)
            {
//                if(grantResults[i]==PackageManager.PERMISSION_GRANTED)
//                    Toast.makeText(this, "allowd", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setClickListener()
    {
        imageEconomic.setOnClickListener(this);
        imageMedical.setOnClickListener(this);
        imageEducation.setOnClickListener(this);
        imageManagement.setOnClickListener(this);
        imageMedicine.setOnClickListener(this);
        imagePsycology.setOnClickListener(this);
        imageTourism.setOnClickListener(this);
    }

    private void findView()
    {
        imageEconomic = findViewById(R.id.imageEconomic);
        imageMedical = findViewById(R.id.imageMedical);
        imageEducation = findViewById(R.id.imageEducation);
        imageManagement = findViewById(R.id.imageManagement);
        imageMedicine = findViewById(R.id.imageMedicine);
        imagePsycology = findViewById(R.id.imagePsyology);
        imageTourism = findViewById(R.id.imageTourism);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menuGlobal=menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_year_category:
                categorizeBasedOnYear();
                return true;
            case R.id.menu_search:
                startActivity(new Intent(MainActivity.this,
                        SearchActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void categorizeBasedOnYear()
    {
        //Toast.makeText(this, "categorize", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(MainActivity.this,CategoryYearActivity.class));
    }

    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(MainActivity.this,ArticleListActivity.class);
        intent.putExtra("tagButton",view.getTag().toString());
        intent.putExtra("tagGroup","subject");
        startActivity(intent);

    }
}