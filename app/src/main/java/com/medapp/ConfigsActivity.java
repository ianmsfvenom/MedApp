package com.medapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.medapp.activitys.HomeActivity;
import com.medapp.activitys.LoginActivity;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.models.MedicUser;
import com.medapp.network.ApiClientDatabase;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfigsActivity extends AppCompatActivity {
    EditText contentNameText;
    EditText contentCPFEdit;
    EditText contentUsernameEdit;
    EditText contentEmailEdit;
    EditText contentBirthEdit;
    EditText contentCRMEdit;
    EditText olderPasswordEdit;
    EditText newerPasswordEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configs);

        contentNameText = findViewById(R.id.contentNameEdit);
        contentCPFEdit = findViewById(R.id.contentCPFEdit);
        contentUsernameEdit = findViewById(R.id.contentUsernameEdit);
        contentEmailEdit = findViewById(R.id.contentEmailEdit);
        contentBirthEdit = findViewById(R.id.contentBirthEdit);
        contentCRMEdit = findViewById(R.id.contentCRMEdit);
        olderPasswordEdit = findViewById(R.id.olderPasswordEdit);
        newerPasswordEdit = findViewById(R.id.newerPasswordEdit);

        contentNameText.setText(MedicUser.mainMedicUser.getName_complete());
        contentCPFEdit.setText(MedicUser.mainMedicUser.getCpf());
        contentUsernameEdit.setText(MedicUser.mainMedicUser.getUsername());
        contentEmailEdit.setText(MedicUser.mainMedicUser.getEmail());
        contentBirthEdit.setText(MedicUser.mainMedicUser.getBirth_date());
        contentCRMEdit.setText(MedicUser.mainMedicUser.getCrm());

        Button logoutButton = findViewById(R.id.logoutButton);
        Button deleteAccountButton = findViewById(R.id.deleteAccountButton);
        Button resetInfoButton = findViewById(R.id.resetInfoButton);
        Button saveInfoButton = findViewById(R.id.saveInfoButton);
        Button leaveButtonConfig = findViewById(R.id.leaveButtonConfig);
        Button saveUpdatePasswordButton = findViewById(R.id.saveUpdatePassword);

        saveUpdatePasswordButton.setOnClickListener(view -> saveUpdatePasswordButton());
        leaveButtonConfig.setOnClickListener(view -> leaveButtonConfig());
        saveInfoButton.setOnClickListener(view -> saveMedicInfoButton());
        resetInfoButton.setOnClickListener(view -> resetMedicInfoButton());
        logoutButton.setOnClickListener(view -> logoutButton());
        deleteAccountButton.setOnClickListener(view -> deleteAccountButton());
    }



    public void leaveButtonConfig() {
        Intent intent = new Intent(ConfigsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void saveUpdatePasswordButton() {
        if(!olderPasswordEdit.getText().toString().trim().equals(MedicUser.mainMedicUser.getPassword())) {
            SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.ERROR_TYPE);
            alertDialog.setTitleText("Senha incorreta!")
                    .setContentText("A senha atual está incorreta! Digite a sua senha atual.")
                    .show();
            return;
        }
        if(newerPasswordEdit.getText().toString().trim().equals(MedicUser.mainMedicUser.getPassword())) {
            SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.ERROR_TYPE);
            alertDialog.setTitleText("Nova senha inválida!")
                    .setContentText("A nova não pode ser igual a senha anterior.")
                    .show();
            return;
        }
        Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
        ApiServicesDatabase servicesDatabase = retrofit.create(ApiServicesDatabase.class);
        Call<MedicUser> medicUserCall = servicesDatabase.updateMedicUser(MedicUser.mainMedicUser.getId(),
                MedicUser.mainMedicUser.getName_complete(), MedicUser.mainMedicUser.getUsername(),
                MedicUser.mainMedicUser.getEmail(), newerPasswordEdit.getText().toString().trim(),
                MedicUser.mainMedicUser.getCpf(), MedicUser.mainMedicUser.getBirth_date(), MedicUser.mainMedicUser.getCrm());
        medicUserCall.enqueue(new Callback<MedicUser>() {
            @Override
            public void onResponse(Call<MedicUser> call, Response<MedicUser> response) {
                if(response.isSuccessful()) {
                    MedicUser medicUser = response.body();
                    MedicUser.mainMedicUser = medicUser;
                    newerPasswordEdit.setText("");
                    olderPasswordEdit.setText("");
                    SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    alertDialog.setTitleText("Senha alterada!")
                            .setContentText("Sua senha foi alterada com sucesso!")
                            .show();
                } else {
                    try {
                        ResponseBody responseBody = response.errorBody();
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setTitleText("Falha ao alternar senha!")
                                .setContentText(jsonObject.getString("msg"))
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MedicUser> call, Throwable t) {
                SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.ERROR_TYPE);
                alertDialog.setTitleText("Falha ao alternar senha!")
                        .setContentText("Houve um erro inesperado ao tentar alterar sua senha.")
                        .show();
            }
        });
    }
    public void saveMedicInfoButton() {
        Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
        ApiServicesDatabase servicesDatabase = retrofit.create(ApiServicesDatabase.class);
        Call<MedicUser> medicUserCall = servicesDatabase.updateMedicUser(MedicUser.mainMedicUser.getId(),
                contentNameText.getText().toString().trim(), contentUsernameEdit.getText().toString().trim(),
                contentEmailEdit.getText().toString().toString().trim(), MedicUser.mainMedicUser.getPassword(),
                contentCPFEdit.getText().toString().trim(), contentBirthEdit.getText().toString().trim(),
                contentCRMEdit.getText().toString().trim());
        medicUserCall.enqueue(new Callback<MedicUser>() {
            @Override
            public void onResponse(Call<MedicUser> call, Response<MedicUser> response) {
                if(response.isSuccessful()) {
                    MedicUser medicUser = response.body();
                    SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    alertDialog.setTitleText("Informações alteradas!")
                            .setContentText("As informações da conta foram alteradas com sucesso!")
                            .show();
                    MedicUser.mainMedicUser = medicUser;
                } else {
                    try {
                        ResponseBody responseBody = response.errorBody();
                        JSONObject jsonObject = new JSONObject(responseBody.string());
                        SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.ERROR_TYPE);
                        alertDialog.setTitleText("Falha ao alternar informações!")
                                .setContentText(jsonObject.getString("msg"))
                                .show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MedicUser> call, Throwable t) {
                SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.ERROR_TYPE);
                alertDialog.setTitleText("Falha ao alternar informações!")
                        .setContentText("Houve um erro inesperado ao tentar alterar as informações da conta.")
                        .show();
            }
        });
    }

    public void resetMedicInfoButton() {
        contentNameText.setText(MedicUser.mainMedicUser.getName_complete());
        contentCPFEdit.setText(MedicUser.mainMedicUser.getCpf());
        contentUsernameEdit.setText(MedicUser.mainMedicUser.getUsername());
        contentEmailEdit.setText(MedicUser.mainMedicUser.getEmail());
        contentBirthEdit.setText(MedicUser.mainMedicUser.getBirth_date());
        contentCRMEdit.setText(MedicUser.mainMedicUser.getCrm());
    }
    public void deleteAccountButton() {
        SweetAlertDialog alertDialogFirst = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.WARNING_TYPE);
        alertDialogFirst.setTitleText("Tem certeza?")
                .setContentText("Você deseja mesmo deletar a sua conta?")
                .setConfirmText("Sim")
                .setCancelText("Não")
                .setConfirmClickListener(sweetAlertDialog -> {
                    SweetAlertDialog alertDialogSecond = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.WARNING_TYPE);
                    alertDialogSecond.setTitleText("Ação irreversível!")
                            .setContentText("Essa ação é irreversível, ao aceitar você nunca mais terá acesso a essa conta novamente!")
                            .setConfirmText("Aceitar")
                            .setCancelText("Recusar")
                            .setConfirmClickListener(sweetAlertDialog1 -> {
                                Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
                                ApiServicesDatabase servicesDatabase = retrofit.create(ApiServicesDatabase.class);
                                Call<Void> removeMedicCall = servicesDatabase.removeMedicUser(MedicUser.mainMedicUser.getId());
                                removeMedicCall.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(response.isSuccessful()) {
                                            SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                            alertDialog.setTitleText("Conta deletada!")
                                                    .setContentText("Sua conta foi deletada com sucesso!")
                                                    .setConfirmClickListener(sweetAlertDialog2 -> {
                                                        SharedPreferences credentialPreferences = getPreferences(Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor credentialsEditor = credentialPreferences.edit();
                                                        credentialsEditor.remove("username");
                                                        credentialsEditor.remove("password");
                                                        credentialsEditor.apply();
                                                        MedicUser.mainMedicUser = null;
                                                        Intent intent = new Intent(ConfigsActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    }).show();
                                        } else {
                                            try {
                                                ResponseBody responseBody = response.errorBody();
                                                JSONObject jsonObject = new JSONObject(responseBody.string());
                                                SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.ERROR_TYPE);
                                                alertDialog.setTitleText("Falha ao deletar conta!")
                                                        .setContentText(jsonObject.getString("msg"))
                                                        .show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.ERROR_TYPE);
                                        alertDialog.setTitleText("Falha ao deletar conta!")
                                                .setContentText("Houve um erro inesperado ao deletar conta!")
                                                .show();
                                    }
                                });
                            }).show();
                    sweetAlertDialog.dismiss();
                }).show();
    }
    public void logoutButton() {
        SweetAlertDialog alertDialog = new SweetAlertDialog(ConfigsActivity.this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("Tem certeza disso?")
                .setContentText("Você está prestes a fazer o logout da sua conta, tem certeza disso?")
                .setConfirmText("Sim")
                .setCancelText("Não")
                .setConfirmClickListener(sweetAlertDialog -> {
                    SharedPreferences credentialPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
                    SharedPreferences.Editor credentialsEditor = credentialPreferences.edit();
                    credentialsEditor.remove("username");
                    credentialsEditor.remove("password");
                    credentialsEditor.apply();
                    MedicUser.mainMedicUser = null;

                    Intent intent = new Intent(ConfigsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }).show();
    }
}