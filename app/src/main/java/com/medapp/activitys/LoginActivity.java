package com.medapp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.medapp.R;
import com.medapp.api.ApiServicesDatabase;
import com.medapp.models.MedicUser;
import com.medapp.models.SearchMedicUserResponse;
import com.medapp.network.ApiClientDatabase;
import com.medapp.utils.Validators;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit apiClientDatabase = ApiClientDatabase.getRetrofitClient();
        ApiServicesDatabase apiServicesDatabase = apiClientDatabase.create(ApiServicesDatabase.class);

        SharedPreferences credentialPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor credentialsEditor = credentialPreferences.edit();
        String savedUsername = credentialPreferences.getString("username", null);
        String savedPassword = credentialPreferences.getString("password", null);

        if(savedUsername != null && savedPassword != null) {
            Call<SearchMedicUserResponse> medicVerifyCall = apiServicesDatabase.searchMedic(savedUsername, savedPassword);
            medicVerifyCall.enqueue(new Callback<SearchMedicUserResponse>() {
                @Override
                public void onResponse(Call<SearchMedicUserResponse> call, Response<SearchMedicUserResponse> response) {
                    if(response.isSuccessful()) {
                        SearchMedicUserResponse medicUserResponse = response.body();
                        MedicUser.mainMedicUser = new MedicUser(medicUserResponse);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<SearchMedicUserResponse> call, Throwable t) {

                }
            });
        }

        EditText userEdit = findViewById(R.id.nameEdit);
        EditText passwordEdit = findViewById(R.id.passwordEdit);
        Button sendButton = findViewById(R.id.registerButton);
        TextView alertText = findViewById(R.id.alertText);
        TextView registerText = findViewById(R.id.registerText);

        registerText.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        sendButton.setOnClickListener(view -> {
            if(Validators.areEditTextsEmpty(userEdit, passwordEdit)) {
                alertText.setEnabled(true);
                alertText.setText("Nome de usuário ou senha vazios!");
                return;
            }
            Call<SearchMedicUserResponse> medicUserCall = apiServicesDatabase.searchMedic(userEdit.getText().toString().trim(), passwordEdit.getText().toString().trim());
            medicUserCall.enqueue(new Callback<SearchMedicUserResponse>() {
                @Override
                public void onResponse(Call<SearchMedicUserResponse> call, Response<SearchMedicUserResponse> response) {
                    if(response.isSuccessful()) {
                        SearchMedicUserResponse medicUserResponse = response.body();
                        MedicUser.mainMedicUser = new MedicUser(medicUserResponse);
                        credentialsEditor.putString("username", medicUserResponse.getUsername());
                        credentialsEditor.putString("password", medicUserResponse.getPassword());
                        credentialsEditor.apply();

                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Login realizado!")
                                .setContentText("Você será redirecionado para pagina inicial")
                                .setConfirmClickListener(sweetAlertDialog1 -> {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .show();
                    } else {
                        try {
                            ResponseBody medicUserResponseError = response.errorBody();
                            alertText.setEnabled(true);
                            alertText.setVisibility(View.VISIBLE);
                            JSONObject jsonError = new JSONObject(medicUserResponseError.string());
                            alertText.setText(jsonError.getString("msg"));
                            userEdit.setText("");
                            passwordEdit.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchMedicUserResponse> call, Throwable t) {
                    System.out.println(t);
                    alertText.setVisibility(View.VISIBLE);
                    alertText.setEnabled(true);
                    alertText.setText("Houve um erro inesperado!");
                    userEdit.setText("");
                    passwordEdit.setText("");
                }
            });
        });

    }
}