package com.medapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.medapp.R;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.list_adapters.MedicineListAdapter;
import com.medapp.models.MedicUser;
import com.medapp.models.Medicine;
import com.medapp.models.Patient;
import com.medapp.network.ApiClientDatabase;

import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PatientDetailActivity extends AppCompatActivity {
    String patientId;
    public static MedicineListAdapter medicineListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        EditText nameTextDetail = findViewById(R.id.nameTextDetail);
        EditText cpfTextDetail = findViewById(R.id.cpfTextDetail);
        EditText birthTextDetail = findViewById(R.id.birthTextDetail);
        EditText bedTextDetail = findViewById(R.id.bedTextDetail);
        EditText addressTextDetail = findViewById(R.id.addressTextDetail);
        ImageView erasePatientButton = findViewById(R.id.erasePatientButton);

        ListView medicinePatientList = findViewById(R.id.listMedicines);
        Button leaveButtonDetail = findViewById(R.id.leaveButtonDetail);
        Button saveButtonDetail = findViewById(R.id.saveButtonDetail);
        Button addMedicinePatientButton = findViewById(R.id.addMedicinePatientButton);

        Intent intent = getIntent();
        nameTextDetail.setText(intent.getStringExtra("nameComplete"));
        cpfTextDetail.setText(intent.getStringExtra("cpf"));
        birthTextDetail.setText(intent.getStringExtra("birthDate"));
        bedTextDetail.setText(intent.getStringExtra("bed"));
        addressTextDetail.setText(intent.getStringExtra("address"));
        patientId = intent.getStringExtra("id");

        Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
        ApiServicesDatabase servicesDatabase = retrofit.create(ApiServicesDatabase.class);
        Call<List<Medicine>> listMedicineCall = servicesDatabase.getMedicinesPatient(MedicUser.mainMedicUser.getId(), patientId);
        listMedicineCall.enqueue(new Callback<List<Medicine>>() {
            @Override
            public void onResponse(Call<List<Medicine>> call, Response<List<Medicine>> response) {
                if(response.isSuccessful()) {
                    List<Medicine> medicineList = response.body();
                    medicineListAdapter = new MedicineListAdapter(PatientDetailActivity.this, medicineList, patientId);
                    medicinePatientList.setAdapter(medicineListAdapter);
                    System.out.println(medicineList.toString());
                } else {
                    try {
                        ResponseBody responseBody = response.errorBody();
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        SweetAlertDialog alertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setTitleText("Falha ao listar medicamentos!")
                                .setContentText(jsonObject.getString("msg"))
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Medicine>> call, Throwable t) {
                SweetAlertDialog alertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.ERROR_TYPE);
                alertDialog.setTitleText("Falha ao listar medicamentos!")
                        .setContentText("Houve um erro inesperado ao tentar listar os medicamentos do paciente.")
                        .show();
            }
        });

        leaveButtonDetail.setOnClickListener(view -> {
            finish();
        });

        erasePatientButton.setOnClickListener(view -> {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitleText("Tem certeza?")
                    .setContentText("Você está prestes a excluir os dados desse paciente. Tem certeza disso?")
                    .setConfirmText("Sim")
                    .setCancelText("Não")
                    .setConfirmClickListener(sweetAlertDialog1 -> {
                        Retrofit retrofit1 = ApiClientDatabase.getRetrofitClient();
                        ApiServicesDatabase servicesDatabase1 = retrofit1.create(ApiServicesDatabase.class);
                        Call<Void> patientRemoveCall = servicesDatabase1.removePatient(MedicUser.mainMedicUser.getId(), patientId);
                        sweetAlertDialog1.dismiss();
                        patientRemoveCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()) {
                                    SweetAlertDialog alertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                    alertDialog.setTitleText("Removido com sucesso!")
                                            .setContentText("O paciente foi removido com sucesso!")
                                            .setConfirmClickListener(sweetAlertDialog2 -> {
                                                Intent intent1 = new Intent(PatientDetailActivity.this, HomeActivity.class);
                                                startActivity(intent1);
                                            }).show();
                                } else {
                                    System.out.println(response.code());
                                    SweetAlertDialog alertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.ERROR_TYPE);
                                    alertDialog.setTitleText("Falha ao remover!")
                                            .setContentText("Houve um erro ao tentar remover o paciente!")
                                            .show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                SweetAlertDialog alertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.ERROR_TYPE);
                                alertDialog.setTitleText("Falha ao remover!")
                                        .setContentText("Houve um erro inesperado ao tentar remover o paciente!")
                                        .show();
                            }
                        });
                    })
                    .setCancelClickListener(sweetAlertDialog1 -> sweetAlertDialog1.dismiss()).show();
        });
        saveButtonDetail.setOnClickListener(view -> {
            Retrofit retrofit2 = ApiClientDatabase.getRetrofitClient();
            ApiServicesDatabase servicesDatabase2 = retrofit2.create(ApiServicesDatabase.class);
            Call<Patient> updatePatientCall = servicesDatabase2.updatePatient(MedicUser.mainMedicUser.getId(),
                    patientId, nameTextDetail.getText().toString().trim(), cpfTextDetail.getText().toString().trim(),
                    birthTextDetail.getText().toString().trim(), addressTextDetail.getText().toString().trim(),
                    bedTextDetail.getText().toString().trim());
            updatePatientCall.enqueue(new Callback<Patient>() {
                @Override
                public void onResponse(Call<Patient> call, Response<Patient> response) {
                    if(response.isSuccessful()) {
                        Patient patient = response.body();
                        for(int i = 0; i < HomeActivity.patientListAdapter.getCount(); i++) {
                            Patient listPatient = HomeActivity.patientListAdapter.getItem(i);
                            if(listPatient.getId().equals(patient.getId())) {
                                listPatient.setName_complete(patient.getName_complete());
                                listPatient.setCpf(patient.getCpf());
                                listPatient.setBirth_date(patient.getBirth_date());
                                listPatient.setBed(patient.getBed());
                                listPatient.setAddress(patient.getAddress());
                            }
                        }
                        HomeActivity.patientListAdapter.notifyDataSetChanged();

                        SweetAlertDialog alertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        alertDialog.setTitleText("Atualizado com sucesso!")
                                .setContentText("Dados do paciente atualizado com sucesso!")
                                .setConfirmClickListener(sweetAlertDialog -> finish())
                                .show();
                    } else {
                        try {
                            ResponseBody responseBody = response.errorBody();
                            JSONObject jsonError = new JSONObject(responseBody.string());
                            SweetAlertDialog alertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.ERROR_TYPE);
                            alertDialog.setTitleText("Falha ao atualizar!")
                                    .setContentText(jsonError.getString("msg"))
                                    .show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Patient> call, Throwable t) {
                    System.out.println(t);
                    SweetAlertDialog alertDialog = new SweetAlertDialog(PatientDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    alertDialog.setTitleText("Falha ao atualizar!")
                            .setContentText("Houve um erro inesperado ao tentar atualizar dados do paciente!")
                            .show();
                }
            });
        });

        addMedicinePatientButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(PatientDetailActivity.this, AddMedicinePatientActivity.class);
            intent1.putExtra("patientId", patientId);
            startActivity(intent1);
        });
    }
}