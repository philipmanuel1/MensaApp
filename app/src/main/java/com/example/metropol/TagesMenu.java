package com.example.metropol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TagesMenu extends AppCompatActivity {

    //this is the JSON Data URL
    String URL_Meals = "https://worldwidenetworking.000webhostapp.com/mensa/Api.php";

    //a list to store all the meals
    List<Meal> mealList;

    //the recyclerview
    RecyclerView recyclerView;

    public static PrefConfig prefConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tages_menu);
        // Inflate the layout for this fragment
        TextView mytextView;
        mytextView=findViewById(R.id.txt_name_info);
        mytextView.setText("Welcome "+MainActivity.prefConfig.readName());

        Button BnLogOut;
        BnLogOut = findViewById(R.id.bn_logout);
        BnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogOut();
            }
        });

        Button bewerten;
        bewerten = findViewById(R.id.Bewerten);
        bewerten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bewertenStarten();

            }
        });
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the mealList
        mealList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadMeals();
    }

    private void loadMeals() {


         // Creating a String Request
         // Response Listener and a Error Listener

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://worldwidenetworking.000webhostapp.com/mensa/Api.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting meal object from json array
                                JSONObject meal = array.getJSONObject(i);

                                //adding the meal to meal list
                                mealList.add(new Meal(
                                        meal.getInt("id"),
                                        meal.getString("name"),//title
                                        meal.getString("pictureURL"),//shortdesc
                                        meal.getString("description"),//rating
                                        meal.getInt("rating"),//price
                                        meal.getDouble("price")//image
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            MealAdapter adapter = new MealAdapter(TagesMenu.this, mealList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void performLogOut(){

        MainActivity.prefConfig.writeLoginStatus(false);
        MainActivity.prefConfig.writeName("User");
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LoginFragment()).commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void bewertenStarten() {
        Intent intent = new Intent (this, Bewerten.class);
        startActivity(intent);
    }
}
