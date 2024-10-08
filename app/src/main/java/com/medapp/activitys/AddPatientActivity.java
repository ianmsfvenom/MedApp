package com.medapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.medapp.R;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.models.MedicUser;
import com.medapp.models.Patient;
import com.medapp.network.ApiClientDatabase;
import com.medapp.utils.Validators;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddPatientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextCPF = findViewById(R.id.editTextCpf);
        EditText editTextBirth = findViewById(R.id.editTextBirth);
        EditText editTextAddress = findViewById(R.id.editTextMedicine);
        EditText editTextBed = findViewById(R.id.editTextBed);

        TextView alertTextError = findViewById(R.id.alertTextError);

        Button cancelButton = findViewById(R.id.cancelPatientButton);
        Button submitButton = findViewById(R.id.addPatientButton);

        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddPatientActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        submitButton.setOnClickListener(view -> {
            if (Validators.areEditTextsEmpty(editTextName, editTextCPF, editTextBirth, editTextAddress, editTextBed)) {
                alertTextError.setVisibility(View.VISIBLE);
                alertTextError.setText("Todos os campos devem ser preenchidos!");
            }
            if(!Validators.isDateValid(editTextBirth.getText().toString().trim(), "dd/MM/yyyy")) {
                alertTextError.setVisibility(View.VISIBLE);
                alertTextError.setText("Data inválida!");
                return;
            }
            if(!Validators.isCPFValid(editTextCPF.getText().toString().trim())) {
                alertTextError.setVisibility(View.VISIBLE);
                alertTextError.setText("CPF inválido!");
                return;
            }

            Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
            ApiServicesDatabase servicesDatabase = retrofit.create(ApiServicesDatabase.class);
            Call<Patient> patientResponseCall = servicesDatabase.addPatient(MedicUser.mainMedicUser.getId(),
                    editTextName.getText().toString().trim(),
                    editTextCPF.getText().toString().trim(),
                    editTextBirth.getText().toString().trim(),
                    editTextAddress.getText().toString().trim(),
                    editTextBed.getText().toString().trim()
            );
            patientResponseCall.enqueue(new Callback<Patient>() {
                @Override
                public void onResponse(Call<Patient> call, Response<Patient> response) {
                    if(response.isSuccessful()) {
                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(AddPatientActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Paciente adicionado!")
                                .setContentText("O paciente foi adicionado com sucesso!")
                                .setConfirmClickListener(sweetAlertDialog1 -> {
                                    Intent intent = new Intent(AddPatientActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }).show();
                    } else {
                        try {
                            ResponseBody responseBody = response.errorBody();
                            JSONObject jsonError = new JSONObject(responseBody.string());
                            alertTextError.setVisibility(View.VISIBLE);
                            alertTextError.setText(jsonError.getString("msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Patient> call, Throwable t) {
                    alertTextError.setVisibility(View.VISIBLE);
                    alertTextError.setText("Houve um erro inesperado!");
                }
            });
        });
    }
}