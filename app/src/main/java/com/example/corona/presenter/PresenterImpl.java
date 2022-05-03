package com.example.corona.presenter;

import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.corona.ArticleListActivity;
import com.example.corona.DownloadTask;
import com.example.corona.Global;
import com.example.corona.PdfActivity;
import com.example.corona.interfaces.AbstractPresenter;
import com.example.corona.interfaces.AbstractView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PresenterImpl implements AbstractPresenter
{
    AbstractView abstractView;
    String linkOneArticle;
    String fileName;

    public PresenterImpl(AbstractView abstractView) {
        this.abstractView = abstractView;
    }

    @Override
    public void getOneArticleProperties(String title, String year, String abstractionn, String link) {
        linkOneArticle = link;
        fileName = linkOneArticle.substring
                (linkOneArticle.lastIndexOf("/") + 1);

        File dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + Global.FOLDER_NAME_APP + fileName);
        if (dir.exists())
            abstractView.openPdfActivity2(fileName);
        else
        {
            abstractView.setWidgetsOneArticleView(title, year, abstractionn);
        }
    }
    public void askForPermissions2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            if (!Environment.isExternalStorageManager())
            {
                abstractView.openPermissionDialogForAndroid11();
                return;
            }
        }
        goToDoDownload2();
    }
    private void goToDoDownload2()
    {
        try
        {
            File file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    +Global.FOLDER_NAME_APP);
            Log.d("aaa", "!file.exists()");
            if(!file.exists())
            {
                Log.d("aaa", "! exist");
                file.mkdirs();
                file.createNewFile();
            }
            Log.d("aaa", "!file.exists() 2");
        }catch (FileNotFoundException e)
        {
            abstractView.showToast(e.toString());
        }catch (IOException e)
        {
            abstractView.showToast(e.toString());
        }

        abstractView.makeDownloadTaskSample(linkOneArticle,fileName);
    }


}
