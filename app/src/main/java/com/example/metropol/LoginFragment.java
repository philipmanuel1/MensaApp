package com.example.metropol;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginFragment extends Fragment {


    private TextView RegText;
    private EditText UserName,UserPassword;
    private Button LoginBn;
    OnLoginFormActivityListener loginFormActivityListener;


    public interface OnLoginFormActivityListener
    {
        public void performRegister();
        public void performLogin(String name);
    }

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        RegText = view.findViewById(R.id.register_txt);
        UserName = view.findViewById(R.id.user_name);
        UserPassword = view.findViewById(R.id.user_pass);
        LoginBn = view.findViewById(R.id.login_bn);


        LoginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserNameRating();
                performLogin();

            }
        });


        RegText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFormActivityListener.performRegister();
            }
        });
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFormActivityListener = (OnLoginFormActivityListener) activity;
    }

    public String getUserNameRating() {
            final String userNameRating = UserName.getText().toString();
            MainActivity.prefConfig.writeUserName(userNameRating);
            return userNameRating;

    }


    private void performLogin()
    {
        String username = UserName.getText().toString();


        String password = UserPassword.getText().toString();

        Call<User> call= MainActivity.apiInterface.performUserLogin(username,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body().getResponse().equals("ok"))
                {
                    MainActivity.prefConfig.writeLoginStatus(true);
                    loginFormActivityListener.performLogin(response.body().getName());

                }
                else if(response.body().getResponse().equals("failed"))
                {
                    MainActivity.prefConfig.displayToast("Login Failed... Please try again");
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        UserName.setText("");
        UserPassword.setText("");
    }

}
