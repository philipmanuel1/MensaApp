package com.example.metropol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFormActivityListener, WelcomeFragment.OnLogoutListener {

    public static PrefConfig prefConfig;
    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefConfig = new PrefConfig(this);
        apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        if(findViewById(R.id.fragment_container)!=null)
        {
            if(savedInstanceState!= null)
            {
                return;
            }

            if(prefConfig.readLoginStatus())
            {
                Intent intent = new Intent (this, TagesMenu.class);
                startActivity(intent);
            }

            else
            {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new LoginFragment()).commit();
            }
        }
    }

    @Override
    public void performRegister() {

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new RegistrationFragment()).addToBackStack(null).commit();
    }

    @Override
    public void performLogin(String name)
    {
        prefConfig.writeName(name);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new WelcomeFragment()).commit();
        Intent intent = new Intent (this, TagesMenu.class);
        startActivity(intent);
    }

    @Override
    public void logoutPerformed() {
        prefConfig.writeLoginStatus(false);
        prefConfig.writeName("User");
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LoginFragment()).commit();
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
