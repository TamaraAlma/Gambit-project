package com.example.gambit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity {
Toolbar toolbar;
    ArrayList<Dishes> dishes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RecyclerView recyclerView = findViewById(R.id.rec_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ApiService apiService = RetroClient.getClient().create(ApiService.class);
        Call<ArrayList<Dishes>> call = apiService.getDishes("1");
        call.enqueue(new Callback<ArrayList<Dishes>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Dishes>> call,
                                   @NonNull Response<ArrayList<Dishes>> response) {
                dishes = response.body();
                Adapter adapter = new Adapter(MenuActivity.this, dishes);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Dishes>> call, @NonNull Throwable t) {
                Log.d("TAG", "Response = " + t.getMessage());
            }
        });


    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}