package com.example.corona;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class DownloadTask extends AsyncTask<Void,Integer,String>
{
    ProgressDialog progressDialog;
    Context context;
    String urlGlobal;
    String fileName;
    int fileSize;
    AfterDownload afterDownload;

    public DownloadTask(Context context, String url, String fileName) {
        this.context = context;
        this.urlGlobal = url;
        this.fileName = fileName;
        this.afterDownload = (AfterDownload) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setMessage("will download...");
        progressDialog.show();
        Log.d("aaa", "-----onPreExecute() ");
    }

    @Override
    protected String doInBackground(Void... voids) {
        Log.d("aaa", "-----doInBackground 0: ");
        try
        {
            //fileName = urlGlobal.substring(urlGlobal.lastIndexOf("/")+1);
            InputStream input = null;
            OutputStream output=null;
            HttpURLConnection connection = null;

            try
            {

                URL url = new URL(urlGlobal);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return "server returned "+connection.getResponseCode()+" "+connection.getResponseMessage();
                fileSize = connection.getContentLength();
                input = connection.getInputStream();
                output = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()
                        +Global.FOLDER_NAME_APP+fileName);
                byte[] data = new byte[4096];
                long total = 0;
                int count;
                Log.d("aaa", "fileSize "+fileSize);
                while ( (count=input.read(data)) != -1 )
                {

                    if (isCancelled())
                        return null;
                    total += count;

                    if (fileSize > 0){
                        Log.d("aaa", "total "+total);
                        int percentt = (int)( ((float)total/fileSize) * 100 );
                        Log.d("aaa", "percentt "+percentt);
                        publishProgress(percentt);
                    }
                    output.write(data,0,count);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                deletFileByPath();
                return e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                deletFileByPath();
                return e.toString();
            }

            try
            {
                if(input != null)
                    input.close();
                if(output != null)
                    output.close();
            }catch (IOException e)
            {
                e.printStackTrace();
                deletFileByPath();
            }
            if(connection != null)
                connection.disconnect();
        }finally {

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.setProgress(values[0]);
        progressDialog.setMessage("file size "+new DecimalFormat("##.##").format(fileSize/100000) + " MB");

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        Log.d("aaa", "onPostExecute: "+s);
        afterDownload.doThisAfterDownload(s);
    }

    private void deletFileByPath()
    {
        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/godddd/"+"a1.pdf");
        if(file1.exists())
            file1.delete();
    }

    public interface AfterDownload
    {
        public void doThisAfterDownload(String str);
    }
}
