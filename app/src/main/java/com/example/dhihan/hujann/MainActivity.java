package com.example.dhihan.hujann;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    // JSON url
    private static String url = "https://jsonplaceholder.typicode.com/photos";

    @BindView(R.id.assignment_numbers) RecyclerView recyclerView;
    // JSON Object Names
    private static final String TAG_ALBUM_ID = "albumId";
    private static final String TAG_ID = "id";
    public static final String TAG_TITLE = "title";
    private static final String TAG_URL = "url";
    public static final String TAG_THUMBANIL_URL = "thumbnailUrl";

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    protected Unbinder unbinder;

    IsThereInternetConnection isThereInternetConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        isThereInternetConnection = new IsThereInternetConnection();

        if (isThereInternetConnection.isNetworkAvailable(this)){
            // Get JSON
            new GetPhotos().execute();
        }else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private class GetPhotos extends AsyncTask<Void, Void, Void> {

        ArrayList<HashMap<String, String>> photoList;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        // Showing progress loading dialog
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait :D");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            WebRequest webRequest = new WebRequest();
            String jsonStr = webRequest.makeHTTPCall(url);
            photoList = ParseJSON(jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);

            if (progressDialog.isShowing())
                progressDialog.dismiss();

            //Attach Data from JSON to Recycler View\
            adapter = new PhotoAdapter(photoList,getBaseContext());
            mLayoutManager = new LinearLayoutManager(getBaseContext());
            if(recyclerView != null) {
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private ArrayList<HashMap<String, String>> ParseJSON(String json) {
        if (json != null) {
            try {
                ArrayList<HashMap<String, String>> photoList = new ArrayList<HashMap<String, String>>();

                JSONArray jsonObj = new JSONArray(json);

                for (int i = 0; i < jsonObj.length(); i++) {
                    JSONObject c = jsonObj.getJSONObject(i);

                    String albumId = c.getString(TAG_ALBUM_ID);
                    String id = c.getString(TAG_ID);
                    String title = c.getString(TAG_TITLE);
                    String url = c.getString(TAG_URL);
                    String thumbanilURL = c.getString(TAG_THUMBANIL_URL);

                    HashMap<String, String> photo = new HashMap<String, String>();

                    photo.put(TAG_ALBUM_ID, albumId);
                    photo.put(TAG_ID, id);
                    photo.put(TAG_TITLE, title);
                    photo.put(TAG_URL, url);
                    photo.put(TAG_THUMBANIL_URL, thumbanilURL);

                    photoList.add(photo);
                }
                return photoList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            Log.e("information", "No data received from HTTP Request");
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
