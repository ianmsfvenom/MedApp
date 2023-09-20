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
import com.medapp.models.AddMedicUserResponse;
import com.medapp.network.ApiClientDatabase;
import com.medapp.utils.Validators;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView alertText = findViewById(R.id.alertText);

        EditText nameCompleteEdit = findViewById(R.id.nameEdit);
        EditText cpfEdit = findViewById(R.id.cpfEdit);
        EditText birthEdit = findViewById(R.id.birthEdit);
        EditText crmEdit = findViewById(R.id.crmEdit);
        EditText emailEdit = findViewById(R.id.emailEdit);
        EditText usernameEdit = findViewById(R.id.userEdit);
        EditText passwordEdit = findViewById(R.id.passwordEdit);

        Button registerButton = findViewById(R.id.registerButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        registerButton.setOnClickListener(view -> {
            if(Validators.areEditTextsEmpty(nameCompleteEdit, cpfEdit, birthEdit, crmEdit, emailEdit, usernameEdit, passwordEdit)) {
                alertText.setVisibility(View.VISIBLE);
                alertText.setText("Todos os campos devem ser preenchidos!");
                return;
            }
            if(!Validators.isDateValid(birthEdit.getText().toString(), "dd/MM/yyyy")) {
                alertText.setVisibility(View.VISIBLE);
                alertText.setText("Data de nascimento inválida!");
                return;
            }
            if(!Validators.isCPFValid(cpfEdit.getText().toString())) {
                alertText.setVisibility(View.VISIBLE);
                alertText.setText("CPF inválido!");
                return;
            }
            if(!Validators.isCRMValid(crmEdit.getText().toString())) {
                alertText.setVisibility(View.VISIBLE);
                alertText.setText("CRM inválido!");
                return;
            }

            Retrofit retrofit = ApiClientDatabase.getRetrofitClient();
            ApiServicesDatabase apiClientDatabase = retrofit.create(ApiServicesDatabase.class);
            Call<AddMedicUserResponse> medicUserResponseCall = apiClientDatabase.addMedic(
                    nameCompleteEdit.getText().toString().trim(), usernameEdit.getText().toString().trim(),
                    emailEdit.getText().toString().trim(), passwordEdit.getText().toString().trim(),
                    cpfEdit.getText().toString().trim(), birthEdit.getText().toString().trim(),
                    crmEdit.getText().toString().trim()
            );
            medicUserResponseCall.enqueue(new Callback<AddMedicUserResponse>() {
                @Override
                public void onResponse(Call<AddMedicUserResponse> call, Response<AddMedicUserResponse> response) {
                    if(response.isSuccessful()) {
                        SweetAlertDialog successRegisterDialog = new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        successRegisterDialog.setTitleText("Registrado com sucesso!")
                                .setContentText("Seja bem-vindo(a), faça seu login para entrar!")
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }).show();
                    } else {
                        try {
                            ResponseBody medicUserResponseError = response.errorBody();
                            alertText.setEnabled(true);
                            alertText.setVisibility(View.VISIBLE);
                            JSONObject jsonError = new JSONObject(medicUserResponseError.string());
                            alertText.setText(jsonError.getString("msg"));
                            passwordEdit.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddMedicUserResponse> call, Throwable t) {
                    System.out.println(t);
                    alertText.setVisibility(View.VISIBLE);
                    alertText.setEnabled(true);
                    alertText.setText("Houve um erro inesperado!");
                }
            });
        });

    }
}