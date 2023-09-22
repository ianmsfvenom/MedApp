package com.medapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medapp.ConfigsActivity;
import com.medapp.R;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.list_adapters.PatientListAdapter;
import com.medapp.models.MedicUser;
import com.medapp.models.Patient;
import com.medapp.network.ApiClientDatabase;

import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity {

    public static PatientListAdapter patientListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView titleText = findViewById(R.id.titleHomeText);
        TextView medicCountText = findViewById(R.id.patientsCountText);

        RelativeLayout addPatientButton = findViewById(R.id.patientAddButton);
        RelativeLayout reloadPatientButton = findViewById(R.id.reloadPatientButton);
        ListView patientsList = findViewById(R.id.patientsList);
        ImageView configButton = findViewById(R.id.configButton);

        titleText.setText("Olá, " + MedicUser.mainMedicUser.getName_complete());

        Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
        ApiServicesDatabase apiServicesDatabase = retrofit.create(ApiServicesDatabase.class);
        Call<List<Patient>> patientListResponseCall = apiServicesDatabase.getAllPatients(MedicUser.mainMedicUser.getId());
        patientListResponseCall.enqueue(new Callback<List<Patient>>() {
            @Override
            public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                if(response.isSuccessful()) {
                    List<Patient> patientList = response.body();
                    Comparator<Patient> patientComparator = (patient, t1) -> patient.getName_complete().compareTo(t1.getName_complete());
                    patientList.sort(patientComparator);
                    patientListAdapter = new PatientListAdapter(HomeActivity.this, patientList);
                    patientsList.setAdapter(patientListAdapter);
                    medicCountText.setText(Integer.toString(patientList.size()));
                } else {
                    try {
                        ResponseBody responseBody = response.errorBody();
                        JSONObject jsonError = new JSONObject(responseBody.string());
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText("Houve um erro!")
                                .setContentText(jsonError.getString("msg"))
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Patient>> call, Throwable t) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Falha na conexão")
                        .setContentText("Houve uma falha ao tentar atualizar a lista de pacientes")
                        .show();
            }
        });

        addPatientButton.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AddPatientActivity.class);
            startActivity(intent);
            finish();
        });

        reloadPatientButton.setOnClickListener(view -> {
            Retrofit retrofit2 = ApiClientDatabase.getRetrofitClient();
            ApiServicesDatabase apiServicesDatabase2 = retrofit2.create(ApiServicesDatabase.class);
            Call<List<Patient>> patientListResponseCall2 = apiServicesDatabase2.getAllPatients(MedicUser.mainMedicUser.getId());
            patientListResponseCall2.enqueue(new Callback<List<Patient>>() {
                @Override
                public void onResponse(Call<List<Patient>> call, Response<List<Patient>> response) {
                    if(response.isSuccessful()) {
                        List<Patient> patientList = response.body();
                        Comparator<Patient> patientComparator = (patient, t1) -> patient.getName_complete().compareTo(t1.getName_complete());
                        patientList.sort(patientComparator);
                        patientListAdapter = new PatientListAdapter(HomeActivity.this, patientList);
                        patientsList.setAdapter(patientListAdapter);
                    } else {
                        try {
                            ResponseBody responseBody = response.errorBody();
                            JSONObject jsonError = new JSONObject(responseBody.string());
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText("Houve um erro!")
                                    .setContentText(jsonError.getString("msg"))
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Patient>> call, Throwable t) {
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Falha na conexão")
                            .setContentText("Houve uma falha ao tentar atualizar a lista de pacientes")
                            .show();
                }
            });
        });

        configButton.setOnClickListener(view ->  {
            Intent intent = new Intent(HomeActivity.this, ConfigsActivity.class);
            startActivity(intent);
            finish();
        });
    }


}