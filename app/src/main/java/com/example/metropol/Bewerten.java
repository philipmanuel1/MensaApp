package com.example.metropol;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bewerten extends AppCompatActivity {

    Date currentTime;

    Spinner spinner;
    String URL = "https://worldwidenetworking.000webhostapp.com/mensa/names.php";
    ArrayList<String> MealName;
    Integer TempRating;
    RatingBar ratingBar;
    Button insert;
    Button back;

    String user_nameStr, meal_nameStr, ratingStr, timeStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bewerten);
        MealName = new ArrayList<>();
        spinner = (Spinner) findViewById(R.id.spinnerMealNames);
        loadSpinnerData(URL);
        ratingBar = findViewById(R.id.rating);


        back = findViewById(R.id.buttonZurueck);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });


        insert = (Button) findViewById(R.id.insert);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String country = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
               // Toast.makeText(getApplicationContext(), country, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });









        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentTime = Calendar.getInstance().getTime();
                String TempTime = String.valueOf(currentTime);
                String TempRating = String.valueOf(ratingBar.getRating());

                user_nameStr = MainActivity.prefConfig.readUserName();

                meal_nameStr = spinner.getSelectedItem().toString();
                ratingStr = TempRating;
                timeStr = TempTime;

                new Insert().execute();
            }
        });
    }

    public class Insert extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Bewerten.this);
            dialog.setMessage("Ihre Bewertung wird verarbeitet ...");
            dialog.setTitle("Connecting... ");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {


            try {

                List<NameValuePair> insert = new ArrayList<NameValuePair>();
                insert.add(new BasicNameValuePair("user_name", user_nameStr));
                insert.add(new BasicNameValuePair("meal_name", meal_nameStr));
                insert.add(new BasicNameValuePair("rating", ratingStr));
                insert.add(new BasicNameValuePair("time", timeStr));


                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(
                        "https://worldwidenetworking.000webhostapp.com/mensa/get_data.php"); // link to connect to database
                httpPost.setEntity(new UrlEncodedFormEntity(insert));

                HttpResponse response = httpClient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                return true;


            } catch (IOException e) {
                e.printStackTrace();

            }


            return false;
        }

        protected void onPostExecute(Boolean result) {
            dialog.cancel();

            AlertDialog.Builder ac = new AlertDialog.Builder(Bewerten.this);
            ac.setTitle("Info");
            ac.setMessage("Vielen Dank für Ihr Feedback!");
            ac.setCancelable(true);

            ac.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            AlertDialog alert = ac.create();
            alert.show();
        }

    }

    private void loadSpinnerData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("name");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("name");
                        MealName.add(country);
                    }
                    spinner.setAdapter(new ArrayAdapter<String>(Bewerten.this, android.R.layout.simple_spinner_dropdown_item, MealName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    public void backToMenu() {
        Intent intent = new Intent(this, TagesMenu.class);
        startActivity(intent);
    }

}