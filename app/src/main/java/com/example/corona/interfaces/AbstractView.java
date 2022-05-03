package com.example.corona.interfaces;

import android.content.Context;

public interface AbstractView
{
    void openPdfActivity2(String fileName);
    void setWidgetsOneArticleView(String title, String year, String abstraction);
    void openPermissionDialogForAndroid11();
    void showToast(String str);
    void makeDownloadTaskSample(String link, String fileName);
}
