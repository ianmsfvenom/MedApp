package com.medapp.activitys;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.medapp.ConfigsActivity;
import com.medapp.R;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.list_adapters.PatientListAdapter;
import com.medapp.models.MedicUser;
import com.medapp.models.Patient;
import com.medapp.network.ApiClientDatabase;
import com.medapp.utils.CaptureAct;

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

        RelativeLayout scanQRButton = findViewById(R.id.scanQRButton);
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

        scanQRButton.setOnClickListener(view -> {
            ScanOptions scanOptions = new ScanOptions();
            scanOptions.setPrompt("Escaneie o código QR do leito do paciente:");
            scanOptions.setOrientationLocked(true);
            scanOptions.setCaptureActivity(CaptureAct.class);
            resultLauncher.launch(scanOptions);
        });

    }

    ActivityResultLauncher<ScanOptions> resultLauncher = registerForActivityResult(new ScanContract(), result -> {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        else vibrator.vibrate(300);

        if(result.getContents() != null) {
            String content = result.getContents();
            Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
            ApiServicesDatabase servicesDatabase = retrofit.create(ApiServicesDatabase.class);
            Call<Patient> patientSearchCall = servicesDatabase.searchPatient(MedicUser.mainMedicUser.getId(), "bed", content.trim());
            patientSearchCall.enqueue(new Callback<Patient>() {
                @Override
                public void onResponse(Call<Patient> call, Response<Patient> response) {
                    if(response.isSuccessful()) {
                        Patient patientMatched = response.body();
                        Intent intent = new Intent(HomeActivity.this, PatientDetailActivity.class);
                        intent.putExtra("id", patientMatched.getId())
                                .putExtra("nameComplete", patientMatched.getName_complete())
                                .putExtra("address", patientMatched.getAddress())
                                .putExtra("bed", patientMatched.getBed())
                                .putExtra("cpf", patientMatched.getCpf())
                                .putExtra("birthDate", patientMatched.getBirth_date());
                        startActivity(intent);
                    } else {
                        try {
                            ResponseBody responseBody = response.errorBody();
                            JSONObject jsonObject = new JSONObject(responseBody.string());
                            SweetAlertDialog alertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE);
                            alertDialog.setTitleText("Falha ao buscar leito!")
                                    .setContentText(jsonObject.getString("msg"))
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Patient> call, Throwable t) {
                    SweetAlertDialog alertDialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.ERROR_TYPE);
                    alertDialog.setTitleText("Falha ao buscar leito!")
                            .setContentText("Houve um erro inesperado ao tentar buscar dados do leito.")
                            .show();
                }
            });
        }
    });

}