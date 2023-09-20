package com.medapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medapp.R;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.models.MedicUser;
import com.medapp.models.PatientResponse;
import com.medapp.network.ApiClientDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView titleText = findViewById(R.id.titleHomeText);

        RelativeLayout addPatientButton = findViewById(R.id.patientAddButton);
        RelativeLayout reloadPatientButton = findViewById(R.id.reloadPatientButton);

        titleText.setText("Olá, " + MedicUser.mainMedicUser.getName_complete());

        Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
        ApiServicesDatabase apiServicesDatabase = retrofit.create(ApiServicesDatabase.class);
        Call<List<PatientResponse>> patientListResponseCall = apiServicesDatabase.getAllPatients(MedicUser.mainMedicUser.getId());
        patientListResponseCall.enqueue(new Callback<List<PatientResponse>>() {
            @Override
            public void onResponse(Call<List<PatientResponse>> call, Response<List<PatientResponse>> response) {
                if(response.isSuccessful()) {
                    List<PatientResponse> patientResponseList = response.body();
                    System.out.println(patientResponseList.toString());
                }
            }

            @Override
            public void onFailure(Call<List<PatientResponse>> call, Throwable t) {
                System.out.println(t);
            }
        });

        addPatientButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AddPatientActivity.class);
            startActivity(intent);
            finish();
        });
    }
}