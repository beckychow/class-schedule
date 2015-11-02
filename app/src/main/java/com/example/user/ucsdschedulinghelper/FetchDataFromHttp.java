package com.example.user.ucsdschedulinghelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.IOUtils;

/**
 * Created by Huayin Zhou on 10/24/15.
 * Class FetchDataFromHttp is to get the html text from a given URL and return as String
 */

public class FetchDataFromHttp {
    private static final String DEBUG_TAG = "FetchDataFromHttp";
    private String results;

    // Check if there is a network connection. If so, download web page.
    public FetchDataFromHttp(Context context, String stringUrl) throws ExecutionException, InterruptedException{
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            results = new DownloadWebpageTask().execute(stringUrl).get();
        } else {
            Log.w(DEBUG_TAG, "No network connection available");
        }
    }

    public String getResults() {
        return results;
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... urls) {

            // param comes from the execute() call: param[0] is url
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        /* get result from doInBackground() and parse it to correct format
        @Override
        protected void onPostExcecute(String result) {}
        */

        private String downloadUrl(String myUrl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* millisecond */);
                conn.setConnectTimeout(15000 /* millisecond */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // start query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(DEBUG_TAG, "The response is:" + response);
                is = conn.getInputStream();

                // convert inputStream to a string
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                return writer.toString();
                // make sure input stream is closed.
            } finally {
                if (is != null) is.close();
            }
        }
    }
}