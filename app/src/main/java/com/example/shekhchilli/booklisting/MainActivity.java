package com.example.shekhchilli.booklisting;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String DEBUG_TAG = "DebuGGing";
    TextView message;
    String keyword;
    EditText book;
    String word;
    private static String apiurl = "https://www.googleapis.com/books/v1/volumes?q=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        book = (EditText) findViewById(R.id.book_edittext);
        SearchClickListener();
    }

    public void SearchClickListener() {


        Button searchbtn = (Button) findViewById(R.id.button);
        if (searchbtn != null) {
            searchbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected()) {
                        // Write code if internet is working
                        keyword = book.getText().toString();
                        keyword = keyword.replaceAll(" ","%20");
                        new API().execute(apiurl + keyword);
                        Toast.makeText(MainActivity.this,"connected", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this,"Internet is not connected", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    private class API extends AsyncTask<String, Void, ArrayList<BookInfo>> {

        String result;
        ProgressDialog dialog;



        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("wait while a minute!");
            dialog.setTitle("Loading...");
            dialog.show();


        }

        @Override
        protected ArrayList<BookInfo> doInBackground(String... params) {

            InputStream stream = null;
            BufferedReader reader = null;
            ArrayList<BookInfo> arrayList = new ArrayList<>();

            try {
                URL url = new URL(apiurl + keyword);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.connect();
                stream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                stream.close();
                result = stringBuilder.toString();

                Log.e(DEBUG_TAG,stringBuilder.toString());

                JSONObject rootObject = new JSONObject(result);

                JSONArray items = rootObject.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {

                    JSONObject item = items.getJSONObject(i);

                    JSONObject volume = item.getJSONObject("volumeInfo");


//
////                    JSONObject bookCover = item.getJSONObject("imageLinks");
//

//
                    String title = volume.getString("title");
                    String publisher = volume.getString("publisher");
                    String releaseDate = volume.getString("publishedDate");
//                    JSONArray authors = volume.getJSONArray("authors");
//                    String authorname = String.valueOf(authors.get(i));

//
                    JSONObject imageLinks = volume.getJSONObject("imageLinks");
                     String thumbnail= imageLinks.getString("thumbnail");

                    URL imageLink =new URL(thumbnail);
                    Bitmap bitmap = BitmapFactory.decodeStream(imageLink.openConnection().getInputStream());




                    arrayList.add(new BookInfo(bitmap, null, publisher, releaseDate, title));

                    Log.e(DEBUG_TAG, String.valueOf(items.length()));
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<BookInfo> bookInfos) {

            dialog.dismiss();

            ListAdapter adapter = new com.example.shekhchilli.booklisting.ListAdapter(MainActivity.this, bookInfos);
            ListView list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapter);


        }
    }

}


