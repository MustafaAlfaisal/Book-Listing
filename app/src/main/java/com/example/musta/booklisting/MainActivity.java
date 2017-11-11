package com.example.musta.booklisting;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView totalItem;
    ImageButton searchIcon;
    EditText search;
    ListView listView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalItem = (TextView) findViewById(R.id.totalItems);
        search = (EditText) findViewById(R.id.searchInput);
        searchIcon = (ImageButton) findViewById(R.id.searchIcon);
        listView = (ListView) findViewById(R.id.list_view);
        frameLayout = (FrameLayout) findViewById(R.id.framlayout);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (search.getText().toString().isEmpty()) {

                    Toast.makeText(MainActivity.this, "Please enter title!", Toast.LENGTH_SHORT).show();


                }
                SearchAsyncTask task = new SearchAsyncTask();
                task.execute(search.getText().toString());
            }
        });
    }

    public void updateUI(String s) {

        try {

            JSONObject root = new JSONObject(s);
            int totalItems = root.getInt("totalItems");

            if (totalItems == 0) {

                totalItem.setText(R.string.No_Books_Found);
                listView.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);


            } else {
                listView.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.GONE);
                JSONArray items = root.getJSONArray("items");
                final ArrayList<Books> booksArray = new ArrayList<Books>();


                for (int i = 0; i < items.length(); i++) {

                    JSONObject details;
                    JSONObject volumeInfo;
                    String title;
                    String publisher;
                    JSONArray authors;

                    if (root.has("items")) {
                        details = items.getJSONObject(i);


                        if (details.has("volumeInfo")) {
                            volumeInfo = details.getJSONObject("volumeInfo");


                            if (volumeInfo.has("title"))
                                title = volumeInfo.getString("title");
                            else
                                title = getString(R.string.No_title);


                            ArrayList<String> authorss = new ArrayList<>();
                            if (volumeInfo.has("authors")) {

                                authors = volumeInfo.getJSONArray("authors");

                                for (int a = 0; a < authors.length(); a++) {

                                    String author = authors.getString(a);

                                    authorss.add(author);


                                }

                            }

                            if (volumeInfo.has("publisher"))
                                publisher = volumeInfo.getString("publisher");
                            else
                                publisher = getString(R.string.No_publisher);

                            Books bookk = new Books(title, publisher, authorss);
                            booksArray.add(bookk);


                        }
                    }
                }


                totalItem.setText(String.valueOf(totalItems) + getString(R.string.Books));
                SearchAdapter adapter = new SearchAdapter(this, R.layout.search_result, booksArray);
                listView.setAdapter(adapter);


            }
        } catch (JSONException e) {
            e.printStackTrace();


        }


    }

    class SearchAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            StringBuilder JsonData = new StringBuilder();
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;

            try {

                String query = URLEncoder.encode(strings[0], "UTF-8");

                String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query;
                Log.v("NETWORK_URL", urlString);


                URL url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.connect();

                inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    JsonData.append(line);
                    line = reader.readLine();
                }
                Log.v("AsyncTask", "Connected" + httpURLConnection.getResponseCode());
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("AsyncTask", e.getMessage());
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }


            return JsonData.toString();
        }

        @Override
        protected void onPostExecute(String s) {


            if (s != null && !s.isEmpty()) {

                updateUI(s);
            } else
                Toast.makeText(MainActivity.this, R.string.No_Internet, Toast.LENGTH_SHORT).show();
        }
    }


}
