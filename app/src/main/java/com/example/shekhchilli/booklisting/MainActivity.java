package com.example.shekhchilli.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String DEBUG_TAG = "DebuGGing";
    TextView message;
    String keyword;
    EditText book;
    String word;
    private static String apiurl = "https://www.googleapis.com/books/v1/volumes?q=";
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        book = (EditText) findViewById(R.id.book_edittext);
        SearchClickListener();
    }

    public void SearchClickListener() {

        message = (TextView) findViewById(R.id.message);
        Button searchbtn = (Button) findViewById(R.id.button);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Write code if internrt is working
                    keyword = book.getText().toString();
                    message.setText("Internet is Connected");
                    new API().execute(apiurl + keyword);
                } else {
                    message.setText("Internet is not Connected");
                }
            }
        });

    }


    private class API extends AsyncTask<String, Void, String> {

        String result;

        @Override
        protected String doInBackground(String... params) {
            //String Url = "https://www.googleapis.com/books/v1/volumes?q=";
            InputStream stream = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(apiurl + keyword);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream, "utf-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                stream.close();
                result = stringBuilder.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Convert Stream into String using String builder

//            try{
//                BufferedReader bReader = new BufferedReader(new InputStreamReader(stream,"utf-8"),8);
//                StringBuilder stringBuilder = new StringBuilder();
//
//                String line = null;
//                while((line = bReader.readLine()) != null){
//                    stringBuilder.append(line +"\n");
//                }
//                stream.close();
//                result = stringBuilder.toString();
//
//            }catch (Exception e){
//                Log.e(DEBUG_TAG,e.getMessage());
//            }
            return result;

        }

        @Override
        protected void onPostExecute(String s) {

//            String s = new String(FromStream);

            Log.e(DEBUG_TAG, s);
            ArrayList<String> arrayList = new ArrayList<String>();
            try {
                JSONObject rootObject = new JSONObject(s);
                Log.e(DEBUG_TAG, "root object created");
                JSONArray JArray = rootObject.getJSONArray("items");
                Log.e(DEBUG_TAG, "Array JSON created ");
                for (int i = 0; i < JArray.length(); i++) {
                    JSONObject item = JArray.getJSONObject(i);

                    JSONObject volume = item.getJSONObject("volumeInfo");
                    String title = volume.getString("title");

                    arrayList.add(title);
                }


                Log.e(DEBUG_TAG, String.valueOf(JArray.length()));

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);
                message.setText("code completed");


            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.getMessage());
                e.getLocalizedMessage();
            }


        }
    }
}


